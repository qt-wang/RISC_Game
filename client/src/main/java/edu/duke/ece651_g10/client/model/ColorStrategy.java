package edu.duke.ece651_g10.client.model;

import java.util.HashMap;

public class ColorStrategy {
    /**
     * a class for the ease of getting button style string while runtime
     * avoid to access css file
     */

    //a map to store <colorName,regularButtonStyleString>
    public final HashMap<String, String> buttonStyle;

    //a map to store <colorName,hoverButtonStyleString>
    public final HashMap<String, String> buttonHoverStyle;

    //a map to store <colorName,pressedButtonStyleString>
    public final HashMap<String, String> buttonPressedStyle;


    public ColorStrategy(){
        buttonStyle = new HashMap<>();
        buttonHoverStyle = new HashMap<>();
        buttonPressedStyle = new HashMap<>();
        addRegularStyles();
        addHoverStyles();
        addPressedStyles();
    }

    /**
     * init the regular button style map
     */
    private void addRegularStyles(){
        buttonStyle.put("red","-fx-background-color: black,#F30404; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonStyle.put("green","-fx-background-color: black,#3CF807; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonStyle.put("yellow","-fx-background-color: black,#f6f204; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonStyle.put("blue","-fx-background-color: black,#0398E5; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonStyle.put("purple","-fx-background-color: black,purple; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
    }

    /**
     * init the hover button style map
     */
    private void addHoverStyles(){
        buttonHoverStyle.put("red","-fx-background-color: #e95555,#F30404; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonHoverStyle.put("green","-fx-background-color: #aaf58c,#3CF807; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonHoverStyle.put("yellow","-fx-background-color: #f6f48b,#f6f204; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonHoverStyle.put("blue","-fx-background-color: #88caef,#0398E5; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonHoverStyle.put("purple","-fx-background-color: #dd23dd,purple; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
    }

    /**
     * init the pressed button style map
     */
    private void addPressedStyles(){
        buttonPressedStyle.put("red","-fx-background-color: #750101,#750101; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonPressedStyle.put("green","-fx-background-color: #156201,#156201; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonPressedStyle.put("yellow","-fx-background-color: #5f5d02,#5f5d02; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonPressedStyle.put("blue","-fx-background-color: #065381,#065381; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
        buttonPressedStyle.put("purple","-fx-background-color: #450045,#450045; " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 5, 5; " +
                "-fx-background-insets: 2, 4;");
    }

    /**
     * get the regular button style string
     * @param colorName the requested color name
     * @return the regular button style string
     */
    public String getRegularStyle(String colorName){
        return buttonStyle.getOrDefault(colorName,null);
    }

    /**
     * get the hover button style string
     * @param colorName the requested color name
     * @return the hover button style string
     */
    public String getHoverStyle(String colorName){
        return buttonHoverStyle.getOrDefault(colorName,null);
    }

    /**
     * get the pressed button style string
     * @param colorName the requested color name
     * @return the pressed button style string
     */
    public String getPressedStyle(String colorName){
        return buttonPressedStyle.getOrDefault(colorName,null);
    }
}
