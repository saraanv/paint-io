package io.paint.data.model;

import io.paint.controller.MainController;
import io.paint.data.enums.PainterType;

import java.awt.*;

public class Cell {
    private Point location;
    private PainterType painter;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Painter getPainter() {
        if (painter.equals(PainterType.HUMAN)) {
            return MainController.humanPainter;
        } else {
            return MainController.systemPainter;
        }
    }

    public void setPainter(final Painter painter) {
        this.painter = painter.getType();
    }

    @Override
    public String toString() {
        return "Cell{" + "location=" + location + ", painter=" + painter + '}';
    }
}
