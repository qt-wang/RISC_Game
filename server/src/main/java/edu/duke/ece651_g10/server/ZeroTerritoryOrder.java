package edu.duke.ece651_g10.server;

import java.util.HashMap;

public abstract class ZeroTerritoryOrder extends Order {
  protected GameMap gMap;
  protected Player player;
  final HashMap<Integer, Integer> maxTechLevelTable;
  final HashMap<Integer, Integer> maxVirusLevelTable;
  final HashMap<Integer, Integer> maxVaccineLevelTable;

    /**
     *
     * @param playerID is the ID of player that initiate the order
     * @param gMap is the game map
     * @param player is the player that initiate the order
     */
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
        this.maxVirusLevelTable = new HashMap<>();
        this.maxVirusLevelTable.put(1, 30);
        this.maxVirusLevelTable.put(2, 60);
        this.maxVirusLevelTable.put(3, 110);
        this.maxVirusLevelTable.put(4, 220);
        this.maxVirusLevelTable.put(5, 330);
        this.maxVaccineLevelTable = new HashMap<>();
        this.maxVaccineLevelTable.put(1, 60);
        this.maxVaccineLevelTable.put(2, 100);
        this.maxVaccineLevelTable.put(3, 170);
        this.maxVaccineLevelTable.put(4, 250);
        this.maxVaccineLevelTable.put(5, 340);
   }

  public abstract void execute();

  // Added by yt136
  public Player getPlayer() {
    return player;
  }

  public HashMap<Integer, Integer> getMaxTechLevelTable() {
    return maxTechLevelTable;
  }

  public HashMap<Integer, Integer> getMaxVirusLevelTable() {
        return maxVirusLevelTable;
    }
  
}












