package application;

import java.util.ArrayList;
import java.util.List;

public class GomokuAIEasy {
    public static int[] getMove(Board board) {
        int[][] grid = board.getBoard();
        int size = Board.SIZE;

        // 1. Tấn công nếu AI có chuỗi 4 (-> thành 5)
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[x][y] == 0) {
                    grid[x][y] = 2;
                    if (formsLine(grid, x, y, 2, 5)) {
                        grid[x][y] = 0;
                        return new int[]{x, y};
                    }
                    grid[x][y] = 0;
                }
            }
        }

        // 2. Chặn nếu người chơi có chuỗi 4 (ít nhất 1 đầu hở)
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[x][y] == 0) {
                    grid[x][y] = 1;
                    if (formsLine(grid, x, y, 1, 5)) {
                        grid[x][y] = 0;
                        return new int[]{x, y};
                    }
                    grid[x][y] = 0;
                }
            }
        }

        // 3. Chặn nếu người chơi có chuỗi 3 với 2 đầu hở
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[x][y] == 0 && isDoubleOpenThree(grid, x, y, 1)) {
                    return new int[]{x, y};
                }
            }
        }

        // 4. Ưu tiên nước gần các quân đã đánh
        List<int[]> candidates = getNearbyEmptyCells(grid, size);
        if (!candidates.isEmpty()) {
            return candidates.get(0);
        }

        // fallback: đi ô đầu tiên còn trống
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[x][y] == 0) {
                    return new int[]{x, y};
                }
            }
        }

        return null;
    }

    // Kiểm tra có chuỗi liên tiếp đủ độ dài không
    private static boolean formsLine(int[][] grid, int x, int y, int player, int required) {
        int[][] dirs = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] d : dirs) {
            int count = 1;
            int dx = d[0], dy = d[1];
            for (int i = 1; i < required; i++) {
                int nx = x + i * dx, ny = y + i * dy;
                if (inBounds(nx, ny) && grid[nx][ny] == player) count++;
                else break;
            }
            for (int i = 1; i < required; i++) {
                int nx = x - i * dx, ny = y - i * dy;
                if (inBounds(nx, ny) && grid[nx][ny] == player) count++;
                else break;
            }
            if (count >= required) return true;
        }
        return false;
    }

    // Kiểm tra chuỗi 3 của đối thủ có 2 đầu hở không
    private static boolean isDoubleOpenThree(int[][] grid, int x, int y, int player) {
        int[][] dirs = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] d : dirs) {
            int dx = d[0], dy = d[1];
            int count = 1;
            int block1 = 0, block2 = 0;

            for (int i = 1; i <= 3; i++) {
                int nx = x + i * dx, ny = y + i * dy;
                if (inBounds(nx, ny)) {
                    if (grid[nx][ny] == player) count++;
                    else if (grid[nx][ny] != 0) { block1 = 1; break; }
                    else break;
                }
            }
            for (int i = 1; i <= 3; i++) {
                int nx = x - i * dx, ny = y - i * dy;
                if (inBounds(nx, ny)) {
                    if (grid[nx][ny] == player) count++;
                    else if (grid[nx][ny] != 0) { block2 = 1; break; }
                    else break;
                }
            }
            if (count >= 4 && block1 + block2 == 0) return true;
        }
        return false;
    }

    private static boolean inBounds(int x, int y) {
        return x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE;
    }

    // Tìm ô trống gần các nước đã đánh (trong phạm vi 1 ô)
    private static List<int[]> getNearbyEmptyCells(int[][] grid, int size) {
        List<int[]> result = new ArrayList<>();
        boolean[][] visited = new boolean[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[x][y] != 0) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            int nx = x + dx, ny = y + dy;
                            if (inBounds(nx, ny) && grid[nx][ny] == 0 && !visited[nx][ny]) {
                                result.add(new int[]{nx, ny});
                                visited[nx][ny] = true;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}
