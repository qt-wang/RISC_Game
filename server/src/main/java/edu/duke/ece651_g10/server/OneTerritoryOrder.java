package edu.duke.ece651_g10.server;

import java.util.HashMap;

public abstract class OneTerritoryOrder extends Order {
  protected Territory source;
  protected int unitNum;
  protected GameMap gMap;
  protected Player player;
  final HashMap<Integer, Integer> unitUpgradeTable;

  public OneTerritoryOrder(int playerID, String source, int unitNum, GameMap gMap, Player player) {
    super(playerID);
    this.gMap = gMap;
    this.source = gMap.getTerritory(source);
    this.unitNum = unitNum;
    this.player = player;
    this.unitUpgradeTable = new HashMap<Integer, Integer>();
    this.unitUpgradeTable.put(0, 3);
    this.unitUpgradeTable.put(1, 8);
    this.unitUpgradeTable.put(2, 19);
    this.unitUpgradeTable.put(3, 25);
    this.unitUpgradeTable.put(4, 35);
    this.unitUpgradeTable.put(5, 50);
  }

  public HashMap<Integer, Integer> getUnitUpgradeTable() {
    return unitUpgradeTable;
  }

  public abstract Territory getSourceTerritory();

  public abstract void execute();

  public abstract int getNumUnit();

  public abstract int getLevel();

}
