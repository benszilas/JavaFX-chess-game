package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;

public abstract class ChessPiece {
    protected Position position;
    protected final Color color;
    protected MoveList possibleMoves;
    protected ChessPieceList piecesOnBoard;

    public ChessPiece(Position position, Color color) {
        this.position = position;
        this.color = color;
        this.possibleMoves = new MoveList();
    }

    public MoveList range(ChessPieceList piecesOnBoard, int deltaCol, int deltaRow) {
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
     *
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
     *
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
     *
     * @param piecesOnBoard board to check
     * @param targets       the moves to try
     * @return all targets excluding friendly pieces
     */
    protected MoveList stepOrJump(ChessPieceList piecesOnBoard, MoveList targets) {
        MoveList possibleMoves = new MoveList();

        for (Position p : targets) {
            ChessPiece piece = piecesOnBoard.getPiece(p);
            if (piece == null || piece.getColor() != this.color)
                possibleMoves.add(p.clone());
        }

        return possibleMoves;
    }

    /**
     * "Seeing" a piece means there are no pieces between this and the other one.<br>
     * The other piece has to stand in a straight or diagonal line from this one.
     *
     * @return true if the piece is seen
     */
    public boolean canSeePiece(Position otherPiece, ChessPieceList piecesOnBoard) {
        if (!otherPiece.isInStraightLine(this.getPosition()) && !otherPiece.isDiagonal(this.getPosition()))
            return false;

        int rowDelta = this.getPosition().rowDelta(otherPiece);
        int columnDelta = this.getPosition().columnDelta(otherPiece);

        Position next = new Position(this.getPosition().getColumn() + columnDelta, this.getPosition().getRow() + rowDelta);
        while (piecesOnBoard.getPiece(next) == null)
            next = new Position(next.getColumn() + columnDelta, next.getRow() + rowDelta);

        return next.equals(otherPiece);
    }

    /**
     * a piece is pinned if moving it could potentially put its own king in check<br>
     * if pinned, the piece can only move in a line between the own king and the attacker, or take the attacker
     *
     * @return - null if not pinned<br>
     * - if pinned, a list of positions between the king and the attacker including the attacker
     */
    protected MoveList pinnedMoves() {
        Position ownKing = piecesOnBoard.findPieces(King.class, this.color).getFirst().getPosition();
        boolean isPinned = false;
        MoveList pinnedMoves = null;

        if (this.canSeePiece(ownKing, piecesOnBoard)) {
            int columnDelta = ownKing.columnDelta(this.position);
            int rowDelta = ownKing.rowDelta(this.position);
            pinnedMoves = range(piecesOnBoard, columnDelta, rowDelta);
            if (!pinnedMoves.isEmpty() && piecesOnBoard.getPiece(pinnedMoves.getLast()) != null) {
                ChessPiece opponentPiece = piecesOnBoard.getPiece(pinnedMoves.getLast());
                if (opponentPiece.getPosition().isInStraightLine(this.position)
                        && (opponentPiece.getClass() == Queen.class || opponentPiece.getClass() == Rook.class)) {
                    isPinned = true;
                }
                if (opponentPiece.getPosition().isDiagonal(this.position)
                        && (opponentPiece.getClass() == Queen.class || opponentPiece.getClass() == Bishop.class)) {
                    isPinned = true;
                }
            }
            if (isPinned) {
                pinnedMoves.addAll(range(piecesOnBoard, -columnDelta, -rowDelta));
            } else {
                pinnedMoves = null;
            }
        }
        return pinnedMoves;
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

    protected void preventSelfCheck() {
        var pinnedMoves = pinnedMoves();
        if (pinnedMoves != null)
            possibleMoves.removeIf(move -> !pinnedMoves.contains(move));
    }

    public void setLegalMoves(ChessPieceList piecesOnBoard) {
        this.piecesOnBoard = piecesOnBoard;
        setPossibleMoves(piecesOnBoard);
        preventSelfCheck();
    }

    public boolean possibleMove(Position position) {
        return this.possibleMoves.contains(position);
    }
}
