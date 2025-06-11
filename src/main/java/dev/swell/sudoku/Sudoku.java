package dev.swell.sudoku;

import dev.swell.sudoku.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sudoku {

    private Sudokable sudokable;

    public void setSudokable(Sudokable sudokable) {
        this.sudokable = sudokable;
    }

    private int[][] sudokuGrid;

    private final SudokuPuzzleBuilder builder = new SudokuPuzzleBuilder();

    private final SudokuSolver solver = new SudokuSolver();

    public int[][] getGameGrid() {
        return sudokuGrid;
    }

    private void notifyEvent(SudokuState state, List<Position> positions) {
        if (sudokable != null) {
            sudokable.playEvent(state, positions);
        }
    }

    public void newGame(Difficulty difficulty) {
        var board = new SudokuGenerator().getBoard();
        sudokuGrid = builder.buildPuzzle(board, difficulty);
    }

    public boolean solverGame() {
        return solver.solve(sudokuGrid);
    }

    public void makeMove(int i, int j, int number) {
        if (number <= 0 || number > 9) {
            return;
        }
        sudokuGrid[i][j] = number;
        var positions = verifyLine(j);

        if (solver.isSolved(sudokuGrid)) {
            notifyEvent(SudokuState.WINNER, positions);
            return;
        }

        if (!positions.isEmpty()) {
            notifyEvent(SudokuState.ERROR, positions);
            return;
        }

        positions = verifyColumn(i);

        if (!positions.isEmpty()) {
            notifyEvent(SudokuState.ERROR, positions);
            return;
        }

        positions = verifyBlock(i / 3, j / 3);
        if (!positions.isEmpty()) {
            notifyEvent(SudokuState.ERROR, positions);
            return;
        }

        notifyEvent(SudokuState.SUCCESS, null);

    }
    public List<Position> verifyLine(int linha) {
        Map<Integer, List<Integer>> values = new HashMap<>();

        for (int col = 0; col < 9; col++) {
            int val = sudokuGrid[linha][col];
            if (val == 0) continue;

            values.computeIfAbsent(val, k -> new ArrayList<>()).add(col);
        }

        List<Position> cells = new ArrayList<>();
        for (var entry : values.entrySet()) {
            if (entry.getValue().size() > 1) {
                for (int c : entry.getValue()) {
                    cells.add(new Position(linha, c));
                }
            }
        }
        return cells;
    }

    public List<Position> verifyColumn(int coluna) {
        Map<Integer, List<Integer>> values = new HashMap<>();

        for (int line = 0; line < 9; line++) {
            int val = sudokuGrid[line][coluna];
            if (val == 0) continue;

            values.computeIfAbsent(val, k -> new ArrayList<>()).add(line);
        }

        List<Position> cells = new ArrayList<>();
        for (var entry : values.entrySet()) {
            if (entry.getValue().size() > 1) {
                for (int l : entry.getValue()) {
                    cells.add(new Position(l, coluna));
                }
            }
        }
        return cells;
    }

    public List<Position> verifyBlock(int lineBlock, int colBlock) {

        Map<Integer, List<Position>> values = new HashMap<>();

        int baseLine = lineBlock * 3;
        int baseColumn = colBlock * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int val = sudokuGrid[baseLine + i][baseColumn + j];
                if (val == 0) continue;

                values.computeIfAbsent(val, k -> new ArrayList<>()).add(new Position(baseLine + i, baseColumn + j));
            }
        }

        List<Position> cells = new ArrayList<>();
        for (var entry : values.entrySet()) {
            if (entry.getValue().size() > 1) {
                cells.addAll(entry.getValue());
            }
        }
        return cells;
    }


    public Sudoku() {
        newGame(Difficulty.EASY);
    }


    public String printBoard() {
        var sb = new StringBuilder();
        for (int row = 0; row < sudokuGrid.length; row++) {
            if (row % 3 == 0 && row != 0) sb.append("------+-------+------\n");
            for (int col = 0; col < sudokuGrid[row].length; col++) {
                if (col % 3 == 0 && col != 0) sb.append("| ");
                sb.append((sudokuGrid[row][col] == 0 ? ". " : sudokuGrid[row][col] + " "));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
