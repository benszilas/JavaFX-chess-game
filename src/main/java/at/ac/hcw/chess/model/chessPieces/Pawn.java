package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public class Pawn extends ChessPiece {
    private int rank = 1;

    public Pawn(Position position, Color color) {
        super(position, color);
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        this.possibleMoves = new MoveList();

        int direction = (this.color == Color.WHITE) ? 1 : -1;

        // Ein Feld vorwärts
        try {
            Position oneForward = new Position(position.getColumn(), position.getRow() + direction);
            if (piecesOnBoard.getPiece(oneForward) == null) {
                possibleMoves.add(oneForward);

                // Zwei Felder vorwärts (nur von Startposition)
                boolean isStartPosition = (this.color == Color.WHITE && position.getRow() == 2) ||
                        (this.color == Color.BLACK && position.getRow() == 7);
                if (isStartPosition) {
                    Position twoForward = new Position(position.getColumn(), position.getRow() + 2 * direction);
                    if (piecesOnBoard.getPiece(twoForward) == null) {
                        possibleMoves.add(twoForward);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ignored) {}

        // Diagonal schlagen (links)
        try {
            Position diagLeft = new Position(position.getColumn() - 1, position.getRow() + direction);
            ChessPiece target = piecesOnBoard.getPiece(diagLeft);
            if (target != null && target.getColor() != this.color) {
                possibleMoves.add(diagLeft);
            }
        } catch (IndexOutOfBoundsException ignored) {}

        // Diagonal schlagen (rechts)
        try {
            Position diagRight = new Position(position.getColumn() + 1, position.getRow() + direction);
            ChessPiece target = piecesOnBoard.getPiece(diagRight);
            if (target != null && target.getColor() != this.color) {
                possibleMoves.add(diagRight);
            }
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "♙";
        return "♟";
    }
}