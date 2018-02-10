package com.csci6461.team13.simulator.ui.helpers;

import javafx.beans.property.SimpleBooleanProperty;

public class MemControlHelper {

    // signals for MemControlPanel

    // new value text signal, indicates if there is a new text value
    public SimpleBooleanProperty nval = new SimpleBooleanProperty(false);
    // memory address signal, indicates if the memory address has been updated
    public SimpleBooleanProperty memaddr = new SimpleBooleanProperty(false);
    // store signal, indicates if the new value has been stored into the
    // memory address
    public SimpleBooleanProperty stored = new SimpleBooleanProperty(false);

}
