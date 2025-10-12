package at.ac.hcw.chess;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    @FXML
    public ChessBoard chessBoard;

    // This method is called automatically after FXML loading
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chessBoard.populateBoard();
    }
}
