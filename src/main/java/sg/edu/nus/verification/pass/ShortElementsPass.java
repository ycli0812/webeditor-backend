package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.element.Element;
import sg.edu.nus.verification.element.Pin;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.ArrayList;

public class ShortElementsPass extends Pass {
    public  ShortElementsPass() {
        super();
        this.id = "ShortElementsPass";
        this.preRequirements.add("ConnectivityAnalysisPass");
    }

    @Override
    public Boolean execute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception {
        for(Element e : target.getElementList()) {
            if(e.isShort()) {
                this.addOutput(new Info("Short element found.", InfoType.WARNING, e.getOriginId()));
            }
        }
        return true;
    }
}
