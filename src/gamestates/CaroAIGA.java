package gamestates;

import algorithm.GAPlayer;
import algorithm.Chromosome;

public class CaroAIGA {

    private static final int SIZE = 15;

    public static int[] getMove(char[][] board) {
        GameState gameState = convertToGameState(board);
        GAPlayer gaPlayer = new GAPlayer(gameState, 'O');
        Chromosome bestMove = gaPlayer.run();

        if (bestMove != null) {
            return new int[]{bestMove.x, bestMove.y};
        }
        return null;
    }

    private static GameState convertToGameState(char[][] board) {
        GameState gameState = new GameState();
        int[][] state = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 'X') {
                    state[i][j] = 1;
                } else if (board[i][j] == 'O') {
                    state[i][j] = 2;
                } else {
                    state[i][j] = 0;
                }
            }
        }

        gameState.setBoard(state); 
        return gameState;
    }
}
