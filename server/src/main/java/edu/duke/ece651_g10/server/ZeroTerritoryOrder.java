package edu.duke.ece651_g10.server;

import java.util.HashMap;

public abstract class ZeroTerritoryOrder extends Order {
  protected GameMap gMap;
  protected Player player;
  final HashMap<Integer, Integer> maxTechLevel;

  public ZeroTerritoryOrder(int playerID, GameMap gMap, Player player) {
        super(playerID);
        this.gMap = gMap;
        this.player = player;
        this.maxTechLevel = new HashMap<Integer, Integer>();
        this.maxTechLevel.put(1, 50);
        this.maxTechLevel.put(2, 75);
        this.maxTechLevel.put(3, 125);
        this.maxTechLevel.put(4, 200);
        this.maxTechLevel.put(5, 300);
   }

  public abstract void execute();

  // Added by yt136
  public Player getPlayer() {
    return player;
  }

  public HashMap<Integer, Integer> getMaxTechLevel() {
    return maxTechLevel;
  }
  
}












