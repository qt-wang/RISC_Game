package edu.duke.ece651_g10.server;

import java.util.Random;

/**
* A Dice which can return a random number between [0, 20).
*/
public class TwentySidesDice implements Dice{

//    @Override
//    public int roll() {
//        Random random = new Random();
//        return random.nextInt(20);
//    }

    @Override
    public int roll(long seed) {
        Random random = new Random();
        return random.nextInt(20);
    }
}
