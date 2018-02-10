package com.csci6461.team13.simulator.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FXMLUtil {

    public static final String UI_RESOURCE_PATH_PREFIX = "/";
    public static ArrayList controllers = new ArrayList();

    /**
     * load node from fxml file
     * the provided path is only the file name
     */
    public static <T> FXMLLoadResult loadAsNode(String path) throws
            IOException {
        URL url = loadAsUrl(UI_RESOURCE_PATH_PREFIX + path);
        FXMLLoader loader = new FXMLLoader(url);
        Parent node = loader.load();
        FXMLLoadResult<T> result = new FXMLLoadResult<>();
        result.setNode(node);
        result.setController(loader.getController());
        return result;
    }

    /**
     * load resource from the path as URL
     * the provided path is full path
     */
    public static final URL loadAsUrl(String path) {
        URL url = FXMLUtil.class.getResource(path);
        return url;
    }

    /**
     * load stylesheets from the path
     * the provided path is only the file name
     */
    public static final void addStylesheets(Parent node, String path) {
        URL url = loadAsUrl(UI_RESOURCE_PATH_PREFIX + path);
        node.getStylesheets().add(url.toExternalForm());
    }

    public static final ArrayList getControllers() {
        return controllers;
    }
}
