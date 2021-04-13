package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class SpyUnitTest {
    @Test
    public void test_spy_unit() {
        Player p1 = mock(Player.class);
        V1Territory t = new V1Territory("test");
        V1Territory newPosition = new V1Territory("new");
        Spy spyUnit = new SpyUnit(p1, t);
        t.addEnemySpy(spyUnit);
        assertEquals(t.enemySpies.contains(spyUnit), true);
        assertEquals(true, t.spyInTerritory(p1));
        assertEquals(p1, spyUnit.getOwner());
        assertEquals(t, spyUnit.getCurrentPosition());

        spyUnit.moveTo(newPosition);
        assertEquals(false, t.spyInTerritory(p1));
        assertEquals(newPosition, spyUnit.getCurrentPosition());

        assertEquals(true, newPosition.enemySpies.contains(spyUnit));
        assertEquals(false, t.enemySpies.contains(spyUnit));
        assertEquals(true, newPosition.spyInTerritory(p1));
    }
}