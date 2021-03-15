package edu.duke.ece651_g10.server;

public class MoveOrder extends Order{
    private Territory source;
    private Territory dest;
    private int unitNum;
    GameMap gMap;
    // HashSet<Unit> units;

    public MoveOrder(int playerID, String source, String dest, int unitNum, GameMap gMap){  //need to change Territory to string
        super(playerID);
        this.source = gMap.getTerritory(source);
        this.dest = gMap.getTerritory(dest);
        this.unitNum = unitNum;
    }

    /**
     *This function executes move order. It decreases the number of units in the army of
     *source territory, and increases the number of units in the army of destination territory.
     */
    public void execute(){
        checkValidMove();
        source.decreaseUnit(unitNum);
        dest.increaseUnit(unitNum);
    }

    private void checkValidMove(){
        RuleChecker rule = new PlayerSelfOrderChecker(new SelfTerritoryChecker(new SufficientUnitChecker(null)));
        rule.checkOrder(this, gameMap);
    }

    public Territory getSourceTerritory(){
        return source;
    }

    public Territory getTargetTerritory(){
        return dest;
    }

    public int getUnitNum(){
        return unitNum;
    }
}
