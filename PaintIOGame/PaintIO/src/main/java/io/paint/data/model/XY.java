package io.paint.data.model;

import java.awt.*;

public class XY extends Point {

    public XY() {
    }

    public XY(final Point p) {
        super(p);
    }

    public XY(final int x , final int y) {
        super(x , y);
    }

    public boolean equals(final int x , final int y) {
        return this.x == x && this.y == y;
    }

    public void setX(final int x) {
        super.x = x;
    }

    public void setY(final int y) {
        super.y = y;
    }

}
