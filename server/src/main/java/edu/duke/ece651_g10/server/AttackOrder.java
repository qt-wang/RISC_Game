package edu.duke.ece651_g10.server;

import java.util.HashMap;

/**
 * This class aims to execute all attacks in one turn.
 * An attack needs an attacker, a defender and the number of involved units.
 * An attack must change the unit number in both territories, and may lead to a change
 * of territory ownership.
 */
public class AttackOrder extends Order{
    //private HashMap<Territory, Army> attack;
    private Territory attacker;
    private Territory defender;
    private  int unitNum;

    /**
     * @param playerID identifies the player who initiates this order.
     * @param att is the attacker territory
     * @param def is the defender territory
     * @param num is the unit number that the attacker sends to the defender.
     */
    public AttackOrder(int playerID, Territory att, Territory def, int num){
        super(playerID);
        this.attacker = att;
        this.defender = def;
        this.unitNum = num;
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
        attacker.decreaseUnit(unitNum);
        while(!attackerWin && !defenderWin){
            int attDice = dice.roll();
            int defDice = dice.roll();
            if(attDice > defDice){     //attacker wins a round
                defender.decreaseUnit(1);
            }
            else{                      //defender wins a round
                unitNum -= 1;
            }
            if(unitNum  == 0){
                defenderWin = true;
            }
            if(defender.getUnitNum() == 0){
                defender.setPlayerID(playerID);
                defender.setUnitNum(unitNum);
                attackerWin = true;
            }
        }
    }

    public Territory getSourceTerritory(){
        return attacker;
    }

    public Territory getTargetTerritory(){
        return defender;
    }

    public int getUnitNum(){
        return unitNum;
    }
}
