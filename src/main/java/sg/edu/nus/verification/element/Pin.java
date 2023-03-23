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
 * Pin of an element.
 *
 * @author Lyc
 * @version 2023.02.06
 */
public class Pin {
    /**
     * ID(name, more strictly) of the pin. Different instances of one type of element have pins with same IDs. IDs of
     * all pins under one type of element should be unique.
     */
    private final String id;
    private int originX;
    private int originY;

    /**
     * Which element this pin belongs to.
     */
    private String elementId;

    /**
     * List of pins connected to this pin.
     */
    private ArrayList<Pin> connections;

    public Pin(String id) {
        this.id = id;
    }

    public Pin(int originX, int originY, String id, String elementId) {
        this.id = id;
        this.originX = originX;
        this.originY = originY;
        this.elementId = elementId;
        this.connections = new ArrayList<Pin>();
    }

    /**
     * Connect a pin to this pin.
     * @param p Pin to connect
     */
    public void connect(Pin p) {
        connections.add(p);
    }

    public String getId() {
        return id;
    }

    public ArrayList<Pin> getConnections() {
        return connections;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public String getElementId() {
        return elementId;
    }
}
