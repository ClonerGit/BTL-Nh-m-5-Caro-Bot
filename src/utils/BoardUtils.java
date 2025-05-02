package utils;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class BoardUtils {
    public static boolean checkWin(char[][] board, int row, int col, char symbol) {
        return checkDirection(board, row, col, symbol, 1, 0)
            || checkDirection(board, row, col, symbol, 0, 1)
            || checkDirection(board, row, col, symbol, 1, 1)
            || checkDirection(board, row, col, symbol, 1, -1);
    }

    private static boolean checkDirection(char[][] board, int row, int col, char symbol, int dx, int dy) {
        int count = 1, SIZE = board.length;
        int r = row + dx, c = col + dy;
        while (inBounds(r, c, SIZE) && board[r][c] == symbol) { count++; r += dx; c += dy; }
        r = row - dx; c = col - dy;
        while (inBounds(r, c, SIZE) && board[r][c] == symbol) { count++; r -= dx; c -= dy; }
        return count >= 5;
    }

    private static boolean inBounds(int r, int c, int SIZE) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public static void highlightWinningLine(char[][] board, StackPane[][] cells, int row, int col, char symbol) {
        int[][] directions = {{1,0},{0,1},{1,1},{1,-1}};
        int SIZE = board.length;
        for (int[] d : directions) {
            int dx = d[0], dy = d[1], count = 1, startRow = row, startCol = col;
            int r = row + dx, c = col + dy;
            while (inBounds(r, c, SIZE) && board[r][c] == symbol) { count++; r += dx; c += dy; }
            r = row - dx; c = col - dy;
            while (inBounds(r, c, SIZE) && board[r][c] == symbol) { count++; startRow = r; startCol = c; r -= dx; c -= dy; }
            if (count >= 5) {
                for (int i = 0; i < 5; i++) {
                    int rr = startRow + i * dx, cc = startCol + i * dy;
                    cells[rr][cc].setBackground(new Background(new BackgroundFill(Color.web("#FFB74D"), null, null)));
                }
                return;
            }
        }
    }
}
