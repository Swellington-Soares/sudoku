package dev.swell.sudoku.core;

import java.util.List;

public interface Sudokable {
    void playEvent(SudokuState state, List<Position> cell);
}
