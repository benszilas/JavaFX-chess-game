package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.utils.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ChessPieceTest {
    public FileWriter fileWriter;
    public PrintWriter pw;

    public ChessPieceTest() {
        try {
            this.fileWriter = new FileWriter("TestMain.output.log");
            this.pw = new PrintWriter(fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void compareMoves(MoveList given, MoveList expected) {
        this.checkDuplicates(given);

        if (given.size() != expected.size())
            throw new AssertionError("Wrong total of moves " + given.size());

        for (Position p : given) {
            if (!expected.contains(p))
                throw new AssertionError("Invalid move " + p);
        }
    }

    private void checkDuplicates(MoveList positions) {
        for (int i = 0; i < positions.size(); i++) {
            Position p = positions.get(i);
            for (int j = i + 1; j < positions.size() - 1; j++) {
                if (positions.get(j).equals(p)) {
                    System.err.println(
                            "Position " + p + " at index " + i + " is the same as Position " + positions.get(j) + " at index " + j
                    );
                    throw new RuntimeException("Position " + p + " is duplicated in " + positions);
                }
            }
        }
    }

    private void testMoves(String pieceName, ChessPiece piece, ChessPieceList board, MoveList expected) {
        this.pw.println("Validating moves of " + pieceName);

        piece.setValidMoves(board);
        var pieceMoves = piece.getValidMoves();

        this.pw.println(pieceMoves);
        try {
            compareMoves(pieceMoves, expected);
        } catch (Exception e) {
            this.pw.println(e);
            throw new RuntimeException(e);
        }
        this.pw.println(pieceName + " moves are validated");
    }

    private GameModel newBoard(ChessPieceList pieces) {
        GameModel board = new GameModel(pieces);
        this.pw.println("created board");
        this.pw.println(board);
        return board;
    }

    public void rookTest() {
        GameModel board = this.newBoard(PopulateBoard.noPawnsBoard());
        var piecesOnBoard = board.getChessPieces();

        ChessPiece A1Rook = piecesOnBoard.getPiece(new Position(SquareName.A1));

        var validMoves = new MoveList();
        validMoves.add(new Position(SquareName.A2));
        validMoves.add(new Position(SquareName.A3));
        validMoves.add(new Position(SquareName.A4));
        validMoves.add(new Position(SquareName.A5));
        validMoves.add(new Position(SquareName.A6));
        validMoves.add(new Position(SquareName.A7));
        validMoves.add(new Position(SquareName.A8));

        this.testMoves("A1 rook", A1Rook, piecesOnBoard, validMoves);

        ChessPiece A8Rook = piecesOnBoard.getPiece(new Position(SquareName.A8));
        validMoves.removeLast();
        validMoves.add(new Position(SquareName.A1));

        this.testMoves("A8 rook", A8Rook, piecesOnBoard, validMoves);
    }

    public void testTheTest() {
        var moves = new MoveList();
        moves.add(new Position(SquareName.B3));
        moves.add(new Position(SquareName.B4));

        var expected = new MoveList();
        expected.add(new Position(SquareName.B3));
        expected.add(new Position(SquareName.B4));

        try {
            compareMoves(moves, expected);
        } catch (Exception e) {
            System.err.println("exception happened in ChessPieceTest.compareMoves(): " + e);
        }
    }

    public static void main(String[] args) throws IOException {
        ChessPieceTest test = new ChessPieceTest();
        try {
            test.testTheTest();
            test.rookTest();
        } finally {
            test.pw.close();
            test.fileWriter.close();
        }
    }
}
