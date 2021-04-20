package edu.duke.ece651_g10.server;

public class VaccineOrder extends ZeroTerritoryOrder{
    int vaccineLevel;
    public VaccineOrder(int playerID, GameMap gMap, Player player, int vLevel){
        super(playerID, gMap, player);
        this.vaccineLevel = vLevel;
    }

    public void execute(){
        player.setVaccineLevel(vaccineLevel);
        int techCost = 50 * vaccineLevel;
        player.setTechnologyResourceTotal(player.getTechnologyResourceTotal() - techCost);
    }
}
