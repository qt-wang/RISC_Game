package edu.duke.ece651_g10.server;

import java.util.HashSet;

/**
 * The army represents a number of units belong to a specific player.
 * Maintained by Guancheng Fu
 */
public interface Army {

    public void decreaseUnits(int numberOfUnits);

    public void increaseUnits(int numberOfUnits);

    public int getArmyUnits();

    public int getLevel();

    public int getBonus();
}
