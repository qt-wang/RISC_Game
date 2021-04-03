package edu.duke.ece651_g10.server;

public abstract class TerritoryToTerritoryOrder extends Order{
    protected Territory source;
    protected Territory dest;
    protected int unitNum;
    protected GameMap gMap;

    public TerritoryToTerritoryOrder(int playerID, String source, String dest, int unitNum, GameMap gMap) {
        super(playerID);
        this.gMap = gMap;
        this.source = gMap.getTerritory(source);
        this.dest = gMap.getTerritory(dest);
        this.unitNum = unitNum;
    }

  public abstract Territory getSourceTerritory();

  public abstract int getNumUnit();

  public abstract Territory getTargetTerritory();

  public abstract void execute();

  public abstract void addUnits(int number);

}








