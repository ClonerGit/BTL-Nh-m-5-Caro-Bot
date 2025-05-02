package menu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;

public class MenuView {
    private Stage stage;
    private MenuController controller;

    private Button btnVsComputer;
    private Button btnVsPlayer;

    public MenuView(Stage stage, MenuController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);

        // Tiêu đề
        Label title = new Label("TIC TAC TOE");
        title.setFont(Font.font("Arial Black", 36));
        title.getStyleClass().add("title");

        // Icon caro
        Label icon = new Label("⭕❌⭕\n❌⭕❌\n⭕❌❌");
        icon.setFont(Font.font("Arial", 32));
        icon.getStyleClass().add("icon");

        // Nút 1: Người vs Máy
        btnVsComputer = new Button("  \uD83D\uDC64  VS  \uD83D\uDCBB  ");
        btnVsComputer.getStyleClass().add("menu-button");

        // Nút 2: Người vs Người
        btnVsPlayer = new Button("  \uD83D\uDC64  VS  \uD83D\uDC64  ");
        btnVsPlayer.getStyleClass().add("menu-button");

        // Responsive width
        btnVsComputer.maxWidthProperty().bind(root.widthProperty().subtract(80));
        btnVsPlayer.maxWidthProperty().bind(root.widthProperty().subtract(80));

        // Gán sự kiện cho nút
        btnVsComputer.setOnAction(e -> controller.onVsComputer());
        btnVsPlayer.setOnAction(e -> controller.onVsPlayer());

        root.getChildren().addAll(title, icon, btnVsComputer, btnVsPlayer);

        Scene scene = new Scene(root, 400, 700);
        scene.getStylesheets().add(getClass().getResource("/resources/dark-theme.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe Menu");
        stage.show();
    }
}
