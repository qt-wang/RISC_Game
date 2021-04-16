package edu.duke.ece651_g10.server;

import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameTest {

    private Game createTestGame() {
        RuleChecker moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(null)))));
        RuleChecker attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(null)))));
        GameMap map = new FixedGameMapFactory().createGameMap(3);
        Server mockServer = mock(Server.class);
        Game game = new Game(map, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), new GameBoardTextView(map), 20, 3, Executors.newCachedThreadPool(), mockServer, null, null);
        return game;
    }

    @Test
    public void test_present_game_info() {
        Game testGame = createTestGame();
        Player p = mock(Player.class);
        when(p.getPlayerID()).thenReturn(1);
        testGame.addPlayer(p);
        assertEquals(true, testGame.containsPlayer(p));
        JSONObject test = testGame.presentGameInfo();
        //assertEquals(0, test.getInt("gameId"));
        assertEquals(3, test.getInt("numberOfTerritories"));
        //assertEquals(1, test.getInt("currentPlayer"));
        assertEquals(3, test.getInt("totalPlayers"));
        Player p2 = mock(Player.class);
        when(p2.getPlayerID()).thenReturn(2);
        Player p3 = mock(Player.class);
        when(p3.getPlayerID()).thenReturn(3);
        assertEquals(false, testGame.containsPlayer(p2));
        testGame.addPlayer(p2);
        assertEquals(false, testGame.isGameFull());
        testGame.addPlayer(p3);
        assertEquals(true, testGame.isGameFull());
        test = testGame.presentGameInfo();
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
        Game game = new Game(mockMap, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), mockView, 20, 3, Executors.newCachedThreadPool(), mockServer, null, null);

        game.addPlayer(mockPlayer1);
        game.addPlayer(mockPlayer2);
        game.addPlayer(mockPlayer3);

        when(mockPlayer1.getIsLost()).thenReturn(false);
        when(mockPlayer2.getIsLost()).thenReturn(false);
        when(mockPlayer3.getIsLost()).thenReturn(true);

//        String expected = "First phase, soldiers distribution\n" +
//                //"Player 1:\n" + "A\n" + "-----------------------\n" +
//                "10 units in Narnia (next to: Elantris, Midkemia)\n";
//        JSONObject test = game.firstPhaseInformation(mockPlayer1.getPlayerID());
//        assertEquals(test.getString("prompt"), expected);
//
//        expected = "Player 2:\n" +
//                "-----------------------\n" +
//                "12 units in Test (next to: Eltris, Mida)\n";
//        assertEquals(game.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()), expected);
//
//        String secondPhaseBeginMessage = "Second phase, attack territories\n" +
//                "Player 1:\n" + "-----------------------\n" +
//                "10 units in Narnia (next to: Elantris, Midkemia)\n" + expected;
//
//        test = game.secondPhaseInformation(mockPlayer1.getPlayerID(), game.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()));
//        assertEquals(secondPhaseBeginMessage, test.get("prompt"));
//
//
//        when(mockPlayer3.getIsLost()).thenReturn(false);
//
//        when(mockView.territoryForUser(mockPlayer3)).thenReturn("4 units in Test (next to: Eltris, Mida)\n");
//
//        String expectedPlayer3 = "Player 3:\n" +
//                "-----------------------\n" +
//                "4 units in Test (next to: Eltris, Mida)\n";
//
//        secondPhaseBeginMessage += expectedPlayer3;
//        test = game.secondPhaseInformation(mockPlayer1.getPlayerID(), game.getEnemyTerritoryInformation(mockPlayer1.getPlayerID()));
//        assertEquals(secondPhaseBeginMessage, test.get("prompt"));
//
//
//        JSONObject receivedJson = game.generateServerResponse("valid\n", "", "connection");
//        assertEquals(receivedJson.getString("prompt"), "valid\n");
//        assertEquals(receivedJson.getString("reason"), "");
//        assertEquals(receivedJson.getString("type"), "connection");
    }


    @Test
    public void test_merge_json() {
        JSONObject one = new JSONObject();
        one.put("11", 1);
        one.put("12", new JSONObject().put("wow", "test"));

        JSONObject two = new JSONObject();
        two.put("21", 2);
        two.put("22", new JSONObject().put("22wow", "test"));

        JSONObject merged = Game.mergeJSONObject(one, two);
        JSONObject test = merged.getJSONObject("12");
        assertEquals("test", test.getString("wow"));
        assertEquals(1, merged.getInt("11"));


//        Player p = mock(Player.class);
//        when(p.getPlayerID()).thenReturn(1);
//        Set<Territory> t1 = new HashSet<>();
//        Set<Territory> t2 = new HashSet<>();
//        Territory t = new V1Territory("test");
//        t.setOwner(p);
//        t2.add(t);
//        merged = Game.mergeJSONObject(Game.generateTerritoriesInfo(t1, p), Game.generateTerritoriesInfo(t2, p));
//        JSONObject info = merged.getJSONObject("test");
    }

    @Test
    public void test_toOrder() {
        Game testGame = createTestGame();
        JSONObject json = new JSONObject().put("type", "order");
        json = json.put("orderType", "move");
        json = json.put("sourceTerritory", "Elantris");
        json = json.put("destTerritory", "Roshar");
        json = json.put("unitLevel", 0);
        json = json.put("unitNumber", 2);
        json = json.put("level", 0);
        Order order = testGame.toOrder(1, json);
        assertEquals(true, order instanceof MoveOrder);
        assertEquals("Roshar", ((MoveOrder) order).getTargetTerritory().getName());
        assertEquals("Elantris", ((MoveOrder) order).getSourceTerritory().getName());
        assertEquals(2, ((MoveOrder) order).getNumUnit());

    }

    @Disabled
    @Test
    public void test_toOrderAttack() {
        Game testGame = createTestGame();
        JSONObject json = new JSONObject().put("type", "order");
        json = json.put("orderType", "attack");
        json = json.put("sourceTerritory", "Elantris");
        json = json.put("destTerritory", "Narnia");
        json = json.put("unitLevel", 0);
        json = json.put("unitNumber", 2);
        json = json.put("level", 0);
        Order order = testGame.toOrder(1, json);
        assertEquals(true, order instanceof AttackOrder);
//        assertEquals("Narnia", ((AttackOrder) order).getTargetTerritory().getName());
//        assertEquals("Elantris", ((AttackOrder) order).getSourceTerritory().getName());
//        assertEquals(2, ((AttackOrder) order).getNumUnit());

    }

    @Test
    public void test_toOrderUpgradeUnit() {
        Game testGame = createTestGame();
        JSONObject json = new JSONObject().put("type", "order");
        json = json.put("orderType", "upgradeUnit");
        json = json.put("sourceTerritory", "Elantris");
        json = json.put("destTerritory", "Narnia");
        json = json.put("unitLevel", 3);
        json = json.put("unitNumber", 2);
        Order order = testGame.toOrder(1, json);
        assertEquals(true, order instanceof UpgradeUnitOrder);
        assertEquals("Elantris", ((UpgradeUnitOrder) order).getSourceTerritory().getName());
        assertEquals(2, ((UpgradeUnitOrder) order).getNumUnit());
        assertEquals(3, ((UpgradeUnitOrder) order).getLevel());
    }

    @Test
    public void test_toOrderUpgradeTech() {
        Game testGame = createTestGame();
        JSONObject json = new JSONObject().put("type", "order");
        json = json.put("orderType", "upgradeTech");
        json = json.put("sourceTerritory", "Elantris");
        json = json.put("destTerritory", "Narnia");
        json = json.put("unitLevel", 3);
        json = json.put("unitNumber", 2);
        Order order = testGame.toOrder(1, json);
        assertEquals(true, order instanceof UpgradeTechOrder);
    }
}
