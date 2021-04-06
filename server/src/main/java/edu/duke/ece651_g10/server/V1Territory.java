package edu.duke.ece651_g10.server;

import java.util.*;

/**
 * This class represents one territory of the map. Maintained by Guancheng Fu.
 */
public class V1Territory implements Territory {

  @Override
  public void setUnitNumber(int unit) {
    /*ownedUnits = new LinkedList<>();
    for (int i = 0; i < unit; i++) {
      ownedUnits.add(new V1Unit());
      }*/
    armies.get(0).increaseUnits(unit);
  }
 
  @Override
  public void decreaseUnit(int level, int unit) {
    /*for (int i = 0; i < unit; i++) {
      ownedUnits.remove(0);
      }*/
    armies.get(level).decreaseUnits(unit);
  }

  @Override
  public void increaseUnit(int level, int unit) {
    /* for (int i = 0; i < unit; i++) {
      ownedUnits.add(new V1Unit());
      }*/
    armies.get(level).increaseUnits(unit);
  }

  private final String name;
  private Player owner;
  private HashMap<Player, Integer> units;
  private HashSet<Territory> neighbours;

  // This units are all owned by the owner of the territory.
  //private List<Unit> ownedUnits;

  // Version 2 variables.
  private int size;
  private int foodResourceGenerationRate;
  private int technologyResourceGenerationRate;
  LinkedList<Army> armies;

  @Override
  public void setOwner(Player player) {
    this.owner = player;
  }

  private void initiateArmies() {
    this.armies = new LinkedList<>();
     V2ArmyFactory af = new V2ArmyFactory();
    Army level0Army = af.generateLZeroArmy(0);
    armies.add(level0Army);
    Army level1Army = af.generateLOneArmy(0);
    armies.add(level1Army);
    Army level2Army = af.generateLTwoArmy(0);
    armies.add(level2Army);
    Army level3Army = af.generateLThreeArmy(0);
    armies.add(level3Army);
    Army level4Army = af.generateLFourArmy(0);
    armies.add(level4Army);
    Army level5Army = af.generateLFiveArmy(0);
    armies.add(level5Army);
    Army level6Army = af.generateLSixArmy(0);
    armies.add(level6Army);
  }

  public V1Territory(String name) {
    this.name = name;
    neighbours = new HashSet<>();
    units = new HashMap<>();
    //ownedUnits = new LinkedList<>();
    size = 0;
    foodResourceGenerationRate = 0;
    technologyResourceGenerationRate = 0;
    initiateArmies();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getNumUnit() {
    int sum = 0;
    for(int i = 0; i < armies.size(); i++){
       sum += armies.get(i).getArmyUnits();
    }
    return sum;
    //return ownedUnits.size();
  }

  @Override
  public Set<Territory> getNeighbours() {
    return neighbours;
  }

  @Override
  public Army getArmyWithLevel(int level) {
    return armies.get(level);
  }

  @Override
  public void addNeighbour(Territory neighbour) {
    if (!neighbours.contains(neighbour)) {
      neighbours.add(neighbour);
    }
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public int getFoodResourceGenerationRate() {
    return foodResourceGenerationRate;
  }

  @Override
  public int getTechnologyResourceGenerationRate() {
    return technologyResourceGenerationRate;
  }

  @Override
  public void setFoodResourceGenerationRate(int rate) {
    this.foodResourceGenerationRate = rate;
  }

  @Override
  public void setTechnologyResourceGenerationRate(int rate) {
    this.technologyResourceGenerationRate = rate;
  }

  @Override
  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public int getUnitNumber(int level) {
    return armies.get(level).getArmyUnits();
  }
}
