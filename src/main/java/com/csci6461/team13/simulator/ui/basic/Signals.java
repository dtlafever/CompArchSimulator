package com.csci6461.team13.simulator.ui.basic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author zhiyuan
 *
 * */
public class Signals {

    public BooleanProperty mode = new SimpleBooleanProperty(true);
    public BooleanProperty on = new SimpleBooleanProperty(false);
    public BooleanProperty loaded = new SimpleBooleanProperty(false);
    public BooleanProperty started = new SimpleBooleanProperty(false);

}
