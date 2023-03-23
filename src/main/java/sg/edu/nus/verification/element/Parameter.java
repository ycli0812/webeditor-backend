/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.element;

/**
 * Feature of an element.
 *
 * @author Lyc
 * @version 2023.02.06
 */
public class Parameter {
    /**
     * Name of the feature.
     */
    private String name;

    /**
     * Value of the feature. All types of the element features will be parsed into strings to get rid of possible bugs
     * and to make the code simpler. These string values should be parsed into various types in {@code analyseFeatures}
     * method in element classes.
     */
    private String value;

    /**
     * Unit of the feature.
     */
    private String unit;

    public Parameter() {}

    public Parameter(String name, String value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Check if two features are the same.
     *
     * @param p Another feature to compare
     * @return If they are the same
     * @deprecated Maybe we should override {@code equals} method.
     */
    public Boolean compareWith(Parameter p) {
        // TODO compare two Parameters, return true if name, value and unit are all the same
        return true;
    }
}