package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public class Pawn extends ChessPiece {
    private final int targetRank;
    private final int direction;

    public Pawn(Position position, Color color) {
        super(position, color);
        targetRank = (color == Color.WHITE) ? 8 : 1;
        direction = (this.color == Color.WHITE) ? 1 : -1;
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        this.possibleMoves = new MoveList();

        try {
            Position oneForward = new Position(position.getColumn(), position.getRow() + direction);
            if (piecesOnBoard.getPiece(oneForward) == null) {
                possibleMoves.add(oneForward);

                if (!this.hasMoved()) {
                    Position twoForward = new Position(position.getColumn(), position.getRow() + 2 * direction);
                    if (piecesOnBoard.getPiece(twoForward) == null) {
                        possibleMoves.add(twoForward);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        for (int side : new int[]{1, -1}) {
            try {
                Position diagonal = new Position(position.getColumn() + side, position.getRow() + direction);
                possibleMoves.add(diagonal);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    /**
     * handles edge case when the king can not move in front of an opponent pawn<br>
     * king should be able to move in front of opponent pawns, because they can only take diagonally
     */
    public void removeStraightMoves() {
        try {
            Position firstMove = new Position(position.getColumn(), position.getRow() + direction);
            possibleMoves.remove(firstMove);
            Position secondMove = new Position(position.getColumn(), position.getRow() + direction * 2);
            possibleMoves.remove(secondMove);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public void setLegalMoves(ChessPieceList pieceList) {
        super.setLegalMoves(pieceList);

        for (int side : new int[]{1, -1}) {
            try {
                Position diagonalTake = new Position(position.getColumn() + side, position.getRow() + direction);
                ChessPiece target = piecesOnBoard.getPiece(diagonalTake);

                if (target == null || target.getColor() == this.color) {
                    possibleMoves.remove(diagonalTake);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    public boolean canPromote() {
        return position.getRow() == targetRank;
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "♙";
        return "♟";
    }
}