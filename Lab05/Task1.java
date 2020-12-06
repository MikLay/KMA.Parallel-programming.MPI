package com.mathpar.LABS.Lab05;

import com.mathpar.NAUKMA.examples.Transport;
import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Task1 {
    public static void main(String[] args) throws MPIException,
            IOException, ClassNotFoundException {

        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        int ord = Integer.parseInt(args[0]);

        int k = ord / size;

        int n = ord - k * (size - 1);

        int[] result = new int[ord];
        int[] sendBuf = new int[ord];
        if (rank == 0) {
            int den = 10000;
            Random rnd = new Random();
            MatrixS A = new MatrixS(ord, ord, den, new int[]{5, 5},
                    rnd, NumberZp32.ONE, ring);
            System.out.println("Matrix A = " + A.toString());
            // array of results for proc 0
            Element[] res0 = new Element[n];
            for (int i = 0; i < n; i++) {
                res0[i] = new VectorS(A.M[i]).multiply(A.M[i], ring);
            }
            for (int j = 1; j < size; j++) {
                for (int z = 0; z < k; z++) {
                    Transport.sendObject(A.M[n + (j - 1) * k + z], j, 100 + j);
                }
            }
            sendBuf[0] = Arrays.stream(res0).map(Element::intValue).reduce(Integer::sum).get();

        } else {
            // Get rows
            for (int i = 0; i < k; i++) {
                sendBuf[i] = Arrays.stream((Element[])
                        Transport.recvObject(0, 100 + rank)).map(e -> e.intValue() * e.intValue()).reduce(Integer::sum).get();
            }

        }
        MPI.COMM_WORLD.barrier();

        MPI.COMM_WORLD.reduce(sendBuf, result, ord, MPI.INT, MPI.SUM, 0);

        if (rank == 0) {
            double norm = Math.sqrt(Arrays.stream(result).sum());
            System.out.println("Rank = " + rank + ": Matrix norm = " + norm);
        }
        MPI.Finalize();


    }
}

/*
miklay@miklay-VirtualBox:~$ openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 4 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/Lab05/Task1 4
        Matrix A =
        [[25, 12, 5,  21]
        [2,  18, 16, 7 ]
        [4,  29, 11, 22]
        [1,  25, 9,  12]]
        Rank = 0: Matrix norm = 64.66065264130884

miklay@miklay-VirtualBox:~$ openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 8 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/Lab05/Task1 10
        Matrix A =
        [[1,  10, 22, 24, 3,  12, 29, 0,  0,  24]
        [20, 20, 12, 16, 18, 7,  3,  21, 5,  15]
        [26, 4,  2,  6,  8,  16, 13, 1,  17, 1 ]
        [6,  15, 19, 12, 20, 21, 20, 19, 22, 9 ]
        [11, 0,  7,  12, 14, 29, 17, 12, 28, 22]
        [22, 25, 11, 17, 27, 7,  20, 13, 21, 5 ]
        [30, 26, 24, 28, 8,  24, 30, 10, 27, 20]
        [20, 0,  15, 31, 11, 26, 28, 16, 29, 30]
        [4,  28, 30, 23, 30, 19, 5,  19, 6,  30]
        [26, 26, 3,  22, 2,  17, 25, 7,  9,  15]]
        Rank = 0: Matrix norm = 186.09674903124989

miklay@miklay-VirtualBox:~$ openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 12 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/Lab05/Task1 10
        Matrix A =
        [[0,  1,  0,  19, 13, 4,  18, 10, 14, 21]
        [16, 3,  0,  10, 19, 8,  0,  5,  15, 3 ]
        [23, 17, 5,  0,  6,  2,  10, 5,  20, 9 ]
        [23, 17, 21, 22, 18, 21, 28, 18, 28, 15]
        [4,  27, 28, 13, 27, 2,  12, 0,  17, 3 ]
        [3,  31, 31, 4,  2,  31, 27, 17, 2,  17]
        [27, 21, 29, 27, 7,  28, 28, 26, 3,  8 ]
        [2,  23, 29, 17, 22, 8,  20, 1,  22, 9 ]
        [10, 16, 26, 27, 11, 28, 6,  29, 30, 11]
        [20, 9,  11, 5,  1,  17, 13, 13, 23, 26]]
        Rank = 0: Matrix norm = 176.56160398002731
*/
