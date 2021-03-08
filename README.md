# ECE651_G10_RISK

```mermaid
classDiagram
 
    Server "1" --> "n" Player
    Client "1" --> "1" RuleChecker
    Server "1" --> "1" Map
    Map "1" --> "n" Territory
    Player "1" --> "n" Unit
    TextMapFactory "1" <|-- "1" MapFactory
    MapFactory -- Map: "Create"
    Map "1" --> "1" View
    MoveOrder "1" <|-- "1" Order
    AttackOrder "1" <|-- "1" Order
    OrderParser "1" --> "n" Order
    AddUnitOrder "1" <|-- "1" Order
    Player "1" --> "1" OrderParser
    Order "1" --> "n" Dice
    RuleChecker "1" <-- "1" OrderParser
    RuleChecker "1" --|> "1" SelfTerritoryChecker
    RuleChecker "1" --|> "1" EnemyTerritoryChecker
    RuleChecker "1" --|> "1" ConnectedTerritoryChecker
    RuleChecker "1" --|> "1" AdjacentTerritoryChecker
    RuleChecker "1" --|> "1" EnoughUnitsChecker
    InputChecker "1" <|-- "1" RuleChecker


    class Server{
        - int numUnitPerPlayer
        - int numTerritoryPerPlayer
        + checkRule()
        + readInput()
        - checkEndGame()
        + gameInit()
        + distributeResults()
        + issueOrders()
        + exceuteOrders()
    }

    class OrderParser {
        - ArrayList~Order~ orderExcSequence
        + addOrders()
        + clearOrders()
    }

    class Order {
        + executeOrder()
    }

    class MoveOrder {
        - Territory source
        - Territory dest
        - int unitToMove 

    }

    class AttackOrder {
        + HashMap~Territory, int~ unitSource
        + Territory target
    }

    class AddUnitOrder{
        - int numUnitToAdd
    }

    class Dice{

    }

    class Client {
        - int PlayerID
        - bool couldCommand
        - bool isDisconnected
        + checkRule()
        + sendOrders()
        + displyResult()
    }

    class RuleChecker {
        + checkRuleList()
        + checkMyRule()
    }
    
    class SelfTerritoryChecker {

    }

    class EnemyTerritoryChecker {

    }

    class ConnectedTerritoryChecker {

    }

    class AdjacentTerritoryChecker {

    }

    class EnoughUnitsChecker {

    }

    class InputChecker {

    }

    class Player {
        - int ID
        + bool isCommit
        + pickTerritory()
        + setUnits()
        + checkLost()
        + addUnit()
        - moveUnit()
        + destoryUnit()
    }

    class Map {
        - HashMap~Territory, Player~ ownership
    }

    class View {

    }

    class MapFactory {
        + creatMap()
    }

    class TextMapFactory {

    }

    class Territory {
        - HashSet~Territory~ neighbours
    }

    class Unit {
        - Player owner
        - Territory position 
    }       
```