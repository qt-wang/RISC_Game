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
        boolean flag = false;
        while(!flag) {
            Random random = new Random();
            int randLevel = random.nextInt(7);
            Army sourceArmy = source.getArmyWithLevel(randLevel);
            Army destArmy = dest.getArmyWithLevel(randLevel);
            if(sourceArmy.getArmyUnits() >= unitNum) {
                sourceArmy.decreaseUnits(unitNum);
                destArmy.increaseUnits(unitNum);
                flag = true;
            }else if(sourceArmy.getArmyUnits() != 0 && sourceArmy.getArmyUnits() < unitNum){
                sourceArmy.decreaseUnits(sourceArmy.getArmyUnits());
                destArmy.increaseUnits(sourceArmy.getArmyUnits());
                unitNum -= sourceArmy.getArmyUnits();
            }
        }
        int foodCost = sp.minCost(source, dest);
        player.setFoodResourceTotal(player.getFoodResourceTotal() - foodCost);
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

 public Territory getSourceTerritory() {
        return source;
    }

}
