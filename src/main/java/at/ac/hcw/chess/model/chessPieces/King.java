package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public class King extends ChessPiece {

    public King(Position position, Color color) {
        super(position, color);
    }

    @Override
    public String toString() {
        // Returns the unicode chess character for console display
        return (this.getColor() == Color.WHITE) ? "♔" : "♚";
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        MoveList targets = new MoveList();
        int c = this.position.getColumn();
        int r = this.position.getRow();

        // Check all 8 squares around the king
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue; // Skip current position

                try {
                    // Try to create position; catch block handles out-of-bounds
                    targets.add(new Position(c + x, r + y));
                } catch (Exception ignored) { }
            }
        }

        // stepOrJump handles collision with own pieces
        this.possibleMoves = stepOrJump(piecesOnBoard, targets);
    }
}