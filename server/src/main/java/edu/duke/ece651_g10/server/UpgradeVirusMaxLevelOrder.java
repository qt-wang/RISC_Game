package edu.duke.ece651_g10.server;

public class UpgradeVirusMaxLevelOrder extends ZeroTerritoryOrder{
    public UpgradeVirusMaxLevelOrder(int playerID, GameMap gMap, Player player){
        super(playerID, gMap, player);
    }

    public void execute(){
        int currentVirusMaxLevel = player.getVirusMaxLevel();
        int techCost = maxVirusLevelTable.get(currentVirusMaxLevel);
        player.setTechnologyResourceTotal(player.getTechnologyResourceTotal() - techCost);
        player.incrementVirusMaxLevel();
    }
}
