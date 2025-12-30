package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.King;
import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;
import at.ac.hcw.chess.view.GameView;

public class GameController {
    private final GameModel model;
    private final GameView view;

    public GameController() {
        model = new GameModel();
        view = new GameView(model);
    }

    public GameView getView() {
        return view;
    }

    /**
     * set all possible moves for the next player
     * set all legal (no self-check) moves for the current player
     */
    private void updateMoves() {
        ChessPieceList currentPieces = new ChessPieceList();

        currentPieces.forEach(chessPiece -> {
            if (chessPiece.getColor() != model.getCurrentPlayer())
                chessPiece.setPossibleMoves(currentPieces);
        });
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

    private void emitGameResult() {
        updateMoves();
        MoveList kingCheckedFrom = kingCheckedFrom();
    }
}
