/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.element.Element;
import sg.edu.nus.verification.element.Pin;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This pass checks if there are connections that can not be implemented in the real world in the circuit.
 *
 * <p>Here is the checking list:</p>
 *
 * <ul>
 *     <li>Multiple pins in one holes</li>
 *     <li>Still adding more...</li>
 * </ul>
 *
 * @author Lyc
 * @version 2023.02.18
 */
public class ImpossibleConnectionPass extends Pass{
    public ImpossibleConnectionPass() {
        super();
        this.id = "ImpossibleConnectionPass";
    }

    @Override
    public Boolean execute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception {
        if(!this.checkPreRequirements(donePasses)) {
            this.addOutput(new Info("Pre-requirements not satisfied.", InfoType.ERROR));
            return false;
        }

        boolean res = true;

        HashMap<Integer, String> pinOwner = new HashMap<Integer, String>();
        for(Element e : target.getElementList()) {
            for(Pin pin : e.getPins()) {
                int hashCode = Objects.hash(pin.getOriginX(), pin.getOriginY());
                if(pinOwner.get(hashCode) == null) {
                    pinOwner.put(hashCode, e.getOriginId());
                } else {
                    this.output.add(new Info("Multiple pins in one hole.", InfoType.ERROR, e.getOriginId(), pinOwner.get(hashCode)));
                    res = false;
                }
            }
        }

        return res;
    }
}
