package at.ac.hcw.chess;

import at.ac.hcw.chess.controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * code adapted from <a href=https://www.pragmaticcoding.ca/beginners/part2>tutorial</a>
 */
public class ChessBoardOnlyApp extends Application {
    private GameController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent(),
                Screen.getPrimary().getBounds().getWidth() - 100,
                Screen.getPrimary().getBounds().getHeight() - 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Region createContent() {
        this.controller = new GameController();
        return controller.getView();
    }
}

