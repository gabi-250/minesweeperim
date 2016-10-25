package minesweeper;

import java.util.Random;

public class MineSweeper {

    private Cell[][] cells;
    private int flagged;
    private int cellsExplored;
    private int mines;
    private int rows;
    private int columns;
    private boolean gameOver;
    private boolean bomb;

    public static final int defaultRowCount = 10;
    public static final int defaultColumnCount = 10;
    public static final int defaultMineCount = 10;
    public static final int defaultCellCount = 100;

    public MineSweeper() {

        this(10, 10, 10);
    }

    public MineSweeper(int rows, int columns, int mines) {

        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        resetGame();
    }

    public void resetGame() {

        gameOver = false;
        bomb = false;
        flagged = 0;
        cellsExplored = 0;
        createCells(rows, columns);
        updateAdjacentMineCount();
    }

    private void createCells(int rows, int columns) {

        cells = new Cell[rows][columns];
        Random random = new Random();
        int minesLeft = mines;

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {

                int randValue = random.nextInt(rows);
                // a value of 0 means a mine needs to placed in a cell
                if (minesLeft > 0 && randValue == 0) {
                    cells[i][j] = new Cell(true);
                    --minesLeft;
                }
                else {
                    cells[i][j] = new Cell(false);
                }
            }
        }

        // ensure all mines are placed on the board
        while (minesLeft > 0) {

            int mineRow = random.nextInt(rows);
            int mineCol = random.nextInt(columns);

            while (cells[mineRow][mineCol].hasMine()) {
                mineRow = random.nextInt(rows);
                mineCol = random.nextInt(columns);
            }
            cells[mineRow][mineCol].addMine();
            --minesLeft;
        }
    }

    private void updateAdjacentMineCount() {

        int[] rowMove = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnMove = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int i = 0; i < cells.length; ++i) {
            for (int j = 0; j < cells[0].length; ++j) {
                if (cells[i][j].hasMine()) {
                    for (int k = 0; k < 8; ++k) {
                        if (validMove(i + rowMove[k], j + columnMove[k])) {
                            cells[i + rowMove[k]][j + columnMove[k]].incrementAdjacentMines();
                        }
                    }
                }
            }
        }
    }

    public int getMineCount() {

        return mines;
    }

    public int getFlagged() {

        return flagged;
    }

    public int getCellsExplored() {

        return cellsExplored;
    }

    public int getTotalCells() {

        return cells.length * cells[0].length;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getMines() {
        return mines;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }


    public boolean isExplored(int row, int column) {

        return cells[row][column].isExplored();
    }

    public String getCellText(int row, int column) {

        if (cells[row][column].isExplored()) {

            if (!cells[row][column].hasMine()) {
                return cells[row][column].getAdjacentMines() + "";
            }
            else {
                return "X";
            }
        }
        else if (cells[row][column].isFlagged()) {
            return "F";
        }
        return "  ";
    }

    public boolean gameOver() {

        return gameOver;
    }

    public boolean win() {

        return cellsExplored + flagged == cells.length * cells[0].length
                && !bomb;
    }

    public void enterCell(int row, int column) {

        if(!cells[row][column].isFlagged()) {
            if (!cells[row][column].isExplored()) {
                if (!cells[row][column].hasMine()) {
                    expandCells(row, column);
                }
                else {
                    revealCell(row, column);
                    bomb = true;
                    gameOver = true;
                }
            }
        }
    }

    private void expandCells(int row, int column) {

        if (!cells[row][column].hasMine() && !gameOver) {
            revealCell(row, column);
            int[] rowMove = {-1, 0, 1, 0};
            int[] columnMove = {0, 1, 0, -1};

            if (getAdjacentMines(row, column) == 0 && !cells[row][column].hasMine()) {
                for (int i = 0; i < 4; ++i) {
                    if (validMove(row + rowMove[i], column + columnMove[i]) &&
                        !cells[row + rowMove[i]][column + columnMove[i]].isExplored()
                        && !cells[row + rowMove[i]][column + columnMove[i]].isFlagged()) {
                        expandCells(row + rowMove[i], column + columnMove[i]);
                    }
                }
            }
        }
    }

    public void toggleFlag(int row, int column) {

        if (!cells[row][column].isFlagged() && flagged < mines
            && !cells[row][column].isExplored()) {
            flagged += 1;
            cells[row][column].toggleFlag();
        }
        else if (cells[row][column].isFlagged()) {
            flagged -= 1;
            cells[row][column].toggleFlag();
        }
    }

    public int getAdjacentMines(int row, int column) {

        return cells[row][column].getAdjacentMines();
    }

    private void revealCell(int row, int column) {

        cells[row][column].explore();
        cellsExplored += 1;
        gameOver |= (cellsExplored + flagged == cells.length * cells.length);
    }

    private boolean validMove(int row, int column) {
        return (row >= 0 && row < cells.length &&
                column >= 0 && column < cells[0].length);
    }

    public void printBoard() {

        for (int i = 0; i < cells.length; ++i) {
            for (int j = 0; j < cells[i].length; ++j) {
                System.out.print(cells[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public void printAdjacentMineCount() {

        for (int i = 0; i < cells.length; ++i) {
            for (int j = 0; j < cells[i].length; ++j) {
                System.out.print(cells[i][j].getAdjacentMines() + " ");
            }
            System.out.print("\n");
        }
    }
}
