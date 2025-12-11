package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public abstract class ChessPiece {
    private Position position;
    private final Color color;
    private MoveList validMoves;

    public ChessPiece(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return this.position;
    }

    /**
     * Override this function in the derived classes
     */
    public void setValidMoves(ChessPieceList piecesOnBoard) {
        this.validMoves = new MoveList();
    }

    public MoveList getValidMoves() {
        return this.validMoves;
    }

    public boolean validateMove(Position position) {
        return this.validMoves.contains(position);
    }
}
