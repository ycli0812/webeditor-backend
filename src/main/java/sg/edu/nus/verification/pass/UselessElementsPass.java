/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.element.Breadboard;
import sg.edu.nus.verification.element.Element;
import sg.edu.nus.verification.element.Pin;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.ArrayList;

/**
 * This pass finds and removes elements whose one or more pins are not on any breadboards.
 *
 * @author Lyc
 * @version 2023.02.06
 */
public class UselessElementsPass extends Pass {
    public UselessElementsPass() {
        // Call parent constructor
        super();
        // Set id
        this.id = "UselessElementsPass";
        // Set pre-requirements
    }

    /**
     * Find the breadboard in the given circuit.
     *
     * @param circuit Circuit to find breadboard in
     * @return Pointer to the found breadboard. Return null if no breadboards are found
     */
    private Breadboard findBreadboard(Circuit circuit) {
        // TODO: 2023/3/17 find all breadboard instead of the first one
        Breadboard bd = null;
        for(Element e : circuit.getElements()) {
            if(e.getType().equals("breadboard")) {
                bd = (Breadboard) e;
                break;
            }
        }
        return bd;
    }

    @Override
    public Boolean execute(Circuit example, Circuit target) {
//        if(!this.checkPreRequirements(donePasses)) {
//            this.addOutput(new Info("Pre-requirements not satisfied.", InfoType.ERROR));
//            return false;
//        }

        Breadboard bd = this.findBreadboard(target);

        // Find breadboard
        if(bd == null) {
            this.addOutput(new Info("No breadboard.", InfoType.WARNING));
            return false;
        }

        // Check all the elements
        for (int i = 0; i< target.getElements().size(); i++) {
            Element e = target.getElements().get(i);
            for(Pin pin : e.getPins()) {
                if(!bd.isOnBreadboard(pin.getOriginX(), pin.getOriginY())) {
                    this.addOutput(new Info("Element not on breadboard.", InfoType.WARNING, e.getOriginId()));
                    target.getElements().remove(i);
                    break;
                }
            }
        }
        return true;
    }
}