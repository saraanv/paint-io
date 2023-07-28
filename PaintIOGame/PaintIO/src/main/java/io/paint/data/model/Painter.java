package io.paint.data.model;


import io.paint.data.enums.PainterType;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Painter {
    private final PainterType type;
    private final Color homeColor;
    private final Color pathColor;
    private final Color faceColor;
    private final Set<String> home;
    private final Set<XY> paths;
    private XY location;
    private XY face;

    public Painter(final PainterType type , final Color homeColor , final Color pathColor , final Color faceColor) {
        this.type = type;
        this.homeColor = homeColor;
        this.pathColor = pathColor;
        this.faceColor = faceColor;
        home = new HashSet<>();
        paths = new HashSet<>();
    }

    public XY getLocation() {
        return location;
    }

    public void setLocation(final XY location) {
        this.location = location;
    }

    public Color getHomeColor() {
        return homeColor;
    }


    public Color getPathColor() {
        return pathColor;
    }

    public Color getFaceColor() {
        return faceColor;
    }

    public XY getFace() {
        return face;
    }

    public void setFace(final XY face) {
        this.face = new XY(face.getLocation());
    }

    public Set<String> getHome() {
        return home;
    }

    public Set<XY> getPaths() {
        return paths;
    }

    public PainterType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name();
    }
}
