package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;

import org.junit.jupiter.api.Test;
class ServerTest {



    @Test
    public void test_attack_area() {
        Territory t1 = new V1Territory("test1");
        Territory t2 = new V1Territory("test2");

        GameMap map = mock(GameMap.class);
        when(map.getTerritory("test1")).thenReturn(t1);
        when(map.getTerritory("test2")).thenReturn(t2);

        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        when(p1.getPlayerID()).thenReturn(1);
        when(p2.getPlayerID()).thenReturn(2);
        OrderProcessor orderProcessor = new V1OrderProcessor();
        for (int i = 0;i < 1000; i ++) {
            t1.setOwner(p1);
            t1.setUnitNumber(1);
            t2.setOwner(p2);
            t2.setUnitNumber(1);

            // p1 attack p2
            Order attack = new AttackOrder(p1.getPlayerID(), "test1", "test2", 1, map, p1, 0);
            Order attack2 = new AttackOrder(p2.getPlayerID(), "test2", "test1", 1, map, p2, 0);

            orderProcessor.acceptOrder(attack);;
            orderProcessor.acceptOrder(attack2);

            orderProcessor.executeEndTurnOrders();
            assertEquals(t1.getOwner(), p2);
            assertEquals(t2.getOwner(), p1);
        }
    }

}












