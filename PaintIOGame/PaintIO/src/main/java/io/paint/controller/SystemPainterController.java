package io.paint.controller;

import io.paint.data.model.XY;
import io.paint.data.enums.MoveTo;
import io.paint.data.enums.PainterType;
import io.paint.data.model.Painter;
import io.paint.view.GamePagePanel;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static io.paint.controller.MainController.mainController;
import static io.paint.controller.MainController.systemPainter;

public final class SystemPainterController extends TimerTask {

    private static SystemPainterController systemPainterController;
    private static final int startTimer = 500;
    private static final int periodTimer = 500;
    private static boolean running;

    private final Random random;
    private final Timer timer;
    private MoveTo moveTo = MoveTo.LEFT;

    private SystemPainterController() {
        running = true;
        systemPainter = new Painter(
                PainterType.SYSTEM ,
                new Color(68 , 12 , 12) ,
                new Color(255 , 143 , 143) ,
                new Color(255 , 217 , 0)
        );
        systemPainter.setLocation(new XY((GamePagePanel.CELL_WIDTH * 20) + (GamePagePanel.CELL_WIDTH * 20) , (GamePagePanel.CELL_WIDTH * 10) + (GamePagePanel.CELL_HEIGHT * 10)));
        systemPainter.setFace(systemPainter.getLocation());
        systemPainter.getHome().add(mainController.getGamePage().getXYKey(systemPainter.getFace().x , systemPainter.getFace().y));
        mainController.doMove(systemPainter , MoveTo.LEFT);
        mainController.doMove(systemPainter , MoveTo.LEFT);
        random = new Random();
        (timer = new Timer()).schedule(this , startTimer , periodTimer);
    }

    public static void start() {
        if (!running || systemPainterController == null) {
            if (systemPainterController != null) {
                systemPainterController.timer.cancel();
            }
            systemPainterController = new SystemPainterController();
        }
    }

    @Override
    public void run() {

        if (!running) {
            timer.cancel();
            return;
        }

        int moving = random.nextInt(4);
        for (int i = 0; i < moving; i++) {

            for (int i1 = 0; i1 < 10; i1++) {
                MainController.mainController.doMove(systemPainter , moveTo);
            }

            final int moveToNumber = random.nextInt(3);
            if (moveTo.equals(MoveTo.LEFT) || moveTo.equals(MoveTo.RIGHT)) {
                moveTo = switch (moveToNumber) {
                    case 1 -> MoveTo.TOP;
                    case 2 , default -> MoveTo.BOTTOM;
                };
            } else if (moveTo.equals(MoveTo.TOP) || moveTo.equals(MoveTo.BOTTOM)) {
                moveTo = switch (moveToNumber) {
                    case 1 -> MoveTo.LEFT;
                    case 2 , default -> MoveTo.RIGHT;
                };
            }
        }
        if (systemPainter.getHome().size() > 0) {
            final XY xy = MainController.mainController.getGamePage().getXYKey(systemPainter.getHome().stream().toList().get(0));
            assert xy != null;
            moveTo = xy.x > systemPainter.getLocation().x ? MoveTo.RIGHT : MoveTo.LEFT;
            while (xy.x != systemPainter.getLocation().x) {
                MainController.mainController.doMove(systemPainter , moveTo);
            }
            moveTo = xy.y > systemPainter.getLocation().y ? MoveTo.BOTTOM : MoveTo.TOP;
            while (xy.x != systemPainter.getLocation().x) {
                MainController.mainController.doMove(systemPainter , moveTo);
            }
        }
    }
}
