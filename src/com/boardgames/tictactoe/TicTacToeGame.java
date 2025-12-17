package com.boardgames.tictactoe;

/**
 * 井字棋的遊戲邏輯模型 (Model)。
 * 負責維護棋盤狀態、判斷勝負以及處理落子邏輯。
 */
public class TicTacToeGame {

    public enum GameState {
        PLAYING,    // 遊戲進行中
        X_WINS,     // 玩家 X 獲勝
        O_WINS,     // 玩家 O 獲勝
        DRAW        // 平局
    }

    private char[][] board; // 3x3 的棋盤陣列
    private char currentPlayer; // 當前玩家 ('X' 或 'O')
    private GameState gameState; // 當前遊戲狀態

    public TicTacToeGame() {
        board = new char[3][3];
        reset();
    }

    /**
     * 重置遊戲狀態。
     * 清空棋盤，將當前玩家設為 'X'，並將狀態設為 PLAYING。
     */
    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' '; // 使用空格表示空位
            }
        }
        currentPlayer = 'X';
        gameState = GameState.PLAYING;
    }

    /**
     * 嘗試在指定位置落子。
     * @param row 列索引 (0-2)
     * @param col 行索引 (0-2)
     * @return 如果落子成功回傳 true，否則回傳 false (例如該位置已有棋子或遊戲已結束)
     */
    public boolean makeMove(int row, int col) {
        // 檢查邊界、是否已佔用、以及遊戲是否正在進行中
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ' || gameState != GameState.PLAYING) {
            return false; // 無效的落子
        }

        board[row][col] = currentPlayer; // 更新棋盤
        updateGameState(); // 檢查勝負狀態
        // 如果遊戲仍在進行，切換玩家
        if (gameState == GameState.PLAYING) {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
        return true;
    }

    /**
     * 更新遊戲狀態。
     * 檢查是否有玩家獲勝或平局。
     */
    private void updateGameState() {
        if (checkWin('X')) {
            gameState = GameState.X_WINS;
        } else if (checkWin('O')) {
            gameState = GameState.O_WINS;
        } else if (isBoardFull()) {
            gameState = GameState.DRAW;
        }
    }

    /**
     * 檢查指定玩家是否獲勝。
     * 檢查所有橫排、直排和對角線。
     */
    private boolean checkWin(char player) {
        // 檢查橫排
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        // 檢查直排
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        // 檢查對角線
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // --- 用於 View 讀取的 Getter 方法 ---
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public char getSymbolAt(int row, int col) {
        return board[row][col];
    }

    /**
     * 取得棋盤的副本。
     * 回傳副本是為了防止外部直接修改內部的棋盤狀態，保持封裝性。
     */
    public char[][] getBoard() {
        char[][] boardCopy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, 3);
        }
        return boardCopy;
    }
}