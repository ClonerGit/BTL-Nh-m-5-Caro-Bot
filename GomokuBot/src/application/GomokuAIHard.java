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
            copy.placeMove(move[0], move[1], 2);
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
        int score = 0;
        int[][] grid = board.getBoard();

        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (grid[x][y] == 2) {
                    score += evaluatePosition(grid, x, y, 2);
                } else if (grid[x][y] == 1) {
                    score -= evaluatePosition(grid, x, y, 1);
                }
            }
        }
        return score;
    }

    private static int evaluatePosition(int[][] grid, int x, int y, int player) {
        int score = 0;
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;
            int openEnds = 0;

            int dx = dir[0], dy = dir[1];
            int forward = countConsecutive(grid, x, y, dx, dy, player);
            int backward = countConsecutive(grid, x, y, -dx, -dy, player);
            count += forward + backward;

            if (isOpenEnd(grid, x + (forward + 1) * dx, y + (forward + 1) * dy)) openEnds++;
            if (isOpenEnd(grid, x - (backward + 1) * dx, y - (backward + 1) * dy)) openEnds++;

            if (count >= 5) {
                score += 10000;
            } else if (count == 4 && openEnds == 2) {
                score += 9000;
            } else if (count == 4 && openEnds == 1) {
                score += (player == 2) ? 5000 : 4000;
            } else if (count == 3 && openEnds == 2) {
                score += (player == 2) ? 500 : 400;
            } else if (count == 2 && openEnds == 2) {
                score += (player == 2) ? 100 : 80;
            } else if (count == 3 && openEnds == 1) {
                score += (player == 2) ? 200 : 150;
            } else if (count == 2 && openEnds == 1) {
                score += (player == 2) ? 50 : 30;
            }
        }

        return score;
    }

    private static int countConsecutive(int[][] grid, int x, int y, int dx, int dy, int player) {
        int count = 0;
        int nx = x + dx;
        int ny = y + dy;
        while (nx >= 0 && ny >= 0 && nx < Board.SIZE && ny < Board.SIZE && grid[nx][ny] == player) {
            count++;
            nx += dx;
            ny += dy;
        }
        return count;
    }

    private static boolean isOpenEnd(int[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < Board.SIZE && y < Board.SIZE && grid[x][y] == 0;
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
