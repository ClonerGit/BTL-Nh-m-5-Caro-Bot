package application;

import java.util.ArrayList;
import java.util.List;

public class GomokuAIHard {

    private static final int MAX_DEPTH = 3;

    public static int[] getMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : generateMoves(board)) {
            Board copy = copyBoard(board);
            copy.placeMove(move[0], move[1], 2); // AI là player 2
            int score = minimax(copy, MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private static int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        int winner = board.checkWinner();
        if (depth == 0 || winner != 0) {
            if (winner == 2) return 99999999;
            if (winner == 1) return -99999999;
            return evaluateBoard(board);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : generateMoves(board)) {
                Board copy = copyBoard(board);
                copy.placeMove(move[0], move[1], 2);
                int eval = minimax(copy, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : generateMoves(board)) {
                Board copy = copyBoard(board);
                copy.placeMove(move[0], move[1], 1);
                int eval = minimax(copy, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private static List<int[]> generateMoves(Board board) {
        List<int[]> moves = new ArrayList<>();
        int[][] grid = board.getBoard();

        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (grid[x][y] == 0 && isNearOccupied(grid, x, y)) {
                    moves.add(new int[]{x, y});
                }
            }
        }
        return moves;
    }

    private static boolean isNearOccupied(int[][] grid, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if ((dx != 0 || dy != 0) && nx >= 0 && ny >= 0 && nx < Board.SIZE && ny < Board.SIZE) {
                    if (grid[nx][ny] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int evaluateBoard(Board board) {
        int[][] grid = board.getBoard();
        int scoreAI = 0, scoreHuman = 0;

        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                for (int[] dir : new int[][]{{1, 0}, {0, 1}, {1, 1}, {1, -1}}) {
                    scoreAI += evaluatePattern(grid, x, y, dir[0], dir[1], 2);
                    scoreHuman += evaluatePattern(grid, x, y, dir[0], dir[1], 1);
                }
            }
        }
        return scoreAI - scoreHuman;
    }

    private static int evaluatePattern(int[][] grid, int x, int y, int dx, int dy, int player) {
        int[][] patterns = {
            {0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0},
            {1, 1, 0, 1, 0},
            {0, 0, 1, 1, 1},
            {1, 1, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 0},
            {1, 1, 1, 0, 1},
            {1, 1, 0, 1, 1},
            {1, 0, 1, 1, 1},
            {1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1},
            {0, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1}
        };

        int[] scores = {
            50, 50, 200, 500, 500, 5000,
            5000, 5000, 5000, 5000, 5000,
            5000, 5000, 50000, 99999999
        };

        int score = 0;
        for (int p = 0; p < patterns.length; p++) {
            int[] pattern = patterns[p];
            boolean matched = true;

            for (int i = 0; i < pattern.length; i++) {
                int nx = x + i * dx;
                int ny = y + i * dy;
                if (nx < 0 || ny < 0 || nx >= Board.SIZE || ny >= Board.SIZE) {
                    matched = false;
                    break;
                }
                int expected = (pattern[i] == 1) ? player : 0;
                if (grid[nx][ny] != expected) {
                    matched = false;
                    break;
                }
            }

            if (matched) {
                score += scores[p];
            }
        }

        return score;
    }

    private static Board copyBoard(Board original) {
        Board copy = new Board();
        int[][] grid = original.getBoard();
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (grid[x][y] != 0) {
                    copy.placeMove(x, y, grid[x][y]);
                }
            }
        }
        return copy;
    }
}
