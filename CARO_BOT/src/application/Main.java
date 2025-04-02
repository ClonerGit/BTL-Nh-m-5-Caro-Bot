package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    private static final int SIZE = 15; // Board size
    private static final int CELL_SIZE = 40; // Cell size
    private int[][] board = new int[SIZE][SIZE]; // 0: empty, 1: X, 2: O
    private boolean playerXTurn = true; // X starts first
    private boolean botFirst = false; // Determines if AI plays first
    
    private int lastMoveRow = -1, lastMoveCol = -1;


    @Override
    public void start(Stage primaryStage) {
        // Ask if AI should play first
        botFirst = askBotFirst();

        Canvas canvas = new Canvas(SIZE * CELL_SIZE, SIZE * CELL_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBoard(gc);

        // If AI starts first, make the first move
        if (botFirst) {
            playerXTurn = false;
            aiMove(gc); // AI plays as X
        }

        canvas.setOnMouseClicked(event -> handleMouseClick(event, gc));

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Project Nhap mon AI : Caro Game with AI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean askBotFirst() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose First Move");
        alert.setHeaderText("Do you want the AI to play first as X?");
        alert.setContentText("Press OK for AI first, Cancel for Player first.");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void drawBoard(GraphicsContext gc) {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, SIZE * CELL_SIZE, SIZE * CELL_SIZE);

        gc.setStroke(Color.BLACK);
        for (int i = 0; i <= SIZE; i++) {
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, SIZE * CELL_SIZE);
            gc.strokeLine(0, i * CELL_SIZE, SIZE * CELL_SIZE, i * CELL_SIZE);
        }
    }

    private void handleMouseClick(MouseEvent event, GraphicsContext gc) {
        if (!playerXTurn) return; // Wait for AI

        int col = (int) (event.getX() / CELL_SIZE);
        int row = (int) (event.getY() / CELL_SIZE);

        if (col >= SIZE || row >= SIZE || board[row][col] != 0) {
            return; // Ignore invalid moves
        }

        board[row][col] = botFirst ? 2 : 1; // Player's move
        drawMove(gc, row, col, !botFirst); // Draw X if botFirst, otherwise O
        playerXTurn = false;

        lastMoveRow = row;  // Track last move
        lastMoveCol = col;

        if (checkWin(row, col)) {
            System.out.println("Player Wins!");
            return;
        }

        aiMove(gc); // AI plays next
    }


    private void aiMove(GraphicsContext gc) {
        int row, col;

        // AI's first move should be at the center
        if (lastMoveRow == -1 && lastMoveCol == -1) {
            row = SIZE / 2;
            col = SIZE / 2;
        } else {
            int[] move = findBestMove();
            if (move == null) return;
            row = move[0];
            col = move[1];
        }

        board[row][col] = botFirst ? 1 : 2; // AI's move (X if botFirst, O otherwise)
        drawMove(gc, row, col, botFirst); // Draw X if botFirst, otherwise O

        lastMoveRow = row;  // Update last move
        lastMoveCol = col;

        if (checkWin(row, col)) {
            System.out.println("AI Wins!");
        }

        playerXTurn = true; // Player's turn
    }


    private int[] findBestMove() {
        int bestScore = -1;
        int[] bestMove = null;

        int searchRadius = 2; // Only check nearby cells

        for (int r = Math.max(0, lastMoveRow - searchRadius); r <= Math.min(SIZE - 1, lastMoveRow + searchRadius); r++) {
            for (int c = Math.max(0, lastMoveCol - searchRadius); c <= Math.min(SIZE - 1, lastMoveCol + searchRadius); c++) {
                if (board[r][c] == 0) {
                    int aiThreat = evaluateThreat(r, c, botFirst ? 1 : 2);  // AI's move score
                    int playerThreat = evaluateThreat(r, c, botFirst ? 2 : 1);  // Player's threat score

                    int score = Math.max(aiThreat, playerThreat); // Take the highest value
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{r, c};
                    }
                }
            }
        }
        return bestMove;
    }

    private int evaluateThreat(int row, int col, int player) {
        int threatScore = 0;

        // Check all four directions
        threatScore += checkThreat(row, col, 1, 0, player);  // Horizontal
        threatScore += checkThreat(row, col, 0, 1, player);  // Vertical
        threatScore += checkThreat(row, col, 1, 1, player);  // Diagonal \
        threatScore += checkThreat(row, col, 1, -1, player); // Diagonal /

        return threatScore;
    }

    private int checkThreat(int row, int col, int dRow, int dCol, int player) {
        int count = 0, openEnds = 0;

        // Count forward
        for (int i = 1; i < 5; i++) {
            int newRow = row + i * dRow, newCol = col + i * dCol;
            if (isValid(newRow, newCol) && board[newRow][newCol] == player) {
                count++;
            } else if (isValid(newRow, newCol) && board[newRow][newCol] == 0) {
                openEnds++;
                break;
            } else {
                break;
            }
        }

        // Count backward
        for (int i = 1; i < 5; i++) {
            int newRow = row - i * dRow, newCol = col - i * dCol;
            if (isValid(newRow, newCol) && board[newRow][newCol] == player) {
                count++;
            } else if (isValid(newRow, newCol) && board[newRow][newCol] == 0) {
                openEnds++;
                break;
            } else {
                break;
            }
        }

        // Score threats: open-ended moves are more valuable
        if (count >= 4) return (openEnds == 2) ? 10000 : 5000;  // Win or block win
        if (count == 3) return (openEnds == 2) ? 500 : 250;  // Open three
        if (count == 2) return (openEnds == 2) ? 100 : 50;   // Open two
        return 0;
    }

    private void drawMove(GraphicsContext gc, int row, int col, boolean isX) {
        double x = col * CELL_SIZE + CELL_SIZE / 2;
        double y = row * CELL_SIZE + CELL_SIZE / 2;
        double radius = CELL_SIZE / 3;

        gc.setStroke(Color.BLACK);
        if (isX) {
            gc.setStroke(Color.RED);
            gc.strokeLine(x - radius, y - radius, x + radius, y + radius);
            gc.strokeLine(x + radius, y - radius, x - radius, y + radius);
        } else {
            gc.setStroke(Color.BLUE);
            gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
        }
    }
    
    private boolean checkWin(int row, int col) {
        int player = board[row][col];
        return checkDirection(row, col, 1, 0, player) || // Horizontal
               checkDirection(row, col, 0, 1, player) || // Vertical
               checkDirection(row, col, 1, 1, player) || // Diagonal \
               checkDirection(row, col, 1, -1, player);  // Diagonal /
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, int player) {
        int count = 1;

        for (int i = 1; i < 5; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;
            if (isValid(newRow, newCol) && board[newRow][newCol] == player) {
                count++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 5; i++) {
            int newRow = row - i * dRow;
            int newCol = col - i * dCol;
            if (isValid(newRow, newCol) && board[newRow][newCol] == player) {
                count++;
            } else {
                break;
            }
        }

        return count >= 5;
    }


    private boolean isValid(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
