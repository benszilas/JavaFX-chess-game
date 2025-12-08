package at.ac.hcw.chess.model;

import org.junit.jupiter.api.*;

import java.util.Objects;

public class GameModelTest {
    @Test
    public void createGameModel() {
        GameModel model = new GameModel();

        model.selectPiece(model.getChessPieces().getFirst());

        if (!model.getSelectedPiece().getPosition().equals(
                model.getChessPieces().getFirst().getPosition()
        )) throw new AssertionError();

        if (!Objects.equals(model.getSelectedPiece().getPosition().toString(),
                model.getChessPieces().getFirst().toString())
        ) throw new AssertionError();

    }

    @Test
    void getChessPieces() {
    }

    @Test
    void getPromotablePieces() {
    }

    @Test
    void getSelectedPiece() {
    }

    @Test
    void selectPiece() {
    }

    @Test
    void testSelectPiece() {
    }

    @Test
    void getCurrentPlayer() {
    }

    @Test
    void changePlayer() {
    }
}
