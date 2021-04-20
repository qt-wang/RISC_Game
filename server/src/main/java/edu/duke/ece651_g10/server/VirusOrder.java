package edu.duke.ece651_g10.server;

public class VirusOrder extends ZeroTerritoryOrder{
    Player targetPlayer;
    int virusLevel;
    public VirusOrder(int playerID, GameMap gMap, Player player, String territory, int vLevel){
        super(playerID, gMap, player);
        this.targetPlayer = gMap.getTerritory(territory).getOwner();
        this.virusLevel = vLevel;
    }

    public void execute(){
        int techAttack = 100 * virusLevel;
        int foodAttack = 200 * virusLevel;
        if(virusLevel > targetPlayer.getVaccineLevel()) {
            if (targetPlayer.getTechnologyResourceTotal() <= techAttack) {
                targetPlayer.setTechnologyResourceTotal(0);
            } else{
                targetPlayer.setTechnologyResourceTotal(targetPlayer.getTechnologyResourceTotal() - techAttack);
            }
            if (targetPlayer.getFoodResourceTotal() <= foodAttack) {
                targetPlayer.setFoodResourceTotal(0);
            } else{
                targetPlayer.setFoodResourceTotal(targetPlayer.getFoodResourceTotal() - foodAttack);
            }
            player.setTechnologyResourceTotal(player.getTechnologyResourceTotal() - 200); // One virus attack costs 100 tech resources
        }
    }
}
