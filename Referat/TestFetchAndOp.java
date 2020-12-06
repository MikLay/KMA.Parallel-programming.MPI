package com.mathpar.LABS.Referat;

/*
 *
 * This file is a port from "c_fetch_and_op.c" from the ibm
 * regression test package found in the ompi-tests repository.
 * The formatting of the code is similar to the original file.
 *
 *
 * File: CFetchAndOp.java			Author: N. Graham
 *
 */

import static mpi.MPI.slice;

import java.nio.*;

import mpi.*;

public class TestFetchAndOp {
    private static int ITER = 100;
    private static int[] errors = new int[1];
    private static int[] allErrors = new int[1];

    public static void main(String[] args) throws MPIException {
        int rank, nproc;
        IntBuffer valPtr, resPtr;
        Win win;
        errors[0] = 0;
        allErrors[0] = 0;

        MPI.Init(args);

        rank = MPI.COMM_WORLD.getRank();
        nproc = MPI.COMM_WORLD.getSize();

        valPtr = MPI.newIntBuffer(nproc);
        resPtr = MPI.newIntBuffer(nproc);

        win = new Win(valPtr, nproc, 1, MPI.INFO_NULL, MPI.COMM_WORLD);

        selfComm(valPtr, resPtr, win, rank);
        neighborComm(valPtr, resPtr, win, rank, nproc);

        win.free();

        MPI.COMM_WORLD.reduce(errors, allErrors, 1, MPI.INT, MPI.SUM, 0);

        MPI.Finalize();
        if (rank == 0 && allErrors[0] == 0)
            System.out.printf(" No Errors\n");
    }

    // Test self communication
    private static void selfComm(IntBuffer valPtr, IntBuffer resPtr, Win win, int rank) throws MPIException {
        resetVars(valPtr, resPtr, win);

        for (int i = 0; i < ITER; i++) {
            IntBuffer one = MPI.newIntBuffer(1);
            IntBuffer result = MPI.newIntBuffer(1);
            one.put(0, 1);
            result.put(0, -1);

            win.lock(MPI.LOCK_EXCLUSIVE, rank, 0);
            win.fetchAndOp(one, result, MPI.INT, rank, 0, MPI.SUM);
            win.unlock(rank);
        }

        win.lock(MPI.LOCK_EXCLUSIVE, rank, 0);
        win.unlock(rank);
    }

    // Test neighbor communication
    private static void neighborComm(IntBuffer valPtr, IntBuffer resPtr, Win win, int rank, int nproc) throws MPIException {
        resetVars(valPtr, resPtr, win);

        for (int i = 0; i < ITER; i++) {
            IntBuffer one = MPI.newIntBuffer(1);
            IntBuffer result = MPI.newIntBuffer(1);
            one.put(0, 1);
            result.put(0, -1);

            win.lock(MPI.LOCK_EXCLUSIVE, (rank + 1) % nproc, 0);
            win.fetchAndOp(one, result, MPI.INT, (rank + 1) % nproc, 0, MPI.SUM);
            win.unlock((rank + 1) % nproc);
        }

        MPI.COMM_WORLD.barrier();

        win.lock(MPI.LOCK_EXCLUSIVE, rank, 0);
        win.unlock(rank);
    }

    private static void resetVars(IntBuffer valPtr, IntBuffer resPtr, Win win) throws MPIException {
        int rank, nproc;

        rank = MPI.COMM_WORLD.getRank();
        nproc = MPI.COMM_WORLD.getSize();

        win.lock(MPI.LOCK_EXCLUSIVE, rank, 0);

        for (int i = 0; i < nproc; i++) {
            valPtr.put(i, 0);
            resPtr.put(i, -1);
        }
        win.unlock(rank);

        MPI.COMM_WORLD.barrier();
    }

}

