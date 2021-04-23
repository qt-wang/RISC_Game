package edu.duke.ece651_g10.server;

public class UpgradeVirusMaxLevelChecker extends RuleChecker<ZeroTerritoryOrder>{

    public UpgradeVirusMaxLevelChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getVirusMaxLevel() >= order.getPlayer().getTechnologyLevel()) {
            return "The virus max level cannot be higher than technology level";
        }
        return null;
    }
}
