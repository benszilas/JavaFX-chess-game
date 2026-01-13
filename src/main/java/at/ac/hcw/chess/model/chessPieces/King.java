package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.*;
import javafx.scene.image.ImageView;

import java.util.LinkedHashSet;
import java.util.Set;

public class King extends ChessPiece {
    public King(Position position, Color color) {
        super(position, color);
    }

    /**
     * the king is the only piece that can not be pinned<br>
     * this will always return null
     */
    @Override
    protected MoveList pinnedMoves() {
        return null;
    }

    @Override
    public void setPossibleMoves(ChessPieceList piecesOnBoard) {
        MoveList movesToTry = new MoveList();

        for (int row = this.position.getRow() - 1; row <= this.position.getRow() + 1; row++) {
            for (int col = this.position.getColumn() - 1; col <= this.position.getColumn() + 1; col++) {
                try {
                    movesToTry.add(new Position(col, row));
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }

        this.possibleMoves = stepOrJump(piecesOnBoard, movesToTry);
    }

    /**
     * check the rules for castling in one specific direction:<br>
     * - no squares in that direction under attack<br>
     * - at the end of that direction is a friendly rook that has not moved yet
     *
     * @param piecesOnBoard current pieces
     * @param range         list of moves including the potential friendly rook
     */
    public void tryAddCastleMove(ChessPieceList piecesOnBoard, MoveList range) {
        ChessPiece neighbor = piecesOnBoard.getPiece(range.getLast());
        Color opponent = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;

        if (!(neighbor instanceof Rook) || neighbor.getColor() == opponent) return;
        if (this.hasMoved() || neighbor.hasMoved()) return;

        Set<Position> opponentMoves = new LinkedHashSet<>();
        for (ChessPiece opponentPiece : piecesOnBoard.findPieces(ChessPiece.class, opponent)) {
            opponentMoves.addAll(opponentPiece.getPossibleMoves());
        }
        if (opponentMoves.contains(range.get(0)) || opponentMoves.contains(range.get(1)))
            return;

        this.possibleMoves.add(range.get(1));
    }

    @Override
    public void moveTo(Position newPosition, ImageView pieceView) {
        int jump = newPosition.getColumn() - position.getColumn();
        if (Math.abs(jump) > 1) {
            int rookColumn = (jump > 1) ? Position.MAX : Position.MIN;
            pieceView.fireEvent(new CastleEvent(
                    new Position(rookColumn, position.getRow()),
                    new Position(position.getColumn() + jump / Math.abs(jump), position.getRow()))
            );
        }
        super.moveTo(newPosition, pieceView);
    }

    @Override
    protected void preventSelfCheck() {
        var opponentPieces = piecesOnBoard.stream()
                .filter(piece -> piece.getColor() != this.color).toList();

        possibleMoves.removeIf(kingMove ->
                opponentPieces.stream().anyMatch(opponentPiece ->
                        opponentPiece.getPossibleMoves().contains(kingMove)
                )
        );
    }

    @Override
    public String toString() {
        if (this.getColor() == Color.WHITE)
            return "K";
        return "k";
    }
}
