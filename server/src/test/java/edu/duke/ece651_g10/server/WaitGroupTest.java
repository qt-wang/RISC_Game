package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitGroupTest {
    @Test
    public void test_simple_wait_group() {
        WaitGroup test = new WaitGroup(2);
        assertEquals(false, test.getState());

        test.decrease();
        test.decrease();
        assertEquals(true, test.getState());

        test.increase();
        assertEquals(false, test.getState());
    }
}