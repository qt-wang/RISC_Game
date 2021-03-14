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
        while(!container.isEmpty()) {
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

    //TODO: Need more tests (ie. test the if the graph is connected or not).
    @Test
    public void test_create_random_territory_graph() {
        PseudoNumberGenerator rand = new PseudoNumberGenerator();
        V1GameMapFactory factory = new V1GameMapFactory(rand);
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