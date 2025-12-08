package at.ac.hcw.chess.view;

import at.ac.hcw.chess.model.GameModel;
import javafx.scene.layout.StackPane;

public class GameView extends StackPane {
    private final GameModel model;

    public GameView(GameModel model) {
        this.model = model;
    }
}
