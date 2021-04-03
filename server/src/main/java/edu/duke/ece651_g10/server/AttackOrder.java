package edu.duke.ece651_g10.server;

import java.util.HashMap;

/**
 * This class aims to execute all attacks in one turn.
 * An attack needs an attacker, a defender and the number of involved units.
 * An attack must change the unit number in both territories, and may lead to a change
 * of territory ownership.
 */
public class AttackOrder extends TerritoryToTerritoryOrder{
    //private HashMap<Territory, Army> attack;
    //private Territory attacker;
    //private Territory defender;
    //private  int unitNum;
    private Player owner;
  //private GameMap gMap;

    /**
     * @param playerID identifies the player who initiates this order.
     * @param att is the attacker territory
     * @param def is the defender territory
     * @param num is the unit number that the attacker sends to the defender.
     */
    public AttackOrder(int playerID, String att, String def, int num, GameMap gMap, Player player){
      super(playerID, att, def, num, gMap);
        /*this.owner = player;
        this.attacker = gMap.getTerritory(att);
        this.defender = gMap.getTerritory(def);
        this.unitNum = num;
        this.gMap = gMap;*/
        this.owner = player;
    }

    /**
     * This function executes an attack order.
     * If the attacker wins, unit number in both territories decrease and the the attacker
     * takes the ownership of defender territory.
     * If the defender wins, unit number in both territory decrease.
     */
    public void execute(){
        Dice dice = new TwentySidesDice();
        boolean attackerWin = false;
        boolean defenderWin = false;
        //System.out.println(defender.getNumUnit());
        if(dest.getNumUnit() == 0){
            attackerWin = true;
            dest.setOwner(owner);
            dest.increaseUnit(unitNum);
            return;
        }
        //attacker.decreaseUnit(unitNum);
        if(dest.getNumUnit() == 0){
            attackerWin = true;
            dest.setOwner(source.getOwner());
            dest.increaseUnit(unitNum);
        }
        while(!attackerWin && !defenderWin){
            long seed = System.currentTimeMillis();
            int attDice = dice.roll(seed);
            int defDice = dice.roll(seed+1000);
            if(attDice > defDice){     //attacker wins a round
                dest.decreaseUnit(1);
            }
            else{                      //defender wins a round
                unitNum -= 1;
            }
            if(unitNum  == 0){
                defenderWin = true;
            }
            if(dest.getNumUnit() == 0){
                dest.setOwner(owner);
                dest.increaseUnit(unitNum);
                attackerWin = true;
            }
        }
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






