package com.boardgames;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import com.boardgames.reversi.ReversiGUI;
import com.boardgames.tictactoe.TicTacToeGUI;

/**
 * 遊戲選擇主視窗。
 * 提供使用者選擇「井字棋」或「黑白棋」的介面。
 */
public class GameSelectionGUI extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // --- 現代化簡約配色 (靈感來自 shadcn/ui) ---
    private final Color COLOR_BACKGROUND = new Color(248, 249, 250); // 米白色背景
    private final Color COLOR_CARD = Color.WHITE; // 卡片/按鈕背景
    private final Color COLOR_TEXT_PRIMARY = new Color(33, 37, 41); // 深灰色文字
    private final Color COLOR_PRIMARY = new Color(73, 80, 87); // 灰階強調色
    private final Color COLOR_BORDER = new Color(222, 226, 230); // 淺灰邊框

    // --- 字體設定 ---
    private final Font FONT_MODE_TITLE = new Font("微軟正黑體", Font.BOLD, 32);
    private final Font FONT_MODE_BUTTON = new Font("微軟正黑體", Font.BOLD, 18);

    public GameSelectionGUI() {
        // 設定介面風格為系統預設風格 (例如 Windows 風格)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("遊戲選擇");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 視窗置中

        JPanel gameSelectionPanel = createGameSelectionPanel();

        mainPanel.add(gameSelectionPanel, "GAME_SELECTION");

        add(mainPanel);
        cardLayout.show(mainPanel, "GAME_SELECTION");
    }

    /**
     * 建立遊戲選擇面板，包含標題與遊戲按鈕。
     */
    private JPanel createGameSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // 每個元件佔滿一行
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("請選擇遊戲", SwingConstants.CENTER);
        titleLabel.setFont(FONT_MODE_TITLE);
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        gbc.insets = new Insets(10, 10, 30, 10); // 增加標題下方的間距
        panel.add(titleLabel, gbc);

        // 建立並設定「井字棋」按鈕
        JButton ticTacToeButton = createStyledModeButton("井字棋");
        ticTacToeButton.addActionListener(e -> {
            this.dispose(); // 關閉目前視窗
            new TicTacToeGUI().setVisible(true); // 開啟井字棋視窗
        });
        gbc.insets = new Insets(10, 40, 10, 40); // 調整按鈕左右邊距
        panel.add(ticTacToeButton, gbc);

        // 建立並設定「黑白棋」按鈕
        JButton reversiButton = createStyledModeButton("黑白棋");
        reversiButton.addActionListener(e -> {
            this.dispose(); // 關閉目前視窗
            new ReversiGUI().setVisible(true); // 開啟黑白棋視窗
        });
        panel.add(reversiButton, gbc);

        return panel;
    }

    /**
     * 建立統一風格的按鈕，包含滑鼠懸停 (Hover) 效果。
     */
    private JButton createStyledModeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_MODE_BUTTON);
        button.setBackground(COLOR_CARD);
        button.setForeground(COLOR_TEXT_PRIMARY);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 2),
                BorderFactory.createEmptyBorder(15, 30, 15, 30) // 增加內距讓按鈕更大氣
        ));
        
        // 加入滑鼠監聽器以實作 Hover 變色效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_PRIMARY);
                button.setForeground(COLOR_CARD);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_CARD);
                button.setForeground(COLOR_TEXT_PRIMARY);
            }
        });
        return button;
    }
}