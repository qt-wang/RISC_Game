package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class V2SetNumGeneratorTest {

    @Test
    public void test_set_num_generator() {
        SetSumGenerator generator = new V2SetNumGenerator(4, 1, 50, 50);
        int[] test = generator.get();
        int total = 0;
        for (int i = 0; i < 4; i++) {
            //System.out.println(test[i]);
            total += test[i];
        }
        assertEquals(50, total);
    }
}