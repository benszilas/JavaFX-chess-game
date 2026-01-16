package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.GameEndedEvent;
import at.ac.hcw.chess.view.MenuView;
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
    private GameController gameController;

    public MenuController(Stage stage) {
        this.stage = stage;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.screenWidth = screenBounds.getWidth();
        this.screenHeight = screenBounds.getHeight();
        this.gameController = new GameController(this::exitApplication);
    }

    public void showMainMenu() {
        if (mainMenuScene == null) {
            MenuView mainMenuView = new MenuView(this, MenuView.MenuType.MAIN_MENU);
            Region root = mainMenuView.build();
            mainMenuScene = new Scene(root, screenWidth, screenHeight);
        }
        stage.setTitle("Main menu");
        stage.setScene(mainMenuScene);
    }

    public void showGameMenu() {
        gameController = new GameController(this::exitApplication);
        if (gameMenuScene == null) {
            MenuView gameMenuView = new MenuView(this, MenuView.MenuType.GAME_MENU);
            Region root = gameMenuView.build();
            gameMenuScene = new Scene(root, screenWidth, screenHeight);
        }
        stage.setTitle("Game menu");
        stage.setScene(gameMenuScene);
    }

    public void startGame() {
        Region gameRoot = gameController.getView();
        gameController.getBotMove();
        Scene gameScene = new Scene(gameRoot, screenWidth, screenHeight);
        stage.setTitle("Chess game");
        stage.setScene(gameScene);
        gameScene.addEventHandler(GameEndedEvent.GAME_ENDED, e-> showMainMenu());
    }

    public void selectGame(ChessPieceList selection) {
        if (selection == null) {
            gameController = new GameController(this::exitApplication);
        } else {
            gameController = new GameController(this::exitApplication, selection);
        }
    }

    public void addBot(boolean white, int depth) {
        Color color = (white) ? Color.WHITE : Color.BLACK;
        gameController.addBot(color, depth);
    }

    public void exitApplication() {
        Platform.exit();
    }
}
