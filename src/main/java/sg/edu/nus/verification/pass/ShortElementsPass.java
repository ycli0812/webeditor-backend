package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.element.Element;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.ArrayList;

/**
 * Pass that finds short elements using isShort() method od Element class.
 *
 * @author Lyc
 * @version 2023.04.01
 */
public class ShortElementsPass extends Pass {
    public  ShortElementsPass() {
        super();
        this.id = "ShortElementsPass";
        this.preRequirements.add("ConnectivityAnalysisPass");
    }

    @Override
    public Boolean execute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception {
        for(Element e : target.getElements()) {
            if(e.isShort()) {
                this.addOutput(new Info("Short element found.", InfoType.WARNING, e.getOriginId()));
            }
        }
        return true;
    }
}
