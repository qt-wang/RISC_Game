package edu.duke.ece651_g10.server;

public class ResearchCloakOrder extends ZeroTerritoryOrder{
    public ResearchCloakOrder(int playerID, GameMap gMap, Player player){
        super(playerID, gMap, player);
    }

    public void execute(){
        player.setTechnologyResourceTotal(player.getTechnologyResourceTotal() - 100);
        player.setCanResearchCloak(false);
    }

}
