package com.boardgames.reversi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 黑白棋的 AI 玩家邏輯。
 * 採用「貪婪演算法」(Greedy Algorithm)，選擇能翻轉最多棋子的位置。
 */
public class ReversiAIPlayer {

    /**
     * 為 AI 玩家尋找最佳落子點。
     * 這個 AI 會計算每個合法位置能翻轉的棋子數量，並選擇數量最多的那個位置。
     * 如果有多個位置翻轉數量相同，則隨機選擇一個。
     *
     * @param game 當前的遊戲物件（用於取得棋盤和玩家資訊）
     * @return 包含 [row, col] 的陣列表示最佳位置，如果沒有合法步數則回傳 null
     */
    public static int[] findBestMove(ReversiGame game) {
        List<int[]> validMoves = new ArrayList<>();
        int maxFlips = 0;
        
        char[][] board = game.getBoard();
        char player = game.getCurrentPlayer(); // AI 當前的顏色

        // 遍歷整個棋盤尋找合法步數
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isValidMoveForPlayer(i, j, player)) {
                    // 計算這一步能翻轉多少棋子
                    int flips = countFlips(board, i, j, player);
                    
                    if (flips > maxFlips) {
                        // 找到更好的步數，清除舊的紀錄
                        maxFlips = flips;
                        validMoves.clear();
                        validMoves.add(new int[]{i, j});
                    } else if (flips == maxFlips) {
                        // 找到一樣好的步數，加入列表
                        validMoves.add(new int[]{i, j});
                    }
                }
            }
        }

        if (validMoves.isEmpty()) {
            return null; // 無法移動
        }

        // 從最佳選擇中隨機挑選一個（增加變化性）
        Random random = new Random();
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    /**
     * 計算在指定位置落子能翻轉的棋子數量。
     */
    private static int countFlips(char[][] board, int row, int col, char player) {
        int flips = 0;
        char opponent = (player == 'B') ? 'W' : 'B';
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int r = row + dr[i];
            int c = col + dc[i];
            int lineFlips = 0;
            // 沿著方向計算對手棋子
            while (r >= 0 && r < board.length && c >= 0 && c < board.length && board[r][c] == opponent) {
                r += dr[i];
                c += dc[i];
                lineFlips++;
            }
            // 必須在最後遇到己方棋子才算數
            if (lineFlips > 0 && r >= 0 && r < board.length && c >= 0 && c < board.length && board[r][c] == player) {
                flips += lineFlips;
            }
        }
        return flips;
    }
}