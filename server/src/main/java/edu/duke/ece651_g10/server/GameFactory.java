package edu.duke.ece651_g10.server;

/**
 * A Game factory used to generate games.
 */
public interface GameFactory {

    public Game createFixedGame(int people);

    /**
     * Create a test game for the purpose of testing persistence.
     * Test whether all the attributes of a game can be saved into the db.
     * @param people
     * @return
     */
    public Game createTestGame(int people);


    public static RuleChecker getMoveRuleChecker() {
        return new TerritoryExistChecker(new PlayerSelfOrderChecker(new SelfTerritoryChecker(new ConnectedTerritoryChecker(new SufficientUnitChecker(new MoveFoodChecker(null))))));
    }

    public static RuleChecker getAttackRuleChecker() {
        return new TerritoryExistChecker(new PlayerSelfOrderChecker(new EnemyTerritoryChecker(new AdjacentTerritoryChecker(new SufficientUnitChecker(new AttackFoodChecker(null))))));
    }

    public static RuleChecker getUpgradeUnitChecker() {
        return new SelfUpgradeOrderChecker(new UnitUpgradeTechChecker(new UnitUpgradeRangeChecker(new UpgradeSufficientUnitChecker(new TechResourceUnitChecker(null)))));
    }

    public static RuleChecker getUpgradeTechChecker() {
        return new CanUpgradeTechChecker(new TechUpgradeRangeChecker(new SufficientTechResourceChecker(null)));
    }

    public static RuleChecker getResearchCloakChecker() {
        return new ResearchCloakResourceChecker(new ResearchCloakLevelChecker(new CanResearchCloakChecker(null)));
    }

    public static RuleChecker getCloakChecker() {
        return new OneTerritorySelfChecker(new CloakResourceChecker(new CanCloakChecker(null)));
    }

    public static RuleChecker getBombChecker(){
        return new OneTerritoryEnemyChecker(new CanBombChecker(null));
    }

    public static RuleChecker getVirusChecker(){
        return new OneTerritoryEnemyChecker(new VirusResourceChecker(new VirusLevelChecker(null)));
    }

    public static RuleChecker getUpgradeVirusMaxChecker() {
        return new UpgradeVirusMaxLevelChecker(new UpgradeVirusMaxResourceChecker(null));
    }

    public static RuleChecker getVaccineChecker(){
        return new VaccineLevelChecker(new VaccineResourceChecker(null));
    }

    public static RuleChecker getUpgradeVaccineMaxChecker(){
        return new UpgradeVaccineMaxLevelChecker(new UpgradeVirusMaxResourceChecker(null));
    }

    public static RuleChecker getUpgradeSpyChecker(){
        return new OneTerritorySelfChecker(new UpgradeSpyEnoughUnitChecker(new UpgradeSpyLevelChecker(new UpgradeSpyResourceChecker(null))));
    }

    public static RuleChecker getMoveSpyChecker(){
        return new MoveSpyEnoughSpies(new SpyMoveTerritoryChecker(null));
    }
}
