package edu.duke.ece651_g10.server;

public class V2ArmyFactory implements ArmyFactory {
    public V2ArmyFactory() {

    }

    @Override
    public Army generateLZeroArmy(int units) {
        //return null;
        Army army = new V2Army(0, 0, units);
        return army;
    }

    @Override
    public Army generateLOneArmy(int units) {
        //return null;
        Army army = new V2Army(1, 1, units);
        return army;
    }

    @Override
    public Army generateLTwoArmy(int units) {
        //return null;
        Army army = new V2Army(2, 3, units);
        return army;
    }

    @Override
    public Army generateLThreeArmy(int units) {
        //return null;
        Army army = new V2Army(3, 5, units);
        return army;
    }

    @Override
    public Army generateLFourArmy(int units) {
        //return null;
        Army army = new V2Army(4, 8, units);
        return army;
    }

    @Override
    public Army generateLFiveArmy(int units) {
        //return null;
        Army army = new V2Army(5, 11, units);
        return army;
    }

    @Override
    public Army generateLSixArmy(int units) {
        //return null;
        Army army = new V2Army(6, 15, units);
        return army;
    }
}
