package at.ac.hcw.chess;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.swing.text.Position;

import static at.ac.hcw.chess.ChessBoardPrototype.SQUARE_SIZE;

public class ChessPiece extends Circle {
    private final ChessBoardPrototype.PLAYER _player;
    private int _column;
    private int _row;
    private ChessBoard _chessBoard; // Reference to the board for square manipulation

    public ChessPiece(ChessBoardPrototype.PLAYER player) {
        this._player = player;
        this.setRadius(SQUARE_SIZE / 2);

        if (player == ChessBoardPrototype.PLAYER.WHITE) {
            this.setFill(Color.BLUE);
        } else {
            this.setFill(Color.RED);
        }
        
        // Add CSS class for styling
        this.getStyleClass().add("chess-piece");
        
        // Set up mouse handlers
        setupMouseHandlers();
    }
    
    /**
     * Set the reference to the chess board (called when piece is added to board)
     * @param chessBoard the board this piece belongs to
     */
    public void setChessBoard(ChessBoard chessBoard) {
        this._chessBoard = chessBoard;
    }

    public ChessBoardPrototype.PLAYER getPlayer() {
        return this._player;
    }

    private boolean allowedStep(int rowDelta, int columnDelta) {
        boolean allowed = true;
        this.updatePosition();
        Point2D newPosition = new Point2D(this._column + columnDelta, this._row + rowDelta);
        //out of bounds check
        if (newPosition.getX() < 0 || newPosition.getY() < 0
        || newPosition.getX() > 7 || newPosition.getY() > 7) {
            allowed = false;
        }
        return allowed;
    }

    public void updatePosition() {
        this.updateRow();
        this.updateColumn();
    }

    public void updateRow() {
        Integer row = GridPane.getRowIndex(this);
        this._row = (row != null) ? row : 0;
    }

    public void updateColumn() {
        Integer col = GridPane.getColumnIndex(this);
        this._column = (col != null) ? col : 0;
    }

    public int getColumn() {
        this.updateColumn();
        return this._column;
    }

    public int getRow() {
        this.updateRow();
        return this._row;
    }

    public void logInfo() {
        this.updatePosition();
        System.out.println("Piece clicked:");
        System.out.println("Position: (row: " + this.getRow() + ",col: " + this.getColumn() + ")");
        System.out.println("Player: " + this.getPlayer());
        System.out.println("---");
    }
    
    /**
     * Sets up all mouse handlers for this piece
     */
    private void setupMouseHandlers() {
        // Mouse pressed handler
        this.setOnMousePressed(event -> {
            logInfo();
            checkAllDirections();
            highlightCurrentSquare();
        });
        
        // Mouse entered (hover start) handler
        this.setOnMouseEntered(event -> {
            onHoverStart();
        });
        
        // Mouse exited (hover end) handler
        this.setOnMouseExited(event -> {
            onHoverEnd();
        });
    }
    
    /**
     * Called when mouse starts hovering over this piece
     */
    private void onHoverStart() {
        if (_chessBoard != null) {
            updatePosition();
            // Highlight the current square
            _chessBoard.highlightSquare(_column, _row, "square-selected");
            
            // Optionally highlight possible moves
            highlightPossibleMoves();
        }
    }
    
    /**
     * Called when mouse stops hovering over this piece
     */
    private void onHoverEnd() {
        if (_chessBoard != null) {
            updatePosition();
            // Remove highlight from current square
            _chessBoard.removeHighlight(_column, _row, "square-selected");
            
            // Clear possible move highlights
            clearPossibleMoveHighlights();
        }
    }
    
    /**
     * Highlight the current square when piece is clicked
     */
    private void highlightCurrentSquare() {
        if (_chessBoard != null) {
            updatePosition();
            _chessBoard.highlightSquare(_column, _row, "square-last-move");
        }
    }
    
    /**
     * Highlight all possible moves for this piece
     */
    private void highlightPossibleMoves() {
        if (_chessBoard == null) return;
        
        updatePosition();
        
        // Define the 8 directions
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };
        
        for (int[] direction : directions) {
            int rowDelta = direction[0];
            int columnDelta = direction[1];
            
            if (allowedStep(rowDelta, columnDelta)) {
                int targetCol = _column + columnDelta;
                int targetRow = _row + rowDelta;
                _chessBoard.highlightSquare(targetCol, targetRow, "square-possible-move");
            }
        }
    }
    
    /**
     * Clear highlights for possible moves
     */
    private void clearPossibleMoveHighlights() {
        if (_chessBoard == null) return;
        
        // Clear all possible move highlights from the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                _chessBoard.removeHighlight(col, row, "square-possible-move");
            }
        }
    }
    
    /**
     * debug function just to see if it works
     * <p>
     * Checks all 8 directions from current position and prints allowed/not allowed moves
     */
    private void checkAllDirections() {
        System.out.println("Checking moves in all 8 directions:");
        
        // Define the 8 directions: up, down, left, right, and 4 diagonals
        int[][] directions = {
            {-1, 0},  // up
            {1, 0},   // down
            {0, -1},  // left
            {0, 1},   // right
            {-1, -1}, // up-left diagonal
            {-1, 1},  // up-right diagonal
            {1, -1},  // down-left diagonal
            {1, 1}    // down-right diagonal
        };
        
        String[] directionNames = {
            "up", "down", "left", "right",
            "up-left", "up-right", "down-left", "down-right"
        };
        
        for (int i = 0; i < directions.length; i++) {
            int rowDelta = directions[i][0];
            int columnDelta = directions[i][1];
            boolean allowed = allowedStep(rowDelta, columnDelta);
            
            System.out.println(directionNames[i] + ": " + (allowed ? "ALLOWED" : "NOT ALLOWED"));
        }
        System.out.println("---");
    }
}
