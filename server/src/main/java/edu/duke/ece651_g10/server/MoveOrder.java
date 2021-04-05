package edu.duke.ece651_g10.server;

import java.util.Random;

public class MoveOrder extends TerritoryToTerritoryOrder{
  /*private Territory source;
    private Territory dest;
    private int unitNum;
    GameMap gMap;*/
//    // HashSet<Unit> units;
    private ShortestPath sp;

    public MoveOrder(int playerID, String source, String dest, int unitNum, GameMap gMap, Player p){  //need to change Territory to string
      super(playerID, source, dest, unitNum, gMap, p);
      this.sp = new ShortestPath(gMap);
    }

    /**
     *This function executes move order. It decreases the number of units in the army of
     *source territory, and increases the number of units in the army of destination territory.
     */
    public void execute(){
        source.decreaseUnit(unitNum);
        dest.increaseUnit(unitNum);
        //找到source要移动的level的army， 减少数量。
        //找到dest要移动的level的army，增加数量。
        //通过minCostCalculator 计算从source至dest的最小cost。 可能在MoveOrder中新建一个属性叫ShortestPath，
        //是一个类， 类里面有一个方法叫minCostCalculator计算最小cost。或者直接在MoveOrder中加一个方法来计算minCost？
        //从player的foodResource中减去损耗。
        Random random = new Random();
        int randLevel = random.nextInt(7);
        //随机生成level数，如果这个level的unitNum不够，则先把这些unitNum减掉，然后再随机生成level数
        //因为一个territory的总共unitNum一定是大于MoveOrder的unitNum的，这是rulechecker做到的，所以
        //这样随机做一定可以完成的。
        Army sourceArmy = source.getArmyWithLevel(randLevel);
        source.decreaseUnit(unitNum);
        Army destArmy = dest.getArmyWithLevel(randLevel);
        destArmy.increaseUnits(unitNum);
        int foodCost = sp.minCost(source, dest);
        player.setFoodResourceTotal(player.getFoodResourceTotal() - foodCost);
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












