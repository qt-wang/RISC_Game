package edu.duke.ece651_g10.server;

public class CanResearchCloakChecker extends RuleChecker<ZeroTerritoryOrder>{

    public CanResearchCloakChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (!order.getPlayer().getCanResearchCloak()) {
            return "The player can not research cloak in this game.";
        }
        return null;
    }
}
