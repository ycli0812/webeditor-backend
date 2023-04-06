/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.verifier;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;
import sg.edu.nus.verification.pass.Pass;

import java.util.ArrayList;

/**
 * Manager of the whole verification process.
 *
 * @author Lyc
 * @version 2023.02.06
 */
public class Verifier {
    private Circuit example, target;
    private ArrayList<Info> output;
    private ArrayList<Pass> passList;
    private ArrayList<String> donePasses;

    public Verifier(String example, String target) {
        this();
        this.example.load(example);
        this.target.load(target);
    }

    public Verifier() {
        this.example = new Circuit();
        this.target = new Circuit();
        this.output = new ArrayList<Info>();
        this.passList = new ArrayList<Pass>();
        this.donePasses = new ArrayList<String>();
    }

    public int updateExample(String jsonStr) {
        this.example.load(jsonStr);
        return 0;
    }

    public int updateTarget(String jsonStr) {
        this.target.load(jsonStr);
        return 0;
    }

    public Boolean execute(Pass pass) {
        boolean res = false;
        this.output.add(new Info("Executing " + pass.getId() + ".", InfoType.INFO));
        try {
            res = pass.doExecute(this.example, this.target, this.donePasses);
            this.output.addAll(pass.getOutput());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            this.output.addAll(pass.getOutput());
            this.output.add(new Info("An exception occurred in pass " + pass.getId() + ".", InfoType.ERROR));
        } finally {
            if(res) {
                this.donePasses.add(pass.getId());
                this.output.add(new Info(pass.getId() + " succeeded.", InfoType.SUCCESS));
            } else {
                this.output.add(new Info("Pass " + pass.getId() + " failed.", InfoType.ERROR));
            }
        }
        return res;
    }

    public Boolean executeAllPasses() {
        for(Pass p : this.passList) {
            if(!this.execute(p)) {
                return false;
            }
        }
        this.output.add(new Info("Verification done.", InfoType.SUCCESS));
        return true;
    }

    public Boolean addPass(Pass p) {
        ArrayList<String> preRequires = p.getPreRequirements();
        // Check if all pre-requirements are satisfied
        for(String pre : preRequires) {
            boolean included = false;
            for(Pass pAdded : this.passList) {
                if (pre.equals(pAdded.getId())) {
                    included = true;
                    break;
                }
            }
            if(!included) {
                System.out.println("Can not add Pass " + p.getId() + ", because some pre-requirements are not satisfied.");
                return false;
            }
        }
        // Check if pass has been added
        for(Pass pAdded : this.passList) {
            if(pAdded.getId().equals(p.getId())) {
                System.out.println("Can not add Pass " + p.getId() + ", this pass has already been added.");
                return false;
            }
        }
        this.passList.add(p);
        return true;
    }

    public void summaryInfo() {
        for(Info info : this.output) {
            System.out.println(info.toString());
        }
    }

    public ArrayList<Info> getOutput() {
        return output;
    }
}
