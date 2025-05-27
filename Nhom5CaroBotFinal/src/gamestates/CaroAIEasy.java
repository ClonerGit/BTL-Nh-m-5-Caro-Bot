package gamestates;

import java.util.ArrayList;
import java.util.List;
import utils.BoardUtils;

public class CaroAIEasy implements CaroBot{
    private static final int SIZE = 15;

    @Override
    public  int[] getMove(char[][] board) {
        // 1. Tấn công nếu AI có thể thắng
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[x][y] == '\0') {
                    board[x][y] = 'O';
                    if (BoardUtils.checkWin(board, x, y, 'O')) {
                        board[x][y] = '\0';
                        return new int[]{x, y};
                    }
                    board[x][y] = '\0';
                }
            }
        }

        // 2. Chặn nếu người chơi sắp thắng
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[x][y] == '\0') {
                    board[x][y] = 'X';
                    if (BoardUtils.checkWin(board, x, y, 'X')) {
                        board[x][y] = '\0';
                        return new int[]{x, y};
                    }
                    board[x][y] = '\0';
                }
            }
        }

        // 3. Ưu tiên nước gần quân đã đánh
        List<int[]> candidates = new ArrayList<>();
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

        // 4. Nếu không có ô nào gần, đi ngẫu nhiên
        List<int[]> emptyCells = new ArrayList<>();
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

        return null; // không còn nước đi
    }

    private static boolean isNearOccupied(char[][] board, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = y + dy;
                if ((dx != 0 || dy != 0) && inBounds(nx, ny) && board[nx][ny] != '\0') {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }
    
    @Override
    public String getName() {
        return "Easy AI";
    }
}
