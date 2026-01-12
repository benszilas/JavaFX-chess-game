package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.view.GameMenuView;
import at.ac.hcw.chess.view.MainMenuView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class MenuController {
    private final Stage stage;
    private Scene mainMenuScene;
    private Scene gameMenuScene;

    public MenuController(Stage stage) {
        this.stage = stage;
    }

    public void showMainMenu() {
        if (mainMenuScene == null) {
            MainMenuView mainMenuView = new MainMenuView(this);
            Region root = mainMenuView.build();
            mainMenuScene = new Scene(root, 900, 900);
        }
        stage.setTitle("SCHACH");
        stage.setScene(mainMenuScene);
    }

    public void showGameMenu() {
        if (gameMenuScene == null) {
            GameMenuView gameMenuView = new GameMenuView(this);
            Region root = gameMenuView.build();
            gameMenuScene = new Scene(root, 900, 900);
        }
        stage.setTitle("SPIELMENU");
        stage.setScene(gameMenuScene);
    }

    public void startGame() {
        GameController gameController = new GameController();
        Region gameRoot = gameController.getView();
        Scene gameScene = new Scene(gameRoot, 900, 900);
        stage.setTitle("Chess");
        stage.setScene(gameScene);
    }

    public void exitApplication() {
        Platform.exit();
    }
}
