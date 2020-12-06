package com.mathpar.LABS.Lab06;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.NumberZp32;
import com.mathpar.number.Ring;
import com.mathpar.parallel.utils.MPITransport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Random;

public class Task07 {
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

        double time = 0;
        long m = System.currentTimeMillis();

        int ord = Integer.parseInt(args[0]);

        Ring ring = new Ring("Z[x]");

        //iнiцiалiзацiя MPI
        MPI.Init(new String[0]);
        // отримання номера процесора
        int rank = MPI.COMM_WORLD.getRank();
        if (rank == 0) {
            ring.setMOD32(mod);
            int den = 10000;
            Random rnd = new Random();
            MatrixS A = new MatrixS(ord, ord, den, new int[]{5, 5}, rnd, NumberZp32.ONE, ring);
            MatrixS B = new MatrixS(ord, ord, den, new int[]{5, 5}, rnd, NumberZp32.ONE, ring);
            MatrixS CC = null;
            if (ord > 64) {

                MatrixS[] DD = new MatrixS[4];


                MatrixS[] splittedA = A.split();
                MatrixS[] splittedB = B.split();

                MatrixS s1 = splittedA[2].add(splittedA[3], ring);
                MatrixS s2 = s1.subtract(splittedA[0], ring);
                MatrixS s3 = splittedA[0].subtract(splittedA[2], ring);
                MatrixS s4 = splittedA[1].subtract(s2, ring);
                MatrixS s5 = splittedB[1].subtract(splittedB[0], ring);
                MatrixS s6 = splittedB[3].subtract(s5, ring);
                MatrixS s7 = splittedB[3].subtract(splittedB[1], ring);
                MatrixS s8 = s6.subtract(splittedB[2], ring);


                MPITransport.sendObjectArray(new Object[]{s2, s6}, 0, 2, 1, 1);
                MPITransport.sendObjectArray(new Object[]{splittedA[0], splittedB[0]}, 0, 2, 2, 2);
                MPITransport.sendObjectArray(new Object[]{splittedA[1], splittedB[2]}, 0, 2, 3, 3);
                MPITransport.sendObjectArray(new Object[]{s3, s7}, 0, 2, 4, 4);
                MPITransport.sendObjectArray(new Object[]{s1, s5}, 0, 2, 5, 5);
                MPITransport.sendObjectArray(new Object[]{s4, splittedB[3]}, 0, 2, 6, 6);

                MatrixS p7 = strassenMultiply(splittedA[3], s8, ring);
                MatrixS p1 = (MatrixS) MPITransport.recvObject(1, 1);
                MatrixS p2 = (MatrixS) MPITransport.recvObject(2, 2);
                MatrixS p3 = (MatrixS) MPITransport.recvObject(3, 3);
                MatrixS p4 = (MatrixS) MPITransport.recvObject(4, 4);
                MatrixS p5 = (MatrixS) MPITransport.recvObject(5, 5);
                MatrixS p6 = (MatrixS) MPITransport.recvObject(6, 6);

                MatrixS t1 = p1.add(p2, ring);
                MatrixS t2 = t1.add(p4, ring);

                DD[0] = p2.add(p3, ring);
                DD[1] = t1.add(p5, ring).add(p6, ring);
                DD[2] = t2.subtract(p7, ring);
                DD[3] = t2.add(p5, ring);

                CC = MatrixS.join(DD);
                time = (double) (System.currentTimeMillis() - m) / 1000;
            } else {
                CC = A.multiply(B, ring);
                time = (double) (System.currentTimeMillis() - m) / 1000;
            }
            System.out.println("RES= " + CC.toString());
            System.out.println("Time: " + time);
        } else {
            if (ord > 64) {
                ring.setMOD32(mod);
                Object[] n = new Object[2];

                MPITransport.recvObjectArray(n, 0, 2, 0, rank);

                MatrixS res = strassenMultiply((MatrixS) n[0], (MatrixS) n[1], ring);
                MPITransport.sendObject(res, 0, rank);
            }
        }
        MPI.Finalize();
    }
}
