package gamestates;

public class CaroAIEasy {
    private static final int SIZE = 15;
    
    public static int[] getMove(char[][] board) {
        // 1. Tấn công nếu AI có chuỗi 4 (-> thành 5)
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[x][y] == '\0') {
                    board[x][y] = 'O';
                    if (checkWin(board, x, y)) {
                        board[x][y] = '\0';
                        return new int[]{x, y};
                    }
                    board[x][y] = '\0';
                }
            }
        }

        // 2. Chặn nếu người chơi có chuỗi 4
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[x][y] == '\0') {
                    board[x][y] = 'X';
                    if (checkWin(board, x, y)) {
                        board[x][y] = '\0';
                        return new int[]{x, y};
                    }
                    board[x][y] = '\0';
                }
            }
        }

        // 3. Ưu tiên nước gần các quân đã đánh
        java.util.List<int[]> candidates = new java.util.ArrayList<>();
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[x][y] == '\0' && isNearOccupied(board, x, y)) {
                    candidates.add(new int[]{x, y});
                }
            }
        }
        if (!candidates.isEmpty()) {
            return candidates.get((int)(Math.random() * candidates.size()));
        }

        // 4. Đi ngẫu nhiên vào ô trống
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[x][y] == '\0') {
                    emptyCells.add(new int[]{x, y});
                }
            }
        }
        if (!emptyCells.isEmpty()) {
            return emptyCells.get((int)(Math.random() * emptyCells.size()));
        }

        return null;
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
