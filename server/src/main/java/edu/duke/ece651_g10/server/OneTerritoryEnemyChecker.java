package edu.duke.ece651_g10.server;

public class OneTerritoryEnemyChecker extends RuleChecker<OneTerritoryOrder>{

    public OneTerritoryEnemyChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getSourceTerritory().getOwner().getPlayerID() == order.getPlayerID()) {
            return "The territory belongs to the player";
        }
        return null;
    }
}
