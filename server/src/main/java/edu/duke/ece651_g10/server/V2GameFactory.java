package edu.duke.ece651_g10.server;

import java.util.concurrent.ThreadLocalRandom;

public class V2GameFactory implements GameFactory {

    Server runServer;

    @Override
    public Game createFixedGame(int people) {
        GameMapFactory fixedGameMapFactory = new FixedGameMapFactory();
        int randomUnits = ThreadLocalRandom.current().nextInt(25, 51);
        GameMap map = fixedGameMapFactory.V2CreateGameMap(people);
        RuleChecker<TerritoryToTerritoryOrder> moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(new MoveFoodChecker(null))))));
        RuleChecker<TerritoryToTerritoryOrder> attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(new AttackFoodChecker(null))))));
        RuleChecker<OneTerritoryOrder> upgradeUnitChecker = new SelfUpgradeOrderChecker(new UnitUpgradeTechChecker(new UnitUpgradeRangeChecker(new UpgradeSufficientUnitChecker(new TechResourceUnitChecker(null)))));
        RuleChecker<ZeroTerritoryOrder> upgradeTechChecker = new CanUpgradeTechChecker(new TechUpgradeRangeChecker(new SufficientTechResourceChecker(null)));
        return new Game(map, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), new GameBoardTextView(map), randomUnits, people, this.runServer.threadPool, this.runServer, upgradeTechChecker, upgradeUnitChecker);
    }


    //When use this method, can pass null for refServer.
    @Override
    public Game createTestGame(int people) {
        GameMapFactory fixedGameMapFactory = new FixedGameMapFactory();
        int randomUnits = ThreadLocalRandom.current().nextInt(25, 51);
        GameMap map = fixedGameMapFactory.V2CreateGameMap(people);
        RuleChecker<TerritoryToTerritoryOrder> moveRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(new MoveFoodChecker(null))))));
        RuleChecker<TerritoryToTerritoryOrder> attackRuleChecker = new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(new AttackFoodChecker(null))))));
        RuleChecker<OneTerritoryOrder> upgradeUnitChecker = new SelfUpgradeOrderChecker(new UnitUpgradeTechChecker(new UnitUpgradeRangeChecker(new UpgradeSufficientUnitChecker(new TechResourceUnitChecker(null)))));
        RuleChecker<ZeroTerritoryOrder> upgradeTechChecker = new CanUpgradeTechChecker(new TechUpgradeRangeChecker(new SufficientTechResourceChecker(null)));
        return new Game(map, moveRuleChecker, attackRuleChecker, new V1OrderProcessor(), randomUnits, people, upgradeTechChecker, upgradeUnitChecker);
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
