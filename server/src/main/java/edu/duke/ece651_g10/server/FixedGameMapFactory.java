package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FixedGameMapFactory implements GameMapFactory {

    void setupTerritoryAttributes(GameMap map, int totalFoodResourceGenerationRate, int technologyResourceGenerationRate, int totalSize) {
        HashMap<Integer, HashSet<Territory>> groups = map.getInitialGroups();
        for (Map.Entry<Integer, HashSet<Territory>> entry : groups.entrySet()) {
            HashSet<Territory> territories = entry.getValue();
            int[] foodDistribution = new V2SetNumGenerator(territories.size(), 1, totalFoodResourceGenerationRate, totalFoodResourceGenerationRate).get();
            int[] technologyDistribution = new V2SetNumGenerator(territories.size(), 1, technologyResourceGenerationRate, technologyResourceGenerationRate).get();
            int[] sizeDistribution = new V2SetNumGenerator(territories.size(), 1, totalSize, totalSize).get();
            int count = 0;
            for (Territory t : territories) {
                t.setFoodResourceGenerationRate(foodDistribution[count]);
                t.setTechnologyResourceGenerationRate(technologyDistribution[count]);
                t.setSize(sizeDistribution[count]);
                count += 1;
            }
        }
    }

    /**
     * Create a gamemap, which can be used for a game contains four players
     *
     * @return
     */
    GameMap createFourPeopleMap() {
        HashMap<String, Territory> territoryHashMap = createTerritories(12);
        territoryHashMap.get("A").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("A").addNeighbour(territoryHashMap.get("D"));

        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("A"));
        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("E"));
        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("C"));

        territoryHashMap.get("C").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("C").addNeighbour(territoryHashMap.get("F"));
        territoryHashMap.get("C").addNeighbour(territoryHashMap.get("L"));

        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("A"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("E"));

        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("F"));
        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("H"));

        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("E"));
        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("C"));
        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("H"));

        territoryHashMap.get("G").addNeighbour(territoryHashMap.get("L"));
        territoryHashMap.get("G").addNeighbour(territoryHashMap.get("H"));
        territoryHashMap.get("G").addNeighbour(territoryHashMap.get("I"));

        territoryHashMap.get("H").addNeighbour(territoryHashMap.get("F"));
        territoryHashMap.get("H").addNeighbour(territoryHashMap.get("G"));

        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("G"));
        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("H"));
        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("J"));

        territoryHashMap.get("J").addNeighbour(territoryHashMap.get("K"));
        territoryHashMap.get("J").addNeighbour(territoryHashMap.get("I"));

        territoryHashMap.get("K").addNeighbour(territoryHashMap.get("J"));


        HashMap<Integer, HashSet<Territory>> groups = createGroups(4, territoryHashMap);
        GameMap map = new V1GameMap(new HashSet<>(territoryHashMap.values()), groups);
        setupTerritoryAttributes(map, 120, 45, 90);
        return map;
    }

    HashMap<String, Territory> createTerritories(int numberOfTerritories) {
        HashMap<String, Territory> result = new HashMap<>();
        Character c = 'A';
        for (int i = 0; i < numberOfTerritories; i++) {
            char temp = (char) (c + i);
            String current = Character.toString(temp);
            result.put(current, new V1Territory(current));
        }
        return result;
    }

    HashMap<Integer, HashSet<Territory>> createGroups(int numberOfPlayers, HashMap<String, Territory> territoryHashMap) {
        Character beginChar = 'A' - 1;
        HashMap<Integer, HashSet<Territory>> result = new HashMap<>();
        for (int i = 0; i < numberOfPlayers; i ++) {
            // Build the hashset.
            HashSet<Territory> group = new HashSet<>();
            for (int j = 0; j < 3; j ++) {
                beginChar++;
                String current = Character.toString(beginChar);
                group.add(territoryHashMap.get(current));
            }
            result.put(i + 1, group);
        }
        return result;
    }

    GameMap createThreePeopleMap() {
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
        V1GameMap result = new V1GameMap(territories, groups);
        setupTerritoryAttributes(result, 80, 45, 100);
        return result;
    }

    GameMap createFivePeopleMap() {
        HashMap<String, Territory> territoryHashMap = createTerritories(15);
        territoryHashMap.get("A").addNeighbour(territoryHashMap.get("C"));
        territoryHashMap.get("A").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("A").addNeighbour(territoryHashMap.get("G"));

        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("C"));
        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("A"));
        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("G"));
        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("F"));
        territoryHashMap.get("B").addNeighbour(territoryHashMap.get("D"));

        territoryHashMap.get("C").addNeighbour(territoryHashMap.get("A"));
        territoryHashMap.get("C").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("C").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("C").addNeighbour(territoryHashMap.get("N"));

        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("C"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("N"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("M"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("O"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("E"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("I"));
        territoryHashMap.get("D").addNeighbour(territoryHashMap.get("F"));

        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("O"));
        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("I"));
        territoryHashMap.get("E").addNeighbour(territoryHashMap.get("J"));

        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("G"));
        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("I"));
        territoryHashMap.get("F").addNeighbour(territoryHashMap.get("H"));

        territoryHashMap.get("G").addNeighbour(territoryHashMap.get("A"));
        territoryHashMap.get("G").addNeighbour(territoryHashMap.get("B"));
        territoryHashMap.get("G").addNeighbour(territoryHashMap.get("F"));
        territoryHashMap.get("G").addNeighbour(territoryHashMap.get("H"));

        territoryHashMap.get("H").addNeighbour(territoryHashMap.get("G"));
        territoryHashMap.get("H").addNeighbour(territoryHashMap.get("F"));
        territoryHashMap.get("H").addNeighbour(territoryHashMap.get("I"));
        territoryHashMap.get("H").addNeighbour(territoryHashMap.get("J"));

        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("F"));
        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("E"));
        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("J"));
        territoryHashMap.get("I").addNeighbour(territoryHashMap.get("H"));

        territoryHashMap.get("J").addNeighbour(territoryHashMap.get("H"));
        territoryHashMap.get("J").addNeighbour(territoryHashMap.get("I"));
        territoryHashMap.get("J").addNeighbour(territoryHashMap.get("E"));
        territoryHashMap.get("J").addNeighbour(territoryHashMap.get("O"));
        territoryHashMap.get("J").addNeighbour(territoryHashMap.get("K"));

        territoryHashMap.get("K").addNeighbour(territoryHashMap.get("J"));
        territoryHashMap.get("K").addNeighbour(territoryHashMap.get("O"));
        territoryHashMap.get("K").addNeighbour(territoryHashMap.get("L"));

        territoryHashMap.get("L").addNeighbour(territoryHashMap.get("K"));
        territoryHashMap.get("L").addNeighbour(territoryHashMap.get("O"));
        territoryHashMap.get("L").addNeighbour(territoryHashMap.get("M"));

        territoryHashMap.get("M").addNeighbour(territoryHashMap.get("L"));
        territoryHashMap.get("M").addNeighbour(territoryHashMap.get("O"));
        territoryHashMap.get("M").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("M").addNeighbour(territoryHashMap.get("N"));

        territoryHashMap.get("N").addNeighbour(territoryHashMap.get("M"));
        territoryHashMap.get("N").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("N").addNeighbour(territoryHashMap.get("C"));

        territoryHashMap.get("O").addNeighbour(territoryHashMap.get("D"));
        territoryHashMap.get("O").addNeighbour(territoryHashMap.get("M"));
        territoryHashMap.get("O").addNeighbour(territoryHashMap.get("L"));
        territoryHashMap.get("O").addNeighbour(territoryHashMap.get("K"));
        territoryHashMap.get("O").addNeighbour(territoryHashMap.get("J"));
        territoryHashMap.get("O").addNeighbour(territoryHashMap.get("E"));

        HashMap<Integer, HashSet<Territory>> groups = createGroups(5, territoryHashMap);
        GameMap map = new V1GameMap(new HashSet<>(territoryHashMap.values()), groups);
        setupTerritoryAttributes(map, 120, 45, 90);
        return map;
    }

    @Override
    public GameMap createGameMap(int numberOfPlayers) {
        switch (numberOfPlayers) {
            case 3:
                return createThreePeopleMap();
            case 4:
                return createFourPeopleMap();
            case 5:
                return createFivePeopleMap();
            default:
                System.out.println("Not implemented yet!");
                assert (false);
                return null;
        }
    }
}
