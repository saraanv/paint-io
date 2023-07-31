package io.paint.controller;

import io.paint.data.model.Cell;
import io.paint.data.model.Painter;
import io.paint.data.model.XY;
import io.paint.data.enums.MoveTo;
import io.paint.data.enums.PainterType;
import io.paint.view.AppearanceReady;
import io.paint.view.GamePagePanel;
import io.paint.view.MainView;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public final class MainController extends MainView implements AppearanceReady {

    public static io.paint.data.model.Painter humanPainter, systemPainter;
    public static int speed = 1;
    public static boolean running;
    public static MainController mainController;

    public MainController() {
        super();
        create(this);
    }

    @Override
    protected void onClickBtnStartGame() {
        if (!running) {
            running = true;
            SwingUtilities.invokeLater(() -> btnStart.setText("Pause"));
            ready();
        } else {
            gamePage.clear();
            running = false;
            SwingUtilities.invokeLater(() -> btnStart.setText("Let's Go"));
        }
    }

    @Override
    protected void onChangeSSpeed(final int value) {
        speed = value;
    }

    @Override
    protected void onMovie(final MoveTo moveTo) {
        if (!running) {
            return;
        }
        for (int i = 0; i < speed; i++) {
            doMove(humanPainter , moveTo);
        }
    }

    public void doMove(final io.paint.data.model.Painter painter , final MoveTo moveTo) {

        if (!running) {
            return;
        }

        switch (moveTo) {
            case LEFT -> painter.getLocation().setX((painter.getLocation().x - GamePagePanel.CELL_WIDTH));
            case RIGHT -> painter.getLocation().setX((painter.getLocation().x + GamePagePanel.CELL_WIDTH));
            case TOP -> painter.getLocation().setY((painter.getLocation().y - GamePagePanel.CELL_HEIGHT));
            case BOTTOM -> painter.getLocation().setY((painter.getLocation().y + GamePagePanel.CELL_HEIGHT));
        }
        painter.setFace(painter.getLocation());

        if (painter.getType().equals(PainterType.HUMAN)) {
            final int plusWidthCell = (GamePagePanel.CELL_WIDTH * 4);
            if (painter.getLocation().x - plusWidthCell <= gamePage.getStartX()) {
                gamePage.setStartX(gamePage.getStartX() - GamePagePanel.CELL_WIDTH * 4);
            } else if (painter.getLocation().x + plusWidthCell >= gamePage.getMaxX()) {
                gamePage.setStartX(gamePage.getStartX() + GamePagePanel.CELL_WIDTH * 4);
            }

            final int plusHeightCell = (GamePagePanel.CELL_HEIGHT * 4);
            if (painter.getLocation().y - plusHeightCell <= gamePage.getStartY()) {
                gamePage.setStartY(gamePage.getStartY() - GamePagePanel.CELL_HEIGHT * 4);
            } else if (painter.getLocation().y + plusHeightCell >= gamePage.getMaxY()) {
                gamePage.setStartY(gamePage.getStartY() + GamePagePanel.CELL_HEIGHT * 4);
            }
        }

        final int sx = painter.getLocation().x;
        final int sy = painter.getLocation().y;
        final Cell cell = gamePage.getCell(sx , sy);
        cell.setLocation(new XY(sx , sy));
        cell.setPainter(painter);

        final io.paint.data.model.Painter testPainter = painter.getType().equals(PainterType.HUMAN) ? systemPainter : humanPainter;

        if (testPainter != null) {
            final boolean collisionOnTheWay = testPainter.getPaths().stream().anyMatch(xy -> xy.equals(painter.getFace()));
            if (collisionOnTheWay) {
                gameOver(painter , testPainter);
                return;
            }

            final boolean faceToFace = testPainter.getFace().equals(painter.getFace());
            if (faceToFace) {
                gameOver(null , painter , testPainter);
                return;
            }
        }

        gamePage.getCells().put(gamePage.getXYKey(sx , sy) , cell);
        painter.getPaths().add(new XY(sx , sy));

        gamePage.repaint();
    }

    @Override
    public void ready() {
        if (!running) {
            return;
        }
        mainController = this;
        gamePage.clear();

        humanPainter = new io.paint.data.model.Painter(PainterType.HUMAN , new Color(12 , 68 , 51) , new Color(41 , 231 , 178) , new Color(10 , 86 , 108));
        humanPainter.setLocation(new XY(30 , 0));
        humanPainter.setFace(humanPainter.getLocation());
        onMovie(MoveTo.LEFT);
        onMovie(MoveTo.LEFT);
        humanPainter.getHome().add(gamePage.getXYKey(humanPainter.getFace().x , humanPainter.getFace().y));
        SystemPainterController.start();
    }

    private void gameOver(final io.paint.data.model.Painter winner , final Painter... loser) {
        onClickBtnStartGame();
        final String message = String.format("Winner Is : %s :: The Loser Is: %s" , (winner == null ? "" : winner) , Arrays.toString(loser));
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null , message , "Game Is Over" , JOptionPane.INFORMATION_MESSAGE));
    }

}
