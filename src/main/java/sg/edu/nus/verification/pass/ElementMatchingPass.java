package sg.edu.nus.verification.pass;

import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.element.Element;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.*;

/**
 * Pass checking if two circuits have same amount of elements with same features.
 *
 * @author Lyc
 * @version 2023.04.01
 */
public class ElementMatchingPass extends Pass {
    private Map<String, Set<Element>> counterSample;
    private Map<String, Set<Element>> counterTarget;

    public ElementMatchingPass() {
        super();
        this.id = "ElementMatchingPass";
        this.preRequirements.add("ConnectivityAnalysisPass");
        counterSample = new HashMap<>();
        counterTarget = new HashMap<>();
    }

    @Override
    public Boolean execute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception {
        return this.matchElementAmount(example, target) && this.matchElementFeatures(example, target);
    }

    /**
     * Return true if there are same amount of element types in two circuits and there are same amount of elements of
     * each type, else return false.
     *
     * @param sample Sample circuit
     * @param target Target circuit
     * @return Check result
     */
    private Boolean matchElementAmount(Circuit sample, Circuit target) {
        // group elements by type in sample and target circuit
        for(Element e : sample.getElements()) {
            String type = e.getType();
            Set<Element> temp = new HashSet<>();
            temp.add(e);
            this.counterSample.merge(type, temp, (s1, s2) -> {
                s1.addAll(s2);
                return s1;
            });
        }
        for(Element e : target.getElements()) {
            String type = e.getType();
            Set<Element> temp = new HashSet<>();
            temp.add(e);
            this.counterTarget.merge(type, temp, (s1, s2) -> {
                s1.addAll(s2);
                return s1;
            });
        }

        // check each type has same amount in both circuits
        for(Map.Entry<String, Set<Element>> entry : counterSample.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue().size();
            if (counterTarget.containsKey(type) && counterTarget.get(type).size() == count) {
                System.out.println(type + ": " + count);
                continue;
            } else {
                System.out.println(type + " not match in amount.");
                this.addOutput(new Info("Element amount not match.", InfoType.ERROR));
                return false;
            }
        }

        // check if there are same amount of types
        return counterTarget.size() == counterSample.size();
    }

    /**
     * Return true if every element in both circuits has a same copy(same type and same features) in another circuit,
     * else return false.
     *
     * @param sample Sample circuit
     * @param target Target circuit
     * @return Check result
     */
    private boolean matchElementFeatures(Circuit sample, Circuit target) {
        Map<Integer, String> sampleElementHashToId = new HashMap<>(); // hash code map to given id
        Map<Integer, Integer> sampleElementCounter = new HashMap<>(); // hash code map to amount of certain element

        // generate id and give id to elements in sample circuit, as well as count each unique element does the sample circuit has
        int idCount = 0;
        for(Element e : sample.getElements()) {
            int hash = e.hashCode();
            sampleElementCounter.merge(hash, 1, Integer::sum);
            if(!sampleElementHashToId.containsKey(hash)) {
                // elements have same hash code will be given same id
                sampleElementHashToId.put(hash, "e" + idCount);
                e.setId("e" + idCount);
                idCount++;
            } else {
                e.setId(sampleElementHashToId.get(hash));
            }
        }

        // compare target circuit
        for(Element e : target.getElements()) {
            int hash = e.hashCode();
            if(sampleElementCounter.containsKey(hash) && sampleElementCounter.get(hash) > 0) {
                // match a same element in sample circuit
                sampleElementCounter.merge(hash, -1, Integer::sum);
                // reset id
                e.setId(sampleElementHashToId.get(hash));
            } else {
                this.addOutput(new Info("Element features not match.", InfoType.ERROR));
                return false;
            }
        }

        // check if there are elements not matched in sample circuit
        for(Map.Entry<Integer, Integer> entry : sampleElementCounter.entrySet()) {
            if(entry.getValue() > 0) {
                this.addOutput(new Info("Element features not match.", InfoType.ERROR));
                return false;
            }
        }

        return true;
    }
}
