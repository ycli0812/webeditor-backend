/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.element;

import java.util.ArrayList;

public class Wire extends Element{
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    public Wire(String id, String originId, int originX, int originY, ArrayList<Parameter> features, ArrayList<Pin> pins) {
        super(id, originId, originX, originY, features, pins);
        this.analyseFeatures();
    }

    @Override
    public String getType() {
        return "wire";
    }

    @Override
    public Boolean compareConnection(Element e) {
        return false;
    }

    @Override
    protected void analyseFeatures() {
        for(Parameter p : this.parameters) {
            switch(p.getName()) {
                case "x1": {
                    this.x1 = Integer.parseInt(p.getValue());
                    break;
                }
                case "x2": {
                    this.x2 = Integer.parseInt(p.getValue());
                    break;
                }
                case "y1": {
                    this.y1 = Integer.parseInt(p.getValue());
                    break;
                }
                case "y2": {
                    this.y2 = Integer.parseInt(p.getValue());
                    break;
                }
                default: break;
            }
        }
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }
}
