package sg.edu.nus.verification.pass;

import org.jetbrains.annotations.TestOnly;
import sg.edu.nus.verification.circuit.Circuit;
import sg.edu.nus.verification.element.Breadboard;
import sg.edu.nus.verification.element.Element;
import sg.edu.nus.verification.element.Pin;
import sg.edu.nus.verification.element.Wire;
import sg.edu.nus.verification.info.Info;
import sg.edu.nus.verification.info.InfoType;

import java.util.*;

/**
 * Link pins that are semantically connected in the circuit.
 *
 * <p></p>This pass pushes all the connected Pins into connections list for every Pin in the circuit. In other word,
 * after this pass is executed, an undirected graph whose nodes are Pins and edges are connections will be generated.
 *
 * <p></p>Note that wires will be eliminated in this pass. Extra connections beyond the breadboards introduced by wires
 * will be contained in the graph generated.
 *
 * @author Lyc
 * @version 2023.03.17
 */
public class ConnectivityAnalysisPass extends Pass {
    public ConnectivityAnalysisPass() {
        super();
        this.id = "ConnectivityAnalysisPass";
        this.preRequirements.add("UselessElementsPass");
        this.preRequirements.add("ImpossibleConnectionPass");
    }

    @Override
    public Boolean execute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception {
        if(!this.checkPreRequirements(donePasses)) {
            this.addOutput(new Info("Pre-requirements not satisfied.", InfoType.ERROR));
            return false;
        }

        Analyser analyser = new Analyser();
        // process target circuit
        analyser.analyse(target);
        analyser.printResult();
        // TODO process sample circuit
        return true;
    }
}

/**
 * Real executor of the pass function.
 *
 * <p>There are 4 steps in executing the pass:</p>
 * <ol>
 *     <li>Classify elements and put them into respective lists.</li>
 *     <li>Go through all pins and group them by breadboard connections.</li>
 *     <li>Merge groups connected by wires.</li>
 *     <li>Link all pins in every group.</li>
 * </ol>
 *
 * @author Lyc
 * @version 2023.03.17
 */
class Analyser {
    private Circuit circuit;
    private ArrayList<Breadboard> breadboards;
    private ArrayList<Element> elements;
    private ArrayList<Wire> wires;

    /**
     * Map from group hash to Pin list in the group.
     */
    private HashMap<Integer, ArrayList<Pin>> elementGroupMap;

    /**
     * Map from group hash to list of all wire-connected group hashes.
     */
    private HashMap<Integer, Set<Integer>> wireGroupMap;

    /**
     * Initialize features and load circuit.
     *
     * @param circuit Circuit to be operated
     * @deprecated To make single Analyser able to operate multiple circuits, feature circuit is no longer specified in
     *     the constructor. Instead, a circuit will be passed every time when {@code Analyse} is called.
     */
    public Analyser(Circuit circuit) {
        this();
        this.circuit = circuit;
        this.classifyElements();
    }

    /**
     * Initialize features into empty lists or maps.
     */
    public Analyser() {
        this.breadboards = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.wires = new ArrayList<>();
        this.elementGroupMap = new HashMap<>();
        this.wireGroupMap = new HashMap<>();
    }

    /**
     * Entry of connectivity analysis function.
     *
     * @param c Circuit to be operated
     */
    public void analyse(Circuit c) {
        this.circuit = c;
        this.classifyElements();
        this.groupElementPins();
        this.groupWireEnds();
        this.mergePinGroups();
        this.connectPins();
    }

    @TestOnly
    public void printResult() {
        for (Map.Entry<Integer, ArrayList<Pin>> entry : this.elementGroupMap.entrySet()) {
            ArrayList<Pin> group = entry.getValue();
            int key = entry.getKey();
            System.out.print(key + ": ");
            for(Pin p : group) {
                System.out.print("("+p.getElementId()+")"+p.getId()+" ");
            }
            System.out.println();
        }

        for(Element e : this.elements) {
            System.out.println("[" + e.getOriginId() + "]");
            for(Pin p : e.getPins()) {
                System.out.print(p.getId() + ": ");
                for(Pin pc : p.getConnections()) {
                    System.out.print("(" + pc.getElementId() + ")" + pc.getId() + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Group pins connected by the breadboards.
     */
    private void groupElementPins() {
        for(Element e : this.elements) {
            for(Pin p : e.getPins()) {
                int hash = this.getGroupHashCode(p.getOriginX(), p.getOriginY());
                if(hash == -1) {
                    System.out.println("Error: not on breadboard.");
                    continue;
                }
                if(this.elementGroupMap.get(hash) == null) {
                    ArrayList<Pin> newList = new ArrayList<Pin>();
                    newList.add(p);
                    this.elementGroupMap.put(hash, newList);
                } else {
                    this.elementGroupMap.get(hash).add(p);
                }
            }
        }
    }

    /**
     * Group ends of wires that are connected by the breadboard.
     */
    private void groupWireEnds() {
        for(Wire w : this.wires) {
            int x1 = w.getX1();
            int x2 = w.getX2();
            int y1 = w.getY1();
            int y2 = w.getY2();
            int hashCode1 = this.getGroupHashCode(x1, y1);
            int hashCode2 = this.getGroupHashCode(x2, y2);

            // redundant wire, ignore that
            if(hashCode1 == hashCode2) continue;

            if(this.wireGroupMap.get(hashCode1) == null && this.wireGroupMap.get(hashCode2) == null) {
                // the wire connects two strange groups, create a new set and let the two hash code map to it
                Set<Integer> set = new HashSet<>();
                set.add(hashCode1);
                set.add(hashCode2);
                this.wireGroupMap.put(hashCode1, set);
                this.wireGroupMap.put(hashCode2, set);
            } else {
                // at least one group has been contained in the map
                Set<Integer> end1Set = this.wireGroupMap.get(hashCode1);
                Set<Integer> end2Set = this.wireGroupMap.get(hashCode2);

                // add new group and make new reflection in the map
                if(end1Set != null && end2Set == null) {
                    end1Set.add(hashCode2);
                    this.wireGroupMap.put(hashCode2, end1Set);
                }
                if(end2Set != null && end1Set == null) {
                    end2Set.add(hashCode1);
                    this.wireGroupMap.put(hashCode1, end2Set);
                }

                // merge sets if the wire connects two sets
                if(end1Set != null && end2Set != null && end1Set != end2Set) {
                    Set<Integer> mergedSet = end1Set;
                    for(int toMerge : end2Set) {
                        this.wireGroupMap.put(toMerge, mergedSet);
                    }
                    mergedSet.addAll(end2Set);
                }
            }
        }
    }

    /**
     * Find out which groups are connected by wires and merge these groups.
     */
    private void mergePinGroups() {
        // for each breadboard-connected pin group
        for(Map.Entry<Integer, ArrayList<Pin>> entry : this.elementGroupMap.entrySet()) {
            int hashCode = entry.getKey();
            ArrayList<Pin> curPinTGroup = entry.getValue();
            if(this.wireGroupMap.get(hashCode) != null) { // check if the pin group is connected by wires
                // merge wire-connected groups
                for(int connectedPinGroupHash : this.wireGroupMap.get(hashCode)) {
                    ArrayList<Pin> connectedPinGroup = this.elementGroupMap.get(connectedPinGroupHash);
                    if(connectedPinGroup != null && connectedPinGroup != curPinTGroup) {
                        curPinTGroup.addAll(connectedPinGroup);
                        this.elementGroupMap.put(connectedPinGroupHash, curPinTGroup);
                    }
                }
            }
        }
    }

    /**
     * Call connect method of Pin and connect pins in every group.
     */
    private void connectPins() {
        // record groups that has been processed because some values in the map could be the same pointers
        Set<ArrayList<Pin>> processedGroups = new HashSet<>();
        for (Map.Entry<Integer, ArrayList<Pin>> entry : this.elementGroupMap.entrySet()) {
            ArrayList<Pin> group = entry.getValue();
            if(processedGroups.contains(group)) {
                continue;
            }
            else {
                processedGroups.add(group);
            }
            int len = group.size();
            for (int i = 0; i < len; ++i) {
                for (int j = i + 1; j < len; ++j) {
                    // connect each other
                    group.get(i).connect(group.get(j));
                    group.get(j).connect(group.get(i));
                }
            }
        }
    }

    /**
     * Given a coordinate, return a unique integer respect to the connection group the coordinate is in.
     * @param x x-axis coordinate
     * @param y y-axis coordinate
     * @return Hash code generated
     */
    private int getGroupHashCode(int x, int y) {
        for(Breadboard bd : this.breadboards) {
            if (bd.isOnBreadboard(x, y)) {
                String id = bd.getOriginId();
                String area = bd.area(x, y);
                int hash = 0;
                if (area == "upside" || area == "downSide") {
                    hash = Objects.hash(id, area, y);
                }
                if (area == "upBoard" || area == "downBoard") {
                    hash = Objects.hash(id, area, x);
                }
                return hash;
            }
        }
        return -1;
    }

    /**
     * Put breadboards and wires into respective ArrayLists and the rest into another one.
     */
    private void classifyElements() {
        for(Element e : circuit.getElementList()) {
            if(Objects.equals(e.getType(), "breadboard")) {
                this.breadboards.add((Breadboard) e);
                continue;
            }
            if(Objects.equals(e.getType(), "wire")) {
                this.wires.add((Wire)e);
                continue;
            }
            this.elements.add(e);
        }
    }
}