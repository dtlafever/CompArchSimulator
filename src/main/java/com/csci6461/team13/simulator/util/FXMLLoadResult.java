package com.csci6461.team13.simulator.util;

import javafx.scene.Parent;

public class FXMLLoadResult<T> {

    private Parent node;
    private T controller;

    public Parent getNode() {
        return node;
    }

    public void setNode(Parent node) {
        this.node = node;
    }

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }



}
