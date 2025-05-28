package simulator;

import gamestates.CaroBot;

public class GameSimulator {
    private static final int SIZE = 15;

    public static void playMultipleMatches(CaroBot bot1, CaroBot bot2, int numMatches) {
        int bot1Wins = 0;
        int bot2Wins = 0;
        int draws = 0;

        for (int i = 0; i < numMatches; i++) {
            System.out.println("Match #" + (i + 1));
            Result result = playMatch(bot1, bot2);
            if (result == Result.BOT1_WIN) bot1Wins++;
            else if (result == Result.BOT2_WIN) bot2Wins++;
            else draws++;
        }

        System.out.println("\nFinal results after " + numMatches + " matches:");
        System.out.println(bot1.getName() + " wins: " + bot1Wins);
        System.out.println(bot2.getName() + " wins: " + bot2Wins);
        System.out.println("Draws: " + draws);
    }

    public enum Result {
        BOT1_WIN,
        BOT2_WIN,
        DRAW
    }

    public static Result playMatch(CaroBot bot1, CaroBot bot2) {
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
                return turn == 0 ? Result.BOT2_WIN : Result.BOT1_WIN;
            }

            board[move[0]][move[1]] = symbol;
            moves++;

            if (checkWin(board, move[0], move[1], symbol)) {
                System.out.println(currentBot.getName() + " wins in " + moves + " moves!");
                return turn == 0 ? Result.BOT1_WIN : Result.BOT2_WIN;
            }

            if (moves >= SIZE * SIZE) {
                System.out.println("Game ended in a draw.");
                return Result.DRAW;
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
