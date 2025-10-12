package at.ac.hcw.chess;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.Objects;

import static at.ac.hcw.chess.ChessBoardPrototype.SQUARE_SIZE;
import static at.ac.hcw.chess.ChessBoardPrototype.PLAYER;


public class ChessBoard extends GridPane {
    /**
     * STROKE_WIDTH should match the value in chess-board.css
     */ 
    private static final double STROKE_WIDTH = 3.0;
    
    private Rectangle[][] squares = new Rectangle[8][8];

    public ChessBoard() {
        this.setPrefSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        this.setMaxSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);
        this.setMinSize(8 * SQUARE_SIZE, 8 * SQUARE_SIZE);

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

                squares[row][col] = square;
                this.add(square, col, row);
            }
        }
    }

    /**
     * sets its player and adds it at the specified position
     * @param col starts at 0,0 in the top left corner
     * @param row starts at 0,0 in the top left corner
     * @param player {@link PLAYER}
     */
    public void addPiece(int col, int row, PLAYER player) {
        ChessPiece piece = new ChessPiece(player);
        piece.setChessBoard(this); // Set the board reference
        this.add(piece, col, row);
    }

    public void populateBoard() {
        System.out.println("Populating board");
        this.addPiece(7, 1, PLAYER.WHITE);
        this.addPiece(0, 7, PLAYER.BLACK);
    }
    
    /**
     * Get the square at the specified position
     * @param col column (0-7)
     * @param row row (0-7)
     * @return Rectangle representing the square, or null if out of bounds
     */
    public Rectangle getSquare(int col, int row) {
        if (col >= 0 && col < 8 && row >= 0 && row < 8) {
            return squares[row][col];
        }
        return null;
    }
    
    /**
     * Highlight a square with the specified style class
     * @param col column (0-7)
     * @param row row (0-7)
     * @param styleClass CSS class to add
     */
    public void highlightSquare(int col, int row, String styleClass) {
        Rectangle square = getSquare(col, row);
        if (square != null && !square.getStyleClass().contains(styleClass)) {
            square.getStyleClass().add(styleClass);
        }
    }
    
    /**
     * Remove highlight from a square
     * @param col column (0-7)
     * @param row row (0-7)
     * @param styleClass CSS class to remove
     */
    public void removeHighlight(int col, int row, String styleClass) {
        Rectangle square = getSquare(col, row);
        if (square != null) {
            square.getStyleClass().remove(styleClass);
        }
    }
    
    /**
     * Clear all highlights from a square (except base light/dark square classes)
     * @param col column (0-7)
     * @param row row (0-7)
     */
    public void clearSquareHighlights(int col, int row) {
        Rectangle square = getSquare(col, row);
        if (square != null) {
            square.getStyleClass().removeIf(styleClass -> 
                !styleClass.equals("light-square") && !styleClass.equals("dark-square"));
        }
    }
    
    /**
     * Clear all highlights from all squares
     */
    public void clearAllHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                clearSquareHighlights(col, row);
            }
        }
    }
}
