package at.ac.hcw.chess.controller;

import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPieceTest;
import at.ac.hcw.chess.model.chessPieces.King;
import at.ac.hcw.chess.model.chessPieces.Rook;
import at.ac.hcw.chess.model.utils.*;

public class GameControllerTest {
    private final ChessPieceTest pieceTest = new ChessPieceTest();

    public void test1() {
        var pieces = PopulateBoard.checkmate1();
        var controller = new GameController(pieces);
        System.out.println("testing checkmate");
        controller.lookForGameOver();
        var expectedBlackRookMoves = new MoveList();
        expectedBlackRookMoves.add(new Position(SquareName.B1));
        expectedBlackRookMoves.add(new Position(SquareName.C1));
        expectedBlackRookMoves.add(new Position(SquareName.D1));
        expectedBlackRookMoves.add(new Position(SquareName.E1));
        expectedBlackRookMoves.add(new Position(SquareName.F1));
        expectedBlackRookMoves.add(new Position(SquareName.G1));
        expectedBlackRookMoves.add(new Position(SquareName.H1));
        expectedBlackRookMoves.add(new Position(SquareName.A2));
        expectedBlackRookMoves.add(new Position(SquareName.A3));
        expectedBlackRookMoves.add(new Position(SquareName.A4));
        expectedBlackRookMoves.add(new Position(SquareName.A5));
        expectedBlackRookMoves.add(new Position(SquareName.A6));
        expectedBlackRookMoves.add(new Position(SquareName.A7));
        expectedBlackRookMoves.add(new Position(SquareName.A8));

        pieceTest.testMoves("A1 black Rook",
                pieces.findPieces(Rook.class, Color.BLACK).getFirst().getPossibleMoves(), expectedBlackRookMoves);
    }

    public void test2() {
        var controller = new GameController(PopulateBoard.draw1());
        System.out.println("testing draw");
        controller.lookForGameOver();
    }

    public void test3() {
        var customPieces = PopulateBoard.mateInOne1();
        var controller = new GameController(customPieces);
        System.out.println("testing mate in one");
        controller.lookForGameOver();
        var whiteRook = customPieces.findPieces(Rook.class, Color.WHITE).getFirst();
        var expectedWhiteRookMoves = new MoveList();
        expectedWhiteRookMoves.add(new Position(SquareName.D1));
        System.out.println();
        pieceTest.testMoves("White D8 Rook", whiteRook.getPossibleMoves(), expectedWhiteRookMoves);
        System.out.println(controller.getModel());
    }

    public static void main(String[] args) {
        var gcTest = new GameControllerTest();
        gcTest.test1();
        gcTest.test2();
        gcTest.test3();
    }
}
