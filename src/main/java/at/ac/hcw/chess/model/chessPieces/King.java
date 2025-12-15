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

    @Override
    public boolean isPinned(ChessPieceList piecesOnBoard) {
        return false;
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

    public ArrayList<MoveList> checkedBy(ChessPieceList piecesOnBoard) {
        var checkedBy = new ArrayList<MoveList>();
        return checkedBy;
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "♔";
        return "♚";
    }
}
