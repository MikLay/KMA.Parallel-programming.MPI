package com.mathpar.LABS.Referat;

import java.nio.*;
import mpi.*;

public class TestCompareAndSwap {
    private static int ITER = 100;

    public static void main(String[] args) throws MPIException {
        int rank, nproc;
        int[] errors = new int[1];
        int[] allErrors = new int[1];
        IntBuffer valPtr = MPI.newIntBuffer(1);
        Win win;

        errors[0] = 0;
        allErrors[0] = 0;

        MPI.Init(args);

        rank = MPI.COMM_WORLD.getRank();
        nproc = MPI.COMM_WORLD.getSize();

        valPtr.put(0, 0);

        win = new Win(valPtr, 1, 1, MPI.INFO_NULL, MPI.COMM_WORLD);

        /* Test self communication */

        for (int i = 0; i < ITER; i++) {
            IntBuffer next = MPI.newIntBuffer(1);
            IntBuffer iBuffer = MPI.newIntBuffer(1);
            IntBuffer result = MPI.newIntBuffer(1);

            next.put(0, (i + 1));
            iBuffer.put(0, i);
            result.put(0, -1);

            win.lock(MPI.LOCK_EXCLUSIVE, rank, 0);
            win.compareAndSwap(next, iBuffer, result, MPI.INT, rank, 0);
            win.unlock(rank);

        }

        win.lock(MPI.LOCK_EXCLUSIVE, rank, 0);
        valPtr.put(0, 0);
        win.unlock(rank);

        MPI.COMM_WORLD.barrier();

        /* Test neighbor communication */

        for (int i = 0; i < ITER; i++) {
            IntBuffer next = MPI.newIntBuffer(1);
            IntBuffer iBuffer = MPI.newIntBuffer(1);
            IntBuffer result = MPI.newIntBuffer(1);

            next.put(0, (i + 1));
            iBuffer.put(0, i);
            result.put(0, -1);

            win.lock(MPI.LOCK_EXCLUSIVE, (rank + 1) % nproc, 0);
            win.compareAndSwap(next, iBuffer, result, MPI.INT, (rank + 1) % nproc, 0);
            win.unlock((rank + 1) % nproc);

        }

        MPI.COMM_WORLD.barrier();
        win.lock(MPI.LOCK_EXCLUSIVE, rank, 0);
        valPtr.put(0, 0);
        win.unlock(rank);
        MPI.COMM_WORLD.barrier();

        /* Test contention */

        if (rank != 0) {
            for (int i = 0; i < ITER; i++) {
                IntBuffer next = MPI.newIntBuffer(1);
                IntBuffer iBuffer = MPI.newIntBuffer(1);
                IntBuffer result = MPI.newIntBuffer(1);

                next.put(0, (i + 1));
                iBuffer.put(0, i);
                result.put(0, -1);

                win.lock(MPI.LOCK_EXCLUSIVE, 0, 0);
                win.compareAndSwap(next, iBuffer, result, MPI.INT, 0, 0);
                win.unlock(0);
            }
        }

        MPI.COMM_WORLD.barrier();

        win.free();

        MPI.COMM_WORLD.reduce(errors, allErrors, 1, MPI.INT, MPI.SUM, 0);

        if (rank == 0 && allErrors[0] == 0)
            System.out.printf(" No Errors\n");

        MPI.Finalize();
    }
}
