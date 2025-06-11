module dev.swell.sudoku {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.xml;

    opens dev.swell.sudoku to javafx.fxml;
    exports dev.swell.sudoku;
    exports dev.swell.sudoku.core;
    opens dev.swell.sudoku.core to javafx.fxml;
}