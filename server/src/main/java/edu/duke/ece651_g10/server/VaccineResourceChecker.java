package edu.duke.ece651_g10.server;

public class VaccineResourceChecker extends RuleChecker<ZeroTerritoryOrder>{

    public VaccineResourceChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyResourceTotal() < ((VaccineOrder) order).getVaccineLevel() * 50) {
            return "The player does not have enough technology resource.";
        }
        return null;
    }
}
