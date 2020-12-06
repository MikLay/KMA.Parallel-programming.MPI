package com.mathpar.LABS.Referat;

import java.nio.*;
import mpi.*;

public class TestWinFence
{
    public static void main (String args[]) throws MPIException
    {
        MPI.Init(args);
        IntBuffer buffer = MPI.newIntBuffer(1);
        Win win = new Win(buffer, 1, 1, MPI.INFO_NULL, MPI.COMM_WORLD);
        win.fence(0);
        win.free();
        MPI.Finalize();
    }
}
