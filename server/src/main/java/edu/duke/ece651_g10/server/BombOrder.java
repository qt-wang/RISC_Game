package edu.duke.ece651_g10.server;

import java.util.HashSet;
import java.util.Set;

public class BombOrder extends OneTerritoryOrder{
    public BombOrder(int playerID, String source, GameMap gMap, Player player){
        super(playerID, source, 0, gMap, 0, player);
    }

    public void execute(){
        for(int i = 0; i < 7; i++){
            source.getArmyWithLevel(i).decreaseUnits(source.getArmyWithLevel(i).getArmyUnits());
        }
    }

    /**
     *
     * @return the unit number.
     */
    public int getNumUnit() {
        return unitNum;
    }


    public void addUnits(int number) {
        unitNum += number;
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
}
