# ECE651_G10_RISK

## Running Instruction
 1. Go to root directory in the project.
 2. Run './gradlew installDist'
 3. Run server first through './server/build/install/server/bin/server'.
 4. Run several clients through './client/build/install/client/bin/client'
 5. Enjoy the game!

```mermaid
classDiagram
 
RuleChecker "1" <-- "1" Server
    SufficientUnitChecker "1" <|-- "1" RuleChecker~T extends Order~
    AdjacentTerritoryChecker "1" <|-- "1" RuleChecker~T extends Order~
    SelfTerritoryChecker "1" <|-- "1" RuleChecker~T extends Order~
    PlayerSelfOrderChecker "1" <|-- "1" RuleChecker~T extends Order~
    EnemyTerritoryChecker "1" <|-- "1" RuleChecker~T extends Order~
    ConnectedTerritoryChecker "1" <|-- "1" RuleChecker~T extends Order~
    TerritoryExistChecker "1" <|-- "1" RuleChecker~T extends Order~
    AttackFoodChecker "1" <|-- "1" RuleChecker~T extends Order~
    CanUpgradeTechChecker "1" <|-- "1" RuleChecker~T extends Order~
    TechUpgradeRangeChecker "1" <|-- "1" RuleChecker~T extends Order~
    SufficientTechResourceChecker "1" <|-- "1" RuleChecker~T extends Order~
    Server "1" --> "n" Player
    Server "1" --> "1" Map
    Map "1" --> "n" Territory
    Player "1" --> "n" Unit
    TextMapFactory "1" <|-- "1" MapFactory
    MapFactory -- Map: "Create"
    Client "1" --> "1" View
    MoveOrder "1" <|-- "1" Order
    AttackOrder "1" <|-- "1" Order
    UpgradeUnitOrder "1" <|-- "1" Order
    UpgradeTechOrder "1" <|-- "1" Order
    OrderProcessor "1" --> "n" Order
    AddUnitOrder "1" <|-- "1" Order
    Server "1" --> "1" OrderProcessor
    AttackOrder "1" --> "n" Dice
    Player "1" --> "1" Resources

    class Server{
        -int numUnitPerPlayer
        -int numTerritoryPerPlayer
        +checkRule()
        -combat()
        +readInput()
        -checkEndGame()
        +assignTerritory()
        +distributeResults()
    }

    class OrderProcessor {
        -HashMap~Player, Vector~Order~~ attacksInOneTurn
        +acceptOrder()
        +executeEndTurnOrders()
        -merge()
        -Vector~Order~ obtainAllAttackOrders()
    }
    
    class Order {
        -int playerID
        +execute()
        +getPlayerID()
    }

    class MoveOrder {
        -Territory source
        -Territory dest
        -int unitNum
        -GameMap gMap
        +getNumUnit()
        +getSourceTerritory()
        +getTargetTerritory()
    }

    class AttackOrder {
        -Territory attacker
        -Territory defender
        -int unitNum
        -Player owner
        -GameMap gMap
        +getNumUnit()
        +getSourceTerritory()
        +getTargetTerritory()
    }
    
    class UpgradeUnitOrder {
        -int level
        -GameMap gMap
        -String source
        -int unitNum
        +execute()
    }

    class UpgradeTechOrder {
        -GameMap gMap
        +execute()
    }

    class AddUnitOrder{
        -Territory bornTerritory
        -int unitNum
    }

    class Dice{
        -int sides
        +roll()
    }

    class Client {
        -int PlayerID
        -HashSet~String~ normalOrderSet
        -HashMap~String, String~ orderKeyMap
        -HashMap~String, Runnable~ commandMap
        +connectGame()
        +doPlacement()
        +playGame()
    }

    class RuleChecker~T extends Order~ {
        -RuleChecker nextRule
        -checkMyRule()
        +checkOrder()
    }

    class ConnectedTerritoryChecker {

    }

    class AdjacentTerritoryChecker {

    }

    class SufficientUnitChecker {

    }
    
    class PlayerSelfOrderChecker {

    }

    class SelfTerritoryChecker {

    }

    class EnemyTerritoryChecker {

    }

    class TerritoryExistChecker {

    }

    class AttackFoodChecker{

    }

    class CanUpgradeTechChecker{

    }

    class TechUpgradeRangeChecker{

    }

    class SufficientTechResourceChecker{

    }

    class Player {
        -int ID
        +pickTerritory()
        +setUnits()
        +commitOrders()
        +destoryUnit()
    }

    class Map {
        -HashMap~Territory, Player~ ownership
    }

    class View {

    }

    class MapFactory {
        +creatMap()
    }

    class TextMapFactory {

    }

    class Territory {
        -String name
        -HashSet~Territory~ neighbours
        -HashMap~String,List~Unit~~ units 
        +getNumUnit()
    }

    class Resources{
        -int foodResource
        -int techResource
        -int techLevel
        +consumeFood()
        +consumeTech()   
    }

    class Unit {
        -Player owner
        -Territory position 
    }  
    


```
