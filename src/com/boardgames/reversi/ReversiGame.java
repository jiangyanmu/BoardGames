package com.boardgames.reversi;

/**
 * 黑白棋 (Reversi) 的遊戲邏輯模型 (Model)。
 * 負責處理棋盤狀態、翻轉棋子規則、判斷勝負等核心邏輯。
 */
public class ReversiGame {

    public enum GameState {
        PLAYING,    // 遊戲進行中
        BLACK_WINS, // 黑棋獲勝
        WHITE_WINS, // 白棋獲勝
        DRAW        // 平局
    }

    private final int BOARD_SIZE = 8;
    private char[][] board; // 8x8 棋盤
    private char currentPlayer; // 當前玩家 ('B' 為黑, 'W' 為白)
    private GameState gameState;

    public ReversiGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        reset();
    }

    /**
     * 重置遊戲。
     * 清空棋盤，放置初始的四顆棋子，並設定黑棋先手。
     */
    public void reset() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = ' ';
            }
        }
        // 初始佈局：中間交叉放置黑白棋
        board[3][3] = 'W';
        board[3][4] = 'B';
        board[4][3] = 'B';
        board[4][4] = 'W';
        currentPlayer = 'B'; // 黑棋總是先手
        gameState = GameState.PLAYING;
    }

    /**
     * 執行落子動作。
     *
     * @param row 列索引
     * @param col 行索引
     * @return 如果落子成功回傳 true，否則回傳 false
     */
    public boolean makeMove(int row, int col) {
        if (!isValidMove(row, col)) {
            return false;
        }

        board[row][col] = currentPlayer; // 放置棋子
        flipDiscs(row, col); // 翻轉對手棋子

        // 檢查遊戲是否結束
        if (isGameOver()) {
            updateFinalGameState();
        } else {
            // 切換到對手
            char nextPlayer = getOpponent();
            // 如果對手有合法步數，則切換玩家
            if (hasValidMove(nextPlayer)) {
                switchPlayer();
            } else if (!hasValidMove(currentPlayer)) {
                // 如果對手無步可走，且當前玩家也無步可走（雙方都無法下子），遊戲結束
                // 注意：如果對手無步可走但當前玩家還有步可走，則控制權會保留在當前玩家（Pass）
                updateFinalGameState();
            }
            // 如果對手無步可走但當前玩家可以，則不切換玩家 (隱含的 Pass 規則)
        }

        return true;
    }

    /**
     * 翻轉落子位置周圍可以被夾住的對手棋子。
     */
    private void flipDiscs(int row, int col) {
        // 八個方向的偏移量
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int r = row + dr[i];
            int c = col + dc[i];
            boolean foundOpponent = false;
            // 沿著該方向尋找連續的對手棋子
            while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == getOpponent()) {
                r += dr[i];
                c += dc[i];
                foundOpponent = true;
            }
            // 如果找到了對手棋子，且最後一個是己方棋子，則表示可以夾住
            if (foundOpponent && r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == currentPlayer) {
                // 往回翻轉中間的所有棋子
                int curR = row + dr[i];
                int curC = col + dc[i];
                while (curR != r || curC != c) {
                    board[curR][curC] = currentPlayer;
                    curR += dr[i];
                    curC += dc[i];
                }
            }
        }
    }
    
    /**
     * 檢查目前的落子是否合法（是否能翻轉至少一顆棋子）。
     */
    public boolean isValidMove(int row, int col) {
        // 檢查邊界和是否為空位
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != ' ') {
            return false;
        }

        // 檢查八個方向
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int r = row + dr;
                int c = col + dc;
                boolean foundOpponent = false;
                // 尋找對手棋子
                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == getOpponent()) {
                    r += dr;
                    c += dc;
                    foundOpponent = true;
                }
                // 如果找到對手棋子且被己方棋子包夾，則為合法步
                if (foundOpponent && r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == currentPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 檢查指定玩家是否有任何合法的落子點。
     */
    private boolean hasValidMove(char player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isValidMoveForPlayer(i, j, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 模擬檢查指定玩家在特定位置落子是否合法。
     * 用於 AI 計算或顯示提示，不會實際改變遊戲狀態。
     */
    public boolean isValidMoveForPlayer(int row, int col, char player) {
        if (board[row][col] != ' ') {
            return false;
        }
        // 暫時切換當前玩家來檢查
        char originalPlayer = currentPlayer;
        currentPlayer = player;
        boolean isValid = isValidMove(row, col);
        currentPlayer = originalPlayer; // 恢復當前玩家
        return isValid;
    }
    
    private void switchPlayer() {
        currentPlayer = getOpponent();
    }

    private char getOpponent() {
        return (currentPlayer == 'B') ? 'W' : 'B';
    }

    /**
     * 判斷遊戲是否結束（雙方都無法移動）。
     */
    private boolean isGameOver() {
        return !hasValidMove('B') && !hasValidMove('W');
    }

    /**
     * 結算遊戲結果。
     */
    private void updateFinalGameState() {
        int blackCount = 0;
        int whiteCount = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 'B') {
                    blackCount++;
                } else if (board[i][j] == 'W') {
                    whiteCount++;
                }
            }
        }
        if (blackCount > whiteCount) {
            gameState = GameState.BLACK_WINS;
        } else if (whiteCount > blackCount) {
            gameState = GameState.WHITE_WINS;
        } else {
            gameState = GameState.DRAW;
        }
    }
    
    public int[] getScore() {
        int blackCount = 0;
        int whiteCount = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 'B') {
                    blackCount++;
                } else if (board[i][j] == 'W') {
                    whiteCount++;
                }
            }
        }
        return new int[]{blackCount, whiteCount};
    }

    // --- Getter 方法 ---
    public int getBoardSize() {
        return BOARD_SIZE;
    }

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
     * 取得棋盤副本。
     */
    public char[][] getBoard() {
        char[][] boardCopy = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, BOARD_SIZE);
        }
        return boardCopy;
    }
}