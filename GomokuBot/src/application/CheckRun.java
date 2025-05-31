package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CheckRun extends Application {
    private static final int TILE_SIZE = 40;
    private static final int BOARD_SIZE = 15;
    private final Board board = new Board();
    private GraphicsContext gc;

    private boolean playerStarts = true; // người chơi mặc định đi trước

    @Override
    public void start(Stage primaryStage) {
        // Hộp thoại chọn lượt chơi
        Alert chooseTurn = new Alert(AlertType.CONFIRMATION);
        chooseTurn.setTitle("Chọn lượt đi");
        chooseTurn.setHeaderText("Bạn muốn đi trước hay để máy đi trước?");
        ButtonType playerFirst = new ButtonType("Người chơi");
        ButtonType aiFirst = new ButtonType("Máy");
        chooseTurn.getButtonTypes().setAll(playerFirst, aiFirst);

        ButtonType result = chooseTurn.showAndWait().orElse(playerFirst);
        playerStarts = (result == playerFirst);

        // Thiết lập giao diện
        Canvas canvas = new Canvas(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
        gc = canvas.getGraphicsContext2D();
        drawBoard();

        // Nếu máy đi trước thì đi luôn
        if (!playerStarts) {
            int[] aiMove = GomokuAIHard.getMove(board);
            if (aiMove != null) {
                board.placeMove(aiMove[0], aiMove[1], 2);
                drawBoard();
            }
        }

        // Xử lý khi người chơi click
        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / TILE_SIZE);
            int y = (int) (e.getY() / TILE_SIZE);

            if (board.placeMove(x, y, 1)) {
                drawBoard();
                int winner = board.checkWinner();
                if (winner != 0) {
                    showWinner(winner);
                    return;
                }

                int[] aiMove = GomokuAIHard.getMove(board);
                if (aiMove != null) {
                    board.placeMove(aiMove[0], aiMove[1], 2);
                    drawBoard();

                    winner = board.checkWinner();
                    if (winner != 0) {
                        showWinner(winner);
                    }
                }
            }
        });

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Gomoku - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawBoard() {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);

        gc.setStroke(Color.BLACK);
        for (int i = 0; i < BOARD_SIZE; i++) {
            gc.strokeLine(i * TILE_SIZE + TILE_SIZE / 2, TILE_SIZE / 2,
                          i * TILE_SIZE + TILE_SIZE / 2, TILE_SIZE * BOARD_SIZE - TILE_SIZE / 2);
            gc.strokeLine(TILE_SIZE / 2, i * TILE_SIZE + TILE_SIZE / 2,
                          TILE_SIZE * BOARD_SIZE - TILE_SIZE / 2, i * TILE_SIZE + TILE_SIZE / 2);
        }

        int[][] grid = board.getBoard();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                int val = grid[x][y];
                if (val == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillOval(x * TILE_SIZE + 10, y * TILE_SIZE + 10, 20, 20);
                } else if (val == 2) {
                    gc.setFill(Color.WHITE);
                    gc.fillOval(x * TILE_SIZE + 10, y * TILE_SIZE + 10, 20, 20);
                }
            }
        }
    }

    private void showWinner(int player) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Kết quả");
        alert.setHeaderText(null);
        alert.setContentText((player == 1 ? "Người chơi" : "Máy") + " thắng!");
        alert.showAndWait();

        board.reset();  // Reset bàn cờ
        drawBoard();

        // Nếu máy được chọn đi trước thì đi luôn sau khi reset
        if (!playerStarts) {
            int[] aiMove = GomokuAIMedium.getMove(board);
            if (aiMove != null) {
                board.placeMove(aiMove[0], aiMove[1], 2);
                drawBoard();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/// ctrl F để tùy chọn AIEasy,AIMedium