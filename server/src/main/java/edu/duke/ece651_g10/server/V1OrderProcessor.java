package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * This class is an order processor for v1. It aims to handle all move and attack orders
 * in one turn. If it receives a move order, it executes move order immediately. If it receives
 * an attack order, it stores the order in a hashmap temporarily and executes them at last.
 */
public class V1OrderProcessor implements OrderProcessor{
    private HashMap<Territory, Vector<Order>> attacksInOneTurn;

    public V1OrderProcessor(){
        HashMap<Territory, Vector<Order>> attacksInOneTurn = new HashMap<>();
    }

    /**
     * If the order is a move order, execute it immediately.
     * If the order is an attack order, store it into a container.
     * @param order The order to be accepted.
     */
    public void acceptOrder(Order order){
        if(order instanceof MoveOrder){
            order.execute();
        }

        else if(order instanceof AttackOrder){
            if(attacksInOneTurn.get(((AttackOrder) order).getSourceTerritory()) == null) {
                Vector<Order> vector = new Vector<>();
                vector.addElement(order);
                attacksInOneTurn.put(((AttackOrder) order).getSourceTerritory(), vector);
            }
            //if the order's source is same with other orders' source, then we need to
            //check if this order can be merged with others.
            else {
                Vector<Order> vector = attacksInOneTurn.get(((AttackOrder) order).getSourceTerritory());
                vector.addElement((AttackOrder) order);
                merge(vector);
                attacksInOneTurn.put(((AttackOrder) order).getSourceTerritory(), vector);
            }
        }
    }

    /**
     * This method merges all attack orders with same source and same target.
     * @param vector is a vector of orders in which the source is same.
     */
    private void merge(Vector<Order> vector){
        int length = vector.capacity();
        int index = -1;
        for(int i = 0; i < length - 1; i++){
            for(int j = i + 1; j < length; j++){
                //if destination is same, change the unit number of the ith order. we
                //need to remove the jth order later.
                if(vector.get(i).getTargetTerritory().equals(vector.get(j).getTargetTerritory())){
                    vector.get(i).changeUnitNum(vector.get(j).getUnitNum());
                    index = j;
                }
            }
        }
        if(index != -1){
            vector.remove(vector.get(index));
        }
    }

    /**
     * execute all attack orders.
     */
    public void executeEndTurnOrders(){
        Vector<Order> allAttacks = obtainAllAttackOrders();
        Random rand = new Random();
        while(allAttacks.capacity() != 0){
            int length = allAttacks.capacity();
            int index = rand.nextInt(length);
            allAttacks.get(index).execute();   //the execution sequence is random.
            allAttacks.remove(allAttacks.get(index));  //after execution, remove this order.
        }
    }

    /**
     * This method obatin all orders that have been merged.
     * @return a vector of merged orders.
     */
    private Vector<Order> obtainAllAttackOrders(){
        Vector<Order> allAttacks = new Vector<>();
        for(Territory territory : attacksInOneTurn.keySet()){
            Vector<Order> v = attacksInOneTurn.get(territory);
            for(Order order : v){
                order.getSourceTerritory().decreaseUnit(order.getUnitNum());
                allAttacks.addElement(order);
            }
        }
        return allAttacks;
    }
}
