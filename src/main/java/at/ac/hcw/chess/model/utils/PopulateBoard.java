package at.ac.hcw.chess.model.utils;

import at.ac.hcw.chess.model.chessPieces.*;

public class PopulateBoard {

    public static ChessPieceList classicGameBoard() {
        ChessPieceList board = new ChessPieceList();
            board.add(new Rook(new Position(1, 1), Color.WHITE));
            board.add(new Knight(new Position(2, 1), Color.WHITE));
            board.add(new Bishop(new Position(3, 1), Color.WHITE));
            board.add(new King(new Position(4, 1), Color.WHITE));
            board.add(new Queen(new Position(5, 1), Color.WHITE));
            board.add(new Bishop(new Position(6, 1), Color.WHITE));
            board.add(new Knight(new Position(7, 1), Color.WHITE));
            board.add(new Rook(new Position(8, 1), Color.WHITE));

            board.add(new Rook(new Position(1, 8), Color.BLACK));
            board.add(new Knight(new Position(2, 8), Color.BLACK));
            board.add(new Bishop(new Position(3, 8), Color.BLACK));
            board.add(new King(new Position(4, 8), Color.BLACK));
            board.add(new Queen(new Position(5, 8), Color.BLACK));
            board.add(new Bishop(new Position(6, 8), Color.BLACK));
            board.add(new Knight(new Position(7, 8), Color.BLACK));
            board.add(new Rook(new Position(8, 8), Color.BLACK));

        for (int column = 1; column <= 8; column++) {
            board.add(new Pawn(new Position(column, 2), Color.WHITE));
            board.add(new Pawn(new Position(column, 7), Color.BLACK));
        }
        return board;
    }

    public static ChessPieceList noPawnsBoard() {
        ChessPieceList board = new ChessPieceList();
        board.add(new Rook(new Position(1, 1), Color.WHITE));
        board.add(new Knight(new Position(2, 1), Color.WHITE));
        board.add(new Bishop(new Position(3, 1), Color.WHITE));
        board.add(new King(new Position(4, 1), Color.WHITE));
        board.add(new Queen(new Position(5, 1), Color.WHITE));
        board.add(new Bishop(new Position(6, 1), Color.WHITE));
        board.add(new Knight(new Position(7, 1), Color.WHITE));
        board.add(new Rook(new Position(8, 1), Color.WHITE));

        board.add(new Rook(new Position(1, 8), Color.BLACK));
        board.add(new Knight(new Position(2, 8), Color.BLACK));
        board.add(new Bishop(new Position(3, 8), Color.BLACK));
        board.add(new King(new Position(4, 8), Color.BLACK));
        board.add(new Queen(new Position(5, 8), Color.BLACK));
        board.add(new Bishop(new Position(6, 8), Color.BLACK));
        board.add(new Knight(new Position(7, 8), Color.BLACK));
        board.add(new Rook(new Position(8, 8), Color.BLACK));
        return board;
    }
}
