package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public class Knight extends ChessPiece {
    public Knight(Position position, Color color) {
        super(position, color);
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {

        MoveList movesToTry = new MoveList();

        int[][] offsets = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] offset : offsets) {
            try {
                Position target = new Position(
                        position.getColumn() + offset[0],
                        position.getRow() + offset[1]
                );
                movesToTry.add(target);
            } catch (IndexOutOfBoundsException ignored) {}
        }

        this.possibleMoves = stepOrJump(piecesOnBoard, movesToTry);
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "N";
        return "n";
    }
}