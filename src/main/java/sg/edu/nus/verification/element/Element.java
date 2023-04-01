/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.element;

import java.util.ArrayList;

/**
 * Abstract of element in the circuit, including wires.
 *
 * @author Lyc
 * @version 2023.02.06
 */
public abstract class Element {
    /**
     * A given ID only used during the verification process.
     */
    protected String id;

    /**
     * Original ID in the input circuit.
     */
    protected String originId;

    /**
     * Pins of the element.
     */
    protected ArrayList<Pin> pins;

    /**
     * Features of the element.
     */
    protected ArrayList<Parameter> parameters;

    /**
     * X-axis coordinate of the element.
     */
    protected int originX;

    /**
     * Y-axis coordinate of the element.
     */
    protected int originY;

    public Element(String id, String originId, int originX, int originY, ArrayList<Parameter> features, ArrayList<Pin> pins) {
        this.pins = pins;
        this.id = id;
        this.originId = originId;
        this.originX = originX;
        this.originY = originY;
        this.parameters = features;
    }

    public Pin getPin(String id) {
        for(Pin p : this.pins) {
            if(id.equals(p.getId())) {
                return p;
            }
        }
        return null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getOriginId() {
        return originId;
    }

    public ArrayList<Pin> getPins() {
        return pins;
    }

    public Boolean isConnectedTo(String elementId) {
        // TODO check if element with given id is connected to this one, any pin is ok
        return true;
    }

    public abstract String getType();

    public abstract Boolean compareConnection(Element e); // Should be overridden by extended classes

    /**
     * Parse parameter list and assign feature properties.
     *
     * <p>Specific element classes may have properties indicating element features. These properties should be
     * initialized int this method based on {@code features}.</p>
     */
    protected abstract void analyseFeatures();

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    /**
     * Check if the element is short.
     *
     * @return Result of the check
     */
    public boolean isShort() {
        return false;
    }
}
