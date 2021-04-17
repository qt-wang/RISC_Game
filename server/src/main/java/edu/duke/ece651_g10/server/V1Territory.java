package edu.duke.ece651_g10.server;

import org.json.JSONObject;

import java.util.*;

/**
 * This class represents one territory of the map. Maintained by Guancheng Fu.
 */
public class V1Territory implements Territory {
    @Deprecated
    @Override
    public void setUnitNumber(int unit) {
        //armies.get(0).increaseUnits(unit);
        armies.get(0).decreaseUnits(armies.get(0).getArmyUnits());
        armies.get(0).increaseUnits(unit);
    }


    @Override
    public void decreaseUnit(int unit, int level) {
        armies.get(level).decreaseUnits(unit);
    }


    @Override
    public void increaseUnit(int unit, int level) {
        armies.get(level).increaseUnits(unit);
    }


    private final String name;
    private Player owner;
    private HashSet<Territory> neighbours;

    // This units are all owned by the owner of the territory.
//    private List<Unit> ownedUnits;


    // Version 2 variables.
    private int size;
    private int foodResourceGenerationRate;
    private int technologyResourceGenerationRate;
    LinkedList<Army> armies;

    // Version 3 variables.
    Set<Spy> ownedSpies;
    Set<Spy> enemySpies;
    //TODO: Update the oldViews at the beginning of the initialization.
    HashMap<Player, JSONObject> oldViews;
    //TODO: If user use the command, then set this number to 4 (it will be automatically decreased at the end of turn.)
    //If this number is 0, then this territory is not hidden.
    private int hiddenFromOthers;


    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    private void initiateArmies(){
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
        size = 0;
        foodResourceGenerationRate = 0;
        technologyResourceGenerationRate = 0;
        armies = new LinkedList<>();
        initiateArmies();
        this.ownedSpies = new HashSet<>();
        this.enemySpies = new HashSet<>();
        this.oldViews = new HashMap<>();
        this.hiddenFromOthers = 0;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getNumUnit() {
        int sum = 0;
        for (int i = 0; i < armies.size();i ++) {
            sum += armies.get(i).getArmyUnits();
        }
        return sum;
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
    public JSONObject presentTerritoryInformation() {
        JSONObject result = new JSONObject();
        JSONObject army = new JSONObject();
        for (int i = 0;i < this.armies.size(); i ++) {
            army.put(Integer.toString(i), armies.get(i).getArmyUnits());
        }
        result.put("foodResourceGenerationRate", this.foodResourceGenerationRate);
        result.put("technologyResourceGenerationRate", this.technologyResourceGenerationRate);
        result.put("armies", army);
        result.put("owner", this.owner.getPlayerID());
        result.put("territorySize", this.size);
        return result;
    }



    /**
     * Check if the player 'p' has a spy within the territory.
     * @param p The player.
     * @return True if the player p has a spy within the territory.
     */
    public boolean spyInTerritory(Player p) {
        for (Spy spy: enemySpies) {
            if (spy.getOwner() == p) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addOwnedSpy(Spy spy) {
        if (!this.ownedSpies.contains(spy)) {
            this.ownedSpies.add(spy);
        }
    }

  @Override
  public Set<Spy> getOwnedSpy() {
    return ownedSpies;
  }

  @Override
  public Set<Spy> getEnemySpy() {
    return enemySpies;
  }

    @Override
    public void setPlayerView(Player player, JSONObject view) {
        oldViews.put(player, view);
    }

//
//    @Override
//    public JSONObject presentTerritoryInformation(Player player) {
//        return null;
//    }

    @Override
    public void addEnemySpy(Spy spy) {
        enemySpies.add(spy);
    }

    //TODO:Need test.
    @Override
    public JSONObject getOldView(Player player) {
        if (oldViews.containsKey(player)) {
            return oldViews.get(player);
        } else {
            return null;
        }
    }

    @Override
    public void decreaseSpy(Spy spy) {
        if (ownedSpies.contains(spy)) {
            ownedSpies.remove(spy);
            return;
        }
        if (enemySpies.contains(spy)) {
            enemySpies.remove(spy);
        }
    }



    @Override
    public Set<Territory> getNeighborBelongToOwner() {
        Set<Territory> result = new HashSet<>();
        for (Territory t: neighbours) {
            if (t.getOwner() == this.owner) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public boolean isHidden() {
        return !(this.hiddenFromOthers == 0);
    }

    @Override
    public void decreaseCloakLastTime() {
        if (this.hiddenFromOthers != 0) {
            this.hiddenFromOthers -= 1;
        }
    }

    @Override
    public void getCloaked() {
        this.hiddenFromOthers += 4;
    }


    @Override
    public void addNeighbour(Territory neighbour) {
        if (!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
    }

    @Override
    public Player getOwner(){
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
