package edu.duke.ece651_g10.server;

public class SpyUnit implements Spy{

    private Player owner;
    private Territory currentPosition;
    private boolean canMove;

    public SpyUnit(Player owner, Territory currentPosition) {
        this.owner = owner;
        this.currentPosition = currentPosition;
        canMove = true;
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public Territory getCurrentPosition() {
        return this.currentPosition;
    }


    @Override
    public void moveTo(Territory territory) {
        if (territory != currentPosition) {
            currentPosition.decreaseSpy(this);

            if (territory.getOwner() == owner) {
                territory.addOwnedSpy(this);
            } else {
                territory.addEnemySpy(this);
            }
            this.currentPosition = territory;
        }
    }

    @Override
    public void setCanMoveAttribute(boolean canMove) {
        this.canMove = canMove;
    }
}
