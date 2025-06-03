package gamestates;

public class CaroAIHard implements CaroBot {

	private static final int SIZE = 15; // Kích thước bàn cờ
	private static final int MAX_DEPTH = 3;

	@Override
	public int[] getMove(char[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '\0' && isNearOccupied(board, i, j)) {
                    board[i][j] = 'O';
                    int score = minimax(board, MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    board[i][j] = '\0';

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }

        return bestMove;
    }

	private static int minimax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        int winner = checkWinner(board);
        if (depth == 0 || winner != 0) {
            if (winner == 2) return 99999999;
            if (winner == 1) return -99999999;
            return evaluateBoard(board);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == '\0' && isNearOccupied(board, i, j)) {
                        board[i][j] = 'O';
                        int eval = minimax(board, depth - 1, alpha, beta, false);
                        board[i][j] = '\0';
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == '\0' && isNearOccupied(board, i, j)) {
                        board[i][j] = 'X';
                        int eval = minimax(board, depth - 1, alpha, beta, true);
                        board[i][j] = '\0';
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break;
                    }
                }
            }
            return minEval;
        }
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

    private static boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < SIZE && y < SIZE;
    }

    private static int evaluateBoard(char[][] board) {
        int[][] grid = new int[SIZE][SIZE];

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (board[x][y] == 'O') grid[x][y] = 2;
                else if (board[x][y] == 'X') grid[x][y] = 1;
                else grid[x][y] = 0;
            }
        }

        int scoreAI = 0, scoreHuman = 0;
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int[] dir : directions) {
                    scoreAI += evaluatePattern(grid, x, y, dir[0], dir[1], 2);
                    scoreHuman += evaluatePattern(grid, x, y, dir[0], dir[1], 1);
                }
            }
        }

        return scoreAI - scoreHuman;
    }

    private static int evaluatePattern(int[][] grid, int x, int y, int dx, int dy, int player) {
        int[][] patterns = {
            {0, 1, 1, 0, 0}, {0, 0, 1, 1, 0}, {1, 1, 0, 1, 0},
            {0, 0, 1, 1, 1}, {1, 1, 1, 0, 0}, {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 0}, {0, 1, 1, 0, 1, 0}, {1, 1, 1, 0, 1},
            {1, 1, 0, 1, 1}, {1, 0, 1, 1, 1}, {1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 0}, {1, 1, 1, 1, 1}
        };

        int[] scores = {
            50, 50, 200, 500, 500, 5000, 5000, 5000,
            5000, 5000, 5000, 5000, 5000, 50000, 99999999
        };

        int score = 0;
        for (int p = 0; p < patterns.length; p++) {
            int[] pattern = patterns[p];
            boolean matched = true;

            for (int i = 0; i < pattern.length; i++) {
                int nx = x + i * dx;
                int ny = y + i * dy;

                if (!inBounds(nx, ny)) {
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

    // Dummy checkWinner function to be replaced with real implementation
    private static int checkWinner(char[][] board) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] == 'O')
				{
					if (evaluatePosition(board, i, j, 'O')) return 2; // AI wins
				}
				if (board[i][j] == 'X')
				{
					if (evaluatePosition(board, i, j, 'X')) return 1; // Human wins;
				}
			}
		}
        return 0;
    }
    
    private static boolean evaluatePosition(char[][] board, int x, int y, char symbol) {
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;
           

            int dx = dir[0], dy = dir[1];
            int forward = countConsecutive(board, x, y, dx, dy, symbol);
            int backward = countConsecutive(board, x, y, -dx, -dy, symbol);
            count += forward + backward;

            if (count >= 5) {
                return true;
            }
    
        }
        return false;
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
    
    @Override
    public String getName() {
        return "Hard AI";
    }
}
