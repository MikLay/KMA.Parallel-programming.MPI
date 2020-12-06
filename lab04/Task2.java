package com.mathpar.LABS.lab04;

import mpi.MPI;

import java.util.Arrays;

public class Task2 {
    public static void main(String[] args)
            throws Exception {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.getRank();
        int np = MPI.COMM_WORLD.getSize();
        int n = Integer.parseInt(args[0]);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = myrank;
        System.out.println("rank = " + myrank + " : array = "
                + Arrays.toString(a));
        int[] q = new int[n * np];
        MPI.COMM_WORLD.gather(a, n, MPI.INT, q, n, MPI.INT,
                1);
        if (myrank == 1)
            System.out.println("rank = " + myrank + " : final array = "
                    + Arrays.toString(q));
        MPI.Finalize();
    }
}

/*
openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 4 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/lab04/Task2 1
        rank = 1 : array = [1]
        rank = 2 : array = [2]
        rank = 3 : array = [3]
        rank = 0 : array = [0]
        rank = 1 : final array = [0, 1, 2, 3]


openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 8 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/lab04/Task2 1
        rank = 2 : array = [2]
        rank = 6 : array = [6]
        rank = 0 : array = [0]
        rank = 5 : array = [5]
        rank = 7 : array = [7]
        rank = 3 : array = [3]
        rank = 1 : array = [1]
        rank = 4 : array = [4]
        rank = 1 : final array = [0, 1, 2, 3, 4, 5, 6, 7]


openmpi/bin/mpirun --hostfile /home/miklay/hostfile -np 12 java -cp /home/miklay/IdeaProjects/dap/target/classes com/mathpar/LABS/lab04/Task2 1
        rank = 6 : array = [6]
        rank = 1 : array = [1]
        rank = 9 : array = [9]
        rank = 10 : array = [10]
        rank = 5 : array = [5]
        rank = 2 : array = [2]
        rank = 0 : array = [0]
        rank = 3 : array = [3]
        rank = 7 : array = [7]
        rank = 4 : array = [4]
        rank = 8 : array = [8]
        rank = 11 : array = [11]
        rank = 1 : final array = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
*/
