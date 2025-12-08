package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.Position;

public class Pawn extends ChessPiece{
    public Pawn(Position position, Color color) {
        super(position, color);
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "♙";
        return "♟";
    }
}
