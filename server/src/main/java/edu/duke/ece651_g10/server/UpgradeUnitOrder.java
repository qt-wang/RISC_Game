package edu.duke.ece651_g10.server;

public class UpgradeUnitOrder extends Order {
  private String dest;
  private int unitNum;
  private GameMap gMap;
  private int level;

  public UpgradeUnitOrder(int playerID, String dest, int  unitNum, GameMap gMap, int  level) {
    super(playerID);
    this.dest = dest;
    this.unitNum = unitNum;
    this.level = level;
    this.gMap = gMap;
  }

  public void execute(){
    //gMap.upgradeUnit(playerID, dest, unitNum, level);
  }
  
}









