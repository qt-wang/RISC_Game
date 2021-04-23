package edu.duke.ece651_g10.server;

public class UpgradeVirusMaxResourceChecker extends RuleChecker<ZeroTerritoryOrder>{

    public UpgradeVirusMaxResourceChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyResourceTotal() < order.getMaxVirusLevelTable().get(order.getPlayer().getTechnologyLevel())) {
            return "The player does not have enough technology to upgrade.";
        }
        return null;
    }
}
