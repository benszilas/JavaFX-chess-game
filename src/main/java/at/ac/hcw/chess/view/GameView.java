package at.ac.hcw.chess.view;

import at.ac.hcw.chess.controller.GameController;
import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.utils.*;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;
import at.ac.hcw.chess.model.utils.ChessPieceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameView implements Builder<Region> {

    private static final int BOARD_SIZE = 8;
    private static final int UI_SIZE = BOARD_SIZE + 2;

    private final GameModel model;
    private final GameController controller;
    private final Runnable exitCallback;

    private BorderPane rootNode;
    private GridPane board;
    private VBox capturedWhitePanel;
    private VBox capturedBlackPanel;
    private final List<Node> pieceViews;

    public GameView(GameModel model, GameController controller, Runnable exitCallback) {
        this.model = model;
        this.controller = controller;
        this.exitCallback = exitCallback;
        this.pieceViews = new ArrayList<>();
    }

    @Override
    public Region build() {
        BorderPane root = new BorderPane();
        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("chess-board.css")).toExternalForm()
        );
        root.getStyleClass().add("game-root");
        root.setPadding(new Insets(20));

        board = new GridPane();

        StackPane boardContainer = new StackPane(board);
        boardContainer.getStyleClass().add("board-container");
        boardContainer.maxWidthProperty().bind(boardContainer.heightProperty());
        setupGridConstraints(board);
        drawBoard();
        drawPieces();

        // Create side panels for captured pieces
        capturedWhitePanel = createCapturedPiecesPanel("Captured pieces", "White");
        capturedBlackPanel = createCapturedPiecesPanel("Captured pieces", "Black");

        VBox leftWrapper = new VBox(capturedWhitePanel);
        leftWrapper.setAlignment(Pos.CENTER);
        leftWrapper.setPadding(new Insets(0, 15, 0, 0));
        leftWrapper.getChildren().add(createExitButton(Color.WHITE));

        VBox rightWrapper = new VBox(capturedBlackPanel);
        rightWrapper.setAlignment(Pos.CENTER);
        rightWrapper.setPadding(new Insets(0, 0, 0, 15));
        rightWrapper.getChildren().add(createExitButton(Color.BLACK));

        Button exitButton = createExitButton(null);
        board.getChildren().addLast(exitButton);
        GridPane.setConstraints(exitButton, UI_SIZE - 1, UI_SIZE - 1);

        root.setCenter(boardContainer);
        root.setLeft(leftWrapper);
        root.setRight(rightWrapper);

        rootNode = root;
        return root;
    }

    public GridPane getBoard() {
        return board;
    }

    public void addHighlight(ChessPiece piece) {
        if (piece != null) {
            Node pieceView = chessBoardChildNode(piece.getPosition(), ImageView.class);
            if (pieceView != null)
                pieceView.getStyleClass().add("selected-piece");
        }
    }

    public void removeHighlight(ChessPiece piece) {
        if (piece != null) {
            Node pieceView = chessBoardChildNode(piece.getPosition(), ImageView.class);
            if (pieceView != null)
                pieceView.getStyleClass().removeIf(name -> name.equals("selected-piece"));
        }
    }

    public Node chessBoardChildNode(Position position, Class<?> wantedClass) {
        for (Node node : board.getChildren()) {
            if (!wantedClass.isInstance(node)) continue;
            int c = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
            int r = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);

            if (c == position.getColumn() && r == position.getRow())
                return node;
        }
        return null;
    }

    public void addHighlight(MoveList moves) {
        for (Position move : moves) {
            chessBoardChildNode(move, StackPane.class).getStyleClass().add("square-possible-move");
            var opponentPieceView = chessBoardChildNode(move, ImageView.class);
            if (opponentPieceView != null)
                opponentPieceView.getStyleClass().add("opponent-piece");
        }
    }

    public void removeHighlight(MoveList moves) {
        for (Position move : moves) {
            chessBoardChildNode(move, StackPane.class).getStyleClass().removeIf(name -> name.equals("square-possible-move"));
            var opponentPieceView = chessBoardChildNode(move, ImageView.class);
            if (opponentPieceView != null)
                opponentPieceView.getStyleClass().removeIf(name -> name.equals("opponent-piece"));
        }
    }

    public List<Node> getPieceViews() {
        return pieceViews;
    }

    /* =========================
       BOARD
       ========================= */

    private void drawBoard() {
        for (int row = 0; row < UI_SIZE; row++) {
            for (int col = 0; col < UI_SIZE; col++) {

                Node node;

                if (row == 0 && col > 0 && col < UI_SIZE - 1) {
                    Label label = new Label(String.valueOf((char) ('A' + col - 1)));
                    label.getStyleClass().add("board-label");
                    node = label;
                } else if (col == 0 && row > 0 && row < UI_SIZE - 1) {
                    Label label = new Label(String.valueOf(row));
                    label.getStyleClass().add("board-label");
                    node = label;
                } else if (row > 0 && row < UI_SIZE - 1 && col < UI_SIZE - 1) {
                    StackPane square = new StackPane();
                    try {
                        Position position = new Position(col, row);
                    } catch (IndexOutOfBoundsException e) {
                        throw new RuntimeException("Position with column " + col + " and row " + row + " is out of bounds: " + e);
                    }
                    square.getStyleClass().add((row + col) % 2 == 0 ? "dark-square" : "light-square");

                    square.setOnMouseClicked(controller::clickChessBoard);

                    node = square;
                } else {
                    node = new Pane();
                }

                board.add(node, col, row);
            }
        }
        board.addEventHandler(CastleEvent.CASTLE, e -> {
            controller.castleRook(e.getOldRookPosition(), e.getNewRookPosition());
        });
        board.addEventHandler(EnPassantEvent.EN_PASSANT, e -> {
            controller.take(model.getChessPieces().getPiece(e.getTargetPawn()));
        });
        board.addEventHandler(PromotionEvent.PROMOTION, e -> {
            showPromotionAlert(e.getPawn());
        });
    }

    /* =========================
       PIECES
       ========================= */

    private void drawPieces() {
        for (ChessPiece piece : model.getChessPieces()) {
            Node view = createPieceView(piece);
            view.setOnMouseClicked(controller::clickChessBoard);
            if (piece.getColor() == Color.WHITE)
                view.setCursor(Cursor.HAND);
            pieceViews.add(view);
            board.add(view, piece.getPosition().getColumn(), piece.getPosition().getRow());
        }
    }

    public void showPromotionAlert(ChessPiece pawn) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        ButtonType skip = new ButtonType("Skip this promotion", ButtonBar.ButtonData.OK_DONE);
        alert.setTitle("Pawn Promotion for " + model.getCurrentPlayer());
        alert.setHeaderText("Choose a piece");
        alert.getButtonTypes().add(skip);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);

        model.currentUniquePromotable().forEach(chessPiece -> {
            ImageView view = createPieceView(chessPiece);
            content.getChildren().add(view);

            view.setOnMouseClicked(e -> {
                content.getChildren().remove(view);
                pieceViews.add(view);
                board.getChildren().add(view);
                view.setOnMouseClicked(controller::clickChessBoard);
                controller.take(pawn);
                controller.promote(chessPiece, view, pawn.getPosition().clone());
                alert.close();
            });
        });
        if (content.getChildren().isEmpty())
            return;

        alert.setGraphic(content);
        alert.showAndWait();
    }

    public void showBotError(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setHeaderText("Bot move");
        alert.setContentText(message);
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType retry = new ButtonType("Retry", ButtonBar.ButtonData.APPLY);
        ButtonType back = new ButtonType("Back to menu", ButtonBar.ButtonData.BACK_PREVIOUS);

        alert.getButtonTypes().addAll(ok, retry, back);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == retry) controller.getBotMove();
            else if (result.get() == back) board.fireEvent(new GameEndedEvent());
        }

    }

    public void showResult(String title, String message) {
        board.setDisable(true);
        System.out.println(model);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
        board.fireEvent(new GameEndedEvent());
    }

    private Button createExitButton(Color resigned) {
        String title = (resigned == null) ? "Exit" : "Resign for " + resigned.name();
        Button exitButton = new Button(title);

        if (resigned == null)
            exitButton.setOnAction(e -> exitCallback.run());
        else {
            exitButton.setOnAction(e -> {
                showResult("Resignation", resigned.name() + " has resigned!");
            });
        }

        exitButton.setAlignment(Pos.BOTTOM_CENTER);
        exitButton.getStyleClass().add("exit-button");
        return exitButton;
    }

    private ImageView createPieceView(ChessPiece piece) {
        String color = piece.getColor().name().toLowerCase();
        String type = piece.getClass().getSimpleName().toLowerCase();
        String path = "/images/" + color + "_" + type + ".png";

        ImageView view = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm())
        );

        view.setPreserveRatio(true);
        view.fitWidthProperty().bind(board.widthProperty().divide(UI_SIZE));
        view.fitHeightProperty().bind(board.heightProperty().divide(UI_SIZE));

        return view;
    }

    /* =========================
       CAPTURED PIECES PANELS
       ========================= */

    private VBox createCapturedPiecesPanel(String title, String subtitle) {
        VBox panel = new VBox(5);
        panel.getStyleClass().add("captured-panel");
        panel.setMinWidth(120);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("captured-panel-title");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("captured-panel-title");

        panel.getChildren().addAll(titleLabel, subtitleLabel);
        return panel;
    }

    public void refreshCapturedPieces() {
        refreshCapturedPanel(capturedWhitePanel, model.getCapturedWhitePieces());
        refreshCapturedPanel(capturedBlackPanel, model.getCapturedBlackPieces());
    }

    private void refreshCapturedPanel(VBox panel, ChessPieceList capturedPieces) {
        // Remove all children except the title labels (first two children)
        if (panel.getChildren().size() > 2) {
            panel.getChildren().remove(2, panel.getChildren().size());
        }

        // Add captured piece images
        for (ChessPiece piece : capturedPieces) {
            ImageView pieceView = createCapturedPieceView(piece);
            panel.getChildren().add(pieceView);
        }
    }

    private ImageView createCapturedPieceView(ChessPiece piece) {
        ImageView view = createPieceView(piece);

        view.fitWidthProperty().bind(new SimpleIntegerProperty(35));
        view.fitWidthProperty().bind(new SimpleIntegerProperty(35));
        view.getStyleClass().add("captured-piece-view");

        return view;
    }

    private void setupGridConstraints(GridPane grid) {
        for (int i = 0; i < UI_SIZE; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / UI_SIZE);
            grid.getColumnConstraints().add(col);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / UI_SIZE);
            grid.getRowConstraints().add(row);
        }
    }

    public void setCursorForPieceViews(ChessPieceList pieces, Cursor cursor) {
        pieces.forEach(piece -> {
            chessBoardChildNode(piece.getPosition(), ImageView.class).setCursor(cursor);
        });
    }

    public void disableButton(Color color) {
        if (color == Color.BLACK)
            rootNode.getRight().setDisable(true);
        else if (color == Color.WHITE)
            rootNode.getLeft().setDisable(true);
    }
}
