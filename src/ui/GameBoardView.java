package ui;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import utils.BoardUtils;

public class GameBoardView {
    private Stage stage;
    private boolean vsAI; // true nếu chơi với máy
    private static final int SIZE = 15;
    private static final int CELL_SIZE = 40;
    private char[][] board = new char[SIZE][SIZE];
    private StackPane[][] cells = new StackPane[SIZE][SIZE];
    private StackPane lastMove = null;
    private boolean xTurn = true;
    private boolean gameEnded = false;

    public GameBoardView(Stage stage, boolean vsAI) {
        this.stage = stage;
        this.vsAI = vsAI;
    }

    public void show() {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color:#2a0043;");

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                StackPane cell = createCell(row, col);
                grid.add(cell, col, row);
                cells[row][col] = cell;
            }
        }

        Scene scene = new Scene(grid, SIZE * CELL_SIZE, SIZE * CELL_SIZE);
        scene.getStylesheets().add(getClass().getResource("/resources/dark-theme.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Caro Game - X/O");
        stage.show();
    }

    private StackPane createCell(int row, int col) {
        StackPane cell = new StackPane();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");

        Text text = new Text();
        text.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        text.setFill(Color.WHITE);

        cell.getChildren().add(text);
        cell.setOnMouseClicked((MouseEvent e) -> {
            if (gameEnded || board[row][col] != '\0') return;

            char symbol = xTurn ? 'X' : 'O';
            board[row][col] = symbol;
            text.setText(String.valueOf(symbol));
            text.setFill(symbol == 'X' ? Color.web("#FF5252") : Color.web("#64B5F6"));

            if (lastMove != null) {
                lastMove.setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
            }
            cell.setStyle("-fx-border-color: white; -fx-border-width: 2;");
            lastMove = cell;

            if (BoardUtils.checkWin(board, row, col, symbol)) {
                gameEnded = true;
                BoardUtils.highlightWinningLine(board, cells, row, col, symbol);
                showWinner(symbol);
            }

            xTurn = !xTurn;
        });

        return cell;
    }

    private void showWinner(char symbol) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kết thúc trận đấu");
        alert.setHeaderText(null);
        alert.setContentText("Người chơi " + symbol + " thắng!");
        alert.showAndWait();
    }
}
