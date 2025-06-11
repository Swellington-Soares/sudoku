package dev.swell.sudoku;

import dev.swell.sudoku.core.Difficulty;
import dev.swell.sudoku.core.Position;
import dev.swell.sudoku.core.Sudokable;
import dev.swell.sudoku.core.SudokuState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.Consumer;

public class SudokuViewModel implements Sudokable {

    private final IntegerProperty[][] gridModelView = new IntegerProperty[9][9];
    private final BooleanProperty[][] fixedGrid = new SimpleBooleanProperty[9][9];
    private final BooleanProperty isSolved = new SimpleBooleanProperty(false);
    private final BooleanProperty winner = new SimpleBooleanProperty(false);

    public BooleanProperty winnerProperty() {
        return winner;
    }

    private Consumer<List<Position>> effectCallback;
    private Runnable resetEffectCallback;


    public void setEffectHandler(Consumer<List<Position>> callback) {
        this.effectCallback = callback;
    }

    public void setResetEffectHandler(Runnable callback) {
        this.resetEffectCallback = callback;
    }

    private boolean gameStarted = false;

    private final Sudoku sudoku;

    private final StringConverter<Number> converter = new StringConverter<>() {
        @Override
        public String toString(Number number) {
            return (number == null || number.intValue() == 0) ? "" : number.toString();
        }

        @Override
        public Number fromString(String string) {
            if (string == null || string.trim().isEmpty()) {
                return 0;
            }
            try {
                return Integer.parseInt(string.trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    };

    public SudokuViewModel() {
        gameStarted = false;
        sudoku = new Sudoku();
        sudoku.setSudokable(this);
        prepareData();
    }

    private void prepareData() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                gridModelView[i][j] = new SimpleIntegerProperty(0);
                fixedGrid[i][j] = new SimpleBooleanProperty(false);
                int finalI = i;
                int finalJ = j;
                gridModelView[i][j].addListener((observable, oldValue, newValue) -> {
                    if (gameStarted) {
                        sudoku.makeMove(finalI, finalJ, newValue.intValue());
                    }
                });
            }
        }
    }


    public void newGame(Difficulty difficulty) {
        gameStarted = false;
        sudoku.newGame(difficulty);
        winner.set(false);
        isSolved.set(false);
        resetEffect();
        var grid = sudoku.getGameGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                gridModelView[i][j].setValue(grid[i][j]);
                fixedGrid[i][j].setValue(grid[i][j] == 0);
            }
        }
        gameStarted = true;
    }


    public void bindBoard(TextField[][] gridButtons) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Bindings.bindBidirectional(
                        gridButtons[i][j].textProperty(),
                        gridModelView[i][j],
                        converter
                );
                gridButtons[i][j].setTextFormatter(new TextFormatter<>(change -> {
                            String newText = change.getControlNewText();
                            return newText.matches("[1-9]?") ? change : null;
                        })
                );
                gridButtons[i][j].editableProperty().bind(fixedGrid[i][j]);
            }
        }
    }

    public IntegerProperty[][] getGridModelView() {
        return gridModelView;
    }

    public BooleanProperty solvedProperty() {
        return isSolved;
    }

    public void solverGame() {
        if (!gameStarted) return;
        if (sudoku.solverGame()) {
            gameStarted = false;
            isSolved.set(true);
            var grid = sudoku.getGameGrid();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    gridModelView[i][j].setValue(grid[i][j]);
                }
            }
        }
    }


    private void applyEffect(List<Position> list) {
        if (effectCallback != null) effectCallback.accept(list);
    }


    private void resetEffect() {
        if (resetEffectCallback != null) resetEffectCallback.run();
    }

    @Override
    public void playEvent(SudokuState state, List<Position> cell) {
        switch(state) {
            case ERROR -> applyEffect(cell);
            case WINNER -> winnerGame();
            case SUCCESS -> resetEffect();
            default -> {}
        }
    }

    private void winnerGame() {
        gameStarted = false;
        winner.set(true);
    }

    public String getBoardAsString() {
        return sudoku.printBoard();
    }
}
