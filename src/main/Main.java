package main;

import javafx.application.Application;
import javafx.stage.Stage;
import menu.MenuView;
import menu.MenuController;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MenuController controller = new MenuController(stage);
        MenuView menu = new MenuView(stage, controller);
        menu.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}