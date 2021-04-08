package edu.duke.ece651_g10.server;

import java.util.Random;

public class MoveOrder extends TerritoryToTerritoryOrder {
  /*
   * private Territory source; private Territory dest; private int unitNum;
   * GameMap gMap;
   */
  // // HashSet<Unit> units;
  private ShortestPath sp;

  public MoveOrder(int playerID, String source, String dest, int unitNum, GameMap gMap, Player p, int level) { // need     // string
    super(playerID, source, dest, unitNum, gMap, p, level);
    this.sp = new ShortestPath(gMap);
  }

  /**
   * This function executes move order. It decreases the number of units in the
   * army of source territory, and increases the number of units in the army of
   * destination territory.
   */
  public void execute() {
    /*
     * boolean flag = false; while(!flag) { Random random = new Random(); int
     * randLevel = random.nextInt(7); Army sourceArmy =
     * source.getArmyWithLevel(randLevel); Army destArmy =
     * dest.getArmyWithLevel(randLevel); if(sourceArmy.getArmyUnits() >= unitNum) {
     * sourceArmy.decreaseUnits(unitNum); destArmy.increaseUnits(unitNum); flag =
     * true; }else if(sourceArmy.getArmyUnits() != 0 && sourceArmy.getArmyUnits() <
     * unitNum){ sourceArmy.decreaseUnits(sourceArmy.getArmyUnits());
     * destArmy.increaseUnits(sourceArmy.getArmyUnits()); unitNum -=
     * sourceArmy.getArmyUnits(); } }
     */
    source.decreaseUnit(unitNum, level);
    dest.increaseUnit(unitNum, level);
    int foodCost = sp.minCost(source, dest) * unitNum;
    player.setFoodResourceTotal(player.getFoodResourceTotal() - foodCost);
  }

  /**
   *
   * @return the unit number.
   */
  public int getNumUnit() {
    return unitNum;
  }

  /**
   *
   * @return the destination territory
   */
  public Territory getTargetTerritory() {
    return dest;
  }

  public void addUnits(int number) {
    unitNum += number;
  }

  /**
   *
   * @return the source territory
   */
  public Territory getSourceTerritory() {
    return source;
  }

  /**
   *
   * @return the level that a player want to move.
   */
  public int getLevel() {
    return level;
  }
}
