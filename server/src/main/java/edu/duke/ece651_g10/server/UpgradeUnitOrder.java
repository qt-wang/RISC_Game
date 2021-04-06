package edu.duke.ece651_g10.server;

import java.util.HashMap;

public class UpgradeUnitOrder extends OneTerritoryOrder {
  private int level;

  public UpgradeUnitOrder(int playerID, String source, int unitNum, GameMap gMap, int level, Player player) {
    super(playerID, source, unitNum, gMap, player);
    this.level = level;
  }


  public void execute() {
    // gMap.upgradeUnit(playerID, dest, unitNum, level);
    Army fromArmy = source.getArmyWithLevel(level);
    Army toArmy = source.getArmyWithLevel(level + 1);
    fromArmy.decreaseUnits(unitNum);
    toArmy.increaseUnits(unitNum);
    int foodCost = getFoodCost();
    int newFoodResource = player.getFoodResourceTotal() - foodCost;
    player.setFoodResourceTotal(newFoodResource);
  }

  public Territory getSourceTerritory() {
    return source;
  }

  public int getNumUnit() {
    return unitNum;
  }

  public int getLevel(){
    return level;
  }

  public int getFoodCost(){
    return unitUpgradeTable.get(level + 1);
  }

}


