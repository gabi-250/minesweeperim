package gui;

import javafx.scene.control.Button;

public class ButtonCell extends Button {

    private int row;
    private int column;

    public ButtonCell(int row, int column) {

        super("  ");
        this.row = row;
        this.column = column;
    }

    public int getRow() {

        return row;
    }

    public int getColumn() {

        return column;
    }
}
