package edu.duke.ece651_g10.server;

import java.util.HashMap;

public abstract class ZeroTerritoryOrder extends Order {
  protected GameMap gMap;
  protected Player player;
  final HashMap<Integer, Integer> maxTechLevelTable;

  public ZeroTerritoryOrder(int playerID, GameMap gMap, Player player) {
        super(playerID);
        this.gMap = gMap;
        this.player = player;
        this.maxTechLevelTable = new HashMap<Integer, Integer>();
        this.maxTechLevelTable.put(1, 50);
        this.maxTechLevelTable.put(2, 75);
        this.maxTechLevelTable.put(3, 125);
        this.maxTechLevelTable.put(4, 200);
        this.maxTechLevelTable.put(5, 300);
   }

  public abstract void execute();

  // Added by yt136
  public Player getPlayer() {
    return player;
  }

  public HashMap<Integer, Integer> getMaxTechLevelTable() {
    return maxTechLevelTable;
  }
  
}












