package menu;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.control.Dialog;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import ui.GameBoardView;

public class MenuController {
    private Stage stage;

    public MenuController(Stage stage) {
        this.stage = stage;
    }

    public void onVsComputer() {
        Dialog<String> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
            "-fx-background-color: #2a0043; " +
            "-fx-border-color: #ffbd03; " +
            "-fx-border-radius: 16; " +
            "-fx-background-radius: 16; " +
            "-fx-padding: 32;"
        );

        Text title = new Text("Chọn độ khó");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setFill(Color.web("#ffbd03"));

        Text description = new Text("Chọn độ khó cho máy:");
        description.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        description.setFill(Color.WHITE);

        Button easyBtn = createDifficultyButton("Dễ", "#4CAF50");
        Button mediumBtn = createDifficultyButton("Trung bình", "#FFC107");
        Button hardBtn = createDifficultyButton("Khó", "#F44336"); 
        Button cancelBtn = createDifficultyButton("Hủy", "#9E9E9E");

        HBox row1 = new HBox(15, easyBtn, mediumBtn, hardBtn);
        HBox row2 = new HBox(15, cancelBtn);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(10, row1, row2);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, description, buttonBox);

        easyBtn.setOnAction(e -> {
            dialog.setResult("easy");
            dialog.close();
        });

        mediumBtn.setOnAction(e -> {
            dialog.setResult("medium");
            dialog.close();
        });

        hardBtn.setOnAction(e -> {
            dialog.setResult("hard");
            dialog.close();
        });

        cancelBtn.setOnAction(e -> {
            dialog.setResult("cancel");
            dialog.close();
        });

        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);

        dialog.showAndWait().ifPresent(result -> {
            if ("easy".equals(result) || "medium".equals(result) || "hard".equals(result)) {
                GameBoardView gameBoard = new GameBoardView(stage, true, result);
                gameBoard.show();
            } else {
                MenuView menuView = new MenuView(stage, this);
                menuView.show();
            }
        });
    }

    private Button createDifficultyButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 10 20;"
        );
        button.setMinWidth(100);
        return button;
    }

    public void onVsPlayer() {
        GameBoardView gameBoard = new GameBoardView(stage, false, null);
        gameBoard.show();
    }

    public void onBotVsBot() {
        Dialog<String> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
            "-fx-background-color: #2a0043; " +
            "-fx-border-color: #ffbd03; " +
            "-fx-border-radius: 16; " +
            "-fx-background-radius: 16; " +
            "-fx-padding: 32;"
        );

        Text title = new Text("Chọn trận đấu AI vs AI");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setFill(Color.web("#ffbd03"));

        Text description = new Text("Chọn cặp AI:");
        description.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        description.setFill(Color.WHITE);

        Button btnHardVsEasy = createDifficultyButton("Hard vs Easy", "#E91E63");
        Button btnEasyVsMedium = createDifficultyButton("Easy vs Medium", "#3F51B5");
        Button btnMediumVsHard = createDifficultyButton("Medium vs Hard", "#009688");
        Button cancelBtn = createDifficultyButton("Hủy", "#9E9E9E");

        HBox row1 = new HBox(15, btnHardVsEasy, btnEasyVsMedium, btnMediumVsHard);
        HBox row2 = new HBox(15, cancelBtn);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(10, row1, row2);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, description, buttonBox);

        btnHardVsEasy.setOnAction(e -> {
            dialog.setResult("hard-easy");
            dialog.close();
        });

        btnEasyVsMedium.setOnAction(e -> {
            dialog.setResult("easy-medium");
            dialog.close();
        });

        btnMediumVsHard.setOnAction(e -> {
            dialog.setResult("medium-hard");
            dialog.close();
        });

        cancelBtn.setOnAction(e -> {
            dialog.setResult("cancel");
            dialog.close();
        });

        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);

        dialog.showAndWait().ifPresent(result -> {
            if (!"cancel".equals(result)) {
                GameBoardView gameBoard = new GameBoardView(stage, true, result); // truyền chuỗi định danh độ khó đôi
                gameBoard.show();
            } else {
                MenuView menuView = new MenuView(stage, this);
                menuView.show();
            }
        });
    }


}
