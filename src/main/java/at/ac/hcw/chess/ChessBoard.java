package at.ac.hcw.chess;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.Objects;

import static at.ac.hcw.chess.ChessBoardPrototype.SQUARE_SIZE;


public class ChessBoard extends GridPane {
    /**
     * STROKE_WIDTH should match the value in chess-board.css
     */ 
    private static final double STROKE_WIDTH = 3.0;

    public ChessBoard() {
        this.setPrefSize( 8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        this.setMaxSize( 8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        this.setMinSize( 8 * SQUARE_SIZE, 8 * SQUARE_SIZE);

        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("chess-board.css")).toExternalForm());

        double effectiveSquareSize = SQUARE_SIZE - STROKE_WIDTH;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = new Rectangle(effectiveSquareSize, effectiveSquareSize);

                // even squares are white
                if ((row + col) % 2 == 0) {
                    square.getStyleClass().add("light-square");
                } else {
                    square.getStyleClass().add("dark-square");
                }
                
                this.add(square, col, row);
            }
        }
    }

    public void populateBoard() {
        System.out.println("Populating board");
        Circle piece = new Circle();
        piece.setFill(Color.BLUE);
        piece.setRadius(SQUARE_SIZE / 2);
        this.add(piece, 7, 1);
    }
}
