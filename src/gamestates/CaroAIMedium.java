package gamestates;

public class CaroAIMedium {
    private static final int SIZE = 15;
    
    public static int[] getMove(char[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[x][y] == '\0') {
                    int score = evaluateMove(board, x, y);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{x, y};
                    }
                }
            }
        }

        return bestMove;
    }

    private static int evaluateMove(char[][] board, int x, int y) {
        int score = 0;

        // Đánh giá theo cả 2 phía: AI (O) và người chơi (X)
        score += evaluatePosition(board, x, y, 'O') * 2; // Ưu tiên tấn công nhiều hơn
        score += evaluatePosition(board, x, y, 'X');

        // Ưu tiên gần quân đã đánh
        if (isNearOccupied(board, x, y)) {
            score += 5;
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
}
