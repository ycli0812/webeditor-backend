package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;

import java.util.ArrayList;

/**
 * @author Lyc
 * @version 2023.04.01
 */
public class ConnectionMatchingPass extends Pass {
    public ConnectionMatchingPass() {
        super();
        this.id = "ConnectionMatchingPass";
    }

    @Override
    public Boolean execute(Circuit example, Circuit target) throws Exception {
        return true;
    }
}
