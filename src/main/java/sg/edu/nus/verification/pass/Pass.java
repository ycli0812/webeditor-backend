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
     * Check if all the pre-requirements are satisfied by the given list of IDs of executed passes.
     *
     * @param donePasses List of IDs of executed passes
     * @return Whether all IDs in {@code preRequirements} are in {@code donePasses}
     */
    protected Boolean checkPreRequirements(ArrayList<String> donePasses) {
        for(String pre : this.preRequirements) {
            if(!donePasses.contains(pre)) return false;
        }
        return true;
    }

    /**
     * Entry of the real pass function.
     *
     * @param example Sample circuit
     * @param target Target circuit
     * @param donePasses List of IDs of executed passes
     * @return Result of execution
     * @throws Exception If something goes wrong, any type of exception might be thrown out
     */
    public abstract Boolean execute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception;

    public String getId() {
        return id;
    }

    public ArrayList<Info> getOutput() {
        return output;
    }

    public ArrayList<String> getPreRequirements() {
        return preRequirements;
    }

    protected void addOutput(Info info) {
        this.output.add(info);
    }
}
