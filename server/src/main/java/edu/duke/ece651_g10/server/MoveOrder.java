package edu.duke.ece651_g10.server;

public class MoveOrder implements Order{
    private Territory source;
    private Territory dest;
    private int unitNum;
    // HashSet<Unit> units;

    public MoveOrder(Territory source, Territory dest, int unitNum){
        this.source = source;
        this.dest = dest;
        this.unitNum = unitNum;
    }

    /**
     *This function executes move order. It decreases the number of units in the army of
     *source territory, and increases the number of units in the army of destination territory.
     */
    public void execute(){
        source.decreaseUnit(unitNum);
        dest.increaseUnit(unitNum);
    }

    public Territory getSourceTerritory(){
        return source;
    }

    public Territory getDestinationTerritory(){
        return dest;
    }

    public int getUnitNum(){
        return unitNum;
    }
}
