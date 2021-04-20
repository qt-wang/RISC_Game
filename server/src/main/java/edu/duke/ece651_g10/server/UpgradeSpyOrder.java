package edu.duke.ece651_g10.server;

public class UpgradeSpyOrder extends OneTerritoryOrder{

    public UpgradeSpyOrder(int playerID, String source, int unitNum, GameMap gMap, Player player){
        super(playerID, source, unitNum, gMap, 0, player);
    }

    public void execute(){
        Army fromArmy = source.getArmyWithLevel(level);
        fromArmy.decreaseUnits(unitNum);
        for(int i = 0; i < unitNum; i++){
            SpyUnit spy = new SpyUnit(player, source);
            source.addOwnedSpy(spy);
        }
        player.setTechnologyResourceTotal(player.getTechnologyResourceTotal() - 20);
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

}
