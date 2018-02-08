package com.csci6461.team13.simulator.ui.basic;

import javafx.beans.property.SimpleBooleanProperty;

public class Signals {

    public SimpleBooleanProperty mode = new SimpleBooleanProperty(true);
    public SimpleBooleanProperty on = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty loaded = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty started = new SimpleBooleanProperty(false);

}
