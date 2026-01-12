package at.ac.hcw.chess.view;

import at.ac.hcw.chess.controller.GameController;
import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.utils.CastleEvent;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.MoveList;
import at.ac.hcw.chess.model.utils.Position;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;
import at.ac.hcw.chess.model.utils.ChessPieceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameView implements Builder<Region> {

    private static final int BOARD_SIZE = 8;
    private static final int UI_SIZE = BOARD_SIZE + 2;

    private final GameModel model;
    private final GameController controller;

    private GridPane board;
    private VBox capturedWhitePanel;
    private VBox capturedBlackPanel;
    private final List<Node> pieceViews;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
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
        setupGridConstraints(board);

        StackPane boardContainer = new StackPane(board);
        boardContainer.getStyleClass().add("board-container");

        // Create side panels for captured pieces
        capturedWhitePanel = createCapturedPiecesPanel("Captured", "White");
        capturedBlackPanel = createCapturedPiecesPanel("Captured", "Black");

        VBox leftWrapper = new VBox(capturedWhitePanel);
        leftWrapper.setAlignment(Pos.CENTER);
        leftWrapper.setPadding(new Insets(0, 15, 0, 0));

        VBox rightWrapper = new VBox(capturedBlackPanel);
        rightWrapper.setAlignment(Pos.CENTER);
        rightWrapper.setPadding(new Insets(0, 0, 0, 15));

        root.setCenter(boardContainer);
        root.setLeft(leftWrapper);
        root.setRight(rightWrapper);
        redraw();
        return root;
    }

    public void redraw() {
        board.getChildren().clear();
        pieceViews.clear();
        drawBoard();
        drawPieces();
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
                    square.getStyleClass().add((row + col) % 2 == 0 ? "light-square" : "dark-square");

                    square.setOnMouseClicked(e ->
                            controller.clickChessBoard(e, square)
                    );

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
    }

    /* =========================
       PIECES
       ========================= */

    private void drawPieces() {
        for (ChessPiece piece : model.getChessPieces()) {
            Node view = createPieceView(piece);
            view.setOnMouseClicked(e -> controller.clickChessBoard(e, view));
            view.getStyleClass().add("chess-piece");
            pieceViews.add(view);
            board.add(view, piece.getPosition().getColumn(), piece.getPosition().getRow());
        }
    }

    private ImageView createPieceView(ChessPiece piece) {
        String color = piece.getColor() == Color.WHITE ? "white" : "black";
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
        String color = piece.getColor() == Color.WHITE ? "white" : "black";
        String type = piece.getClass().getSimpleName().toLowerCase();
        String path = "/images/" + color + "_" + type + ".png";

        ImageView view = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm())
        );

        view.setPreserveRatio(true);
        view.setFitWidth(35);
        view.setFitHeight(35);
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
}
