package edu.duke.ece651_g10.server;

import java.util.Random;


/**
 * Generate random number with specified range, and total.
 */
public class V2SetNumGenerator implements SetSumGenerator {
    private final double[][] distributionTable;
    private static final Random rand = new Random();
    private int numberCount;
    private int min;
    private int range;
    private int sum;

    /**
     * Construct a PermutationPartitionGenerator.
     *
     * @param numberCount How many numbers are in the set.
     * @param min         Each number's minimum value.
     * @param max         Each number's maximum value.
     * @param sum         The sum for all these numbers.
     */
    public V2SetNumGenerator(int numberCount, int min, int max, int sum) {
        this.numberCount = numberCount;
        this.min = min;
        this.range = max - min;
        sum -= numberCount * min;
        this.sum = sum;
        distributionTable = calculateSolutionCountTable();
    }

    //TODO: may need to change this.
    /**
     * Private helper method
     * @return
     */
    private double[][] calculateSolutionCountTable() {
        double[][] table = new double[numberCount + 1][sum + 1];
        Integer[] a = new Integer[sum + 1];
        Integer[] b = new Integer[sum + 1];
        for (int i = 1; i <= sum; i++) {
            a[i] = 0;
        }
        a[0] = 1;
        table[0][0] = 1;
        for (int n = 1; n <= numberCount; n++) {
            double[] t = table[n];
            for (int s = 0; s <= sum; s++) {
                int z = 0;
                for (int i = Math.max(0, s - range); i <= s; i++) {
                    z = z + a[i];
                }
                b[s] = z;
                t[s] = z;
            }
            // swap a and b
            Integer[] c = b;
            b = a;
            a = c;
        }
        return table;
    }

    @Override
    public int[] get() {
        int[] p = new int[numberCount];
        int s = sum; // current sum
        for (int i = numberCount - 1; i >= 0; i--) {
            double t = rand.nextDouble() * distributionTable[i + 1][s];
            double[] tableRow = distributionTable[i];
            int oldSum = s;
            // lowerBound is introduced only for safety, it shouldn't be crossed 
            int lowerBound = s - range;
            if (lowerBound < 0)
                lowerBound = 0;
            s++;
            do
                t -= tableRow[--s];
                // s can be equal to lowerBound here with t > 0 only due to imprecise subtraction
            while (t > 0 && s > lowerBound);
            p[i] = min + (oldSum - s);
        }
        //assert s == 0;
        return p;
    }
}


