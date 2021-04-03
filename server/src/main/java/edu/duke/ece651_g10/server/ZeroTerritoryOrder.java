package edu.duke.ece651_g10.server;

public abstract class ZeroTerritoryOrder extends Order {
  protected GameMap gMap;
  protected Player player;

  public ZeroTerritoryOrder(int playerID, GameMap gMap, Player player) {
        super(playerID);
        this.gMap = gMap;
        this.player = player;
   }

  public abstract void execute();

  // Added by yt136
  public Player getPlayer() {
    return player;
  }
  
}










