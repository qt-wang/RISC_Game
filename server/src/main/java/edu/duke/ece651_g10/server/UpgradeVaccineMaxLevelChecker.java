package edu.duke.ece651_g10.server;

public class UpgradeVaccineMaxLevelChecker extends RuleChecker<ZeroTerritoryOrder>{

    public UpgradeVaccineMaxLevelChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getVaccineMaxLevel() >= order.getPlayer().getTechnologyLevel()) {
            return "The vaccine max level cannot be higher than technology level.";
        }
        return null;
    }
}
