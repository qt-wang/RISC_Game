package edu.duke.ece651_g10.server;

public class VirusOrder extends OneTerritoryOrder{
    Player targetPlayer;
    public VirusOrder(int playerID, GameMap gMap, Player player, String territory, int vLevel){
        super(playerID, territory, 0, gMap, vLevel, player);
        this.targetPlayer = gMap.getTerritory(territory).getOwner();
    }

    public void execute(){
        int techAttack = 100 * level;
        int foodAttack = 200 * level;
        if(level > targetPlayer.getVaccineLevel()) {
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
