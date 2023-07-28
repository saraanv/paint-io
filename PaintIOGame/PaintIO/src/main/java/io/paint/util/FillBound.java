package io.paint.util;

import io.paint.data.model.XY;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class FillBound {

    private List<XY> points;
    private int[][] imageRGB;
    private int area;
    private int numberFound;

    public FillBound() {
    }

    public List<XY> fill(final BufferedImage image , final int area , final int rgb) {
        this.area = area;

        points = new ArrayList<>();
        final List<XY> colorsPoints = new ArrayList<>();

        final int width = image.getWidth();
        final int height = image.getHeight();

        imageRGB = new int[width][height];

        for (int x = 0; x < width; x += area) {
            for (int y = 0; y < height; y += area) {
                imageRGB[x][y] = image.getRGB(x , y);
            }
        }

        for (int x = 0; x < width; x += area) {
            for (int y = 0; y < height; y += area) {
                numberFound = 0;
                searchPlusX(width , x , y , rgb);
                searchPlusY(height , x , y , rgb);
                searchMineX(x , y , rgb);
                searchMineY(x , y , rgb);
                if (numberFound == 4) {
                    colorsPoints.addAll(new ArrayList<>(points));
                    coloring(image , rgb);
                }
                points.clear();
            }
        }

        imageRGB = null;
        System.gc();

        return colorsPoints;
    }

    private void coloring(final BufferedImage image , final int rgb) {
        for (final Point point : points) {
            image.setRGB(point.x , point.y , rgb);
        }
    }

    private void searchPlusX(final int width , final int startX , final int startY , final int rgb) {
        for (int x = startX; x < width; x += area) {
            points.add(new XY(x , startY));
            if (imageRGB[x][startY] == rgb) {
                numberFound++;
                break;
            }
        }
    }

    private void searchMineX(final int startX , final int startY , final int rgb) {
        for (int x = startX; x >= 0; x -= area) {
            points.add(new XY(x , startY));
            if (imageRGB[x][startY] == rgb) {
                numberFound++;
                break;
            }
        }
    }

    private void searchPlusY(final int height , final int startX , final int startY , final int rgb) {
        for (int y = startY; y < height; y += area) {
            points.add(new XY(startX , y));
            if (imageRGB[startX][y] == rgb) {
                numberFound++;
                break;
            }
        }
    }

    private void searchMineY(final int startX , final int startY , final int rgb) {
        for (int y = startY; y >= 0; y -= area) {
            points.add(new XY(startX , y));
            if (imageRGB[startX][y] == rgb) {
                numberFound++;
                break;
            }
        }
    }

}
