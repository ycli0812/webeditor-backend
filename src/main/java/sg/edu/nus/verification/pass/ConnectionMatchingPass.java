package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.element.Element;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lyc
 * @version 2023.04.01
 */
public class ConnectionMatchingPass extends Pass {
    public ConnectionMatchingPass() {
        super();
        this.id = "ConnectionMatchingPass";
        this.preRequirements.add("ConnectivityAnalysisPass");
        this.preRequirements.add("ElementMatchingPass");
    }

    @Override
    public Boolean execute(Circuit example, Circuit target) throws Exception {
        Set<String> checkedId = new HashSet<>();
        for(Element eSample : example.getElements()) {
            boolean matched = false;
            for(Element eTarget : target.getElements()) {
                if(eSample.compareConnection(eTarget)) {
                    matched = true;
                    break;
                }
            }
            if(!matched) {
                this.output.add(new Info("Element connection not matched.", InfoType.ERROR));
                return false;
            }
        }
        return true;
    }
}
