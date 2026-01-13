package at.ac.hcw.chess.view;

import at.ac.hcw.chess.controller.MenuController;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.PopulateBoard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.Objects;

public class GameMenuView implements Builder<Region> {

    private final MenuController controller;

    public GameMenuView(MenuController controller) {
        this.controller = controller;
    }

    @Override
    public Region build() {
        VBox root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("menu.css")).toExternalForm()
        );
        root.getStyleClass().add("menu-root");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("\u2190");
        backBtn.getStyleClass().add("back-button");
        backBtn.setOnAction(e -> controller.showMainMenu());

        Label title = new Label("SPIELMENU");
        title.getStyleClass().add("menu-title");

        header.getChildren().addAll(backBtn, title);

        TilePane buttonPane = new TilePane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setHgap(20);
        buttonPane.setVgap(20);
        buttonPane.setPrefColumns(2);

        Button vsFriendBtn = createMenuButton("Gegen einen Freund");
        vsFriendBtn.setOnAction(e -> controller.startGame(null));

        Button vsBotBtn = createMenuButton("Gegen einen Bot");
        vsBotBtn.setOnAction(e -> {
            controller.startGame(controller.customGame(PopulateBoard.classicGameBoard(), Color.BLACK, 10));
        });

        buttonPane.getChildren().addAll(vsFriendBtn, vsBotBtn);

        root.getChildren().addAll(header, buttonPane);
        return root;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-tile-button");
        button.setPrefSize(250, 150);
        return button;
    }
}
