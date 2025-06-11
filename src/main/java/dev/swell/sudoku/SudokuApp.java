package dev.swell.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApp.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();
        SudokuController controller = fxmlLoader.getController();
        controller.setViewModel(new SudokuViewModel());
        Scene scene = new Scene(root);

        stage.setTitle("Sudoku");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}