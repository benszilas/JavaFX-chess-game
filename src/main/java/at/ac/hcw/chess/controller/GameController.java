package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.King;
import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;
import at.ac.hcw.chess.view.GameView;
import javafx.scene.layout.Region;

public class GameController {
    private final GameModel model;
    private final GameView view;

    public GameController() {
        model = new GameModel();
        view = new GameView(model);
    }

    public GameController(GameModel model) {
        this.model = model;
        view = new GameView(model);
    }

    // hook on click:
    // 1. validate player
    // 2.a select piece by calling model.selectPiece();
    // highlight selected piece in the view
    // 2.b else if model.getSelectedPiece() == the piece that is clicked
    // call model.validateMove()
    // 3. move piece

    public Region getView() {
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
