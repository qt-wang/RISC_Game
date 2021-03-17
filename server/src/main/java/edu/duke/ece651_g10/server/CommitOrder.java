package edu.duke.ece651_g10.server;

public class CommitOrder extends Order {
    public CommitOrder(int playerID) {
        super(playerID);
    }

    @Override
    public int getNumUnit() {
        return 0;
    }

    @Override
    public int getPlayerID() {
        return 0;
    }

    @Override
    public void addUnits(int number) {

    }

    @Override
    public void execute() {

    }

    @Override
    public Territory getTargetTerritory() {
        return null;
    }

    @Override
    public Territory getSourceTerritory() {
        return null;
    }
}
