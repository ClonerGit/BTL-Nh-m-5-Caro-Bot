package simulator;

import gamestates.CaroBot;

public class GameSimulator {
    private static final int SIZE = 15;

    public static void playMatch(CaroBot bot1, CaroBot bot2) {
        char[][] board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '.';
            }
        }

        CaroBot[] players = { bot1, bot2 };
        char[] symbols = { 'X', 'O' };

        int turn = 0;
        int moves = 0;
        while (true) {
            CaroBot currentBot = players[turn];
            char symbol = symbols[turn];

            int[] move = currentBot.getMove(board);

            if (move == null || move.length != 2 ||
                !inBounds(move[0], move[1]) || board[move[0]][move[1]] != '.') {
                System.out.println(currentBot.getName() + " made invalid move. " +
                                   players[1 - turn].getName() + " wins!");
                break;
            }

            board[move[0]][move[1]] = symbol;
            moves++;

            if (checkWin(board, move[0], move[1], symbol)) {
                System.out.println(currentBot.getName() + " wins in " + moves + " moves!");
                break;
            }

            if (moves >= SIZE * SIZE) {
                System.out.println("Game ended in a draw.");
                break;
            }

            turn = 1 - turn;
        }
    }

    private static boolean checkWin(char[][] board, int x, int y, char symbol) {
        int[] dx = {1, 0, 1, 1};
        int[] dy = {0, 1, 1, -1};

        for (int d = 0; d < 4; d++) {
            int count = 1;
            for (int dir = -1; dir <= 1; dir += 2) {
                int nx = x + dir * dx[d];
                int ny = y + dir * dy[d];
                while (inBounds(nx, ny) && board[nx][ny] == symbol) {
                    count++;
                    nx += dir * dx[d];
                    ny += dir * dy[d];
                }
            }
            if (count >= 5) return true;
        }
        return false;
    }

    private static boolean inBounds(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }
}
