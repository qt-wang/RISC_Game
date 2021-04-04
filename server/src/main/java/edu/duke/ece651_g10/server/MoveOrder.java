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
        //找到source要移动的level的army， 减少数量。
        //找到dest要移动的level的army，增加数量。
        //通过minCostCalculator 计算从source至dest的最小cost。 可能在MoveOrder中新建一个属性叫ShortestPath，
        //是一个类， 类里面有一个方法叫minCostCalculator计算最小cost。或者直接在MoveOrder中加一个方法来计算minCost？
        //从player的foodResource中减去损耗。
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












