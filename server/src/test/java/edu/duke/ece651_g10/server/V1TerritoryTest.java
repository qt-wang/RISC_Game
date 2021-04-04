package edu.duke.ece651_g10.server;

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
    public void test_getter_setter() {
        Territory t = new V1Territory("test");
        assertEquals(t.getFoodResourceGenerationRate(), 0);
        assertEquals(t.getTechnologyResourceGenerationRate(), 0);

        t.setFoodResourceGenerationRate(100);
        assertEquals(100, t.getFoodResourceGenerationRate());

        t.setTechnologyResourceGenerationRate(120);
        assertEquals(120, t.getTechnologyResourceGenerationRate());
    }
}