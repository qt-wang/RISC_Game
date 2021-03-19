package edu.duke.ece651_g10.server;

/**
 * Dice should have a method to generate a random number.
 */
public interface Dice {

//    public int roll();
    public int roll(long seed);
}
