package at.ac.hcw.chess.view;

import at.ac.hcw.chess.controller.MenuController;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.PopulateBoard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.Objects;

public class MenuView implements Builder<Region> {
    private final MenuController controller;
    private final MenuType type;

    public enum MenuType {
        MAIN_MENU,
        GAME_MENU
    }

    public MenuView(MenuController controller, MenuType type) {
        this.controller = controller;
        this.type = type;
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

        Label title = new Label(this.getType());
        title.getStyleClass().add("menu-title");

        TilePane buttonPane = new TilePane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setHgap(20);
        buttonPane.setVgap(20);
        buttonPane.setPrefColumns(2);

        if (type == MenuType.MAIN_MENU)
            buttonPane.getChildren().addAll(mainMenuButtons());
        else if (type == MenuType.GAME_MENU)
            buttonPane.getChildren().addAll(gameMenuButtons());

        root.getChildren().addAll(title, buttonPane);
        return root;
    }

    private ObservableList<Node> mainMenuButtons() {
        Button vsPlayerBtn = createMenuButton("Classic chess");
        vsPlayerBtn.setOnAction(e -> controller.showGameMenu());

        Button demoBtn = createMenuButton("Demo: mate-in-1");
        demoBtn.setOnAction(e -> {
            controller.showGameMenu();
            controller.selectGame(PopulateBoard.mateInOne1());
        });

        Button noPawns = createMenuButton("Demo: lost pawns");
        noPawns.setOnAction(e -> {
            controller.showGameMenu();
            controller.selectGame(PopulateBoard.noPawnsBoard());
        });

        Button exitBtn = createMenuButton("Exit");
        exitBtn.setOnAction(e -> controller.exitApplication());

        return FXCollections.observableArrayList(vsPlayerBtn, demoBtn, noPawns, exitBtn);
    }

    private ObservableList<Node> gameMenuButtons() {
        Slider depthSlider = new Slider(1, 10, 5);
        depthSlider.setMajorTickUnit(1);
        depthSlider.setMinorTickCount(0);
        depthSlider.getStyleClass().add("menu-slider");

        Label depthLabel = new Label("Bot depth: 5");
        depthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            depthLabel.setText("Bot depth: " + newVal.intValue());
        });
        depthLabel.getStyleClass().add("menu-label");

        ToggleButton botColorButton = new ToggleButton();
        botColorButton.getStyleClass().add("menu-toggle");

        Label botColorLabel = new Label("Bot color: black");
        botColorButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            String color = (newValue) ? "white" : "black";
            botColorLabel.setText("Bot color: " + color);
        });
        botColorLabel.getStyleClass().add("menu-label");

        VBox botSettings = new VBox();
        botSettings.setAlignment(Pos.CENTER);
        botSettings.getChildren().addAll(depthLabel, depthSlider, botColorLabel, botColorButton);
        botSettings.getStyleClass().add("menu-tile-button");

        Button backBtn = createMenuButton("Back");
        backBtn.setOnAction(e -> controller.showMainMenu());

        Button vsFriendBtn = createMenuButton("Play against a friend");
        vsFriendBtn.setOnAction(e -> controller.startGame());

        Button vsBotBtn = createMenuButton("Play against a bot");
        vsBotBtn.setOnAction(e -> {
            controller.addBot(botColorButton.isSelected(), depthSlider.valueProperty().intValue());
            controller.startGame();
        });

        return FXCollections.observableArrayList(vsFriendBtn, vsBotBtn, botSettings, backBtn);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-tile-button");
        button.setPrefSize(250, 150);
        return button;
    }

    public String getType() {
        return type.name().toUpperCase().replace("_", " ");
    }
}
