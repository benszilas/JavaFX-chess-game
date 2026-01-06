package at.ac.hcw.chess.model.utils;

import at.ac.hcw.chess.model.chessPieces.*;

public class PopulateBoard {

    public static ChessPieceList classicGameBoard() {
        ChessPieceList board = new ChessPieceList();
            board.add(new Rook(new Position(1, 1), Color.WHITE));
            board.add(new Knight(new Position(2, 1), Color.WHITE));
            board.add(new Bishop(new Position(3, 1), Color.WHITE));
            board.add(new Queen(new Position(4, 1), Color.WHITE));
            board.add(new King(new Position(5, 1), Color.WHITE));
            board.add(new Bishop(new Position(6, 1), Color.WHITE));
            board.add(new Knight(new Position(7, 1), Color.WHITE));
            board.add(new Rook(new Position(8, 1), Color.WHITE));

            board.add(new Rook(new Position(1, 8), Color.BLACK));
            board.add(new Knight(new Position(2, 8), Color.BLACK));
            board.add(new Bishop(new Position(3, 8), Color.BLACK));
            board.add(new Queen(new Position(4, 8), Color.BLACK));
            board.add(new King(new Position(5, 8), Color.BLACK));
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
        ChessPieceList board = classicGameBoard();
        board.removeIf(chessPiece -> chessPiece.getClass() == Pawn.class);
        return board;
    }

    public static ChessPieceList checkmate1() {
        var board = new ChessPieceList();
        board.add(new King(new Position(SquareName.G1), Color.WHITE));
        board.add(new King(new Position(SquareName.G3), Color.BLACK));
        board.add(new Rook(new Position(SquareName.A1), Color.BLACK));
        return board;
    }

    public static ChessPieceList draw1() {
        var board = new ChessPieceList();
        board.add(new King(new Position(SquareName.H1), Color.WHITE));
        board.add(new King(new Position(SquareName.H3), Color.BLACK));
        board.add(new Rook(new Position(SquareName.G3), Color.BLACK));
        return board;
    }

    public static ChessPieceList mateInOne1() {
        var board = checkmate1();
        board.add(new Rook(new Position(SquareName.D8), Color.WHITE));
        return board;
    }
}
