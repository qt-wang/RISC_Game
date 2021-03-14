package edu.duke.ece651_g10.server;
public abstract class Order {
    int playerID;

    public Order(int playerID) {
        this.playerID = playerID;
    }

    public abstract int getUnitNum();

    public abstract Territory getSourceTerritory();

    public abstract Territory getTargetTerritory();

    public abstract void execute();

    public int getPlayerID(){
      return playerID;
    }
}













