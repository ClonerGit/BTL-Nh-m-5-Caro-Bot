package gamestates;

public class CaroAIHard {
    private static final int SIZE = 15;
    private static final int MAX_DEPTH = 3;

    public static int[] getMove(char[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : generateMoves(board)) {
            char[][] copy = copyBoard(board);
            copy[move[0]][move[1]] = 'O';
            int score = minimax(copy, MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private static int minimax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || checkGameEnd(board)) {
            return evaluateBoard(board);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : generateMoves(board)) {
                char[][] copy = copyBoard(board);
                copy[move[0]][move[1]] = 'O';
                int eval = minimax(copy, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : generateMoves(board)) {
                char[][] copy = copyBoard(board);
                copy[move[0]][move[1]] = 'X';
                int eval = minimax(copy, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private static java.util.List<int[]> generateMoves(char[][] board) {
        java.util.List<int[]> moves = new java.util.ArrayList<>();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] == '\0' && isNearOccupied(board, x, y)) {
                    moves.add(new int[]{x, y});
                }
            }
        }
        return moves;
    }

    private static boolean isNearOccupied(char[][] board, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if ((dx != 0 || dy != 0) && inBounds(nx, ny) && board[nx][ny] != '\0') {
                    return true;
                }
            }
        }
        return false;
    }

    public static int evaluateBoard(char[][] board) {
        int score = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] == 'O') {
                    score += evaluatePosition(board, x, y, 'O');
                } else if (board[x][y] == 'X') {
                    score -= evaluatePosition(board, x, y, 'X');
                }
            }
        }
        return score;
    }

    private static int evaluatePosition(char[][] board, int x, int y, char symbol) {
        int score = 0;
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;
            int openEnds = 0;

            int dx = dir[0], dy = dir[1];
            int forward = countConsecutive(board, x, y, dx, dy, symbol);
            int backward = countConsecutive(board, x, y, -dx, -dy, symbol);
            count += forward + backward;

            if (isOpenEnd(board, x + (forward + 1) * dx, y + (forward + 1) * dy)) openEnds++;
            if (isOpenEnd(board, x - (backward + 1) * dx, y - (backward + 1) * dy)) openEnds++;

            if (count >= 5) {
                score += 10000;
            } else if (count == 4 && openEnds == 2) {
                score += 9000;
            } else if (count == 4 && openEnds == 1) {
                score += (symbol == 'O') ? 5000 : 4000;
            } else if (count == 3 && openEnds == 2) {
                score += (symbol == 'O') ? 500 : 400;
            } else if (count == 2 && openEnds == 2) {
                score += (symbol == 'O') ? 100 : 80;
            } else if (count == 3 && openEnds == 1) {
                score += (symbol == 'O') ? 200 : 150;
            } else if (count == 2 && openEnds == 1) {
                score += (symbol == 'O') ? 50 : 30;
            }
        }

        return score;
    }

    private static int countConsecutive(char[][] board, int x, int y, int dx, int dy, char symbol) {
        int count = 0;
        int nx = x + dx;
        int ny = y + dy;
        while (inBounds(nx, ny) && board[nx][ny] == symbol) {
            count++;
            nx += dx;
            ny += dy;
        }
        return count;
    }

    private static boolean isOpenEnd(char[][] board, int x, int y) {
        return inBounds(x, y) && board[x][y] == '\0';
    }

    private static boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    private static boolean checkGameEnd(char[][] board) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] != '\0' && checkWin(board, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkWin(char[][] board, int row, int col) {
        char symbol = board[row][col];
        return checkDirection(board, row, col, symbol, 1, 0)
            || checkDirection(board, row, col, symbol, 0, 1)
            || checkDirection(board, row, col, symbol, 1, 1)
            || checkDirection(board, row, col, symbol, 1, -1);
    }

    private static boolean checkDirection(char[][] board, int row, int col, char symbol, int dx, int dy) {
        int count = 1;
        int r = row + dx, c = col + dy;
        while (inBounds(r, c) && board[r][c] == symbol) { count++; r += dx; c += dy; }
        r = row - dx; c = col - dy;
        while (inBounds(r, c) && board[r][c] == symbol) { count++; r -= dx; c -= dy; }
        return count >= 5;
    }

    private static char[][] copyBoard(char[][] original) {
        char[][] copy = new char[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                copy[x][y] = original[x][y];
            }
        }
        return copy;
    }
} 
