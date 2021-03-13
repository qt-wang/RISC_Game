package edu.duke.ece651_g10.server;

import java.util.HashMap;

/**
 * This class aims to execute all attacks in one turn.
 * An attack needs an attacker, a defender and the number of involved units.
 * An attack must change the unit number in both territories, and may lead to a change
 * of territory ownership.
 */
public class AttackOrder implements Order{
    //private HashMap<Territory, Army> attack;
    private Territory attacker;
    private Territory defender;
    private  int unitNum;

    /**
     * @param att is the attacker territory
     * @param def is the defender territory
     * @param num is the unit number that the attacker sends to the defender.
     * @param s is the player who starts the attack.
     */
    public AttackOrder(Player s, Territory att, Territory def, int num){
        this.attacker = att;
        this.defender = def;
        this.unitNum = num;
        //this.starter = s;
    }

    /**
     * This function executes an attack order.
     * If the attacker wins, unit number in both territories decrease and the the attacker
     * takes the ownership of defender territory.
     * If the defender wins, unit number in both territory decrease.
     */
    public void execute(){
    }

    public Territory getSourceTerritory(){
        return attacker;
    }

    public Territory getDestinationTerritory(){
        return defender;
    }

    public int getUnitNum(){
        return unitNum;
    }
}
