package edu.duke.ece651_g10.server;

public class VaccineLevelChecker extends RuleChecker<ZeroTerritoryOrder>{

    public VaccineLevelChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (((VaccineOrder) order).getVaccineLevel() > order.getPlayer().getVaccineMaxLevel()) {
            return "The vaccine level cannot be higher than vaccine max level.";
        }
        return null;
    }
}
