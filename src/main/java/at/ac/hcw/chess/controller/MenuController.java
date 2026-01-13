package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.GameEndedEvent;
import at.ac.hcw.chess.view.GameMenuView;
import at.ac.hcw.chess.view.MainMenuView;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MenuController {
    private final Stage stage;
    private Scene mainMenuScene;
    private Scene gameMenuScene;
    private final double screenWidth;
    private final double screenHeight;

    public MenuController(Stage stage) {
        this.stage = stage;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.screenWidth = screenBounds.getWidth();
        this.screenHeight = screenBounds.getHeight();
    }

    public void showMainMenu() {
        if (mainMenuScene == null) {
            MainMenuView mainMenuView = new MainMenuView(this);
            Region root = mainMenuView.build();
            mainMenuScene = new Scene(root, screenWidth, screenHeight);
        }
        stage.setTitle("SCHACH");
        stage.setScene(mainMenuScene);
    }

    public void showGameMenu() {
        if (gameMenuScene == null) {
            GameMenuView gameMenuView = new GameMenuView(this);
            Region root = gameMenuView.build();
            gameMenuScene = new Scene(root, screenWidth, screenHeight);
        }
        stage.setTitle("SPIELMENU");
        stage.setScene(gameMenuScene);
    }

    public void startGame(GameController gameController) {
        if (gameController == null)
            gameController = new GameController(this::exitApplication);
        Region gameRoot = gameController.getView();
        Scene gameScene = new Scene(gameRoot, screenWidth, screenHeight);
        stage.setTitle("Chess");
        stage.setScene(gameScene);
        gameScene.addEventHandler(GameEndedEvent.GAME_ENDED, e-> showMainMenu());
    }

    public GameController customGame(ChessPieceList customPieces, Color botColor, int botDepth) {
        GameController gameController;
        if (customPieces == null) {
            gameController = new GameController(this::exitApplication);
        } else {
            gameController = new GameController(this::exitApplication, customPieces);
        }

        if (botColor != null) {
            gameController.addBot(botColor, botDepth);
        }

        return gameController;
    }

    public void exitApplication() {
        Platform.exit();
    }
}
