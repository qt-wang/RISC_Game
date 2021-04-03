package edu.duke.ece651_g10.server;

public class MoveOrder extends TerritoryToTerritoryOrder{
  /*private Territory source;
    private Territory dest;
    private int unitNum;
    GameMap gMap;*/
//    // HashSet<Unit> units;
    public MoveOrder(int playerID, String source, String dest, int unitNum, GameMap gMap, Player p){  //need to change Territory to string
      super(playerID, source, dest, unitNum, gMap, p);
    }

    /**
     *This function executes move order. It decreases the number of units in the army of
     *source territory, and increases the number of units in the army of destination territory.
     */
    public void execute(){
        //checkValidMove();
        source.decreaseUnit(unitNum);
        dest.increaseUnit(unitNum);
    }

   public Territory getSourceTerritory() {
        return source;
    }

    public int getNumUnit() {
        return unitNum;
    }

    public Territory getTargetTerritory() {
        return dest;
    }

    public void addUnits(int number) {
        unitNum += number;
    }


}












