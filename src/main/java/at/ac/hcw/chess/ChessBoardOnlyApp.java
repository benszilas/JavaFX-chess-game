package at.ac.hcw.chess;

import at.ac.hcw.chess.controller.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ChessBoardOnlyApp extends Application {

    @Override
    public void start(Stage stage) {
        MenuController menuController = new MenuController(stage);
        menuController.showMainMenu();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
