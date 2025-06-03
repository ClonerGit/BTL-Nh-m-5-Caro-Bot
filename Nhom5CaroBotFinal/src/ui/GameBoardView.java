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
import javafx.scene.control.ButtonType;
import gamestates.CaroBot;

public class GameBoardView {
    private Stage stage;
    private boolean vsAI;
    private String aiLevel; // "easy", "medium", "hard", or "easy-hard"
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
    private CaroBot currentAI;
    private CaroBot botX;
    private CaroBot botO;
    private boolean isBotVsBot = false;

    public GameBoardView(Stage stage, boolean vsAI, String aiLevel) {
        this.stage = stage;
        this.vsAI = vsAI;
        this.aiLevel = aiLevel;

        if (vsAI && aiLevel != null && aiLevel.contains("-")) {
            this.isBotVsBot = true;
        } else {
            this.isBotVsBot = false;
        }
    }


    public void show() {
        // Nút chức năng
        Button btnHome = new Button("🏠 Home");
        Button btnNewGame = new Button("🆕 New Game");
        Button btnHelp = new Button("❓ Help");
        Button btnUndo = new Button("↩️ Undo");

        btnHome.setPrefWidth(150);
        btnNewGame.setPrefWidth(150);
        btnHelp.setPrefWidth(150);
        btnUndo.setPrefWidth(150);

        btnHome.setStyle("-fx-background-color: #FF9800; -fx-text-fill: #2a0043; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnNewGame.setStyle("-fx-background-color: #ffbd03; -fx-text-fill: #2a0043; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnHelp.setStyle("-fx-background-color: #64B5F6; -fx-text-fill: #2a0043; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnUndo.setStyle("-fx-background-color: #fff176; -fx-text-fill: #2a0043; -fx-font-weight: bold; -fx-background-radius: 10;");

        btnHome.setTooltip(new javafx.scene.control.Tooltip("Quay lại menu chính"));
        btnNewGame.setTooltip(new javafx.scene.control.Tooltip("Bắt đầu ván mới"));
        btnHelp.setTooltip(new javafx.scene.control.Tooltip("Hướng dẫn chơi"));
        btnUndo.setTooltip(new javafx.scene.control.Tooltip("Hoàn tác nước đi trước"));

        btnHome.setOnAction(e -> {
            if (!gameEnded && !confirmReset()) return;
            menu.MenuController controller = new menu.MenuController(stage);
            menu.MenuView menuView = new menu.MenuView(stage, controller);
            menuView.show();
        });
        
        btnUndo.setOnAction(e -> undoLastMove());
        btnNewGame.setOnAction(e -> {
            if (gameEnded || confirmReset()) {
                resetBoard();
                if (isBotVsBot) playBotVsBot();
            }
        });

        btnHelp.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hướng dẫn chơi");
            alert.setHeaderText("Luật chơi Caro");
            alert.setContentText("• Hai người chơi lần lượt đánh X và O.\n• Ai có 5 quân liên tiếp (ngang/dọc/chéo) sẽ thắng.");
            alert.showAndWait();
        });

        HBox buttonBox = new HBox(18, btnHome, btnNewGame, btnHelp, btnUndo);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new javafx.geometry.Insets(14));
        buttonBox.setStyle("-fx-background-color: #fffbe6; -fx-background-radius: 18; -fx-border-color: #ffbd03; -fx-border-radius: 18;");

        
        
        String playerXName;
        if (isBotVsBot) {
            playerXName = "Bot X: " + aiLevel.split("-")[0];
        } else if (vsAI) {
            playerXName = "Người chơi X: Con người";
        } else {
            playerXName = "Người chơi X";
        }
        Label playerX= new Label(playerXName);
        playerX.setTextFill(Color.web("#FF5252"));
        playerX.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        scoreLabel = new Label("Tỉ số: " + scoreX + " - " + scoreO);
        scoreLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #ffbd03;");
        
        
        String playerOName;
        if (isBotVsBot) {
            playerOName = "Bot O: " + aiLevel.split("-")[1];
        } else if (vsAI) {
            playerOName = "Bot: " + aiLevel;
        } else {
            playerOName = "Người chơi O";
        }
        Label playerO = new Label(playerOName);
        playerO.setTextFill(Color.web("#64B5F6"));
        playerO.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");


        VBox rightBox = new VBox(18, playerX, scoreLabel, playerO);
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setPadding(new javafx.geometry.Insets(24));

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color:#2a0043;");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                StackPane cell = createCell(row, col);
                grid.add(cell, col, row);
                cells[row][col] = cell;
            }
        }

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(grid);
        mainLayout.setBottom(buttonBox);
        mainLayout.setRight(rightBox);
        mainLayout.setStyle("-fx-background-color: #2a0043;");
        mainLayout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(mainLayout, SIZE * CELL_SIZE + 180, SIZE * CELL_SIZE + 120);
        stage.setScene(scene);
        stage.setTitle("Caro Game - X/O");
        stage.show();

        if (isBotVsBot) {
            initBotPlayers();
            playBotVsBot();
        }
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

        Move last = moveHistory.pop();
        board[last.row][last.col] = '\0';
        Text cellText = (Text) cells[last.row][last.col].getChildren().get(0);
        cellText.setText("");
        cells[last.row][last.col].setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
        cells[last.row][last.col].setBackground(null);
        gameEnded = false;
        xTurn = (last.symbol == 'X');
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                cells[r][c].setBackground(null);
    }

    private boolean confirmReset() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận hành động");
        alert.setHeaderText("Bạn có chắc chắn?");
        alert.setContentText("Ván đang chơi sẽ bị hủy.");
        ButtonType yes = new ButtonType("Có");
        ButtonType no = new ButtonType("Không");
        alert.getButtonTypes().setAll(yes, no);
        return alert.showAndWait().orElse(no) == yes;
    }

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
        moveHistory.clear();
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
            if (gameEnded || board[row][col] != '\0' || isBotVsBot) return;

            char symbol = xTurn ? 'X' : 'O';
            board[row][col] = symbol;
            moveHistory.push(new Move(row, col, symbol));
            text.setText(String.valueOf(symbol));
            text.setFill(symbol == 'X' ? Color.web("#FF5252") : Color.web("#64B5F6"));

            if (lastMove != null)
                lastMove.setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
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

            if (vsAI && !isBotVsBot && !gameEnded && !xTurn) {
                makeAIMove();
            }
        });

        return cell;
    }

    private void makeAIMove() {
        if (currentAI == null) {
            switch (aiLevel.toLowerCase()) {
                case "easy": currentAI = new CaroAIEasy(); break;
                case "medium": currentAI = new CaroAIMedium(); break;
                case "hard": currentAI = new CaroAIHard(); break;
            }
        }

        int[] move = currentAI.getMove(board);
        if (move == null) return;

        int row = move[0], col = move[1];
        char symbol = 'O';
        board[row][col] = symbol;
        moveHistory.push(new Move(row, col, symbol));
        Text text = (Text) cells[row][col].getChildren().get(0);
        text.setText(String.valueOf(symbol));
        text.setFill(Color.web("#64B5F6"));

        if (lastMove != null)
            lastMove.setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
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

    private void initBotPlayers() {
        String[] levels = aiLevel.split("-");
        switch (levels[0]) {
            case "easy": botX = new CaroAIEasy(); break;
            case "medium": botX = new CaroAIMedium(); break;
            case "hard": botX = new CaroAIHard(); break;
        }
        switch (levels[1]) {
            case "easy": botO = new CaroAIEasy(); break;
            case "medium": botO = new CaroAIMedium(); break;
            case "hard": botO = new CaroAIHard(); break;
        }
    }

    private void playBotVsBot() {
        new Thread(() -> {
        	int center = SIZE / 2;
            javafx.application.Platform.runLater(() -> {
                if (!gameEnded && board[center][center] == '\0') {
                    int row = center, col = center;
                    char symbol = 'X';
                    board[row][col] = symbol;
                    moveHistory.push(new Move(row, col, symbol));
                    Text text = (Text) cells[row][col].getChildren().get(0);
                    text.setText(String.valueOf(symbol));
                    text.setFill(Color.web("#FF5252"));
                    cells[row][col].setStyle("-fx-border-color: white; -fx-border-width: 2;");
                    lastMove = cells[row][col];
                    xTurn = false;
                }
            });

            // Chờ một chút rồi bắt đầu lượt bot O
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!gameEnded) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                javafx.application.Platform.runLater(() -> {
                    if (gameEnded) return;

                    CaroBot currentBot = xTurn ? botX : botO;
                    int[] move = currentBot.getMove(board);
                    if (move == null) return;

                    int row = move[0], col = move[1];
                    char symbol = xTurn ? 'X' : 'O';
                    board[row][col] = symbol;
                    moveHistory.push(new Move(row, col, symbol));
                    Text text = (Text) cells[row][col].getChildren().get(0);
                    text.setText(String.valueOf(symbol));
                    text.setFill(symbol == 'X' ? Color.web("#FF5252") : Color.web("#64B5F6"));

                    if (lastMove != null)
                        lastMove.setStyle("-fx-border-color: gray; -fx-border-width: 0.5;");
                    cells[row][col].setStyle("-fx-border-color: white; -fx-border-width: 2;");
                    lastMove = cells[row][col];

                    if (BoardUtils.checkWin(board, row, col, symbol)) {
                        gameEnded = true;
                        BoardUtils.highlightWinningLine(board, cells, row, col, symbol);
                        showWinner(symbol);
                        if (symbol == 'X') scoreX++; else scoreO++;
                        scoreLabel.setText("Tỉ số: " + scoreX + " - " + scoreO);
                        return;
                    }

                    xTurn = !xTurn;
                });
            }
        }).start();
    }

    private void showWinner(char symbol) {
        WinnerDialog.show(stage, symbol, new WinnerDialog.WinnerDialogListener() {
            @Override
            public void onNewGame() {
                resetBoard();
                if (isBotVsBot) playBotVsBot();
            }

            @Override
            public void onHome() {
                if (!gameEnded && !confirmReset()) return;
                menu.MenuController controller = new menu.MenuController(stage);
                menu.MenuView menuView = new menu.MenuView(stage, controller);
                menuView.show();
            }
        });
    }
}
