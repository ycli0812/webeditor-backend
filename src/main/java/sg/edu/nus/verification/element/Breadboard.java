/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.element;

import java.util.ArrayList;

public class Breadboard extends Element{
    private int columns;
    private boolean extended;

    public Breadboard(String id, String originId, int originX, int originY, ArrayList<Parameter> features, ArrayList<Pin> pins) {
        super(id, originId, originX, originY, features, pins);
        this.analyseFeatures();
        this.pins = new ArrayList<Pin>();
    }

    @Override
    public String getType() {
        return "breadboard";
    }

    @Override
    public Boolean compareConnection(Element e) {
        return true;
    }

    @Override
    public Boolean compareFeatures(Element e) {
        return e.getType() == "breadboard";
    }

    @Override
    protected void analyseFeatures() {
        for(Parameter p : this.parameters) {
            switch (p.getName()) {
                case "column": {
                    this.columns = Integer.parseInt(p.getValue());
                    break;
                }
                case "extended": {
                    this.extended = Boolean.parseBoolean(p.getValue());
                    break;
                }
                default: break;
            }
        }
    }

    public Boolean isOnBreadboard(int x, int y) {
        int dx = x - this.originX;
        int dy = y - this.originY;
        if(this.extended) {
            return ((dy > 0 && dy < 3) || (dy > 3 && dy < 9) || (dy > 9 && dy < 15) || (dy > 15 && dy < 18)) && (dx > 0 && dx <= this.columns);
        } else {
            return ((dy > 3 && dy < 9) || (dy > 9 && dy < 15)) && (dx > 0 && dx <= this.columns);
        }
    }

    public String area(int x, int y) {
        int dx = x - this.originX;
        int dy = y - this.originY;
        if(!(dx > 0 && dx <= this.columns)) {
            return "outside";
        }
        if(dy > 3 && dy < 9) {
            return "upBoard";
        }
        if(dy > 9 && dy < 15) {
            return "downBoard";
        }
        if(this.extended) {
            if(dy > 0 && dy < 3) {
                return "upSide";
            }
            if(dy > 15 && dy < 18) {
                return "downSide";
            }
        }
        return "outside";
    }
}
