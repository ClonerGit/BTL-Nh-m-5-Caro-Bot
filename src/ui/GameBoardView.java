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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import java.util.Stack;
import gamestates.CaroAIEasy;
import gamestates.CaroAIMedium;
import gamestates.CaroAIHard;

public class GameBoardView {
    private Stage stage;
    private boolean vsAI;
    private String aiLevel; // "easy", "medium", "hard"
    private static final int SIZE = 15;
    private static final int CELL_SIZE = 40;
    private char[][] board = new char[SIZE][SIZE];
    private StackPane[][] cells = new StackPane[SIZE][SIZE];
    private StackPane lastMove = null;
    private boolean xTurn = true;
    private boolean gameEnded = false;
    private int scoreX = 0;
    private int scoreO = 0;
    private Label scoreLabel;
    private Stack<Move> moveHistory = new Stack<>();
    
    public GameBoardView(Stage stage, boolean vsAI, String aiLevel) {
        this.stage = stage;
        this.vsAI = vsAI;
        this.aiLevel = aiLevel;
    }


    public void show() {
        // Tạo các nút chức năng 
        Button btnNewGame = new Button("🆕 New Game");
        Button btnHelp = new Button("❓ Help");
        Button btnUndo = new Button("↩️ Undo");

        btnNewGame.setPrefWidth(150); // Đặt chiều rộng ưu tiên giống nhau
        btnHelp.setPrefWidth(150);
        btnUndo.setPrefWidth(150);
        
        btnNewGame.setStyle("-fx-background-color: #ffbd03; -fx-text-fill: #2a0043; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnHelp.setStyle("-fx-background-color: #64B5F6; -fx-text-fill: #2a0043; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnUndo.setStyle("-fx-background-color: #fff176; -fx-text-fill: #2a0043; -fx-font-weight: bold; -fx-background-radius: 10;");

        btnNewGame.setTooltip(new javafx.scene.control.Tooltip("Bắt đầu ván mới"));
        btnHelp.setTooltip(new javafx.scene.control.Tooltip("Hướng dẫn chơi"));
        btnUndo.setTooltip(new javafx.scene.control.Tooltip("Hoàn tác nước đi trước"));

        btnUndo.setOnAction(e -> undoLastMove());
        btnNewGame.setOnAction(e -> resetBoard());
        btnHelp.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hướng dẫn chơi");
            alert.setHeaderText("Luật chơi Caro");
            alert.setContentText(
                "• Hai người chơi lần lượt đánh X và O.\n" +
                "• Ai có 5 quân liên tiếp (ngang/dọc/chéo) sẽ thắng.\n"
            );
            alert.showAndWait();
        });

        HBox buttonBox = new HBox(18, btnNewGame, btnHelp, btnUndo);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new javafx.geometry.Insets(14));
        buttonBox.setStyle(
            "-fx-background-color: #fffbe6; " +
            "-fx-background-radius: 18; " +
            "-fx-border-color: #ffbd03; " +
            "-fx-border-radius: 18;"
        );

        // Avatar và tên người chơi bên phải 
        Label playerX = new Label("Người chơi X");
        playerX.setTextFill(Color.web("#FF5252"));
        playerX.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        scoreLabel = new Label("Tỉ số: " + scoreX + " - " + scoreO);
        scoreLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #ffbd03;");

        Label playerO = new Label("Người chơi O");
        playerO.setTextFill(Color.web("#64B5F6"));
        playerO.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        VBox rightBox = new VBox(18, playerX, scoreLabel, playerO);
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setPadding(new javafx.geometry.Insets(24));

        // Bàn cờ
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color:#2a0043;");

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                StackPane cell = createCell(row, col);
                grid.add(cell, col, row);
                cells[row][col] = cell;
            }
        }

        // Layout tổng 
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(grid);            // Bàn cờ ở giữa
        mainLayout.setBottom(buttonBox);       // 3 nút ở dưới
        mainLayout.setRight(rightBox);         // Avatar, tên, tỉ số bên phải
        mainLayout.setStyle("-fx-background-color: #2a0043;");
        mainLayout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(mainLayout, SIZE * CELL_SIZE + 180, SIZE * CELL_SIZE + 120);
        stage.setScene(scene);
        stage.setTitle("Caro Game - X/O");
        stage.show();
    }

    private static class Move {
        int row, col;
        char symbol;
        public Move(int row, int col, char symbol) {
            this.row = row;
            this.col = col;
            this.symbol = symbol;
        }
    }

    private void undoLastMove() {
        if (moveHistory.isEmpty() || gameEnded) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Không thể hoàn tác!");
            alert.showAndWait();
            return;
        }

        // Lấy nước đi cuối cùng
        Move last = moveHistory.pop();
        board[last.row][last.col] = '\0';

        // Xóa text trên ô
        Text cellText = (Text) cells[last.row][last.col].getChildren().get(0);
        cellText.setText("");
        cells[last.row][last.col].setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
        cells[last.row][last.col].setBackground(null);

        // Nếu vừa undo một ván đã thắng, bỏ trạng thái thắng
        gameEnded = false;

        // Đổi lượt lại cho đúng
        xTurn = (last.symbol == 'X');

        // Reset lại toàn bộ background
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                cells[r][c].setBackground(null);
    }

    // Reset bàn cờ về trạng thái ban đầu
    private void resetBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = '\0';
                ((Text) cells[row][col].getChildren().get(0)).setText("");
                cells[row][col].setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
                cells[row][col].setBackground(null); 
            }
        }
        xTurn = true;
        gameEnded = false;
        lastMove = null;
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
            moveHistory.push(new Move(row, col, symbol));
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
                if (symbol == 'X') scoreX++; else scoreO++;
                scoreLabel.setText("Tỉ số: " + scoreX + " - " + scoreO);
                return;
            }


            xTurn = !xTurn;

            // Nếu là chế độ Người vs Máy và đến lượt O (máy), cho máy đi
            if (vsAI && !gameEnded && !xTurn) {
                makeAIMove();
            }
        });


        return cell;
    }
    private void makeAIMove() {
        int[] move = null;
        switch (aiLevel.toLowerCase()) {
            case "easy":
                move = CaroAIEasy.getMove(board);
                break;
            case "medium":
                move = CaroAIMedium.getMove(board);
                break;
            case "hard":
                move = CaroAIHard.getMove(board);
                break;
        }

        if (move == null) return;

        int row = move[0], col = move[1];
        char symbol = 'O';
        board[row][col] = symbol;
        moveHistory.push(new Move(row, col, symbol));
        Text text = (Text) cells[row][col].getChildren().get(0);
        text.setText(String.valueOf(symbol));
        text.setFill(Color.web("#64B5F6"));

        if (lastMove != null) {
            lastMove.setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
        }
        cells[row][col].setStyle("-fx-border-color: white; -fx-border-width: 2;");
        lastMove = cells[row][col];

        if (BoardUtils.checkWin(board, row, col, symbol)) {
            gameEnded = true;
            BoardUtils.highlightWinningLine(board, cells, row, col, symbol);
            showWinner(symbol);
            scoreO++;
            scoreLabel.setText("Tỉ số: " + scoreX + " - " + scoreO);
            return;
        }

        xTurn = !xTurn;
    }


    private void showWinner(char symbol) {
        WinnerDialog.show(stage, symbol, new WinnerDialog.WinnerDialogListener() {
            @Override
            public void onNewGame() {
                resetBoard();
            }

            @Override
            public void onHome() {
                // Quay về menu
                menu.MenuController controller = new menu.MenuController(stage);
                menu.MenuView menuView = new menu.MenuView(stage, controller);
                menuView.show();
            }
        });
    }

    
}
