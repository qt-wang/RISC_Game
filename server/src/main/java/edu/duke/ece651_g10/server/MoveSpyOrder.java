package edu.duke.ece651_g10.server;

public class MoveSpyOrder extends TerritoryToTerritoryOrder{
    public MoveSpyOrder(int playerID, String source, String dest, int unitNum, GameMap gMap, Player p) {
        super(playerID, source, dest, unitNum, gMap, p, 0);
    }

    public void execute(){
        for(Spy spy : source.getOwnedSpies()){
            spy.moveTo(dest);
            unitNum--;
            if(unitNum <= 0){
                break;
            }
        }
    }

    /**
     *
     * @return the unit number.
     */
    public int getNumUnit() {
        return unitNum;
    }

    /**
     *
     * @return the destination territory
     */
    public Territory getTargetTerritory() {
        return dest;
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
     * @return the level that a player want to move.
     */
    public int getLevel() {
        return level;
    }

    public void addUnits(int number) {
        unitNum += number;
    }

    public Player getPlayer(){
        return player;
    }
}
