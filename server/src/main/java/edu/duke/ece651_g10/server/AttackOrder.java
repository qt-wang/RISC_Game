package edu.duke.ece651_g10.server;

import java.util.*;

/**
 * This class aims to execute all attacks in one turn. An attack needs an
 * attacker, a defender and the number of involved units. An attack must change
 * the unit number in both territories, and may lead to a change of territory
 * ownership.
 */
public class AttackOrder extends TerritoryToTerritoryOrder {

    private ArrayList<Integer> attackLevel;

    private void initiateAttackLevel() {
        this.attackLevel = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        attackLevel.set(level, unitNum);
    }

    /**
     * @param playerID identifies the player who initiates this order.
     * @param att      is the attacker territory
     * @param def      is the defender territory
     * @param num      is the unit number that the attacker sends to the defender.
     */
    public AttackOrder(int playerID, String att, String def, int num, GameMap gMap, Player player, int level) {
        super(playerID, att, def, num, gMap, player, level);
        initiateAttackLevel();
    }

    private int getLowestAttackerLevel() {
        for (int i = 0; i < attackLevel.size(); ++i) {
            if (attackLevel.get(i) > 0) {
                return i;
            }
        }
        return 0;// should never reach here
    }

    private int getHighestAttackerLevel() {
        for (int j = attackLevel.size() - 1; j >= 0; --j) {
            if (attackLevel.get(j) > 0) {
                return j;
            }
        }
        return 0;// should never reach here
    }

    private int getLowestDefenderLevel(Territory t) {
        for (int j = 0; j < 7; ++j) {
            if (t.getArmyWithLevel(j).getArmyUnits() > 0) {
                return j;
            }
        }
        return 0;// should never reach here
    }

    private int getHighestDefenderLevel(Territory t) {
        for (int j = 6; j >= 0; --j) {
            if (t.getArmyWithLevel(j).getArmyUnits() > 0) {
                return j;
            }
        }
        return 0;// should never reach here
    }

    /**
     * This function executes an attack order. If the attacker wins, unit number in
     * both territories decrease and the the attacker takes the ownership of
     * defender territory. If the defender wins, unit number in both territory
     * decrease.
     */
    public void execute() {
        if (dest.getNumUnit() == 0) {
            for (int i = 0; i < 7; i++) {
                dest.getArmyWithLevel(i).increaseUnits(attackLevel.get(i));
                dest.setOwner(player);
            }
            return;
        }
        boolean flag = true;
        boolean attackerLost = false;
        boolean defenderLost = true;
        int roundLoser = 0;// 0 represents that attacker lost the previous battle of units, 1 represents
        // that defender lost, it is initially set to 0 because we will let the higest
        // bonus unit of attacker to attack the lowest bonus of defender unit first
        while (flag) {
            int attackHighestLevel = this.getHighestAttackerLevel();
            int attackLowestLevel = this.getLowestAttackerLevel();
            int defendHighestLevel = this.getHighestDefenderLevel(dest);
            int defendLowestLevel = this.getLowestDefenderLevel(dest);
            int attackBonus = 0;
            int defenderBonus = 0;
            int aLevel;
            int dLevel;
            if (roundLoser == 0) { // Attacker Lost last round
                attackBonus = source.getArmyWithLevel(attackHighestLevel).getBonus();
                defenderBonus = dest.getArmyWithLevel(defendLowestLevel).getBonus();
                aLevel = attackHighestLevel;
                dLevel = defendLowestLevel;
            } else {
                attackBonus = source.getArmyWithLevel(attackLowestLevel).getBonus();
                defenderBonus = dest.getArmyWithLevel(defendHighestLevel).getBonus();
                aLevel = attackLowestLevel;
                dLevel = defendHighestLevel;
            }
            Random dice1 = new Random();
            int attackDice = dice1.nextInt(20) + 1 + attackBonus;
            Random dice2 = new Random();
            int defenderDice = dice2.nextInt(20) + 1 + defenderBonus;
            if (defenderDice >= attackDice) {
                attackLevel.set(aLevel, attackLevel.get(aLevel) - 1);
                roundLoser = 0;
            } else {
                dest.getArmyWithLevel(dLevel).decreaseUnits(1);
                roundLoser = 1;
            }
            defenderLost = true;
            for (int i = 0; i < 7; i++) {
                if (dest.getArmyWithLevel(i).getArmyUnits() > 0) {
                    defenderLost = false;
                }
            }
            attackerLost = true;
            for (int i = 0; i < 7; i++) {
                if (attackLevel.get(i) > 0) {
                    attackerLost = false;
                }
            }
            flag = (!attackerLost) && (!defenderLost);
        }
        if (defenderLost) {
            dest.setOwner(this.player);
            for (int i = 0; i < 7; ++i) {
                dest.getArmyWithLevel(i).increaseUnits(attackLevel.get(i));
            }
            //version3
            for(Spy spy : dest.getOwnedSpies()){
                dest.getEnemySpies().add(spy);
                dest.getOwnedSpies().remove(spy);
            }
            for(Spy spy : dest.getEnemySpies()) {
                if (spy.getOwner().equals(player)) {
                    dest.getOwnedSpies().add(spy);
                    dest.getEnemySpies().remove(spy);
                }
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

    public ArrayList<Integer> getAttackLevel() {
        return attackLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public void setAttackLevel(ArrayList<Integer> atkLevel) {
        for (int i = 0; i < 7; i++) {
            attackLevel.set(i, attackLevel.get(i) + atkLevel.get(i));
        }
    }

    public int getLevel() {
        return level;
    }
}
