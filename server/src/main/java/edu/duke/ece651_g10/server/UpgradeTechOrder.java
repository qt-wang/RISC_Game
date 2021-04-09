package edu.duke.ece651_g10.server;

public class UpgradeTechOrder extends ZeroTerritoryOrder {

  /**
   *
   * @param playerID is the ID of player that initiate the order
   * @param gMap is the game map
   * @param player is the player that initiate the order
   */
  public UpgradeTechOrder(int playerID, GameMap gMap, Player player) {
    super(playerID, gMap, player);
  }

  /**
   * This function executes this order.
   */
  public void execute() {
    // gMap.upgradeTech(playerID, gMap);
    /*int currentTechLevel = player.getTechnologyLevel();
    int currentTechResource = player.getTechnologyResourceTotal();
    int newTechLevel = currentTechLevel + 1;
    int techResourceCost = 25 * (newTechLevel - 1) * (newTechLevel - 2) / 2 + 50;
    int newTechResource = currentTechResource - techResourceCost;
    player.setTechnologyResourceTotal(newTechResource);*/
    player.incrementTechnologyLevel();
  }
}













