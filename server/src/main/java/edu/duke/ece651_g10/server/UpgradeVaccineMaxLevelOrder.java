package edu.duke.ece651_g10.server;

public class UpgradeVaccineMaxLevelOrder extends ZeroTerritoryOrder{

    public UpgradeVaccineMaxLevelOrder(int playerID, GameMap gMap, Player player){
        super(playerID, gMap, player);
    }

    public void execute(){
        int currentVaccineMaxLevel = player.getVaccineMaxLevel();
        int techCost = maxVaccineLevelTable.get(currentVaccineMaxLevel);
        player.setTechnologyResourceTotal(player.getTechnologyResourceTotal() - techCost);
        player.incrementVaccineMaxLevel();
    }
}
