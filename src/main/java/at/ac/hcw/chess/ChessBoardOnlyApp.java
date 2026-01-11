package at.ac.hcw.chess;

import at.ac.hcw.chess.controller.GameController;
import at.ac.hcw.chess.model.utils.PopulateBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ChessBoardOnlyApp extends Application {

    @Override
    public void start(Stage stage) {
        GameController controller = new GameController(PopulateBoard.noPawnsBoard());

        Region root = controller.getView();

        Scene scene = new Scene(root, 900, 900);
        stage.setTitle("Chess – GameView Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
