package application;

public class Board {
    public static final int SIZE = 15; // Kích thước bàn cờ
    private int[][] grid; // 0: trống, 1: người chơi, 2: máy

    public Board() {
        grid = new int[SIZE][SIZE];
    }

    public boolean placeMove(int x, int y, int player) {
        if (isValid(x, y) && grid[x][y] == 0) {
            grid[x][y] = player;
            return true;
        }
        return false;
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public int getCell(int x, int y) {
        if (isValid(x, y)) {
            return grid[x][y];
        }
        return -1;
    }

    public int[][] getBoard() {
        return grid;
    }

    // Tạm thời trả về 0 (chưa ai thắng), sau sẽ bổ sung logic kiểm tra thắng/thua
    public int checkWinner() {
    	for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int player = grid[x][y];
                if (player == 0) continue;

                if (checkDirection(x, y, 1, 0, player) ||  // hàng ngang →
                    checkDirection(x, y, 0, 1, player) ||  // hàng dọc ↓
                    checkDirection(x, y, 1, 1, player) ||  // chéo chính ↘
                    checkDirection(x, y, 1, -1, player))   // chéo phụ ↗
                {
                    return player;
                }
            }
        }
        return 0; // chưa ai thắng
    }
    
	private boolean checkDirection(int x, int y, int dx, int dy, int player) {
		int count = 1;

		for (int i = 1; i < 5; i++) {
			int newX = x + i * dx;
			int newY = y + i * dy;
			if (isValid(newX, newY) && grid[newX][newY] == player) {
				count++;
			} else {
				break;
			}
		}

		for (int i = 1; i < 5; i++) {
			int newX = x - i * dx;
			int newY = y - i * dy;
			if (isValid(newX, newY) && grid[newX][newY] == player) {
				count++;
			} else {
				break;
			}
		}

		return count >= 5;
	}

    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = 0;
            }
        }
    }
}