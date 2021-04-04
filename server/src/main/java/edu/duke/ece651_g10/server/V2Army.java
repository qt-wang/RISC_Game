package edu.duke.ece651_g10.server;

public class V2Army implements Army{

    final int level;

    final int bonus;

    int units;

    public V2Army(int level, int bonus, int units) {
        this.level = level;
        this.bonus = bonus;
        this.units = units;
    }

    @Override
    public void decreaseUnits(int numberOfUnits) {
        this.units -= numberOfUnits;
    }

    @Override
    public void increaseUnits(int numberOfUnits) {
        this.units += numberOfUnits;
    }

    @Override
    public int getArmyUnits() {
        return this.units;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getBonus() {
        return this.bonus;
    }
}
