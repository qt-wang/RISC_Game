package edu.duke.ece651_g10.server;

import java.util.*;

/**
 * This class is an order processor for v1. It aims to handle all move and
 * attack orders in one turn. If it receives a move order, it executes move
 * order immediately. If it receives an attack order, it stores the order in a
 * hashmap temporarily and executes them at last.
 */
public class V1OrderProcessor implements OrderProcessor {
  private HashMap<Player, Vector<AttackOrder>> attacksInOneTurn;
  private Vector<UpgradeTechOrder> upgradeTechInOneTurn;

  public V1OrderProcessor() {
    attacksInOneTurn = new HashMap<>();
    upgradeTechInOneTurn = new Vector<>();
  }

  /**
   * If the order is a move order, execute it immediately. If the order is an
   * attack order, store it into a container.
   * 
   * @param order The order to be accepted.
   */
  public void acceptOrder(Order order) {
    if (order instanceof MoveOrder) {
      ((MoveOrder) order).execute();
    } else if (order instanceof AttackOrder) {
      // If this order is an attack order, decrease the unit number in the source
      // territory at
      // very beginning.
      ((AttackOrder) order).getPlayer()
          .setFoodResourceTotal(((AttackOrder) order).getPlayer().getFoodResourceTotal() - 1);
      ArrayList<Integer> atkLevel = ((AttackOrder) order).getAttackLevel();
      // ((AttackOrder)order).getSourceTerritory().decreaseUnit(((AttackOrder)order).getNumUnit(),
      // 0);
      for (int i = 0; i < 7; i++) {
        ((AttackOrder) order).getSourceTerritory().decreaseUnit(atkLevel.get(i), i);
      }

      // If the owner of source territory did not attack others before, then create a
      // new item
      // for the hashmap.
      if (attacksInOneTurn.get(((AttackOrder) order).getSourceTerritory().getOwner()) == null) {
        Vector<AttackOrder> vector = new Vector<>();
        vector.addElement((AttackOrder) order);
        attacksInOneTurn.put(((AttackOrder) order).getSourceTerritory().getOwner(), vector);
      }
      // If the owner of source territory attacked others before, then put this order
      // to his
      // vector of orders and then merge with other orders if necessary.
      else {
        Vector<AttackOrder> vector = attacksInOneTurn.get(((AttackOrder) order).getSourceTerritory().getOwner());
        vector.addElement((AttackOrder) order);
        if (vector.size() > 1) {
          mergeAttackLevel(vector);
          merge(vector);
        }
        attacksInOneTurn.put(((AttackOrder) order).getSourceTerritory().getOwner(), vector);
      }
    } else if (order instanceof UpgradeUnitOrder) {
      ((UpgradeUnitOrder) order).execute();
    } else if (order instanceof UpgradeTechOrder) {
      int currentTechLevel = ((UpgradeTechOrder) order).getPlayer().getTechnologyLevel();
      int currentTechResource = ((UpgradeTechOrder) order).getPlayer().getTechnologyResourceTotal();
      int newTechLevel = currentTechLevel + 1;
      int techResourceCost = 25 * (newTechLevel - 1) * (newTechLevel - 2) / 2 + 50;
      int newTechResource = currentTechResource - techResourceCost;
      ((UpgradeTechOrder) order).getPlayer().setCanUpgradeInThisTurn(false);
      ((UpgradeTechOrder) order).getPlayer().setTechnologyResourceTotal(newTechResource);
      upgradeTechInOneTurn.addElement((UpgradeTechOrder) order);
    }
  }

  private void mergeAttackLevel(Vector<AttackOrder> vector){
    int length = vector.size();
    int index = -1;
    for(int i = 0; i < length - 1; i++){
      for(int j = i + 1; j < length; j++){
        if(vector.get(i).getSourceTerritory().equals(vector.get(j).getSourceTerritory()) && vector.get(i).getTargetTerritory().equals(vector.get(j).getTargetTerritory())){
          vector.get(i).setAttackLevel(vector.get(j).getAttackLevel());
          index = j;
        }
      }
    }
    if (index != -1) {
      vector.remove(vector.get(index));
    }
  }

  /**
   * This method merges all attack orders with same territories' owner and same
   * destination.
   * 
   * @param vector is a vector of orders in which the owner of territories is
   *               same.
   */
  private void merge(Vector<AttackOrder> vector) {
    int length = vector.size();
    int index = -1;
    for (int i = 0; i < length - 1; i++) {
      for (int j = i + 1; j < length; j++) {
        // if destination is same, change the unit number of the ith order. we
        // need to remove the jth order later.
        if (vector.get(i).getTargetTerritory().equals(vector.get(j).getTargetTerritory())) {
          // vector.get(i).addUnits(vector.get(j).getNumUnit());
          for (int m = 0; m < 7; m++) {
            vector.get(i).getAttackLevel().set(m,
                vector.get(i).getAttackLevel().get(m) + vector.get(j).getAttackLevel().get(m));
            // System.out.println(vector.get(i).getAttackLevel().get(m));
          }
          index = j;
        }
      }
    }
    // Remove this order, because we have added the unit number of this order to
    // another order.
    // And more importantly, we have decreased the unit number of the source
    // territory of this order.
    if (index != -1) {
      vector.remove(vector.get(index));
    }
  }

  /**
   * execute all attack orders randomly.
   */
  public void executeEndTurnOrders() {
    Vector<Order> allAttacks = obtainAllAttackOrders();
    Random rand = new Random();
    while (allAttacks.size() != 0) {
      int length = allAttacks.size();
      int index = rand.nextInt(length);
      allAttacks.get(index).execute(); // the execution sequence is random.
      allAttacks.remove(allAttacks.get(index)); // after execution, remove this order.
    }
    attacksInOneTurn.clear();
    if (upgradeTechInOneTurn.size() != 0) {
      for (UpgradeTechOrder order : upgradeTechInOneTurn) {
        order.execute();
      }
    }
  }

  /**
   * This method obtain all orders that have been merged.
   * 
   * @return a vector of merged orders.
   */
  private Vector<Order> obtainAllAttackOrders() {
    Vector<Order> allAttacks = new Vector<>();
    for (Player player : attacksInOneTurn.keySet()) {
      Vector<AttackOrder> v = attacksInOneTurn.get(player);
      for (Order order : v) {
        // order.getSourceTerritory().decreaseUnit(order.getNumUnit());
        allAttacks.addElement(order);
      }
    }
    return allAttacks;
  }
}
