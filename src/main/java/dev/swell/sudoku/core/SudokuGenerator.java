package dev.swell.sudoku.core;

import java.util.*;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private static final int SUBGRID = 3;
    private final int[][] board = new int[SIZE][SIZE];
    private final Random random = new Random();

    public SudokuGenerator() {
        generateFullGrid();
    }

    public int[][] getBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    private void generateFullGrid() {
        fillGrid(0, 0);
    }

    private boolean fillGrid(int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;

        List<Integer> numbers = generateShuffledNumbers();

        for (int num : numbers) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;
                if (fillGrid(nextRow, nextCol)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private List<Integer> generateShuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) numbers.add(i);
        Collections.shuffle(numbers, random);
        return numbers;
    }

    private boolean isSafe(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int boxRowStart = (row / SUBGRID) * SUBGRID;
        int boxColStart = (col / SUBGRID) * SUBGRID;

        for (int i = 0; i < SUBGRID; i++) {
            for (int j = 0; j < SUBGRID; j++) {
                if (board[boxRowStart + i][boxColStart + j] == num) return false;
            }
        }
        return true;
    }


}
