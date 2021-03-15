package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;

public class FixedGameMapFactory implements GameMapFactory{
    @Override
    public GameMap createGameMap(int numberOfPlayers, int territoriesPerPlayer) {
        Territory Elantris = new V1Territory("Elantris");
        Territory Roshar = new V1Territory("Roshar");
        Territory Scadrial = new V1Territory("Scadrial");

        Territory Narnia = new V1Territory("Narnia");
        Territory Midkemia = new V1Territory("Midkemia");
        Territory Oz = new V1Territory("Oz");

        Territory Hogwarts = new V1Territory("Hogwarts");
        Territory Mordor = new V1Territory("Mordor");
        Territory Gondor = new V1Territory("Gondor");

        Elantris.addNeighbour(Narnia);
        //Narnia.addNeighbour(Elantris);
        Elantris.addNeighbour(Midkemia);
        //Midkemia.addNeighbour(Elantris);
        Elantris.addNeighbour(Scadrial);
        Elantris.addNeighbour(Roshar);

        Roshar.addNeighbour(Elantris);
        Roshar.addNeighbour(Scadrial);
        Roshar.addNeighbour(Hogwarts);

        Hogwarts.addNeighbour(Roshar);
        Hogwarts.addNeighbour(Scadrial);
        Hogwarts.addNeighbour(Mordor);

        Scadrial.addNeighbour(Elantris);
        Scadrial.addNeighbour(Roshar);
        Scadrial.addNeighbour(Hogwarts);
        Scadrial.addNeighbour(Mordor);
        Scadrial.addNeighbour(Oz);
        Scadrial.addNeighbour(Midkemia);

        Mordor.addNeighbour(Hogwarts);
        Mordor.addNeighbour(Scadrial);
        Mordor.addNeighbour(Oz);
        Mordor.addNeighbour(Gondor);

        Oz.addNeighbour(Scadrial);
        Oz.addNeighbour(Midkemia);
        Oz.addNeighbour(Mordor);
        Oz.addNeighbour(Gondor);

        Gondor.addNeighbour(Oz);
        Gondor.addNeighbour(Mordor);

        Midkemia.addNeighbour(Oz);
        Midkemia.addNeighbour(Scadrial);
        Midkemia.addNeighbour(Elantris);
        Midkemia.addNeighbour(Narnia);

        Narnia.addNeighbour(Elantris);
        Narnia.addNeighbour(Midkemia);

        HashSet<Territory> territories = new HashSet<>();
        HashMap<Integer, HashSet<Territory>> groups = new HashMap<>();

        HashSet<Territory> group1 = new HashSet<>();
        group1.add(Elantris);
        group1.add(Roshar);
        group1.add(Scadrial);
        HashSet<Territory> group2 = new HashSet<>();
        group2.add(Hogwarts);
        group2.add(Mordor);
        group2.add(Gondor);
        HashSet<Territory> group3 = new HashSet<>();
        group3.add(Oz);
        group3.add(Midkemia);
        group3.add(Narnia);
        groups.put(1, group1);
        groups.put(2, group2);
        groups.put(3, group3);

        territories.addAll(group1);
        territories.addAll(group2);
        territories.addAll(group3);
        return new V1GameMap(territories, groups);
    }
}
