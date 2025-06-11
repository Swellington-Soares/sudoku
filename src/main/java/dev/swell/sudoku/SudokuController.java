package dev.swell.sudoku;

import dev.swell.sudoku.core.Difficulty;
import dev.swell.sudoku.core.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;



public class SudokuController {

    private final TextField[][] cells = new TextField[9][9];
    private SudokuViewModel viewModel;

    private final FileChooser  fileChooser = new FileChooser();

    @FXML
    private Button resolverButton;

    @FXML
    private GridPane board;

    @FXML
    private ComboBox<Difficulty> cbDifficult;

    @FXML
    void onButtonNewGameAction() {
        newGame();
    }

    @FXML
    void onButtonSolverAction() {
        viewModel.solverGame();
    }

    @FXML
    void onButtonExportBoardAction() throws FileNotFoundException {
        fileChooser.setTitle("Exportar tabuleiro?");
        fileChooser.setInitialFileName("board.txt");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new  FileChooser.ExtensionFilter("Arquivo de texto", "*.txt")
        );

        File file = fileChooser.showSaveDialog(board.getScene().getWindow());
        if (file != null) {
            var boardText = viewModel.getBoardAsString();
            var write = new PrintWriter(file);
            write.print(boardText);
            write.close();
        }
    }

    @FXML
    void onButtonSaveImageAction() throws IOException {
        fileChooser.setTitle("Exportar image?");
        fileChooser.setInitialFileName("board.png");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new  FileChooser.ExtensionFilter("Imagem PNG", "*.png")
        );

        File file = fileChooser.showSaveDialog(board.getScene().getWindow());
        if (file != null) {
           var image = board.snapshot(null, null);
           ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        }
    }

    private void newGame() {
        viewModel.newGame(cbDifficult.getValue());
    }

    private void assembleBoard(){
        board.getChildren().clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField btn = new TextField("");
                btn.maxWidthProperty().setValue(Double.MAX_VALUE);
                btn.maxHeightProperty().setValue(Double.MAX_VALUE);

                String top = ((j % 3 == 0)) ? "2px" : "0px";
                String right = (i == 8) ? "2px" : "0px";
                String bottom = (j == 8) ? "2px" : "0px";
                String left = (i % 3 == 0) ? "2px" : "0px";

                String style = String.format(
                        "-fx-border-color: black; -fx-border-width: %s %s %s %s;",
                        top, right, bottom, left
                );
                btn.getStyleClass().add("grid-item");
                btn.setStyle(style);
                cells[i][j] = btn;
                board.add(btn, i, j);
            }
        }
    }

    public void setViewModel(SudokuViewModel sudokuViewModel) {
        viewModel = sudokuViewModel;
        assembleBoard();
        viewModel.newGame(Difficulty.EASY);
        viewModel.bindBoard(cells);
        resolverButton.disableProperty().bind(viewModel.solvedProperty());
        viewModel.setEffectHandler(this::setEffect);
        viewModel.setResetEffectHandler(this::resetEffectAll);
        viewModel.winnerProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                ShowWinnerDialog();
            }
        });
    }

    private void ShowWinnerDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Winner");
        alert.setContentText("Parabéns! Você conseguiu resolver.");
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(_a -> newGame());
    }

    private void addError(Node node) {
        String styles = node.getStyle();
        styles = styles.replace("-fx-text-fill: black;", "");
        if (!styles.contains("-fx-text-fill: red;")) {
            styles += "-fx-text-fill: red;";
        }
        node.setStyle(styles);
    }

    private void removeError(Node node) {
        String styles = node.getStyle();
        styles = styles.replace("-fx-text-fill: red;", "");
        if (!styles.contains("-fx-text-fill: black")) {
            styles += "-fx-text-fill: black;";
        }
        node.setStyle(styles);
    }

    private void setEffect(List<Position> cell) {
        for (Position pos : cell) {
            TextField textField = cells[pos.row()][pos.col()];
            addError(textField);
        }
    }

    private void resetEffectAll() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                removeError(cells[i][j]);
            }
        }
    }

    @FXML
    private void initialize() {

        cbDifficult.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Difficulty item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle());
            }
        });

        cbDifficult.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Difficulty item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle());
            }
        });

        cbDifficult.getItems().addAll(Difficulty.values());

        cbDifficult.getSelectionModel().select(0);
    }
}