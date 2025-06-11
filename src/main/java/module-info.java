module dev.swell.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;


    requires org.controlsfx.controls;
    requires java.xml;
    requires java.desktop;

    opens dev.swell.sudoku to javafx.fxml, javafx.graphics, javafx.swing;
    opens dev.swell.sudoku.core to javafx.fxml, javafx.graphics, javafx.swing;
    exports dev.swell.sudoku;
    exports dev.swell.sudoku.core;

}