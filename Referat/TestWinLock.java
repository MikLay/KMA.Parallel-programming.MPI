package com.mathpar.LABS.Referat;/*
 *
 * This file is a port from "c_lock_illegal.c" from the "ompi-ibm-10.0"
 * regression test package. The formatting of the code is
 * mainly the same as in the original file.
 *
 *
 * File: CLockIllegal.java		Author: S. Gross
 *
 */

import java.nio.*;
import mpi.*;

public class TestWinLock
{
    public static void main (String args[]) throws MPIException
    {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();
        Info info = new Info();
        info.set("no_locks", "true");
        IntBuffer buffer = MPI.newIntBuffer(1);
        Win win = new Win(buffer, 1, 1, info, MPI.COMM_WORLD);
        win.setErrhandler(MPI.ERRORS_RETURN);
        info.free();
        if (0 == rank) {
            try
            {
                win.lock(MPI.LOCK_SHARED, 0, 0);
            }
            catch(MPIException ex)
            {

            }
        }
        win.free();
        MPI.Finalize();
    }
}
