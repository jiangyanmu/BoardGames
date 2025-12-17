# Java 桌遊整合平台

## 1. 封面

*   **標題**：Java 桌遊整合平台 (Java Board Games Collection)
*   **副標題**：黑白棋 (Reversi) 與 井字遊戲 (Tic-Tac-Toe) 的實作
*   **作者**：[你的名字]

## 2. 專案概述

*   **目標**：建立一個可擴充的桌遊平台，整合多種棋類遊戲。
*   **主要功能**：
    *   圖形化使用者介面 (GUI) 操作。
    *   包含兩款經典遊戲：井字遊戲、黑白棋。
    *   人機對戰功能 (AI Player)。
    *   遊戲選擇大廳 (`GameSelectionGUI`)。

## 3. 系統架構

*   **套件結構 (Package Structure)**：
    *   `com.boardgames`: 主程式進入點與選單。
    *   `com.boardgames.tictactoe`: 井字遊戲邏輯與介面。
    *   `com.boardgames.reversi`: 黑白棋邏輯與介面。
*   **設計模式 (Design Concepts)**：
    *   **分離關注點**：邏輯 (`Game`)、介面 (`GUI`)、AI (`AIPlayer`) 分開撰寫，易於維護。

## 4. 遊戲介紹 1：井字遊戲 (Tic-Tac-Toe)

*   **特色**：入門級的棋類實作。
*   **AI 策略**：[請根據 TicTacToeAIPlayer.java 內容填寫，例如：隨機下棋 或 Minimax 演算法]
*   **展示**：放一張遊戲執行的截圖。

## 5. 遊戲介紹 2：黑白棋 (Reversi / Othello)

*   **特色**：較複雜的棋盤邏輯 (翻轉棋子)。
*   **挑戰**：判斷合法步數 (Legal Moves) 與勝負判定。
*   **AI 實作**：[請根據 ReversiAIPlayer.java 內容填寫 AI 的決策方式]
*   **展示**：放一張黑白棋執行的截圖。

## 6. 遭遇困難與解決方案 (Challenges & Solutions)

*   例如：如何處理由 GUI (`GameSelectionGUI`) 切換到個別遊戲視窗？
*   例如：黑白棋的翻轉邏輯如何撰寫？
*   例如：如何讓 AI 不要下出無效的棋步？

## 7. 未來展望 (Future Work)

*   **新增遊戲**：如五子棋、象棋。
*   **功能擴充**：增加網路連線對戰功能 (Socket)、存檔功能、背景音樂。
*   **AI 優化**：加入難度選擇 (簡單/困難)。

## 8. 實機展示 (Live Demo) & Q&A
