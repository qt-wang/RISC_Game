package edu.duke.ece651_g10.server;
public class SpyUnit implements Spy{

    private Player owner;
    private Territory currentPosition;

    public SpyUnit(Player owner, Territory currentPosition) {
        this.owner = owner;
        this.currentPosition = currentPosition;
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
}
