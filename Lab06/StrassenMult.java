package com.mathpar.LABS.Lab06;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

public class Task06 {
    static int tag = 0;
    static int mod = 13;
    static int valuableSize = 64;

    public static MatrixS strassenMultiply(MatrixS a, MatrixS b, Ring ring) {
        if (a.colNumb <= 64) {
            return a.multiplyRecursive(b, ring);
        }

        MatrixS[] splittedA = a.split();
        MatrixS[] splittedB = b.split();

        MatrixS s1 = splittedA[2].add(splittedA[3], ring);
        MatrixS s2 = s1.subtract(splittedA[0], ring);
        MatrixS s3 = splittedA[0].subtract(splittedA[2], ring);
        MatrixS s4 = splittedA[1].subtract(s2, ring);
        MatrixS s5 = splittedB[1].subtract(splittedB[0], ring);
        MatrixS s6 = splittedB[3].subtract(s5, ring);
        MatrixS s7 = splittedB[3].subtract(splittedB[1], ring);
        MatrixS s8 = s6.subtract(splittedB[2], ring);


        MatrixS p1 = strassenMultiply(s2, s6, ring);
        MatrixS p2 = strassenMultiply(splittedA[0], splittedB[0], ring);
        MatrixS p3 = strassenMultiply(splittedA[1], splittedB[2], ring);
        MatrixS p4 = strassenMultiply(s3, s7, ring);
        MatrixS p5 = strassenMultiply(s1, s5, ring);
        MatrixS p6 = strassenMultiply(s4, splittedB[3], ring);
        MatrixS p7 = strassenMultiply(splittedA[3], s8, ring);

        MatrixS t1 = p1.add(p2, ring);
        MatrixS t2 = t1.add(p4, ring);

        MatrixS[] DD = new MatrixS[4];
        DD[0] = p2.add(p3, ring);
        DD[1] = t1.add(p5, ring).add(p6, ring);
        DD[2] = t2.subtract(p7, ring);
        DD[3] = t2.add(p5, ring);
        return MatrixS.join(DD);
    }

    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {


        Ring ring = new Ring("Z[x]");

        //iнiцiалiзацiя MPI
        MPI.Init(new String[0]);
        // отримання номера процесора
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == 0) {
            // програма виконується на нульовому процесорi
            ring.setMOD32(mod);
            int ord = Integer.parseInt(args[0]);
            int den = 10000;
            // представник класу випадкового генератора
            Random rnd = new Random();
            // ord = розмiр матрицi, den = щiльнiсть
            MatrixS A = new MatrixS(ord, ord, den, new int[]{5, 5}, rnd, NumberZp32.ONE, ring);
            MatrixS B = new MatrixS(ord, ord, den, new int[]{5, 5}, rnd, NumberZp32.ONE, ring);

            System.out.println(A.toString());
            System.out.println(B.toString());
            System.out.println("REEES: " + A.multiply(B, ring));

            MatrixS[] DD = new MatrixS[4];
            MatrixS CC = null;
            // розбиваємо матрицю A на 4 частини
            MatrixS[] AA = A.split();
// розбиваємо матрицю B на 4 частини
            MatrixS[] BB = B.split();
// вiдправлення вiд нульового процесора масиву Object процесору 1 з iдентифiкатором tag = 1
            MPITransport.sendObjectArray(new Object[]{AA[0], BB[1], AA[1], BB[3]}, 0, 4, 1, 1);
// вiдправлення вiд нульового процесора масиву Object процесору 2 з iдентифiкатором tag = 2
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[0], AA[3], BB[2]}, 0, 4, 2, 2);
// вiдправлення вiд нульового процесора масиву Object// процесору 3 з iдентифiкатором tag = 3
            MPITransport.sendObjectArray(new Object[]{AA[2], BB[1], AA[3], BB[3]}, 0, 4, 3, 3);
// залишаємо один блок нульовому процесору для оброблення
            DD[0] = strassenMultiply(AA[0], BB[0], ring).add(strassenMultiply(AA[1], BB[2], ring), ring);
// отримуємо результат вiд першого процесора
            DD[1] = (MatrixS) MPITransport.recvObject(1, 1);
            System.out.println("recv 1 to 0");// отримуємо результат вiд другого процесора
            DD[2] = (MatrixS) MPITransport.recvObject(2, 2);
            System.out.println("recv 2 to 0");
            // отримуємо результат вiд третього процесора
            DD[3] = (MatrixS) MPITransport.recvObject(3, 3);
            System.out.println("recv 3 to 0");
            //процедура збору матрицi з блокiв DD[i]//(i=0,...,3)
            CC = MatrixS.join(DD);
            System.out.println("RES= " + CC.toString());
        } else {
            // програма виконується на процесорi з номером rank
            System.out.println("I’m processor " + rank);
            ring.setMOD32(mod);
            // отримуємо масив Object з блоками матриць вiд нульового процесора
            Object[] n = new Object[4];

            MPITransport.recvObjectArray(n, 0, 4, 0, rank);
            MatrixS a = (MatrixS) n[0];
            MatrixS b = (MatrixS) n[1];

// перемножуємо та складаємо блоки матриць
            MatrixS res = strassenMultiply((MatrixS) n[0], (MatrixS) n[1], ring).add(strassenMultiply((MatrixS) n[2], (MatrixS) n[3], ring), ring);
// надсилаємо результат обчислень вiд процесора rank нульовому процесору
            System.out.println("res = " + res);
            MPITransport.sendObject(res, 0, rank);
// повiдомлення на консоль про те, що результат буде надiслано
            System.out.println("send result");
        }
        MPI.Finalize();
    }
}
