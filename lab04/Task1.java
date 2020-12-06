package com.mathpar.LABS.lab04;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;

public class Task1 {
    public static void main(String[] args)
            throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        if (myrank == 2) {
            for (int i = 0; i < n; i++) {
                a[i] = new Random().nextInt(100-20)+20;
            }
            System.out.println("rank = " + myrank + " : array = "
                    + Arrays.toString(a));
        }
        MPI.COMM_WORLD.barrier();
        MPI.COMM_WORLD.bcast(a, a.length, MPI.INT, 2);
        if (myrank != 2)
            System.out.println("rank = " + myrank + " : array = "
                    + Arrays.toString(a));
        MPI.Finalize();
    }
}

/*
openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 4 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/lab04/Task1 4
        rank = 2 : array = [73, 91, 24, 65]
        rank = 0 : array = [73, 91, 24, 65]
        rank = 1 : array = [73, 91, 24, 65]
        rank = 3 : array = [73, 91, 24, 65]


openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 8 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/lab04/Task1 4
        rank = 2 : array = [72, 38, 22, 97]
        rank = 6 : array = [72, 38, 22, 97]
        rank = 4 : array = [72, 38, 22, 97]
        rank = 3 : array = [72, 38, 22, 97]
        rank = 0 : array = [72, 38, 22, 97]
        rank = 1 : array = [72, 38, 22, 97]
        rank = 5 : array = [72, 38, 22, 97]
        rank = 7 : array = [72, 38, 22, 97]

openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 12 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/lab04/Task1 4
        rank = 2 : array = [20, 80, 42, 52]
        rank = 6 : array = [20, 80, 42, 52]
        rank = 10 : array = [20, 80, 42, 52]
        rank = 0 : array = [20, 80, 42, 52]
        rank = 4 : array = [20, 80, 42, 52]
        rank = 8 : array = [20, 80, 42, 52]
        rank = 3 : array = [20, 80, 42, 52]
        rank = 7 : array = [20, 80, 42, 52]
        rank = 1 : array = [20, 80, 42, 52]
        rank = 9 : array = [20, 80, 42, 52]
        rank = 11 : array = [20, 80, 42, 52]
        rank = 5 : array = [20, 80, 42, 52]
*/

