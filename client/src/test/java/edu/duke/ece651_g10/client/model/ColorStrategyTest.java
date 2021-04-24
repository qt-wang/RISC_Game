package edu.duke.ece651_g10.client.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorStrategyTest {

    @Test
    void getRegularStyle() {
        ColorStrategy cs = new ColorStrategy();
        String red = cs.getRegularStyle("red");
        assertEquals(red, "-fx-background-color: black,#F30404; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        assertNull(cs.getRegularStyle("notExist"));
    }

    @Test
    void getHoverStyle() {
        ColorStrategy cs = new ColorStrategy();
        String yellow = cs.getHoverStyle("yellow");
        assertEquals(yellow, "-fx-background-color: #f6f48b,#f6f204; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        assertNull(cs.getHoverStyle("notExist"));
    }

    @Test
    void getPressedStyle() {
        ColorStrategy cs = new ColorStrategy();
        String purple = cs.getPressedStyle("purple");
        assertEquals(purple, "-fx-background-color: #450045,#450045; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        assertNull(cs.getPressedStyle("notExist"));
    }

}