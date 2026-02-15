package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.image.ImageView;

public class Pawn extends ChessPiece {
    private final int targetRank;
    private final int direction;
    private final ChessPieceList enPassantTakers;
    private Position enPassant = null;

    public Pawn(Position position, Color color) {
        super(position, color);
        targetRank = (color == Color.WHITE) ? 8 : 1;
        direction = (this.color == Color.WHITE) ? 1 : -1;
        enPassantTakers = new ChessPieceList();
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        this.possibleMoves = new MoveList();
        this.enPassantTakers.clear();

        try {
            Position oneForward = new Position(position.getColumn(), position.getRow() + direction);
            if (piecesOnBoard.getPiece(oneForward) == null) {
                possibleMoves.add(oneForward);

                if (!this.hasMoved()) {
                    Position twoForward = new Position(position.getColumn(), position.getRow() + 2 * direction);
                    if (piecesOnBoard.getPiece(twoForward) == null) {
                        possibleMoves.add(twoForward);
                        setEnPassantTakers(twoForward, piecesOnBoard);
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

    private void setEnPassantTakers(Position twoForward, ChessPieceList piecesOnBoard) {
        for (int side : new int[]{1, -1}) {
            try {
                Position takerPosition = new Position(twoForward.getColumn() + side, twoForward.getRow());
                ChessPiece taker = piecesOnBoard.getPiece(takerPosition);
                if (taker instanceof Pawn && taker.getColor() != color)
                    enPassantTakers.add(taker);
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
        if (enPassant != null)
            possibleMoves.add(enPassant);
    }

    public boolean canPromote() {
        return position.getRow() == targetRank;
    }

    public void setEnPassant(Position target) {
        enPassant = target;
    }

    @Override
    public void moveTo(Position newPosition, ImageView pieceView) {
        if (!enPassantTakers.isEmpty() && Math.abs(newPosition.getRow() - position.getRow()) == 2) {
            enPassantTakers.forEach(piece -> {
                if (piece instanceof Pawn pawn) {
                    pawn.setEnPassant(new Position(position.getColumn(), position.getRow() + direction));
                }
            });
        }
        if (newPosition.equals(enPassant))
            pieceView.fireEvent(new EnPassantEvent(new Position(enPassant.getColumn(), position.getRow())));
        super.moveTo(newPosition, pieceView);
        if (canPromote())
            pieceView.fireEvent(new PromotionEvent(this));
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "P";
        return "p";
    }
}