package edu.duke.ece651_g10.server;

public class VaccineOrder extends ZeroTerritoryOrder{
    int vaccineLevel;
    public VaccineOrder(int playerID, GameMap gMap, Player player, int vLevel){
        super(playerID, gMap, player);
        this.vaccineLevel = vLevel;
    }

    public void execute(){
        if(vaccineLevel > player.getVaccineLevel()) {
            player.setVaccineLevel(vaccineLevel);
        }
    }

    public int getVaccineLevel(){
        return getVaccineLevel();
    }
}
