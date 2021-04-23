package edu.duke.ece651_g10.server;

public class CanBombChecker extends RuleChecker<OneTerritoryOrder>{

    public CanBombChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getCanBombInThisGame() == false) {
            return "The player cannot bomb in this game.";
        }
        return null;
    }
}
