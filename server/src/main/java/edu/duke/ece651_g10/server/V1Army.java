package edu.duke.ece651_g10.server;

import java.util.Set;

/**
 * A class that implements the Army interface.
 * Maintained by Guancheng Fu
 */
public class V1Army implements Army{

    private Set<Unit> units;


    public V1Army(Set<Unit> units) {
        this.units = units;
    }

    @Override
    public int getArmyUnits() {
        return units.size();
    }

    @Override
    public void increaseUnits(int numberOfUnits) {
        for (int i = 0; i < numberOfUnits; i ++) {
            units.add(new Unit());
        }
    }

    @Override
    public void decreaseUnits(int numberOfUnits) {

    }
}
