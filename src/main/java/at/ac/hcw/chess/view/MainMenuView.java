package at.ac.hcw.chess.view;

import at.ac.hcw.chess.controller.MenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.Objects;

public class MainMenuView implements Builder<Region> {

    private final MenuController controller;

    public MainMenuView(MenuController controller) {
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

        Label title = new Label("SCHACH");
        title.getStyleClass().add("menu-title");

        TilePane buttonPane = new TilePane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setHgap(20);
        buttonPane.setVgap(20);
        buttonPane.setPrefColumns(2);

        Button vsPlayerBtn = createMenuButton("1 vs 1 Spiel");
        vsPlayerBtn.setOnAction(e -> controller.showGameMenu());

        Button demoBtn = createMenuButton("Demospiel");
        demoBtn.setDisable(true);

        Button specialBtn = createMenuButton("Spezialspiel");
        specialBtn.setDisable(true);

        Button exitBtn = createMenuButton("Beenden");
        exitBtn.setOnAction(e -> controller.exitApplication());

        buttonPane.getChildren().addAll(vsPlayerBtn, demoBtn, specialBtn, exitBtn);

        root.getChildren().addAll(title, buttonPane);
        return root;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-tile-button");
        button.setPrefSize(250, 150);
        return button;
    }
}
