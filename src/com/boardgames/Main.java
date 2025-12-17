package com.boardgames;

import javax.swing.SwingUtilities;

/**
 * 應用程式的進入點。
 * 負責啟動主執行緒並顯示遊戲選擇視窗。
 */
public class Main {
    public static void main(String[] args) {
        // 使用 SwingUtilities.invokeLater 確保 GUI 的創建和更新在事件分發執行緒 (EDT) 中執行
        // 這是 Swing 程式的標準做法，以避免執行緒安全問題
        SwingUtilities.invokeLater(() -> {
            GameSelectionGUI gameSelection = new GameSelectionGUI();
            gameSelection.setVisible(true);
        });
    }
}