package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public class Queen extends ChessPiece {
    public Queen(Position position, Color color) {
        super(position, color);
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        // Queens move both straight and diagonally
        this.possibleMoves = new MoveList();
        this.possibleMoves.addAll(straightRange(piecesOnBoard));
        this.possibleMoves.addAll(diagonalRange(piecesOnBoard));
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "Q";
        return "q";
    }
}