package edu.duke.ece651_g10.server;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameTest {

    private Game createTestGame() {
        RuleChecker moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(null)))));
        RuleChecker attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(null)))));
        GameMap map = new FixedGameMapFactory().createGameMap(3, 3);
        Server mockServer = mock(Server.class);
        Game game = new Game(map, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), new GameBoardTextView(map), 20, 3, Executors.newCachedThreadPool(), mockServer);
        return game;
    }

    @Test
    public void test_get_player_info() {
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        when(mockPlayer1.getPlayerID()).thenReturn(1);
        when(mockPlayer2.getPlayerID()).thenReturn(2);
        when(mockPlayer3.getPlayerID()).thenReturn(3);

        GameMap mockMap = mock(V1GameMap.class);

        GameBoardTextView mockView = mock(GameBoardTextView.class);

        when(mockView.territoryForUser(mockPlayer1)).thenReturn("10 units in Narnia (next to: Elantris, Midkemia)\n");

        when(mockView.territoryForUser(mockPlayer2)).thenReturn("12 units in Test (next to: Eltris, Mida)\n");

        RuleChecker moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(null)))));
        RuleChecker attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(null)))));
        Server mockServer = mock(Server.class);
        Game game = new Game(mockMap, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), mockView, 20, 3, Executors.newCachedThreadPool(), mockServer);

        game.addPlayer(mockPlayer1);
        game.addPlayer(mockPlayer2);
        game.addPlayer(mockPlayer3);

        when(mockPlayer1.getIsLost()).thenReturn(false);
        when(mockPlayer2.getIsLost()).thenReturn(false);
        when(mockPlayer3.getIsLost()).thenReturn(true);

        String expected = "First phase, soldiers distribution\n" +
                //"Player 1:\n" + "A\n" + "-----------------------\n" +
                "10 units in Narnia (next to: Elantris, Midkemia)\n";
        JSONObject test = game.firstPhaseInformation(mockPlayer1.getPlayerID());
        assertEquals(test.getString("prompt"), expected);

        expected = "Player 2:\n" +
                "-----------------------\n" +
                "12 units in Test (next to: Eltris, Mida)\n";
        assertEquals(game.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()), expected);

        String secondPhaseBeginMessage = "Second phase, attack territories\n" +
                "Player 1:\n" + "-----------------------\n" +
                "10 units in Narnia (next to: Elantris, Midkemia)\n" + expected;

        test = game.secondPhaseInformation(mockPlayer1.getPlayerID(), game.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()));
        assertEquals(secondPhaseBeginMessage, test.get("prompt"));


        when(mockPlayer3.getIsLost()).thenReturn(false);

        when(mockView.territoryForUser(mockPlayer3)).thenReturn("4 units in Test (next to: Eltris, Mida)\n");

        String expectedPlayer3 = "Player 3:\n" +
                "-----------------------\n" +
                "4 units in Test (next to: Eltris, Mida)\n";

        secondPhaseBeginMessage += expectedPlayer3;
        test = game.secondPhaseInformation(mockPlayer1.getPlayerID(), game.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()));
        assertEquals(secondPhaseBeginMessage, test.get("prompt"));



        JSONObject receivedJson = game.generateServerResponse("valid\n", "", "connection");
        assertEquals(receivedJson.getString("prompt"), "valid\n");
        assertEquals(receivedJson.getString("reason"), "");
        assertEquals(receivedJson.getString("type"), "connection");
    }
}