package com.mathpar.LABS.Referat;/*
 *
 * This file is a port from "c_get.c" from the "ompi-ibm-10.0"
 * regression test package. The formatting of the code is
 * mainly the same as in the original file.
 *
 *
 * File: CGet.java			Author: S. Gross
 *
 */

import java.nio.*;
import java.util.Arrays;

import mpi.*;
import static mpi.MPI.slice;

public class TestWinGet
{
    public static void main (String args[]) throws MPIException
    {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.getRank();
        int size = MPI.COMM_WORLD.getSize();


        IntBuffer winArea = MPI.newIntBuffer(5),
                rcvArea = MPI.newIntBuffer(size);

        Win win = new Win(winArea, 5, 5, MPI.INFO_NULL, MPI.COMM_WORLD);

        /* Have every assign their "get" area to be their rank value */
        winArea.put(0, rank);
        win.fence(0);

        /* Have everyone get from everyone else */
        System.out.println(winArea.toString());
        for (int i = 0; i < size; ++i) {
            rcvArea.put(i, -1);
            win.get(slice(rcvArea, i), 1, MPI.INT, i, 0, 1, MPI.INT);
        }

        win.fence(0);


        win.free();
        MPI.Finalize();
    }
}
