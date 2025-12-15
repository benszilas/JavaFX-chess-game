package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public abstract class ChessPiece {
    protected Position position;
    protected final Color color;
    protected MoveList possibleMoves;

    public ChessPiece(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    protected MoveList range(ChessPieceList piecesOnBoard, int deltaCol, int deltaRow) {
        MoveList possibleRange = new MoveList();
        ChessPiece foundPiece = null;
        Position p = null;
        int col = this.position.getColumn();
        int row = this.position.getRow();

        try {
            do {
                col += deltaCol;
                row += deltaRow;
                p = new Position(col, row);
                foundPiece = piecesOnBoard.getPiece(p);

            } while (foundPiece == null && possibleRange.add(p));

        } catch (IndexOutOfBoundsException ignored) {

        } finally { // opponent piece is possible move
            if (foundPiece != null && foundPiece.getColor() != this.color)
                possibleRange.add(p);
        }
        return possibleRange;
    }

    /**
     * check possible moves in a straight line range
     * <p><b>can't</b> move through pieces</p>
     * @param piecesOnBoard the current pieces on the chessboard
     * @return empty squares and opponent pieces visible in a straight line
     */
    protected MoveList straightRange(ChessPieceList piecesOnBoard) {
        MoveList possibleMoves = new MoveList();

        possibleMoves.addAll(range(piecesOnBoard, 1, 0));
        possibleMoves.addAll(range(piecesOnBoard, -1, 0));
        possibleMoves.addAll(range(piecesOnBoard, 0, 1));
        possibleMoves.addAll(range(piecesOnBoard, 0, -1));

        return possibleMoves;
    }

    /**
     * check possible moves in a diagonal range
     * <p><b>can't</b> move through pieces</p>
     * @param piecesOnBoard the current pieces on the chessboard
     * @return empty squares and opponent pieces visible in a diagonal line
     */
    protected MoveList diagonalRange(ChessPieceList piecesOnBoard) {
        MoveList possibleMoves = new MoveList();

        possibleMoves.addAll(range(piecesOnBoard, 1, 1));
        possibleMoves.addAll(range(piecesOnBoard, 1, -1));
        possibleMoves.addAll(range(piecesOnBoard, -1, 1));
        possibleMoves.addAll(range(piecesOnBoard, -1, -1));

        return possibleMoves;
    }

    /**
     * check if the piece can step or jump to any target
     * <p><b>can</b> move through pieces</p>
     * @param piecesOnBoard board to check
     * @param targets the moves to try
     * @return all targets excluding friendly pieces
     */
    protected MoveList stepOrJump(ChessPieceList piecesOnBoard, MoveList targets) {
        MoveList possibleMoves = new MoveList();

        for (Position p : targets) {
            ChessPiece piece = piecesOnBoard.getPiece(p);
            if (piece == null || piece.getColor() != this.color)
                possibleMoves.add((Position) p.clone());
        }

        return possibleMoves;
    }

    public boolean isPinned(ChessPieceList piecesOnBoard) {
        // implement logic to check if piece is pinned to its King
        return false;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return this.position;
    }

    /**
     * Override this function in the derived classes
     * <p>include opponent pieces</p>
     * <p>ignore game over</p>
     */
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        this.possibleMoves = new MoveList();
    }

    public MoveList getPossibleMoves() {
        return this.possibleMoves;
    }

    public boolean possibleMove(Position position) {
        return this.possibleMoves.contains(position);
    }
}
