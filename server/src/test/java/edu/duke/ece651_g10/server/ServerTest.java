package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class ServerTest {
    @Test
    public void test_get_player_info() {
        Player mockPlayer1 = mock(Player.class);
        HashMap<Integer, Player> container = new HashMap<>();
        container.put(1, mockPlayer1);
        when(mockPlayer1.getIsLost()).thenReturn(false);
        Server server = new Server(container);
        String result = server.getPlayerInfo(1);
        //String expected =
    }
}