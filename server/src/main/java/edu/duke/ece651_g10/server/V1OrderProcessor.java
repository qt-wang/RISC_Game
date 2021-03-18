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
    private HashMap<Player, Vector<Order>> attacksInOneTurn;

    public V1OrderProcessor(){
        attacksInOneTurn = new HashMap<>();
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
            //If this order is an attack order, decrease the unit number in the source territory at
            //very beginning.
            order.getSourceTerritory().decreaseUnit(order.getNumUnit());
            //If the owner of source territory did not attack others before, then create a new item
            //for the hashmap.
            if(attacksInOneTurn.get(order.getSourceTerritory().getOwner()) == null) {
                Vector<Order> vector = new Vector<>();
                vector.addElement(order);
                attacksInOneTurn.put(order.getSourceTerritory().getOwner(), vector);
            }
            //If the owner of source territory attacked others before, then put this order to his
            //vector of orders and then merge with other orders if necessary.
            else {
                Vector<Order> vector = attacksInOneTurn.get(order.getSourceTerritory().getOwner());
                vector.addElement(order);
                if(vector.capacity() > 1) {
                    merge(vector);
                }
                attacksInOneTurn.put(order.getSourceTerritory().getOwner(), vector);
            }
        }
    }

    /**
     * This method merges all attack orders with same territories' owner and same destination.
     * @param vector is a vector of orders in which the owner of territories is same.
     */
    private void merge(Vector<Order> vector){
        int length = vector.capacity();
        int index = -1;
        for(int i = 0; i < length - 1; i++){
            for(int j = i + 1; j < length; j++){
                //if destination is same, change the unit number of the ith order. we
                //need to remove the jth order later.
                //if(vector.get(i).getTargetTerritory().equals(vector.get(j).getTargetTerritory())){
                    vector.get(i).addUnits(vector.get(j).getNumUnit());
                    index = j;
                //}
            }
        }
        //Remove this order, because we have added the unit number of this order to another order.
        //And more importantly, we have decreased the unit number of the source territory of this order.
        if(index != -1){
            vector.remove(vector.get(index));
        }
    }

    /**
     * execute all attack orders randomly.
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
     * This method obtain all orders that have been merged.
     * @return a vector of merged orders.
     */
    private Vector<Order> obtainAllAttackOrders(){
        Vector<Order> allAttacks = new Vector<>();
        for(Player player : attacksInOneTurn.keySet()){
            Vector<Order> v = attacksInOneTurn.get(player);
            for(Order order : v){
                order.getSourceTerritory().decreaseUnit(order.getNumUnit());
                allAttacks.addElement(order);
            }
        }
        return allAttacks;
    }
}
