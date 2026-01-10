package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public class Pawn extends ChessPiece {

    public Pawn(Position position, Color color) {
        super(position, color);
    }

    @Override
    public String toString() {
        // Returns the unicode chess character for console display
        return (this.getColor() == Color.WHITE) ? "♙" : "♟";
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        this.possibleMoves = new MoveList();
        int col = this.position.getColumn();
        int row = this.position.getRow();

        // Direction depends on color. White moves up (-1), Black moves down (+1).
        // Adjust this if your board indices are different (e.g. 0 at bottom).
        int direction = (this.getColor() == Color.WHITE) ? -1 : 1;
        int startRow = (this.getColor() == Color.WHITE) ? 6 : 1;

        try {
            // 1. Standard move: one step forward (must be empty).
            Position oneStep = new Position(col, row + direction);
            if (piecesOnBoard.getPiece(oneStep) == null) {
                this.possibleMoves.add(oneStep);

                // 2. Double step: only allowed from the starting row and if path is clear.
                if (row == startRow) {
                    Position twoStep = new Position(col, row + (direction * 2));
                    if (piecesOnBoard.getPiece(twoStep) == null) {
                        this.possibleMoves.add(twoStep);
                    }
                }
            }

            // 3. Capturing: only allowed diagonally and if an opponent is present.
            int[] captureCols = {col - 1, col + 1};
            for (int c : captureCols) {
                try {
                    Position target = new Position(c, row + direction);
                    ChessPiece targetPiece = piecesOnBoard.getPiece(target);

                    // Can only capture if it's an enemy piece
                    if (targetPiece != null && targetPiece.getColor() != this.color) {
                        this.possibleMoves.add(target);
                    }
                } catch (Exception ignored) { }
            }
        } catch (Exception ignored) {
            // Handles cases where the Pawn reaches the end of the board.
        }
    }
}