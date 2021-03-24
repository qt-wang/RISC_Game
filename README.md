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
    SufficientUnitChecker "1" <|-- "1" RuleChecker
    AdjacentTerritoryChecker "1" <|-- "1" RuleChecker
    SelfTerritoryChecker "1" <|-- "1" RuleChecker
    PlayerSelfOrderChecker "1" <|-- "1" RuleChecker
    EnemyTerritoryChecker "1" <|-- "1" RuleChecker
    ConnectedTerritoryChecker "1" <|-- "1" RuleChecker
    ResourceChecker "1" <|-- "1" RuleChecker
    AttackResourceChecker "1" <|-- "1" ResourceChecker
    MoveResourceChecker "1" <|-- "1" ResourceChecker
    UpgradeResourceChecker "1" <|-- "1" ResourceChecker
    ResourceChecker "1" --> "1" ShortestPath
    InputChecker "1" <-- "1" Client
    Server "1" --> "n" Player
    Server "1" --> "1" Map
    Map "1" --> "n" Territory
    Player "1" --> "n" Unit
    TextMapFactory "1" <|-- "1" MapFactory
    MapFactory -- Map: "Create"
    Client "1" --> "1" View
    MoveOrder "1" <|-- "1" Order
    AttackOrder "1" <|-- "1" Order
    OrderProcessor "1" --> "n" Order
    OrderProcessor "1" --> "1" ShortestPath
    Client "1" --> "n" Order
    UpgradeOrder "1" <|-- "1" Order
    UnitUpgradeOrder "1" <|-- "1" UpgradeOrder
    TechnologyUpgradeOrder "1" <|-- "1" UpgradeOrder
    Server "1" --> "1" OrderProcessor
    AttackOrder "1" --> "n" TwentySidesDice



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

    class ShortestPath {
        -GameMap map
    }

    class OrderProcessor {
        -HashMap<Player, Vector<Order>> attacksInOneTurn
        +acceptOrder(Order order)
        +executeEndTurnOrders()
        -merge(Vector<Order> vector)
        -Vector<Order> obtainAllAttackOrders()
    }

    class Order {
        -int playerID
        +execute()
        +getNumUnit()
        +getSourceTerritory()
        +getTargetTerritory()
        +getPlayerID()
    }

    class MoveOrder {
        -Territory source
        -Territory dest
        -int unitNum
        -GameMap gMap
    }

    class AttackOrder {
        -Territory attacker
        -Territory defender
        -int unitNum
        -Player owner
        -GameMap gMap
    }

    class UpgradeOrder {

    }

    class UnitUpgradeOrder {

    }

    class TechnologyUpgradeOrder {

    }

    class TwentySidesDice{
        -int sides
        +int roll(long seed)
    }

    class Client {
        -int PlayerID
        -bool couldCommand
        -bool isDisconnected
        +checkRule()
        +sendOrders()
        +displyResult()
        +createAccount()
        +login()
        +logout()
    }

    class RuleChecker {
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

    class ResourceChecker {
        -HashMap~String, HashMap~int, int~ ~ ResourceCost 
    }

    class AttackResourceChecker {

    }

    class MoveResourceChecker {

    }

    class UpgradeResourceChecker {
        
    }

    class InputChecker {

    }

    class Player {
        -int ID
        -boolean isUpgrade
        -HashMap~String, int~ resourcesTotal
        -int technologyLevel
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
        -HashMap~String,int~ resources
        +getNumUnit()
    }

    class Unit {
        -Player owner
        -int level
        -Territory position 
    }       
```
