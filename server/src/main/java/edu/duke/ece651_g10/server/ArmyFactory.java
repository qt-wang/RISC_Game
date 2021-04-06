package edu.duke.ece651_g10.server;

/**
 * Army factory, used to generate specific level armies.
 */
public interface ArmyFactory {
    public Army generateLZeroArmy(int units);

    public Army generateLOneArmy(int units);

    public Army generateLTwoArmy(int units);

    public Army generateLThreeArmy(int units);

    public Army generateLFourArmy(int units);

    public Army generateLFiveArmy(int units);

    public Army generateLSixArmy(int units);

}














