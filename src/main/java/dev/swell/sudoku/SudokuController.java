package dev.swell.sudoku;

import dev.swell.sudoku.core.Difficulty;
import dev.swell.sudoku.core.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;


public class HelloController {

    private final TextField[][] cells = new TextField[9][9];

    @FXML
    private Button resolverButton;

    @FXML
    private GridPane board;

    private SudokuViewModel viewModel;

    @FXML
    private ComboBox<Difficulty> cbDifficult;

    @FXML
    void onButtonNewGameAction() {
        newGame();
    }

    private void newGame() {
        viewModel.newGame(cbDifficult.getValue());
    }

    @FXML
    void onButtonSolverAction() {
        viewModel.solverGame();
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