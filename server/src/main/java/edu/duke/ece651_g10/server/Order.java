package edu.duke.ece651_g10.server;
public interface Order {
  public int getNumUnit();

  public Territory getSourceTerritory();

  public Territory getTargetTerritory();

  public int getPlayerID();
}













