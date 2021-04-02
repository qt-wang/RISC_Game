package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class V1GameMapFactoryTest {

    private boolean checkConnected(Set<Territory> territories) {
        List<Territory> temp = new ArrayList<>(territories);
        Territory beginPoint = temp.get(new Random().nextInt(territories.size()));
        // We start at beginPoint, and use bfs algorithm to see if we can go to all the nodes.
        Set<Territory> reached = new HashSet<>();
        Deque<Territory> container = new LinkedList<>();
        container.add(beginPoint);
        reached.add(beginPoint);
        Set<Territory> marked = new HashSet<>();
        marked.add(beginPoint);
        while (!container.isEmpty()) {
            Territory t = container.pollFirst();
            for (Territory territory : t.getNeighbours()) {
                if (!marked.contains(territory)) {
                    container.addLast(territory);
                    marked.add(territory);
                }
                if (!reached.contains(territory)) {
                    reached.add(territory);
                }
            }
        }
        if (reached.size() == territories.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void test_version_map_setup() {
        PseudoNumberGenerator rand = new PseudoNumberGenerator();
        V1GameMapFactory factory = new V1GameMapFactory(rand, 60, 80, 100);
        GameMap map = factory.createGameMap(5, 3);
        HashMap<Integer, HashSet<Territory>> test = map.getInitialGroups();
        for (Map.Entry<Integer, HashSet<Territory>> entry : test.entrySet()) {
            int sizeTotal = 0;
            int foodTotal = 0;
            int resourceTotal = 0;
            for (Territory t: entry.getValue()) {
                sizeTotal += t.getSize();
                foodTotal += t.getFoodResourceGenerationRate();
                resourceTotal += t.getTechnologyResourceGenerationRate();
            }
            assertEquals(100, sizeTotal);
            assertEquals(60, foodTotal);
            assertEquals(80, resourceTotal);
        }
    }

    @Test
    public void test_create_random_territory_graph() {
        PseudoNumberGenerator rand = new PseudoNumberGenerator();
        V1GameMapFactory factory = new V1GameMapFactory(rand, 60, 80, 100);
        Set<Territory> territories = factory.createRandomTerritoryGraph(3, 5);
        assertEquals(territories.size(), 15);
        assertEquals(true, checkConnected(territories));

        territories = factory.createRandomTerritoryGraph(2, 2);
        assertEquals(territories.size(), 4);
        assertEquals(true, checkConnected(territories));

        territories = factory.createRandomTerritoryGraph(5, 6);
        assertEquals(territories.size(), 30);
        assertEquals(true, checkConnected(territories));
//        for (Territory t: territories) {
//            System.out.println(t.getName());
//        }

        Territory t = new V1Territory("t");
        Territory t2 = new V1Territory("t2");
        Territory t3 = new V1Territory("t3");
        t.addNeighbour(t2);
        t2.addNeighbour(t);
        Set<Territory> notConnected = new HashSet<>();
        notConnected.add(t);
        notConnected.add(t2);
        notConnected.add(t3);
        assertEquals(false, checkConnected(notConnected));
    }

    @Test
    public void test_all_belong_to_same_user() {
        HashSet<Territory> territories = new HashSet<>();
        Territory t = mock(Territory.class);
        Territory t2 = mock(Territory.class);

        territories.add(t);
        territories.add(t2);
        HashMap<Integer, HashSet<Territory>> groups = new HashMap<>();

        GameMap map = new V1GameMap(territories, groups);

        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        when(t.getOwner()).thenReturn(p1);
        when(t2.getOwner()).thenReturn(p2);

        assertEquals(null, map.allBelongsToSamePlayer());

        when(t2.getOwner()).thenReturn(p1);
        assertEquals(p1, map.allBelongsToSamePlayer());
    }

//    @Test
//    public void test_with_not_random_number() {
//        // Use mock to create a random number generator.
//        RandomNumberGenerator rand = mock(RandomNumberGenerator.class);
//        when(rand.nextInt()).thenReturn(5);
//
//        V1GameMapFactory factory = new V1GameMapFactory(rand);
//        GameMap map = factory.createGameMap(3, 4);
//
//    }
}
