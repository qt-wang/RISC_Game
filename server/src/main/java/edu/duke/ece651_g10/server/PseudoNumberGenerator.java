package edu.duke.ece651_g10.server;

import java.util.Random;

public class PseudoNumberGenerator implements RandomNumberGenerator{
    Random rand;

    @Override
    public int nextInt(int bound) {
        return rand.nextInt(bound);
    }

    @Override
    public int nextInt() {
        return rand.nextInt();
    }

    public PseudoNumberGenerator() {
        rand = new Random();
    }

    public PseudoNumberGenerator(long seed){
        rand = new Random(seed);
    }
}
