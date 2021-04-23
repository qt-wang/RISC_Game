package edu.duke.ece651_g10.server;

public class CanCloakChecker extends RuleChecker<OneTerritoryOrder>{

    public CanCloakChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getCanResearchCloak() == true) {
            return "The player has not researched a cloak.";
        }
        return null;
    }

}
