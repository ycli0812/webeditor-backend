/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Resistor extends Element {
    private long resistance;

    private String tolerance;

    public Resistor(String id, String originId, int originX, int originY, ArrayList<Parameter> features, ArrayList<Pin> pins) {
        super(id, originId, originX, originY, features, pins);
        this.analyseFeatures();
    }

    @Override
    public String getType() {
        return "resistor";
    }

    @Override
    public Boolean compareConnection(Element e) {
        return true;
    }

    @Override
    public Boolean compareFeatures(Element e) {
        if(e.getType() != "resistor") return false;
        return ((Resistor)e).getResistance() == this.resistance && ((Resistor)e).getTolerance() == this.tolerance;
    }

    @Override
    protected void analyseFeatures() {
        for(Parameter p : this.parameters) {
            String name = p.getName();
            String value = p.getValue();
            String unit = p.getUnit();
            switch (name) {
                case "resistance": {
                    this.resistance = Long.parseLong(value);
                    break;
                }
                case "tolerance": {
                    this.tolerance = value;
                    break;
                }
                default: break;
            }
        }
    }

    /**
     * Resistor is short when the two pins are connected.
     *
     * @return If both pins are connected.
     */
    @Override
    public boolean isShort() {
        List<Pin> pins = this.getPins();
        return pins.get(0).getConnections().contains(pins.get(1));
    }

    @Override
    public int hashCode() {
        return Objects.hash("resistor", this.resistance, this.tolerance);
    }

    public long getResistance() {
        return resistance;
    }

    public String getTolerance() {
        return tolerance;
    }
}
