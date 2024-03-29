package com.csci6461.team13.simulator.ui.helpers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author zhiyuan
 */
public class MemControlHelper {

    // signals for MemControlPanel

    // new value text signal, indicates if there is a new text value
    public BooleanProperty nval = new SimpleBooleanProperty(false);
    // memory address signal, indicates if the memory address has been updated
    public BooleanProperty memaddr = new SimpleBooleanProperty(false);
    // store signal, indicates if the new value has been stored into the
    // memory address
    public BooleanProperty stored = new SimpleBooleanProperty(false);


}
