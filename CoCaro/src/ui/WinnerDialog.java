package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WinnerDialog {
    public interface WinnerDialogListener {
        void onNewGame();
        void onHome();
    }

    public static void show(Stage owner, char winner, WinnerDialogListener listener) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        Label title = new Label("Congratulation!!!");
        title.setFont(Font.font("Arial Black", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #ffbd03; -fx-font-smoothing-type: gray;");

        Label winnerLabel = new Label("Người chơi " + winner + " thắng!");
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        winnerLabel.setStyle("-fx-text-fill: #fff; -fx-padding: 10 0 20 0; -fx-font-smoothing-type: gray;");

        Button btnNew = new Button("🆕 New");
        Button btnHome = new Button("🏠 Home");

        btnNew.setStyle("-fx-background-color: #ffbd03; -fx-text-fill: #2a0043; " +
                        "-fx-font-weight: bold; -fx-background-radius: 10;");
        btnHome.setStyle("-fx-background-color: #64B5F6; -fx-text-fill: #2a0043; " +
                         "-fx-font-weight: bold; -fx-background-radius: 10;");

        btnNew.setMinWidth(100);
        btnHome.setMinWidth(100);

        btnNew.setOnAction(e -> {
            dialog.close();
            if (listener != null) listener.onNewGame();
        });

        btnHome.setOnAction(e -> {
            dialog.close();
            if (listener != null) listener.onHome();
        });

        VBox root = new VBox(18, title, winnerLabel, btnNew, btnHome);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2a0043; -fx-border-color: #ffbd03; " +
                      "-fx-border-radius: 16; -fx-background-radius: 16; -fx-padding: 32;");

        Scene scene = new Scene(root, 320, 240);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        // Đặt dialog ở giữa màn hình game
        dialog.setOnShown(event -> {
            Window ownerWindow = dialog.getOwner();
            double centerXPosition = ownerWindow.getX() + ownerWindow.getWidth()/2;
            double centerYPosition = ownerWindow.getY() + ownerWindow.getHeight()/2;
            
            dialog.setX(centerXPosition - dialog.getWidth()/2);
            dialog.setY(centerYPosition - dialog.getHeight()/2);
        });
        
        dialog.showAndWait();
    }
}
