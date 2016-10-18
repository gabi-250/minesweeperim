package minesweeper;

public class Cell {

    private boolean hasMine;
    private boolean isFlagged;
    private boolean isExplored;
    private int adjacentMines;

    public Cell(boolean hasMine) {

        this.hasMine = hasMine;
        this.isFlagged = false;
        this.isExplored = false;
        this.adjacentMines = 0;
    }

    public boolean hasMine() {

        return hasMine;
    }

    public boolean isFlagged() {

        return isFlagged;
    }

    public boolean isExplored() {

        return isExplored;
    }

    public void addMine() {

        hasMine = true;
    }

    public void incrementAdjacentMines() {

        adjacentMines += 1;
    }

    public void explore() {

        isExplored = true;
    }

    public void toggleFlag() {

        if (isFlagged) {
            isFlagged = false;
        }
        else {
            isFlagged = true;
        }
    }

    public int getAdjacentMines() {

        return adjacentMines;
    }

    public String toString() {

        return hasMine? "M" : "O";
    }
}
