package edu.duke.ece651_g10.server;

public class UpgradeSpyLevelChecker extends RuleChecker<OneTerritoryOrder>{

    public UpgradeSpyLevelChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyLevel() < 1) {
            return "The player's technology level is not high enough to upgrade spy";
        }
        return null;
    }
}
