package edu.duke.ece651_g10.server;

public class V2ArmyFactory implements ArmyFactory {

    public V2ArmyFactory() {

    }

    @Override
    public Army generateLZeroArmy(int units) {
        return new V2Army(0, 0, units);
    }

    @Override
    public Army generateLOneArmy(int units) {
        return new V2Army(1, 1, units);
    }

    @Override
    public Army generateLTwoArmy(int units) {
        return new V2Army(2, 3, units);
    }

    @Override
    public Army generateLThreeArmy(int units) {
        return new V2Army(3, 5, units);
    }

    @Override
    public Army generateLFourArmy(int units) {
        return new V2Army(4, 8, units);
    }

    @Override
    public Army generateLFiveArmy(int units) {
        return new V2Army(5, 11, units);
    }

    @Override
    public Army generateLSixArmy(int units) {
        return new V2Army(6, 15, units);
    }

}
