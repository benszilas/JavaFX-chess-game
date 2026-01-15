package at.ac.hcw.chess;

import at.ac.hcw.chess.controller.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChessApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMaximized(true);
        stage.setResizable(false);

        MenuController menuController = new MenuController(stage);
        menuController.showMainMenu();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
