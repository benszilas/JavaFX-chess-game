package at.ac.hcw.chess.view;

import at.ac.hcw.chess.controller.GameController;
import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.utils.Color;
import at.ac.hcw.chess.model.utils.Position;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.Objects;

public class GameView implements Builder<Region> {

    private static final int BOARD_SIZE = 8;
    private static final int UI_SIZE = 10;

    private final GameModel model;
    private final GameController controller;

    private GridPane board;
    private final StackPane[][] squares = new StackPane[BOARD_SIZE + 1][BOARD_SIZE + 1];

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
    }

    @Override
    public Region build() {
        BorderPane root = new BorderPane();
        board = new GridPane();

        setupGridConstraints(board);
        board.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("chess-board.css")).toExternalForm()
        );

        root.setCenter(board);
        redraw();
        return root;
    }

    public void redraw() {
        board.getChildren().clear();
        drawBoard();
        drawPieces();
    }

    /* =========================
       BOARD
       ========================= */

    private void drawBoard() {
        for (int row = 0; row < UI_SIZE; row++) {
            for (int col = 0; col < UI_SIZE; col++) {

                Node node;

                if (row == 0 && col > 0 && col < UI_SIZE - 1) {
                    node = new Label(String.valueOf((char) ('A' + col - 1)));
                }
                else if (col == 0 && row > 0 && row < UI_SIZE - 1) {
                    node = new Label(String.valueOf(BOARD_SIZE + 1 - row));
                }
                else if (row > 0 && row < UI_SIZE - 1 && col > 0 && col < UI_SIZE - 1) {

                    int chessCol = col;
                    int chessRow = BOARD_SIZE + 1 - row;

                    StackPane square = new StackPane();
                    boolean light = (row + col) % 2 == 0;
                    square.getStyleClass().add(light ? "light-square" : "dark-square");

                    square.setOnMouseClicked(e ->
                            controller.onSquareClicked(new Position(chessCol, chessRow))
                    );

                    squares[chessCol][chessRow] = square;
                    node = square;
                }
                else {
                    node = new Pane();
                }

                board.add(node, col, row);
            }
        }
    }

    /* =========================
       PIECES
       ========================= */

    private void drawPieces() {
        for (ChessPiece piece : model.getChessPieces()) {

            ImageView view = createPieceView(piece);

            if (piece == model.getSelectedPiece()) {
                view.getStyleClass().add("selected-piece");
            }

            StackPane square =
                    squares[piece.getPosition().getColumn()]
                            [piece.getPosition().getRow()];

            square.getChildren().add(view);
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
