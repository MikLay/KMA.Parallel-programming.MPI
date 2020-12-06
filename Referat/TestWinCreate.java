package com.mathpar.LABS.Referat;

import mpi.MPI;
import mpi.MPIException;
import mpi.Win;

import java.nio.IntBuffer;

public class TestWinCreate
{
    public static void main (String args[]) throws MPIException
    {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();

        //MPI_Alloc_mem(sizeof(int), MPI.INFO_NULL, &buffer);
        IntBuffer buffer = MPI.newIntBuffer(1);

        Win win = new Win(buffer, 1, 1, MPI.INFO_NULL, MPI.COMM_WORLD);

        win.free();
        //MPI_Free_mem(buffer);
        MPI.Finalize();
    }
}
