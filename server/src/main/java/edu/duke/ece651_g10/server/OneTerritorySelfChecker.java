package edu.duke.ece651_g10.server;

public class OneTerritorySelfChecker extends RuleChecker<OneTerritoryOrder>{

    public OneTerritorySelfChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getSourceTerritory().getOwner().getPlayerID() != order.getPlayer().getPlayerID()) {
            return "This territory does not belong to the player.";
        }
        return null;
    }
}
