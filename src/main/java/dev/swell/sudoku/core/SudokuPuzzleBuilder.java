package dev.swell.sudoku.core;

import org.w3c.dom.ranges.RangeException;

import java.security.InvalidParameterException;
import java.util.Random;

public class SudokuPuzzleBuilder {
    private static final int SIZE = 9;
    private final SudokuSolver solver = new SudokuSolver();
    private final Random random = new Random();

    public int[][] buildPuzzle(int[][] solutionBoard, Difficulty level) {
        if (level.getLevel()>55) throw new InvalidParameterException("Nível máximo da dificuldade não pode ultrapassar 55.");
        int[][] puzzle = copyBoard(solutionBoard);
        int cellsToRemove = getCellsToRemoveByDifficulty(level);

        int removed = 0;
        while (removed < cellsToRemove) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (puzzle[row][col] == 0) continue;

            int backup = puzzle[row][col];
            puzzle[row][col] = 0;

            if (!solver.hasUniqueSolution(copyBoard(puzzle))) {
                puzzle[row][col] = backup;
            } else {
                removed++;
            }
        }
        return puzzle;
    }

    private int getCellsToRemoveByDifficulty(Difficulty difficulty) {
        return difficulty.getLevel();
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

}
