package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.Position;

import java.util.ArrayList;

public abstract class ChessPiece {
    private Position position;
    private final Color color;

    public ChessPiece(Position position, Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return this.position;
    }

    public ArrayList <Position> getValidMoves() {
        ArrayList <Position> validMoves = new ArrayList <Position>();
        return validMoves;
    }
}
