package com.mathpar.LABS.Lab06;

import java.util.Arrays;

public class SimpleMult {
    static void multiply(int mat1[][],
                         int mat2[][], int res[][]) {
        int i, j, k;
        for (i = 0; i < mat1.length; i++) {
            for (j = 0; j < mat1.length; j++) {
                res[i][j] = 0;
                for (k = 0; k < mat1.length; k++)
                    res[i][j] += mat1[i][k]
                            * mat2[k][j];
            }
        }
    }

    // Driver code
    public static void main(String[] args) {
        int ord = Integer.parseInt(args[0]);

        int mat1[][] = new int[ord][ord];

        int mat2[][] = new int[ord][ord];

        for (int i = 0; i < ord; i++) {
            for (int j = 0; j < ord; j++) {
                mat1[i][j] = (int) ((Math.random() * (20 - 100)) + 100);
                mat2[i][j] = (int) ((Math.random() * (20 - 100)) + 100);
            }
            System.out.println();
        }
        System.out.println(Arrays.deepToString(mat1));
        System.out.println(Arrays.deepToString(mat2));

        double time = 0;
        long m = System.currentTimeMillis();

        // To store result
        int res[][] = new int[ord][ord];
        multiply(mat1, mat2, res);
        time = (double) (System.currentTimeMillis() - m) / 1000;
        System.out.println("Result matrix" +
                Arrays.deepToString(res) + " is ");
        System.out.println("Time: " + time);
    }
}
