package at.ac.hcw.chess.view;

import at.ac.hcw.chess.model.GameModel;
import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import at.ac.hcw.chess.model.chessPieces.Rook;
import at.ac.hcw.chess.model.utils.Color;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameView implements Builder<Region> {
    private final GameModel model;
    private Region viewRoot;
    private GridPane chessBoard;
    private final List<Node> pieceViews;
    private final List<Node> chessSquares;

    public GameView(GameModel model) {
        this.model = model;
        chessSquares = new ArrayList<>();
        pieceViews = new ArrayList<>();
    }

    @Override
    public Region build() {
        BorderPane container = new BorderPane();
        viewRoot = container;
        container.setCenter(createChessBoard());
        container.setTop(new HBox(new Label("Moves placeholder")));
        container.setBottom(new HBox(new Label("Menu placeholder")));
        container.setLeft(new VBox(new Label("White pieces and clock")));
        container.setRight(new VBox(new Label("Black pieces and clock")));
        return container;
    }

    public Region getViewRoot() {
        return viewRoot;
    }

    public List<Node> getChessSquares() {
        return chessSquares;
    }

    public List<Node> getPieceViews() {
        return pieceViews;
    }

    public GridPane getChessBoard() {
        return chessBoard;
    }

    private Region createChessBoard() {
        var board = new GridPane();
        this.chessBoard = board;
        int size = 10;

        board.getStylesheets().add(Objects.requireNonNull(getClass().getResource("chess-board.css")).toExternalForm());

        for (int i = 0; i < size; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / size);
            board.getColumnConstraints().add(col);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / size);
            board.getRowConstraints().add(row);
        }

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Node square;
                if (row == 0 && col != 0 && col != size - 1) {
                    char[] chars = {(char) ((char) col - (char) 1 + 'A'), '\0'};
                    square = new Label(new String(chars));
                } else if (row != 0 && col == 0 && row != size - 1) {
                    square = new Label(String.valueOf(row));
                } else {
                    square = new Region();
                    if (row != 0 && row != size - 1 && col != size - 1) {
                        chessSquares.add(square);
                        square.getStyleClass().add(
                                (row + col) % 2 == 0 ? "light-square" : "dark-square"
                        );
                    }
                }
                board.add(square, col, row);
            }
        }

        var container = new BorderPane(board);
        board.maxWidthProperty().bind(
                Bindings.min(container.widthProperty(), container.heightProperty())
        );
        board.maxHeightProperty().bind(
                board.maxWidthProperty()
        );
        createAndBindPieces(board);
        return container;
    }

    private void createAndBindPieces(GridPane board) {
        for (ChessPiece piece : model.getChessPieces()) {
            ImageView view;
            if (piece.getClass() == Rook.class) {
                if (piece.getColor() == Color.BLACK) {
                    view = new ImageView(
                            new Image(Objects.requireNonNull(getClass().getResource("tower.png")).toExternalForm())
                    );
                } else {
                    //white rook
                    view = new ImageView(
                            new Image(Objects.requireNonNull(getClass().getResource("tower.png")).toExternalForm())
                    );
                }
            } else { // all other pieces
                view = new ImageView(
                        new Image(Objects.requireNonNull(getClass().getResource("placeholder.png")).toExternalForm())
                );
            }

            view.setPreserveRatio(true);
            view.setSmooth(true);
            view.setCache(true);

            board.add(view, piece.getPosition().getColumn(), piece.getPosition().getRow());
        }
    }
}
