package menu;

import javafx.stage.Stage;
import ui.GameBoardView;

public class MenuController {
    private Stage stage;

    public MenuController(Stage stage) {
        this.stage = stage;
    }

    public void onVsComputer() {
        // TODO: Điều hướng sang giao diện chơi với AI (chưa làm)
    }

    public void onVsPlayer() {
        GameBoardView gameBoard = new GameBoardView(stage, false); // false = PVP
        gameBoard.show();
    }
}
