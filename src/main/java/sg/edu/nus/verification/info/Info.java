/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.info;

import java.util.ArrayList;

/**
 * An output generated during the verification.
 *
 * @author Lyc
 * @version 2023.02.20
 */
public class Info {
    ArrayList<String> involvedElementIds;
    String formatString = "This is an output info.";
    InfoType type;

    public Info(InfoType type, String ...elements) {
//        this.formatString = text;
        this.involvedElementIds = new ArrayList<String>();
        this.type = type;
        for(String id : elements) {
            this.involvedElementIds.add(id);
        }
    }

    /**
     * Construct an Info with text.
     *
     * @param format Text of the Info
     * @param type Type, see InfoType for possible options
     * @param elements What elements are involved
     */
    public Info(String format, InfoType type, String ...elements) {
        this(type, elements);
        this.formatString = format;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.type, this.formatString);
    }
}
