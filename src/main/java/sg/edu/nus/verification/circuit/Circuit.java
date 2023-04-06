/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.circuit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sg.edu.nus.verification.element.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A breadboard circuit.
 *
 * @author Lyc
 * @version 2023.02.06
 */
public class Circuit {
    private final ArrayList<Element> elementList;

    public Circuit() {
        this.elementList = new ArrayList<Element>();
    }

    public Circuit(String jsonStr) {
        elementList = new ArrayList<Element>();
        this.load(jsonStr);
    }

    /**
     * Parse a JSON string and push all the elements into {@code elementList}.
     *
     * @param jsonStr JSON string to be parsed
     */
    public void load(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        // Map elementSet field to JsonNode instance
        JsonNode elements;
        try {
            elements = mapper.readValue(jsonStr, JsonNode.class).get("elementSet");
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }
        Iterator<String> it = elements.fieldNames();
        while(it.hasNext()) {
            // Get id, type, and features
            String id = it.next();
            JsonNode element = elements.get(id);
            String type = element.get("type").asText();
            int originX = element.get("x").asInt();
            int originY = element.get("y").asInt();

            // get pins
            JsonNode pinsNode = element.get("pins");
            ArrayList<Pin> pins = new ArrayList<Pin>();
            for(JsonNode pinNode : pinsNode) {
                pins.add(new Pin(pinNode.get("x").asInt(), pinNode.get("y").asInt(),pinNode.get("name").asText(), id));
            }

            // get features
            JsonNode featuresNode = element.get("features");
            ArrayList<Parameter> features = new ArrayList<>();
            for(JsonNode featNode : featuresNode) {
                String name = featNode.get("name").asText();
                String value = featNode.get("value").asText();
                String unit = featNode.get("unit") == null ? "" : featNode.get("unit").asToken().asString();
                features.add(new Parameter(name, value, unit));
            }

            // Add element instance to elementList
            // Do not instantiate Element class since it is an abstract class
            switch (type) {
                case "resistor": {
                    this.elementList.add(new Resistor("unset", id, originX, originY, features, pins));
                    break;
                }
                case "breadboard": {
                    this.elementList.add(new Breadboard("unset", id, originX, originY, features, pins));
                    break;
                }
                case "wire": {
                    this.elementList.add(new Wire("unset", id, originX, originY, features, pins));
                    break;
                }
                default: break;
            }
        }
    }

    public ArrayList<Element> getElements() {
        return elementList;
    }

    /**
     * Get a certain element by ID.
     *
     * @param id ID of the required element
     * @return The element found in the circuit, return null if there is not such an element
     */
    public List<Element> getElementsById(String id) {
        ArrayList<Element> res = new ArrayList<>();
        for(Element e : this.elementList) {
            if(e.getId() == id) {
                res.add(e);
            }
        }
        return res;
    }

    /**
     * Remove an element from the circuit.
     * @param e Element instance to be removed.
     * @return If the element is successfully removed.
     */
    public boolean removeElement(Element e) {
        return this.elementList.remove(e);
    }
}