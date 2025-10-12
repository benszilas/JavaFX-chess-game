module at.ac.hcw.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens at.ac.hcw.chess to javafx.fxml;
    exports at.ac.hcw.chess;
}