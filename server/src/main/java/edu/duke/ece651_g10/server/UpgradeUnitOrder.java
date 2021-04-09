package edu.duke.ece651_g10.server;


public class UpgradeUnitOrder extends OneTerritoryOrder {
  private int level;

  /**
   *
   * @param playerID is the ID of player that initiate the order
   * @param source is the string of source territory
   * @param unitNum is is unit number
   * @param gMap is the game map
   * @param level is the level that a player want to upgrade
   * @param player is the player that initiate the order
   */
  public UpgradeUnitOrder(int playerID, String source, int unitNum, GameMap gMap, int level, Player player) {
    super(playerID, source, unitNum, gMap, player);
    this.level = level;
  }

  /**
   * This function executes the upgrade order.
   */
  public void execute() {
    // gMap.upgradeUnit(playerID, dest, unitNum, level);
    Army fromArmy = source.getArmyWithLevel(level);
    Army toArmy = source.getArmyWithLevel(level + 1);
    fromArmy.decreaseUnits(unitNum);
    toArmy.increaseUnits(unitNum);
    int technologyCost = getTechnologyCost();
    int newTechnologyResource = player.getTechnologyResourceTotal() - technologyCost;
    player.setTechnologyResourceTotal(newTechnologyResource);
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
   * @return the unit number
   */
  public int getNumUnit() {
    return unitNum;
  }

  /**
   *
   * @return the the level that a player want to upgrade
   */
  public int getLevel(){
    return level;
  }

  /**
   *
   * @return return the technology cost that this order needs
   */
  public int getTechnologyCost(){
    return unitUpgradeTable.get(level + 1);
  }

}


