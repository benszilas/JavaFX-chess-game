package at.ac.hcw.chess;

public class ChessPieceMouseHandler {
    public static void attachHandlers(ChessPiece piece, ChessBoard board) {
        
        piece.setOnMousePressed(e -> { /* ... */ });
        piece.setOnMouseEntered(e -> { /* ... */ });
        piece.setOnMouseExited(e -> { /* ... */ });
    }

}
