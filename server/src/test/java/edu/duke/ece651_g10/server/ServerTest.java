package edu.duke.ece651_g10.server;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javax.swing.text.View;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class ServerTest {
    @Test
    public void test_get_player_info() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        when(mockPlayer1.getPlayerID()).thenReturn(1);
        when(mockPlayer2.getPlayerID()).thenReturn(2);
        when(mockPlayer3.getPlayerID()).thenReturn(3);

        HashMap<Integer, Player> container = new HashMap<>();
        container.put(1, mockPlayer1);
        container.put(2, mockPlayer2);
        container.put(3, mockPlayer3);


        when(mockPlayer1.getIsLost()).thenReturn(false);
        when(mockPlayer2.getIsLost()).thenReturn(false);
        when(mockPlayer3.getIsLost()).thenReturn(true);


        Server server = new Server(container);
        String result = server.getPlayerInfo(1);
        String expected ="Player 1:\n" +
                "A\n";
        assertEquals(result, expected);

        GameMap mockMap = mock(V1GameMap.class);

        GameBoardTextView mockView = mock(GameBoardTextView.class);

        when(mockView.territoryForUser(mockPlayer1)).thenReturn("10 units in Narnia (next to: Elantris, Midkemia)\n");

        when(mockView.territoryForUser(mockPlayer2)).thenReturn("12 units in Test (next to: Eltris, Mida)\n");

        server.setView(mockView);
        expected = "First phase, soldiers distribution\n" +
                //"Player 1:\n" + "A\n" + "-----------------------\n" +
        "10 units in Narnia (next to: Elantris, Midkemia)\n";
        JSONObject test = server.firstPhaseInformation(mockPlayer1.getPlayerID());
        assertEquals(test.getString("prompt"), expected);



        expected = "Player 2:\n" +
                "-----------------------\n" +
                "12 units in Test (next to: Eltris, Mida)\n";
        assertEquals(server.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()), expected);


        String secondPhaseBeginMessage = "Second phase, attack territories\n" +
                "Player 1:\n" + "-----------------------\n" +
                "10 units in Narnia (next to: Elantris, Midkemia)\n" + expected;

        test = server.secondPhaseInformation(mockPlayer1.getPlayerID(), server.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()));
        assertEquals(secondPhaseBeginMessage, test.get("prompt"));


        when(mockPlayer3.getIsLost()).thenReturn(false);

        when(mockView.territoryForUser(mockPlayer3)).thenReturn("4 units in Test (next to: Eltris, Mida)\n");

        String expectedPlayer3 = "Player 3:\n" +
                "-----------------------\n" +
                "4 units in Test (next to: Eltris, Mida)\n";

        secondPhaseBeginMessage += expectedPlayer3;
        test = server.secondPhaseInformation(mockPlayer1.getPlayerID(), server.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()));
        assertEquals(secondPhaseBeginMessage, test.get("prompt"));
        System.out.println(secondPhaseBeginMessage);
        //Server server = new Server(container);
        //String expected =
    }
}