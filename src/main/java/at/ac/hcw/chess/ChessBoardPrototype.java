package at.ac.hcw.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChessBoardPrototype extends Application {
    /**
     * the side length of a single chess board square in pixels
     */
    public final static double SQUARE_SIZE = 50;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChessBoardPrototype.class.getResource("chess-board.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        stage.setTitle("Checkmate");
        stage.setScene(scene);
        stage.show();
    }
}
