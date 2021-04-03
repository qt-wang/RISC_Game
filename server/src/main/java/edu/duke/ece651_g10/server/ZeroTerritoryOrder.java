package edu.duke.ece651_g10.server;

public abstract class ZeroTerritoryOrder extends Order {
  protected GameMap gMap;

  public ZeroTerritoryOrder(int playerID, GameMap gMap) {
        super(playerID);
        this.gMap = gMap;
   }

  public abstract void execute();
  
}


