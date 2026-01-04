package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.King;
import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;
import at.ac.hcw.chess.view.GameView;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.List;

public class GameController {
    private final GameModel model;
    private final GameView view;

    public GameController() {
        model = new GameModel();
        view = new GameView(model);
    }

    public GameController(ChessPieceList customPieces) {
        this.model = new GameModel(customPieces);
        view = new GameView(model);
    }

    public GameModel getModel() {
        return model;
    }

    public GameView getView() {
        return view;
    }

    public void play() {
        setHandlers();
    }

    private void setHandlers() {
        List<Node> actionableBoardElements = view.getChessSquares();
        actionableBoardElements.addAll(view.getPieceViews());

        for (Node node : actionableBoardElements) {
            node.setOnMouseClicked(mouseEvent -> clickChessBoard(mouseEvent, node));
        }
    }

    private void clickChessBoard(Event mouseEvent, Node node) {
        int col = GridPane.getColumnIndex(node);
        int row = GridPane.getRowIndex(node);

        selectPiece(col, row);
        if (moveSelectedPiece(col, row)) {
            lookForGameOver();
            changePlayer();
        }
    }

    private void selectPiece(int col, int row) {
        ChessPiece piece = model.getChessPieces().getPiece(col, row);

        if (piece != null && piece.getColor() == model.getCurrentPlayer()) {
            model.selectPiece(piece);
            System.out.println(piece + " at " + piece.getPosition() + " selected by player " + model.getCurrentPlayer());
        }
    }

    private boolean moveSelectedPiece(int col, int row) {
        ChessPiece piece = model.getSelectedPiece();
        if (piece == null) return false;

        MoveList moves = piece.getPossibleMoves();
        Position target = new Position(col, row);
        ChessPiece opponentPiece = model.getChessPieces().getPiece(target);

        if (moves.contains(target)) {
            piece.getPosition().getColumnProperty().set(col);
            piece.getPosition().getRowProperty().set(row);
            System.out.println("moving " + piece + " to " + target);
            if (opponentPiece != null)
                takePiece(opponentPiece);
            return true;
        }
        System.out.println("can't move " + piece + " to " + target);
        return false;
    }

    private void takePiece(ChessPiece taken) {
        if (taken != null) {
            model.getChessPieces().remove(taken);
            model.getPromotablePieces().add(taken);
            System.out.println(model.getCurrentPlayer() + "'s " + taken + " was taken");
        }
    }

    private void changePlayer() {
        model.changePlayer();
        System.out.println("next to move: " + model.getCurrentPlayer());
    }

    // highlight selected piece in the view

    public Region buildView() {
        return view.build();
    }

    /**
     * set all possible moves for the next player
     * set all legal (no self-check) moves for the current player
     */
    private void updateMoves() {
        ChessPieceList currentPieces = model.getChessPieces();
        var currentKing = currentPieces.findPieces(King.class, model.getCurrentPlayer()).getFirst();

        // temporarily remove king from list, so opponent pieces cover fields behind it
        // see edge case GameControllerTest.test1()
        currentPieces.remove(currentKing);
        currentPieces.forEach(chessPiece -> {
            if (chessPiece.getColor() != model.getCurrentPlayer())
                chessPiece.setPossibleMoves(currentPieces);
        });

        currentPieces.add(currentKing);
        currentPieces.forEach(chessPiece -> {
            if (chessPiece.getColor() == model.getCurrentPlayer())
                chessPiece.setLegalMoves(currentPieces);
        });
    }

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
    private void handleCheck() {
        MoveList kingCheckedFrom = kingCheckedFrom();
        var currentPlayersPieces = new ChessPieceList();
        currentPlayersPieces.addAll(model.getChessPieces().stream()
                .filter(piece -> piece.getColor() == model.getCurrentPlayer()).toList());

        switch (kingCheckedFrom.size()) {
            case 0:
                return;
            case 1:
                tryToBlockCheck(currentPlayersPieces, kingCheckedFrom);
                break;
            default:
                forceKingToMove(currentPlayersPieces);
        }

        int allowedMovesCount = currentPlayersPieces.stream().mapToInt(
                chessPiece -> chessPiece.getPossibleMoves().size()
        ).sum();
        if (allowedMovesCount == 0)
            emitCheckmateEvent();
    }

    /**
     * emits an event that ends the game<br>
     * the current player is the loser<br>
     * the next player is the winner
     */
    private void emitCheckmateEvent() {
        Color winner = (model.getCurrentPlayer() == Color.WHITE) ? Color.BLACK : Color.WHITE;
        System.out.println(winner.name() + " has beaten " + model.getCurrentPlayer().name() + " by checkmate!");
        System.out.println(model);

    }

    private void emitDrawEvent() {
        System.out.println("The game is a draw!");
        System.out.println(model);

    }

    private void handleDraw() {
        var currentPlayersPieces = new ChessPieceList();
        currentPlayersPieces.addAll(model.getChessPieces().stream()
                .filter(piece -> piece.getColor() == model.getCurrentPlayer())
                .toList());

        int allowedMovesCount = currentPlayersPieces.stream()
                .mapToInt(chessPiece -> chessPiece.getPossibleMoves().size())
                .sum();

        if (allowedMovesCount == 0)
            emitDrawEvent();
    }

    /**
     * updates allowed moves
     * if the game is over, emit an event for checkmate or draw
     */
    public void lookForGameOver() {
        updateMoves();
        handleCheck();
        handleDraw();
    }
}
