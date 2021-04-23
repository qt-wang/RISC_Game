package edu.duke.ece651_g10.server;

public class UpgradeVaccineMaxOrder extends RuleChecker<ZeroTerritoryOrder>{

    public UpgradeVaccineMaxOrder(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyResourceTotal() < order.getMaxVaccineLevelTable().get(order.getPlayer().getTechnologyLevel())) {
            return "The vaccine max level cannot be higher than technology level.";
        }
        return null;
    }
}
