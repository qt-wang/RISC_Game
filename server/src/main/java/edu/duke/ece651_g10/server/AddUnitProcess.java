package edu.duke.ece651_g10.server;

import java.util.List;

/**
 * This class is utilized to add one unit to every territory after one turn.
 */
public class AddUnitProcess{
    List<Territory> territoryList;

    /**
     * @param terrList is a list containing all territories on the game map.
     */
    public AddUnitProcess(List<Territory> terrList){
        this.territoryList = terrList;
    }

    /**
     * One unit is added to every territory on the game map.
     */
    public void addOneUnit() {
        for(Territory territory : territoryList){
          territory.increaseUnit(0,1);
        }
    }
}
