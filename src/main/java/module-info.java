module at.ac.hcw.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires at.ac.hcw.chess;

    opens at.ac.hcw.chess to javafx.fxml;
    exports at.ac.hcw.chess;
}