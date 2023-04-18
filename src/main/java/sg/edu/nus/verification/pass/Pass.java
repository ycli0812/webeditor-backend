/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.ArrayList;

/**
 * A certain function in the verification process.
 *
 * @author Lyc
 * @version 2023.02.06
 */
public abstract class Pass {
    /**
     * ID of the pass. All the derived classes should have unique IDs.
     */
    protected String id;

    /**
     * Outputs generated during the execution.
     */
    protected ArrayList<Info> output;

    /**
     * List of IDs of passes that must be done before this pass.
     */
    protected ArrayList<String> preRequirements;

    public Pass() {
        this.output = new ArrayList<Info>();
        this.preRequirements = new ArrayList<String>();
    }

    /**
     * Check if all the pre-requirements are satisfied and call {@code execute()} method. This method is supposed to be
     * called by verifiers.
     *
     * @param example Sample circuit
     * @param target Target circuit
     * @param donePasses List of executed pass IDs
     * @return Result of execution
     * @throws Exception Exception thrown by {@code execute()}
     */
    public final boolean doExecute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception {
        for(String pre : this.preRequirements) {
            if(!donePasses.contains(pre)) {
                this.addOutput(new Info("Pre-requirements of " + this.getId() + " not satisfied.", InfoType.ERROR));
                return false;
            }
        }
        return this.execute(example, target);
    }

    /**
     * Entry of the real pass function.
     *
     * @param example Sample circuit
     * @param target Target circuit
     * @return Result of execution
     * @throws Exception If something goes wrong, any type of exception might be thrown out
     */
    public abstract Boolean execute(Circuit example, Circuit target) throws Exception;

    public final String getId() {
        return id;
    }

    public final ArrayList<Info> getOutput() {
        return output;
    }

    public final ArrayList<String> getPreRequirements() {
        return preRequirements;
    }

    protected final void addOutput(Info info) {
        this.output.add(info);
    }
}
