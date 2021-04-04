package edu.duke.ece651_g10.server;

import java.util.HashMap;

public class UpgradeUnitOrder extends OneTerritoryOrder {
  private int fromLevel;
  private HashMap<Integer, Integer> costTable;

  public UpgradeUnitOrder(int playerID, String source, int  unitNum, GameMap gMap, int  level, Player player) {
    super(playerID, source, unitNum, gMap, player);
    this.fromLevel = level;
    this.costTable = new HashMap<>();
    this.costTable.put(0, 0);
    this.costTable.put(1, 3);
    this.costTable.put(2, 8);
    this.costTable.put(3, 19);
    this.costTable.put(4, 25);
    this.costTable.put(5, 35);
    this.costTable.put(6, 50);
  }

  public void execute(){
    //gMap.upgradeUnit(playerID, dest, unitNum, level);
    //找到source中的所有army. 每个territory有7个army的对象， 可以用hashmap来存储， 想要哪个level的就直接在
    //hashmap里找， 然后返回这个对象。
    //用上面的方法找到想要升级的那个level的对象，减少数量，找到想要升级到的level， 增加数量。
    //对player的foodResource进行减少.
    Army fromArmy = source.getArmay(fromLevel);
    Army toArmy = source.getArmy(fromLevel + 1);
    fromArmy.decreaseUnits(unitNum);
    toArmy.increaseUnits(unitNum);
    int foodCost = getFoodCost();
    int newFoodResource = player.getFoodResourceTotal() - foodCost;
    player.setFoodResourceTotal(newFoodResource);
  }

  public Territory getSourceTerritory(){
    return source;
  }

  public  int getNumUnit(){
    return unitNum;
  }

  public int getFromLevel(){
    return fromLevel;
  }

  public int getFoodCost(){
    return costTable.get(fromLevel + 1);
  }
}









