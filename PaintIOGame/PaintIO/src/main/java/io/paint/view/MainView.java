package io.paint.view;

import io.paint.data.enums.MoveTo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class MainView extends JFrame {
    protected JButton btnStart;
    protected GamePagePanel gamePage;
    protected JSlider sSpeed;

    protected MainView() {
    }

    protected void create(final AppearanceReady appearanceReady) {
        SwingUtilities.invokeLater(() -> {
            setTitle("Paint IO Game");
            initComponents();
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
            btnStart.addActionListener(e -> onClickBtnStartGame());
            sSpeed.addChangeListener(e -> onChangeSSpeed(sSpeed.getValue()));


            gamePage.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getID() == MouseEvent.MOUSE_FIRST) {
                        final MoveTo moveTo = switch (e.getButton()) {
                            case MouseEvent.BUTTON1 -> MoveTo.LEFT;
                            case MouseEvent.BUTTON3 -> MoveTo.RIGHT;
                            default -> null;
                        };
                        if (moveTo != null)
                            onMovie(moveTo);
                    }
                }
            });

            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    final MoveTo moveTo = switch (e.getKeyCode()) {
                        case KeyEvent.VK_RIGHT -> MoveTo.RIGHT;
                        case KeyEvent.VK_LEFT -> MoveTo.LEFT;
                        case KeyEvent.VK_DOWN -> MoveTo.BOTTOM;
                        case KeyEvent.VK_UP -> MoveTo.TOP;
                        default -> null;
                    };
                    if (moveTo != null)
                        onMovie(moveTo);
                }
                return false;
            });

            appearanceReady.ready();
        });
    }

    private void initComponents() {

        gamePage = new GamePagePanel();
        btnStart = new JButton();
        sSpeed = new JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        gamePage.setBackground(new java.awt.Color(0 , 153 , 204));

        javax.swing.GroupLayout gamePageLayout = new javax.swing.GroupLayout(gamePage);
        gamePage.setLayout(gamePageLayout);
        gamePageLayout.setHorizontalGroup(gamePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0 , 0 , Short.MAX_VALUE));
        gamePageLayout.setVerticalGroup(gamePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0 , 464 , Short.MAX_VALUE));

        btnStart.setText("let's go");

        sSpeed.setMaximum(3);
        sSpeed.setValue(1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(gamePage , javax.swing.GroupLayout.DEFAULT_SIZE , javax.swing.GroupLayout.DEFAULT_SIZE , Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(btnStart).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(sSpeed , javax.swing.GroupLayout.DEFAULT_SIZE , 833 , Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(gamePage , javax.swing.GroupLayout.PREFERRED_SIZE , javax.swing.GroupLayout.DEFAULT_SIZE , javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnStart).addComponent(sSpeed , javax.swing.GroupLayout.PREFERRED_SIZE , javax.swing.GroupLayout.DEFAULT_SIZE , javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE , Short.MAX_VALUE)));

        pack();
    }

    protected abstract void onClickBtnStartGame();

    protected abstract void onChangeSSpeed(final int value);

    protected abstract void onMovie(final MoveTo moveTo);

    public GamePagePanel getGamePage() {
        return gamePage;
    }
}
