package edu.duke.ece651_g10.server;

public class UpgradeUnitOrder extends OneTerritoryOrder {
  private int level;

  public UpgradeUnitOrder(int playerID, String source, int  unitNum, GameMap gMap, int  level) {
    super(playerID, source, unitNum, gMap);
    this.level = level;
  }

  public void execute(){
    //gMap.upgradeUnit(playerID, dest, unitNum, level);
  }

  public Territory getSourceTerritory(){
    return source;
  }

  public  int getNumUnit(){
    return unitNum;
  }

  public int getLevel(){
    return level;
  }
  
}









