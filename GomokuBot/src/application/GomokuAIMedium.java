package application;

import java.util.ArrayList;
import java.util.List;

public class GomokuAIMedium {
	
    public static int[] getMove(Board board) {
        int[][] grid = board.getBoard();
        int size = Board.SIZE;

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[x][y] == 0) {
                    int score = evaluateMove(grid, x, y); /// thử đi vào từng ô, trả về nước đi tốt nhất
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{x, y};
                    }
                }
            }
        }

        return bestMove;
    }

    private static int evaluateMove(int[][] grid, int x, int y) {
        int score = 0;

        // Ước lượng theo cả 2 phía: AI (2) và người chơi (1)
        score += evaluatePosition(grid, x, y, 2) + evaluatePosition(grid, x, y, 2) / 2; // Ưu tiên tấn công nhiều hơn
        score += evaluatePosition(grid, x, y, 1);

        // Ưu tiên gần quân đã đánh
        if (isNearOccupied(grid, x, y)) {
            score += 5;
        }

        return score;
    }

    private static int evaluatePosition(int[][] grid, int x, int y, int player) {
        int score = 0;
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}}; /// lên xuống , trái phải, chéo 1, chéo 2

        for (int[] dir : directions) {
            int count = 1;
            int openEnds = 0;

            int dx = dir[0], dy = dir[1];
            int forward = countConsecutive(grid, x, y, dx, dy, player);
            int backward = countConsecutive(grid, x, y, -dx, -dy, player); /// đếm số thằng liên tiếp trên các hướng
            count += forward + backward;

            // Kiểm tra mở hai đầu
            if (isOpenEnd(grid, x + (forward + 1) * dx, y + (forward + 1) * dy)) openEnds++;
            if (isOpenEnd(grid, x - (backward + 1) * dx, y - (backward + 1) * dy)) openEnds++;

            // Gán điểm dựa vào độ dài chuỗi và đầu mở
            if (count >= 5) {
                score += 10000;
            } else if(count == 4 && openEnds == 2) {
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
        while (inBounds(nx, ny) && grid[nx][ny] == player) {
            count++;
            nx += dx;
            ny += dy;
        }
        return count;
    }

    private static boolean isOpenEnd(int[][] grid, int x, int y) { // xem bị chặn đuôi không
        return inBounds(x, y) && grid[x][y] == 0;
    }

    private static boolean inBounds(int x, int y) { // kiểm tra ô x,y có nằm trong Bảng không
        return x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE;
    }

    private static boolean isNearOccupied(int[][] grid, int x, int y) { /// gần các ôn đã đánh không 
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if ((dx != 0 || dy != 0) && inBounds(nx, ny) && grid[nx][ny] != 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
