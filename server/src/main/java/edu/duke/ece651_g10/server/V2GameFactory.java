package edu.duke.ece651_g10.server;

import java.util.concurrent.ThreadLocalRandom;

public class V2GameFactory implements GameFactory {

    Server runServer;

//    @Override
//    public Game createRandomGame() {
//        //TODO: Can be changed to create random game.
//        GameMapFactory random = new V1GameMapFactory(new PseudoNumberGenerator(), 50, 35, 100);
//        int playerNumbers = ThreadLocalRandom.current().nextInt(3, 6);
//        int territoryPerPlayer = ThreadLocalRandom.current().nextInt(3, 9);
//        int randomUnits = ThreadLocalRandom.current().nextInt(25, 51);
//        GameMap map = random.createGameMap(playerNumbers, territoryPerPlayer);
//        RuleChecker moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(null)))));
//        RuleChecker attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(new AttackFoodChecker(null))))));
//        return new Game(map, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), new GameBoardTextView(map), randomUnits, playerNumbers, this.runServer.threadPool, this.runServer);
//    }
    @Override
    public Game createFixedGame(int people) {
        GameMapFactory fixedGameMapFactory = new FixedGameMapFactory();
        int randomUnits = ThreadLocalRandom.current().nextInt(25, 51);
        GameMap map = fixedGameMapFactory.createGameMap(people);
        RuleChecker moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(null)))));
        RuleChecker attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(new AttackFoodChecker(null))))));
        // TODO: add the checker!!!!!!
        return new Game(map, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), new GameBoardTextView(map), randomUnits, people, this.runServer.threadPool, this.runServer, null, null);
    }

    /**
     * Construct a Game factory.
     *
     * @param runServer The server where the game will run on.
     */
    public V2GameFactory(Server runServer) {
        this.runServer = runServer;
    }
}
