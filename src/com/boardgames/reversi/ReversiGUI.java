package com.boardgames.reversi;

import com.boardgames.GameSelectionGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ReversiGUI extends JFrame {
    public enum GameMode {
        PLAYER_VS_PLAYER,
        PLAYER_VS_AI
    }

    private static final int BOARD_SIZE = 8;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private final ReversiGame game;
    private GameMode gameMode;

    public ReversiGUI() {
        this.game = new ReversiGame();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("黑白棋 (Reversi)");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel modeSelectionPanel = createModeSelectionPanel();
        JPanel gamePanel = createGamePanel();

        mainPanel.add(modeSelectionPanel, "MODE_SELECTION");
        mainPanel.add(gamePanel, "GAME");

        add(mainPanel);
        cardLayout.show(mainPanel, "MODE_SELECTION");
    }

    private JPanel createModeSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("請選擇遊戲模式", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 32));
        gbc.insets = new Insets(10, 10, 30, 10);
        panel.add(titleLabel, gbc);

        JButton pvpButton = new JButton("玩家 vs. 玩家");
        pvpButton.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        pvpButton.setMargin(new Insets(10, 0, 10, 0));
        pvpButton.addActionListener(e -> startGame(GameMode.PLAYER_VS_PLAYER));
        gbc.insets = new Insets(10, 40, 10, 40);
        panel.add(pvpButton, gbc);

        JButton pvaButton = new JButton("玩家 vs. 電腦");
        pvaButton.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        pvaButton.setMargin(new Insets(10, 0, 10, 0));
        pvaButton.addActionListener(e -> startGame(GameMode.PLAYER_VS_AI));
        panel.add(pvaButton, gbc);

        JButton backButton = new JButton("返回遊戲選擇");
        backButton.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        backButton.addActionListener(e -> {
            this.dispose();
            new GameSelectionGUI().setVisible(true);
        });
        gbc.insets = new Insets(20, 40, 10, 40);
        panel.add(backButton, gbc);

        return panel;
    }

    private JPanel createGamePanel() {
        JPanel gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("請開始遊戲", SwingConstants.CENTER);
        statusLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        scoreLabel = new JLabel("黑: 2, 白: 2", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        headerPanel.add(statusLabel, BorderLayout.NORTH);
        headerPanel.add(scoreLabel, BorderLayout.SOUTH);

        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setBackground(new Color(0, 128, 0));
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBackground(new Color(0, 128, 0));
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }

        JButton newGameButton = new JButton("新遊戲");
        newGameButton.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "MODE_SELECTION"));

        gamePanel.add(headerPanel, BorderLayout.NORTH);
        gamePanel.add(boardPanel, BorderLayout.CENTER);
        gamePanel.add(newGameButton, BorderLayout.SOUTH);

        return gamePanel;
    }

    private void startGame(GameMode mode) {
        this.gameMode = mode;
        game.reset();
        updateView();
        cardLayout.show(mainPanel, "GAME");
    }

    private void handleAITurn() {
        setBoardEnabled(false);
        SwingWorker<int[], Void> worker = new SwingWorker<>() {
            @Override
            protected int[] doInBackground() throws Exception {
                Thread.sleep(500);
                return ReversiAIPlayer.findBestMove(game);
            }

            @Override
            protected void done() {
                try {
                    int[] aiMove = get();
                    if (aiMove != null) {
                        game.makeMove(aiMove[0], aiMove[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    updateView();
                    if (game.getGameState() == ReversiGame.GameState.PLAYING) {
                        setBoardEnabled(true);
                    }
                }
            }
        };
        worker.execute();
    }

    private class ButtonClickListener implements ActionListener {
        private final int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getGameState() != ReversiGame.GameState.PLAYING)
                return;

            if (game.makeMove(row, col)) {
                updateView();
                if (gameMode == GameMode.PLAYER_VS_AI &&
                        game.getGameState() == ReversiGame.GameState.PLAYING &&
                        game.getCurrentPlayer() == 'W') {
                    handleAITurn();
                }
            }
        }
    }

    private void setBoardEnabled(boolean enabled) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j].setEnabled(enabled);
            }
        }
    }

    private Icon createPieceIcon(Color color, int size) {
        if (size <= 0)
            size = 50; // Default size if not rendered yet
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillOval(3, 3, size - 7, size - 7); // Margins for the piece
        g2d.dispose();
        return new ImageIcon(image);
    }

    private Icon createHintIcon(int size) {
        if (size <= 0)
            size = 50;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(192, 192, 192, 128)); // Semi-transparent grey
        int dotSize = size / 4;
        g2d.fillOval(size / 2 - dotSize / 2, size / 2 - dotSize / 2, dotSize, dotSize);
        g2d.dispose();
        return new ImageIcon(image);
    }

    private void updateView() {
        char currentPlayer = game.getCurrentPlayer();
        boolean isGameOver = game.getGameState() != ReversiGame.GameState.PLAYING;
        int buttonSize = buttons[0][0].getWidth();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                char symbol = game.getSymbolAt(i, j);
                JButton button = buttons[i][j];
                button.setText("");
                button.setBackground(new Color(0, 128, 0)); // Standard green

                if (symbol == 'B') {
                    button.setIcon(createPieceIcon(Color.BLACK, buttonSize));
                    button.setDisabledIcon(createPieceIcon(Color.BLACK, buttonSize));
                } else if (symbol == 'W') {
                    button.setIcon(createPieceIcon(Color.LIGHT_GRAY, buttonSize));
                    button.setDisabledIcon(createPieceIcon(Color.LIGHT_GRAY, buttonSize));
                } else { // Empty cell
                    if (!isGameOver && game.isValidMoveForPlayer(i, j, currentPlayer)) {
                        button.setIcon(createHintIcon(buttonSize));
                        button.setDisabledIcon(createHintIcon(buttonSize));
                    } else {
                        button.setIcon(null);
                        button.setDisabledIcon(null);
                    }
                }
            }
        }

        updateStatusLabel();
        int[] score = game.getScore();
        scoreLabel.setText(String.format("黑棋: %d, 白棋: %d", score[0], score[1]));

        setBoardEnabled(!isGameOver);
    }

    private void updateStatusLabel() {
        ReversiGame.GameState state = game.getGameState();
        String statusText;
        switch (state) {
            case PLAYING:
                statusText = "輪到 " + (game.getCurrentPlayer() == 'B' ? "黑棋" : "白棋");
                break;
            case BLACK_WINS:
                statusText = "遊戲結束：黑棋獲勝！";
                break;
            case WHITE_WINS:
                statusText = "遊戲結束：白棋獲勝！";
                break;
            case DRAW:
                statusText = "遊戲結束：平局！";
                break;
            default:
                statusText = "";
                break;
        }
        statusLabel.setText(statusText);
    }
}
