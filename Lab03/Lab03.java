package com.mathpar.LABS;

import mpi.MPI;
import mpi.MPIException;

public class Lab03 {

  public static void main(String[] args) throws MPIException {
    MPI.Init(args);

    int rank = MPI.COMM_WORLD.getRank();
    int size = MPI.COMM_WORLD.getSize();

    int[] a = new int[size];

    MPI.COMM_WORLD.barrier();

    if (rank == 0) {
      int initNumber = Integer.parseInt(args[0]);

      if (size > 1) {
        a[rank] = initNumber;
        MPI.COMM_WORLD.send(a, size, MPI.INT, rank + 1, 300);
        MPI.COMM_WORLD.recv(a, size, MPI.INT, size - 1, 300);
        System.out.println("Final number: " + a[size - 1]);
      } else {
        System.out.println("Final number for only 1: " + initNumber);
      }
    } else {
      MPI.COMM_WORLD.recv(a, size, MPI.INT, rank - 1, 300);
      if (rank % 2 == 0) {
        a[rank] = a[rank - 1] + rank;
      } else {
        a[rank] = a[rank - 1] + 10 * rank;
      }
      MPI.COMM_WORLD.send(a, size, MPI.INT, (rank + size + 1) % size, 300);
    }
    MPI.Finalize();
  }
}
