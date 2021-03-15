package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameBoardTextViewTest {

    @Test
    public void test_board_view() {
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        when(mockPlayer1.getPlayerID()).thenReturn(1);
        when(mockPlayer2.getPlayerID()).thenReturn(2);
        when(mockPlayer3.getPlayerID()).thenReturn(3);

        GameMapFactory f1 = new FixedGameMapFactory();
        GameMap map = f1.createGameMap(0,3);
        HashMap<Integer, HashSet<Territory>> groups = map.getInitialGroups();
        int player = 1;
        while (player != 4) {
            HashSet<Territory> territories = groups.get(player);
            for (Territory t: territories) {
                if (player == 1) {
                    t.setOwner(mockPlayer1);
                } else if (player == 2) {
                    t.setOwner(mockPlayer2);
                } else {
                    t.setOwner(mockPlayer3);
                }
            }
            player += 1;
        }

        GameBoardTextView view = new GameBoardTextView(map);
        assertEquals(null, map.getTerritory("not existed"));

        assertEquals(map.getTerritory("Elantris").getOwner(), mockPlayer1);

        map.getTerritory("Elantris").setUnitNumber(6);
        map.getTerritory("Roshar").setUnitNumber(3);
        map.getTerritory("Scadrial").setUnitNumber(5);

        //System.out.println(view.territoryForUser(mockPlayer1));

        String str = "6 units in Elantris (next to: Midkemia, Narnia, Roshar, Scadrial)\n" +
                "3 units in Roshar (next to: Elantris, Hogwarts, Scadrial)\n" +
                "5 units in Scadrial (next to: Midkemia, Oz, Elantris, Hogwarts, Roshar, Mordor)\n";
        assertEquals(str, view.territoryForUser(mockPlayer1));

    }

}