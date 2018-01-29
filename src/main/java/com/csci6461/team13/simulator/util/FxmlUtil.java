package com.csci6461.team13.simulator.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class FxmlUtil {

    public static final String UI_RESOURCE_PATH_PREFIX =
            "/";

    /**
     * load node from fxml file
     * the provided path is only the file name
     */
    public static final Parent loadAsNode(String path) throws IOException {
        URL url = loadAsUrl(UI_RESOURCE_PATH_PREFIX + path);
        Parent node = FXMLLoader.load(url);
        return node;
    }

    /**
     * load resource from the path as URL
     * the provided path is full path
     */
    public static final URL loadAsUrl(String path) {
        URL url = FxmlUtil.class.getResource(path);
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
}
