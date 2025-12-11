package at.ac.hcw.chess.model;

import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.Queen;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.PopulateBoard;
import at.ac.hcw.chess.model.utils.Position;
import at.ac.hcw.chess.model.utils.SquareName;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameModelTestMain {

    public static void getChessPiecesTest(GameModel model) {
        ChessPiece whiteQueen = new Queen(new Position(3, 3), Color.WHITE);
        if (model.getChessPieces().getFirst().getPosition().toString().equals("A1"))
            System.out.println("first piece position is A1");
        else
            System.out.println("first piece position is" + model.getChessPieces().getFirst().getPosition().toString());

        model.getChessPieces().addLast(whiteQueen);
        if (!model.getChessPieces().contains(whiteQueen))
            throw new AssertionError("model.getChessPieces().add() did not add or model.getChessPieces() did not update");
        System.out.println("model.getChessPieces() tested");
    }

    public static void getPromotablePiecesTest(GameModel model) {
        ChessPiece queen = model.getPromotablePieces().getFirst();
        if (queen.getClass() != Queen.class)
            throw new AssertionError("model.getPromotablePieces() does not have a queen first");
        System.out.println("model.getPromotablePieces() tested");
    }

    public static void selectPieceTest(GameModel model) {
        if (model.getSelectedPiece() != null)
            throw new AssertionError("model.getSelectedPiece() is not null by default");
        System.out.println("model.getSelectedPiece() tested");

        ChessPiece p = model.getChessPieces().getFirst();
        model.selectPiece(p);
        if (!model.getSelectedPiece().equals(p))
            throw new AssertionError("model.selectPiece(piece) did not select piece");
        System.out.println("model.selectPiece(piece) tested");

        Position pos = model.getChessPieces().getLast().getPosition();
        model.selectPiece(pos);
        if (!model.getSelectedPiece().equals(model.getChessPieces().getLast()))
            throw new AssertionError("model.selectPiece(position) did not select by position");
        System.out.println("model.selectPiece(position) tested");
    }

    public static void getCurrentPlayerTest(GameModel model) {
        if (model.getCurrentPlayer() != Color.WHITE)
            throw new AssertionError("model.getCurrentPlayer(): first player is not white!");
        System.out.println("model.getCurrentPlayer() tested");

        model.changePlayer();
        if (model.getCurrentPlayer() != Color.BLACK)
            throw new AssertionError("model.getCurrentPlayer(): player after change is not black!");
        System.out.println("model.changePlayer() and model.getCurrentPlayer() tested");
    }

    public static void addLastTest(GameModel model) {
        int size = model.getChessPieces().size();
        Position position = model.getChessPieces().getLast().getPosition();
        model.getChessPieces().addLast(new Queen(position, Color.BLACK));
        if (model.getChessPieces().size() != size)
            throw new AssertionError("ChessPieces.addLast() did not check for same position");
        System.out.println("tested ChessPieces.addLast() to check for same position");
    }

    public static void main(String[] args) throws IOException {
        GameModel model = new GameModel();

        FileWriter fileWriter = new FileWriter("TestMain.output.log");
        PrintWriter pw = new PrintWriter(fileWriter);
        pw.println("created game model:");
        pw.println(model);

        getCurrentPlayerTest(model);
        selectPieceTest(model);
        getChessPiecesTest(model);

        pw.println("added white queen at C3");
        pw.println(model);

        getPromotablePiecesTest(model);

        GameModel customGame1 = new GameModel(PopulateBoard.noPawnsBoard());
        pw.println("made custom game:");
        pw.println(customGame1);
        ChessPiece A1 = customGame1.getChessPieces().getPiece(new Position(SquareName.A1));
        pw.println("piece at A1: "  + A1);
        pw.close();
    }
}
