package at.ac.hcw.chess.model.chessPieces;

import at.ac.hcw.chess.model.utils.ChessPieceList;
import at.ac.hcw.chess.model.utils.PopulateBoard;
import at.ac.hcw.chess.model.utils.Position;
import at.ac.hcw.chess.model.utils.SquareName;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ChessPieceTest {

    public static void compareMoves(ArrayList<Position> given, ArrayList<Position> expected) {
        if (given.size() != expected.size())
            throw new AssertionError("Wrong total of moves " + given.size());

        for (Position p : given) {
            if (!expected.contains(p))
                throw new AssertionError("Invalid move " + p);
        }
    }

    public static void pawnTest() {
        ChessPieceList piecesOnBoard = PopulateBoard.classicGameBoard();

        ChessPiece b2Pawn =  piecesOnBoard.getPiece(new Position(SquareName.B2));

        var b2PawnMoves = b2Pawn.getValidMoves(PopulateBoard.classicGameBoard());

        var validMoves = new ArrayList<Position>();
        validMoves.add(new Position(SquareName.B3));
        validMoves.add(new Position(SquareName.B4));

        compareMoves(b2PawnMoves, validMoves);
    }

    public static void testTheTest() {
        var moves = new ArrayList<Position>();
        moves.add(new Position(SquareName.B3));
        moves.add(new Position(SquareName.B4));

        var expected = new ArrayList<Position>();
        expected.add(new Position(SquareName.B3));
        expected.add(new Position(SquareName.B4));

        try {
            compareMoves(moves, expected);

            System.out.println("moves passed the test");
        } catch (Exception e) {
            System.err.println("exception happened: " + e);
        }
    }

    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("TestMain.output.log");
        PrintWriter pw = new PrintWriter(fileWriter);
        pw.println("created game model:");

        testTheTest();

        pw.close();
    }
}
