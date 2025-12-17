package com.boardgames.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 井字棋的 AI 玩家邏輯。
 * 這是一個簡單的 AI，只會隨機選擇空位。
 */
public class TicTacToeAIPlayer {

    /**
     * 為 AI 玩家尋找下一步。
     * 這是一個簡單的 AI，只會從空位中隨機選擇一個。
     *
     * @param board 目前的遊戲棋盤
     * @return 包含 [row, col] 的陣列表示落子位置，如果沒有空位則回傳 null
     */
    public static int[] findRandomMove(char[][] board) {
        List<int[]> emptyCells = new ArrayList<>();
        // 找出所有空位
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return null; // 沒有可移動的位置
        }

        // 隨機選擇一個空位
        Random random = new Random();
        int[] chosenMove = emptyCells.get(random.nextInt(emptyCells.size()));
        return chosenMove;
    }
}