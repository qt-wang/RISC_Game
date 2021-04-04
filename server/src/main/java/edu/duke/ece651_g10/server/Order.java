package edu.duke.ece651_g10.server;
public abstract class Order {
    protected int playerID;

    public Order(int playerID) {
        this.playerID = playerID;
    }

  //public abstract int getNumUnit();

  //public abstract Territory getSourceTerritory();

  // public abstract Territory getTargetTerritory();

    public abstract void execute();

  //public abstract void addUnits(int number);

    public int getPlayerID(){
      return playerID;
    }
}













