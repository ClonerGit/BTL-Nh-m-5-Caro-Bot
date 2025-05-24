package algorithm;

import gamestates.GameState;
import gamestates.CaroAIHard;

public class Chromosome {
    public int x, y;
    public int fitness;

    public Chromosome(int x, int y) {
        this.x = x;
        this.y = y;
        this.fitness = 0;
    }
    

    public void evaluateFitness(GameState gameState, char aiSymbol, char opponentSymbol) {
        char[][] board = convertToCharBoard(gameState.getBoard());

        // Giả lập nước đi của AI
        if (board[x][y] != '\0') {
            this.fitness = Integer.MIN_VALUE; // không hợp lệ
            return;
        }

        board[x][y] = aiSymbol;
        int aiScore = CaroAIHard.evaluateBoard(board);

        // Giả lập nếu đối thủ đi vào đó → đánh giá nguy cơ
        board[x][y] = opponentSymbol;
        int opponentScore = CaroAIHard.evaluateBoard(board);

        // Đặt lại nước đi như cũ
        board[x][y] = '\0';

        // Fitness = điểm AI đạt được + phần thưởng nếu chặn được nước nguy hiểm của đối thủ
        this.fitness = aiScore + (opponentScore >= 8000 ? 5000 : 0); // bonus nếu chặn 4 liên tiếp
    }

    private char[][] convertToCharBoard(int[][] intBoard) {
        int size = intBoard.length;
        char[][] board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (intBoard[i][j] == 1) board[i][j] = 'X';
                else if (intBoard[i][j] == 2) board[i][j] = 'O';
                else board[i][j] = '\0';
            }
        }
        return board;
    }

    public Move getBestMove() {
        return new Move(x, y);
    }
}
