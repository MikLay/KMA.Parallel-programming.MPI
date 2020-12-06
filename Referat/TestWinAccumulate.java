package com.mathpar.LABS.Referat;

import java.nio.*;
import mpi.*;

public class TestWinAccumulate
{
    public static void main (String args[]) throws MPIException
    {
        int rank, size, expected, i;

        MPI.Init(args);
        rank = MPI.COMM_WORLD.getRank();
        size = MPI.COMM_WORLD.getSize();

        IntBuffer sendBuf = MPI.newIntBuffer(1),
                recvBuf = MPI.newIntBuffer(1);

        Win win = new Win(recvBuf, 1, 1, MPI.INFO_NULL, MPI.COMM_WORLD);
        sendBuf.put(0, rank + 100);
        recvBuf.put(0, 0);

        /* Accumulate to everyone, just for the heck of it */
        win.fence(MPI.MODE_NOPRECEDE);
        for (i = 0; i < size; ++i)
            win.accumulate(sendBuf, 1, MPI.INT, i, 0, 1, MPI.INT, MPI.SUM);
        win.fence(MPI.MODE_NOPUT | MPI.MODE_NOSUCCEED);

        win.free();
        MPI.Finalize();
    }
}
