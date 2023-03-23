/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

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
    private HashMap<Integer, ArrayList<Pin>> groupMap;

    /**
     * Initialize features and load circuit.
     *
     * @param circuit Circuit to be operated
     * @deprecated To make single Analyser able to operate multiple circuits, feature circuit is no longer specified in
     *     the constructor. Instead, a circuit will be passed every time when {@code Analyse} is called.
     */
    public Analyser(Circuit circuit) {
        this.circuit = circuit;
        this.breadboards = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.wires = new ArrayList<>();
        this.groupMap = new HashMap<Integer, ArrayList<Pin>>();
        this.classifyElements();
    }

    /**
     * Initialize features into empty lists or maps.
     */
    public Analyser() {
        this.breadboards = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.wires = new ArrayList<>();
        this.groupMap = new HashMap<Integer, ArrayList<Pin>>();
    }

    /**
     * Entry of connectivity analysis function.
     *
     * @param c Circuit to be operated
     */
    public void analyse(Circuit c) {
        this.circuit = c;
        this.classifyElements();
        this.groupPins();
        this.analysisWires();
        this.connectPins();
    }

    @TestOnly
    public void printResult() {
        for (Map.Entry<Integer, ArrayList<Pin>> entry : this.groupMap.entrySet()) {
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
    private void groupPins() {
        for(Element e : this.elements) {
            for(Pin p : e.getPins()) {
                int hash = this.getHashCode(p.getOriginX(), p.getOriginY());
                if(hash == -1) continue;
                if(this.groupMap.get(hash) == null) {
                    ArrayList<Pin> newList = new ArrayList<Pin>();
                    newList.add(p);
                    this.groupMap.put(hash, newList);
                } else {
                    this.groupMap.get(hash).add(p);
                }
            }
        }
    }

    /**
     * Find out which groups are connected by wires and merge these groups.
     */
    private void analysisWires() {
        for(Wire w : this.wires) {
            int x1 = w.getX1();
            int x2 = w.getX2();
            int y1 = w.getY1();
            int y2 = w.getY2();
            // get groups of both ends of the wire
            int hashCode1 = this.getHashCode(x1, y1);
            ArrayList<Pin> g1 = this.groupMap.get(hashCode1);
            int hashCode2 = this.getHashCode(x2, y2);
            ArrayList<Pin> g2 = this.groupMap.get(hashCode2);
            // check if the wire is between two groups with elements on them
            if(g1 != null && g2 != null) {
                if(g1 == g2) continue; // repeated wire, the two groups have been merged
                // merge two groups and let both hashCode point to the merged group
                g1.addAll(g2);
                this.groupMap.put(hashCode2, g1);
            }
        }
    }

    /**
     * Call connect method of Pin and connect pins in every group.
     */
    private void connectPins() {
        // record groups that has been processed because some values in the map could be the same pointers
        Set<ArrayList<Pin>> processedGroups = new HashSet<>();
        for (Map.Entry<Integer, ArrayList<Pin>> entry : this.groupMap.entrySet()) {
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
    private int getHashCode(int x, int y) {
        for(Breadboard bd : this.breadboards) {
            if (bd.isOnBreadboard(x, y)) {
                String id = bd.getOriginId();
                String area = bd.area(x, y);
                int hash = 0;
                if (area == "upside" || area == "downSide") {
                    hash = Objects.hash(id, area, y);
                } else {
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