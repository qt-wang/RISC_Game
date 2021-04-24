package edu.duke.ece651_g10.server;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class V1TerritoryTest {

    @Test
    public void test_territory() {
        Territory t = new V1Territory("test");
        assertEquals(t.getName(), "test");
    }


    @Test
    public void test_present_territory_info() {
        Territory t = new V1Territory("test");
        Player p = mock(Player.class);
        when(p.getPlayerID()).thenReturn(1);
        t.setOwner(p);
        t.setFoodResourceGenerationRate(20);
        t.setTechnologyResourceGenerationRate(15);
        t.increaseUnit(3, 3);;
        t.increaseUnit(20, 0);
        JSONObject object = t.presentTerritoryInformation();

        assertEquals(20, object.getInt("foodResourceGenerationRate"));
        assertEquals(15, object.getInt("technologyResourceGenerationRate"));

        JSONObject army = object.getJSONObject("armies");
        assertEquals(3, army.getInt("3"));
        assertEquals(20, army.getInt("0"));
        assertEquals(1, object.getInt("owner"));
    }
    @Test
    public void test_getter_setter() {
        Territory t = new V1Territory("test");
        assertEquals(t.getFoodResourceGenerationRate(), 0);
        assertEquals(t.getTechnologyResourceGenerationRate(), 0);

        t.setFoodResourceGenerationRate(100);
        assertEquals(100, t.getFoodResourceGenerationRate());

        t.setTechnologyResourceGenerationRate(120);
        assertEquals(120, t.getTechnologyResourceGenerationRate());
    }

    @Test
    public void test_cloak() {
        Territory t = new V1Territory("test");
        //User issue the command.
        t.getCloaked();
        assertEquals(true, t.isHidden());
        // Execute the end-turn command.
        t.decreaseCloakLastTime();

        t.decreaseCloakLastTime();
        t.decreaseCloakLastTime();
        assertEquals(true, t.isHidden());
        t.decreaseCloakLastTime();
        assertEquals(false, t.isHidden());
    }
}