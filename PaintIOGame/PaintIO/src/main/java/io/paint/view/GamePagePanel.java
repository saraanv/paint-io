package io.paint.view;

import io.paint.data.model.Cell;
import io.paint.data.model.Painter;
import io.paint.data.model.XY;
import io.paint.util.FillBound;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class GamePagePanel extends JPanel {
    private final Map<String, Cell> cells = new HashMap<>();
    private int startX = 0, startY = 0;
    private BufferedImage image;

    public GamePagePanel() {
    }

    public static final int CELL_WIDTH = 15, CELL_HEIGHT = 15;

    private static final Color COLOR1 = new Color(185 , 185 , 185), COLOR2 = new Color(246 , 246 , 246);

    private FillBound fillBound = new FillBound();

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        image = new BufferedImage(getWidth() , getHeight() , BufferedImage.TYPE_INT_RGB);
        doPaint(g , image.createGraphics());
    }

    private void doPaint(final Graphics g , final Graphics imageG) {
        final int width = getWidth();
        final int height = getHeight();

        Color pageColor = COLOR1;
        Color firstCellColor = pageColor;

        final Set<io.paint.data.model.Painter> painters = new HashSet<>();

        for (int sx = startX, sy = startY, x = 0, y = 0; ; ) {

            final Cell cell = cells.get(getXYKey(sx , sy));
            final io.paint.data.model.Painter painter = cell != null && cell.getPainter() != null ? cell.getPainter() : null;

            pageColor = pageColor.equals(COLOR1) ? COLOR2 : COLOR1;

            final Color setColor;
            if (painter != null && painter.getFace().equals(sx , sy)) {
                setColor = painter.getFaceColor();
            } else {
                if (cell != null && painter != null) {
                    if (painter.getHome().contains(getXYKey(sx , sy))) {
                        setColor = painter.getHomeColor();
                    } else {
                        setColor = painter.getPathColor();
                    }
                } else {
                    setColor = pageColor;
                }
            }

            g.setColor(setColor);
            g.fillRect(x , y , CELL_WIDTH , CELL_HEIGHT);
            imageG.setColor(setColor);
            imageG.fillRect(x , y , CELL_WIDTH , CELL_HEIGHT);

            x += CELL_WIDTH;
            sx += CELL_WIDTH;

            if (x >= width) {
                y += CELL_HEIGHT;
                sy += CELL_HEIGHT;
                firstCellColor = firstCellColor.equals(COLOR1) ? COLOR2 : COLOR1;
                pageColor = firstCellColor;
            }

            if (x > width && y > height) {
                break;
            }

            if (x > width) {
                x = 0;
                sx = startX;
            }

            if (painter != null) {
                painters.add(painter);
            }
        }

        if (painters.size() > 0) {
            for (final io.paint.data.model.Painter painter : painters) {
                if (checkBackToHome(painter)) {
                    for (final XY path : painter.getPaths()) {
                        painter.getHome().add(getXYKey(path.x , path.y));
                    }
                    painter.getPaths().clear();
                }
                fillBound(painter);
            }
        }

    }

    private void fillBound(final io.paint.data.model.Painter painter) {

        final List<XY> fill = fillBound.fill(image , CELL_WIDTH , painter.getHomeColor().getRGB());

        for (int sx = startX, sy = startY, x = 0, y = 0; ; ) {

            final int finalX = x;
            final int finalY = y;
            if (fill.stream().anyMatch(xy -> xy.equals(finalX , finalY))) {
                putCellPainter(sx , sy , painter);
                painter.getHome().add(getXYKey(sx , sy));
            }

            x += CELL_WIDTH;
            sx += CELL_WIDTH;

            if (x >= getWidth()) {
                y += CELL_HEIGHT;
                sy += CELL_HEIGHT;
            }

            if (x > getWidth() && y > getHeight()) {
                break;
            }

            if (x > getWidth()) {
                x = 0;
                sx = startX;
            }
        }
        fill.clear();

    }

    public void putCellPainter(final int x , final int y , final io.paint.data.model.Painter painter) {
        final Cell cell = getCell(x , y);
        cell.setPainter(painter);
        cells.put(getXYKey(x , y) , cell);
    }

    public Cell getCell(final int x , final int y) {
        if (cells.containsKey(getXYKey(x , y))) {
            return cells.get(getXYKey(x , y));
        } else {
            final Cell cell = new Cell();
            cells.put(getXYKey(x , y) , cell);
            return cell;
        }
    }

    private boolean checkBackToHome(final Painter painter) {
        return painter.getHome().contains(getXYKey(painter.getFace().x , painter.getFace().y));
    }

    public String getXYKey(final int x , final int y) {
        return String.format("%s;%s" , x , y);
    }

    public XY getXYKey(final String key) {
        final String[] split = key.split(";");
        if (split.length == 2) {
            return new XY(Integer.parseInt(split[0]) , Integer.parseInt(split[1]));
        }
        return null;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getMaxX() {
        return (getWidth() - 1) - (-startX);
    }

    public int getMaxY() {
        return (getHeight() - 1) - (-startY);
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public Map<String, Cell> getCells() {
        return cells;
    }

    public void clear() {
        startX = 0;
        startY = 0;
        fillBound = new FillBound();
        cells.clear();
        image = null;
    }

}
