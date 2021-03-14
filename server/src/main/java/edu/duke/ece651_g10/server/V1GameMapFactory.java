package edu.duke.ece651_g10.server;

import java.util.*;

/**
 * This is the game map factory used in version one.
 */
public class V1GameMapFactory implements GameMapFactory {

    final String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generate a map factory used in the game.
     *
     * @param intense An argument from 0 - 1, control the sparseness of the map.
     */
    public V1GameMapFactory(double intense) {
        this.intense = intense;
    }

    /**
     * This variable is used to control the sparseness of the graph.
     * The number of edges are actually from N - 1 to N(N - 1) / 2
     * This variable should be in the range (0, 1].
     * When given as one, the graph is fully connected (ie. each node has an edge to another node.)
     */
    private double intense;


    /**
     *
     * @param allTerritories
     * @param territoriesPerPlayer
     * @return
     */
    private Set<Territory> getPlayerTerritoryGroup(List<Territory> allTerritories, int territoriesPerPlayer) {
        Set<Territory> group = new HashSet<>();
        Random rand = new Random();
        Territory beginPoint = allTerritories.remove(rand.nextInt(allTerritories.size()));
        group.add(beginPoint);
        //TODO:Fix this.
        while (group.size() != territoriesPerPlayer) {
            Territory ref = null;
            for (Territory t: beginPoint.getNeighbours()) {
                // If t is still available and t is not in the group, then this territory is able to be added into the group.
                if ((!group.contains(t)) && allTerritories.contains(t)) {
                    ref = t;
                    group.add(t);
                    allTerritories.remove(ref);
                    break;
                }
            }
            if (ref == null) {
                // We need to get a random node from the list and make it the neighbour of the home.
                Territory newNeighbour = allTerritories.remove(rand.nextInt(allTerritories.size()));
                newNeighbour.addNeighbour(beginPoint);
                group.add(newNeighbour);
                beginPoint.addNeighbour(newNeighbour);
                beginPoint = newNeighbour;
            }
        }
        return group;
    }


    /**
     * This function should generate a connected graph with numberOfPlayers * territoriesPerPlayer nodes.
     *
     * @param numberOfPlayers      The number of Players within the game.
     * @param territoriesPerPlayer The territories belong to each player.
     * @return A GameMap which contains all the territories.
     */
    @Override
    public GameMap createGameMap(int numberOfPlayers, int territoriesPerPlayer) {
        HashSet<Territory> allTerritories = (HashSet<Territory>) createRandomTerritoryGraph(numberOfPlayers, territoriesPerPlayer);
        // We need to divide it into numberOfPlayers group, each will has territoriesPerPlayer.
        HashMap<Integer, HashSet<Territory>> groups = new HashMap<>();
        List<Territory> copied = new LinkedList<>(allTerritories);
        for (int i = 0; i < numberOfPlayers; i ++) {
            HashSet<Territory> group = (HashSet<Territory>) getPlayerTerritoryGroup(copied, territoriesPerPlayer);
            groups.put(i, group);
        }
        GameMap map = new V1GameMap(allTerritories, groups);
        return map;
    }


    /**
     * Generate a name with length 'length'
     * Use the lexicon specified in allowedCharacters.
     *
     * @param length The length of the string.
     * @return The generated name of the string.
     */
    private String generateRandomName(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i ++) {
            sb.append(allowedCharacters.charAt(rand.nextInt(allowedCharacters.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a number of random names used in the territories.
     * These name will not be identical.
     *
     * @param number    How many names will be generated.
     * @param minLength The minimum length a name can be.
     * @param maxLength The maximum length a name can be.
     * @return A list contains all the Strings that will be used.
     */
    private List<String> getRandomNames(int number, int minLength, int maxLength) {
        Random rand = new Random();
        Set<String> names = new HashSet<>();
        while (names.size() != number) {
            int length = rand.nextInt(maxLength - minLength + 1) + minLength;
            String name = generateRandomName(length);
            if (!names.contains(name)) {
                names.add(name);
            }
        }
        return new ArrayList<>(names);
    }


    /**
     * Generate numberOfPlayers * territoriesPerPlayer territories, and create random connections between them.
     *
     * @param numberOfPlayers      The number of players in this game.
     * @param territoriesPerPlayer The number of territories per player has.
     * @return A set contains all the territories.
     */
    Set<Territory> createRandomTerritoryGraph(int numberOfPlayers, int territoriesPerPlayer) {
        // First step: Generate all the territories, put it into one set.
        List<Territory> allTerritories = new ArrayList<>();
        Set<Territory> from;
        Set<Territory> to = new HashSet<>();
        int numberOfTerritories = numberOfPlayers * territoriesPerPlayer;
        List<String> names = getRandomNames(numberOfTerritories, 3, 6);
        for (int i = 0; i < numberOfTerritories; i++) {
            // TODO: change this later, add arguments inside.
            Territory temp = new V1Territory(names.remove(0));
            allTerritories.add(temp);
        }
        Collections.shuffle(allTerritories);
        from = new HashSet<>(allTerritories);
        // get the first item, to begin the algorithm.
        Territory currentTerritory = allTerritories.get(0);
        from.remove(currentTerritory);
        to.add(currentTerritory);
        while (!from.isEmpty()) {
            // Get a random territory from all the territories.
            int item = new Random().nextInt(numberOfTerritories);
            Territory randomTerritory = allTerritories.get(item);
            if (!to.contains(randomTerritory)) {
                // We generate an edge from currentTerritory to randomTerritory.
                currentTerritory.addNeighbour(randomTerritory);
                randomTerritory.addNeighbour(currentTerritory);
                from.remove(randomTerritory);
                to.add(randomTerritory);
            }
            currentTerritory = randomTerritory;
        }
        return to;
    }
}
