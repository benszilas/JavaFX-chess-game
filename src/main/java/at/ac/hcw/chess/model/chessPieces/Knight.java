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
    public String toString() {
        // Returns the unicode chess character for console display
        return (this.getColor() == Color.WHITE) ? "♘" : "♞";
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        MoveList targets = new MoveList();
        int c = this.position.getColumn();
        int r = this.position.getRow();

        // Knights move in an L-shape. These are the 8 possible offsets.
        int[][] offsets = {
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2},
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1}
        };

        for (int[] offset : offsets) {
            try {
                // Try to add position. If it's out of bounds, the catch block handles it.
                targets.add(new Position(c + offset[0], r + offset[1]));
            } catch (Exception ignored) { }
        }

        // stepOrJump filters out moves that land on own pieces.
        this.possibleMoves = stepOrJump(piecesOnBoard, targets);
    }
}