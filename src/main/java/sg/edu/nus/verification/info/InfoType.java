/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sg.edu.nus.verification.info;

public enum InfoType {
    /**
     * Ordinary output information. Mainly used to hint the progress of the verification.
     */
    INFO,

    /**
     * Used to report issues that will not terminate the verification.
     */
    WARNING,

    /**
     * Used to report serious problems that terminate the verification.
     */
    ERROR,
    /**
     * Used to report success information.
     */
    SUCCESS
}
