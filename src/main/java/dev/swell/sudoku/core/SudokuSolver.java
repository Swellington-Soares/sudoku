package dev.swell.sudoku.core;

public class SudokuSolver {
    private static final int SIZE = 9;
    private int solutionCount = 0;

    public boolean solve(int[][] board) {
        return solveRecursive(board, 0, 0);
    }

    public boolean hasUniqueSolution(int[][] board) {
        solutionCount = 0;
        countSolutions(board, 0, 0);
        return solutionCount == 1;
    }

    private boolean solveRecursive(int[][] board, int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;

        if (board[row][col] != 0) {
            return solveRecursive(board, nextRow, nextCol);
        }

        for (int num = 1; num <= SIZE; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                if (solveRecursive(board, nextRow, nextCol)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private void countSolutions(int[][] board, int row, int col) {
        if (solutionCount > 1) return;

        if (row == SIZE) {
            solutionCount++;
            return;
        }

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;

        if (board[row][col] != 0) {
            countSolutions(board, nextRow, nextCol);
        } else {
            for (int num = 1; num <= SIZE; num++) {
                if (isSafe(board, row, col, num)) {
                    board[row][col] = num;
                    countSolutions(board, nextRow, nextCol);
                    board[row][col] = 0;
                }
            }
        }
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRowStart + i][boxColStart + j] == num) return false;
            }
        }
        return true;
    }

    public boolean isSolved(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int num = board[row][col];
                if (num == 0) return false;

                board[row][col] = 0;
                if (!isSafe(board, row, col, num)) {
                    board[row][col] = num;
                    return false;
                }
                board[row][col] = num;
            }
        }
        return true;
    }

}
