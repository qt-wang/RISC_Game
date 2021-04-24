package edu.duke.ece651_g10.server;

public class CloakOrder extends OneTerritoryOrder{
    public CloakOrder(int playerID, String source,  GameMap gMap, Player player){
        super(playerID, source, 0, gMap, 0, player);
    }

    public void execute(){
        source.getCloaked();
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

}
