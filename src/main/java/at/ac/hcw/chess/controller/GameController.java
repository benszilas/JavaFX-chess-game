package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.King;
import at.ac.hcw.chess.model.chessPieces.Pawn;
import at.ac.hcw.chess.model.utils.*;
import at.ac.hcw.chess.view.GameView;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class GameController {
    private final GameModel model;
    private final GameView view;

    public GameController() {
        model = new GameModel();
        view = new GameView(model, this);
        lookForGameOver();
    }

    public GameController(ChessPieceList customPieces) {
        this.model = new GameModel();
        model.customGame(customPieces);
        view = new GameView(model, this);
        lookForGameOver();
    }

    public Region getView() {
        return view.build();
    }

    public GameModel getModel() {
        return model;
    }

    public GameView view() {
        return view;
    }

    public void clickChessBoard(Event mouseEvent) {
        Node node = (Node) mouseEvent.getTarget();
        int col = GridPane.getColumnIndex(node);
        int row = GridPane.getRowIndex(node);

        selectPiece(col, row);
        if (moveSelectedPiece(col, row, node)) {
            changePlayer();
            lookForGameOver();
        }
    }

    private void selectPiece(int col, int row) {
        ChessPiece piece = model.getChessPieces().getPiece(col, row);

        if (piece != null && piece.getColor() == model.getCurrentPlayer()) {
            removeHighlight(model.getSelectedPiece());
            addHighlight(piece);
            model.selectPiece(piece);
            System.out.println(piece + " at " + piece.getPosition() + " selected by player " + model.getCurrentPlayer());
        }
    }

    private void removeHighlight(ChessPiece piece) {
        if (piece == null) return;
        view.removeHighlight(piece);
        view.removeHighlight(piece.getPossibleMoves());
    }

    private void addHighlight(ChessPiece piece) {
        if (piece == null) return;
        view.addHighlight(piece);
        view.addHighlight(piece.getPossibleMoves());
    }

    private boolean moveSelectedPiece(int col, int row, Node node) {
        ChessPiece piece = model.getSelectedPiece();
        if (piece == null) return false;

        Position target = new Position(col, row);

        if (piece.getPossibleMoves().contains(target)) {
            System.out.println("moving " + piece + " to " + target);
            take(model.getChessPieces().getPiece(target));
            piece.moveTo(target, (ImageView) view.chessBoardChildNode(piece.getPosition(), ImageView.class));
            return true;
        }
        System.out.println("can't move " + piece + " to " + target);
        return false;
    }

    public void take(ChessPiece target) {
        if (target != null) {
            model.getChessPieces().remove(target);
            model.getPromotablePieces().add(target);

            // Add to captured pieces list based on color
            if (target.getColor() == Color.WHITE) {
                model.getCapturedWhitePieces().add(target);
            } else {
                model.getCapturedBlackPieces().add(target);
            }

            view.getBoard().getChildren().remove(view.chessBoardChildNode(target.getPosition(), ImageView.class));
            view.refreshCapturedPieces();
            System.out.println(model.getCurrentPlayer() + "'s " + target + " was taken");
        }
    }

    public void promote(ChessPiece promoted, ImageView pieceView, Position position) {
        model.getPromotablePieces().remove(promoted);
        if (promoted.getColor() == Color.BLACK)
            model.getCapturedBlackPieces().remove(promoted);
        else
            model.getCapturedWhitePieces().remove(promoted);
        promoted.moveTo(position, pieceView);
        model.getChessPieces().add(promoted);
    }

    private void changePlayer() {
        model.changePlayer();
        removeHighlight(model.getSelectedPiece());
        model.selectPiece((ChessPiece) null);
        System.out.println("next to move: " + model.getCurrentPlayer());
        System.out.println("FEN notation:");
        System.out.println(model + System.lineSeparator());
    }

    /**
     * set all possible moves for the next player
     * set all legal (no self-check) moves for the current player
     */
    private void updateMoves() {
        ChessPieceList currentPieces = model.getChessPieces();
        ChessPiece currentKing = currentPieces.findPieces(King.class, model.getCurrentPlayer()).getFirst();

        currentPieces.remove(currentKing);
        updateNextPlayerMoves(currentPieces);
        currentPieces.add(currentKing);
        updateCurrentPlayerMoves(currentPieces);

        preventSelfCheck(currentKing);
    }

    /**
     * set all possible moves for next player <br>
     * <b>important</b> to remove pawn pushes for check validation
     */
    private void updateNextPlayerMoves(ChessPieceList currentPieces) {
        model.onePlayersPieces(model.getNextPlayer())
                .forEach(piece -> {
                    piece.setPossibleMoves(currentPieces);
                    if (piece instanceof Pawn)
                        ((Pawn) piece).removeStraightMoves();
                });
    }

    /**
     * set all legal (no self-check) moves for current player
     */
    private void updateCurrentPlayerMoves(ChessPieceList currentPieces) {
        model.onePlayersPieces(model.getCurrentPlayer())
                .forEach(piece -> piece.setLegalMoves(currentPieces));
    }

    /**
     * see if after taking any opponent, the king would be in check<br>
     * opponent moves are recalculated for every opponent next to the king
     * @param king the current king
     */
    private void preventSelfCheck(ChessPiece king) {
        ChessPieceList currentPieces = model.getChessPieces();

        var nextToKing = king.getPossibleMoves().stream()
                .filter(position -> currentPieces.getPiece(position) != null).toList();
        currentPieces.remove(king);
        for (Position move : nextToKing) {
            ChessPiece opponentPiece = currentPieces.getPiece(move);

            currentPieces.remove(opponentPiece);
            updateNextPlayerMoves(currentPieces);

            ChessPiece doubleKing = new King(king.getPosition(), king.getColor());
            doubleKing.setLegalMoves(currentPieces);
            king.getPossibleMoves().removeIf(position -> !doubleKing.getPossibleMoves().contains(position));
            currentPieces.add(opponentPiece);
        }

        updateNextPlayerMoves(currentPieces);
        currentPieces.add(king);
    }

    /**
     * <b>ASSUME<b/> the current King is <b>NOT<b/> in check<br>
     * see if the king can castle in either direction.<br>
     * if yes, add that to the moves.
     */
    private void addCastleMoves() {
        King currentKing = (King) model.getChessPieces().findPieces(King.class, model.getCurrentPlayer()).getFirst();
        if (currentKing.hasMoved()) return;

        for (int direction : new int[]{-1, 1}) {
            MoveList range = currentKing.range(model.getChessPieces(), direction, 0);
            if (range.isEmpty()) continue;

            try {
                Position last = new Position(range.getLast().getColumn() + direction, currentKing.getPosition().getRow());
                range.add(last);
                currentKing.tryAddCastleMove(getModel().getChessPieces(), range);
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
    }

    public void castleRook(Position oldPosition, Position newPosition) {
        ImageView rookImage = (ImageView) view.chessBoardChildNode(oldPosition, ImageView.class);
        var saved = model.getSelectedPiece();
        model.selectPiece(oldPosition);
        moveSelectedPiece(newPosition.getColumn(), newPosition.getRow(), rookImage);
        model.selectPiece(saved);
    }

    /**
     * where is the current players king checked from
     *
     * @return a MoveList of Positions of checking opponent Pieces<br>
     * empty MoveList if there is no check
     */
    private MoveList kingCheckedFrom() {
        MoveList kingCheckedFrom = new MoveList();
        Position king = model.getChessPieces().findPieces(King.class, model.getCurrentPlayer()).getFirst().getPosition();

        model.getChessPieces().forEach(chessPiece -> {
            if (chessPiece.getPossibleMoves().contains(king))
                kingCheckedFrom.add(chessPiece.getPosition());
        });
        return kingCheckedFrom;
    }

    /**
     * calculate positions where the check can be countered
     * see if the current player can reach any of them
     *
     * @param kingCheckedFrom a list of positions where attackers of the king are
     */
    private void tryToBlockCheck(ChessPieceList currentPlayersPieces, MoveList kingCheckedFrom) {
        if (kingCheckedFrom.size() != 1 || currentPlayersPieces.size() == 1) return;

        ChessPiece attacker = model.getChessPieces().getPiece(kingCheckedFrom.getFirst());
        ChessPiece king = currentPlayersPieces.findPieces(King.class, model.getCurrentPlayer()).getFirst();
        Position kingPosition = king.getPosition();

        // add positions where the check can be blocked
        if (attacker.canSeePiece(kingPosition, model.getChessPieces())) {
            kingCheckedFrom.addAll(king.range(model.getChessPieces(),
                    kingPosition.columnDelta(attacker.getPosition()), kingPosition.rowDelta(attacker.getPosition()))
            );
        }

        currentPlayersPieces.forEach(chessPiece -> {
            if (chessPiece.getClass() != King.class)
                chessPiece.getPossibleMoves().removeIf(move -> !kingCheckedFrom.contains(move));
        });
    }

    /**
     * force the current king to move by deleting the allowed moves of all other pieces of the current player
     * does not change the current king's moves
     */
    private void forceKingToMove(ChessPieceList currentPlayersPieces) {
        currentPlayersPieces.forEach(chessPiece -> {
            if (chessPiece.getClass() != King.class)
                chessPiece.getPossibleMoves().clear();
        });
    }

    /**
     * if the king is in check, see if it can be blocked.
     * otherwise force the king to avoid the check
     * otherwise emit a checkmate event
     */
    private boolean isCheckmate() {
        MoveList kingCheckedFrom = kingCheckedFrom();
        var currentPlayersPieces = new ChessPieceList();
        currentPlayersPieces.addAll(model.getChessPieces().stream()
                .filter(piece -> piece.getColor() == model.getCurrentPlayer()).toList());

        switch (kingCheckedFrom.size()) {
            case 0:
                addCastleMoves();
                return false;
            case 1:
                tryToBlockCheck(currentPlayersPieces, kingCheckedFrom);
                break;
            default:
                forceKingToMove(currentPlayersPieces);
        }

        int allowedMovesCount = currentPlayersPieces.stream().mapToInt(
                chessPiece -> chessPiece.getPossibleMoves().size()
        ).sum();
        return allowedMovesCount == 0;
    }

    /**
     * emits an event that ends the game<br>
     * the current player is the loser<br>
     * the next player is the winner
     */
    private void emitCheckmateEvent() {
        Color winner = (model.getCurrentPlayer() == Color.WHITE) ? Color.BLACK : Color.WHITE;
        showResult("Checkmate!", winner.name() + " has beaten " + model.getCurrentPlayer().name() + " by checkmate!");
    }

    private void emitDrawEvent(String message) {
        showResult("The game is a draw!", message);
    }

    private void showResult(String title, String message) {
        view.getBoard().setDisable(true);
        System.out.println(model);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
        view.getBoard().fireEvent(new GameEndedEvent());
    }

    private void lookForDraw() {
        if (model.getChessPieces().size() == 2)
            emitDrawEvent("Only two kings left.");

        var currentPlayersPieces = new ChessPieceList();
        currentPlayersPieces.addAll(model.getChessPieces().stream()
                .filter(piece -> piece.getColor() == model.getCurrentPlayer())
                .toList());

        int allowedMovesCount = currentPlayersPieces.stream()
                .mapToInt(chessPiece -> chessPiece.getPossibleMoves().size())
                .sum();

        if (allowedMovesCount == 0)
            emitDrawEvent(model.getCurrentPlayer() + " has no more moves left!");
    }

    /**
     * updates allowed moves
     * if the game is over, emit an event for checkmate or draw
     */
    public void lookForGameOver() {
        updateMoves();
        if (isCheckmate())
            emitCheckmateEvent();
        else
            lookForDraw();
    }
}
