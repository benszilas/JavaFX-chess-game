package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

import java.util.ArrayList;

public class King extends ChessPiece {
    public King(Position position, Color color) {
        super(position, color);
    }

    /**
     * the king is the only piece that can not be pinned<br>
     * this will always return null
     */
    @Override
    protected MoveList pinnedMoves() {
        return null;
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        MoveList movesToTry = new MoveList();

        for (int row = this.position.getRow() - 1; row <= this.position.getRow() + 1; row++) {
            for (int col = this.position.getColumn() - 1; col <= this.position.getColumn() + 1; col++) {
                try {
                    movesToTry.add(new Position(col, row));
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }

        this.possibleMoves = stepOrJump(piecesOnBoard, movesToTry);
    }

    @Override
    protected void preventSelfCheck() {
        var opponentPieces = piecesOnBoard.stream()
                .filter(piece -> piece.getColor() != this.color).toList();

        possibleMoves.removeIf(kingMove ->
                opponentPieces.stream().anyMatch(opponentPiece ->
                        opponentPiece.getPossibleMoves().contains(kingMove)
                )
        );
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "♔";
        return "♚";
    }
}
