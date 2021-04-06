package edu.duke.ece651_g10.server;

public class UpgradeTechOrder extends ZeroTerritoryOrder {

  public UpgradeTechOrder(int playerID, GameMap gMap, Player player) {
    super(playerID, gMap, player);
  }
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













