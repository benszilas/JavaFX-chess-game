package at.ac.hcw.chess.model.utils;

import javafx.beans.property.SimpleIntegerProperty;

public class Position {
    private final SimpleIntegerProperty column;
    private final SimpleIntegerProperty row;

    public Position(int column, int row) throws IndexOutOfBoundsException {
        this.column = new SimpleIntegerProperty();
        this.setColumn(column);
        this.row = new SimpleIntegerProperty();
        this.setRow(row);
    }

    public Position(SquareName squareName) {
        int ordinal = squareName.ordinal();
        this.column = new SimpleIntegerProperty((char)(ordinal % 8) + 'A');
        this.row = new SimpleIntegerProperty((char)(ordinal % 8) + '1');
    }

    public int getColumn() {
        return column.get();
    }

    public int getRow() {
        return row.get();
    }

    protected void setColumn(int column) throws IndexOutOfBoundsException {
        if (column < 1 || column > 8) {
            throw new IndexOutOfBoundsException("Column " + column + " out of range of the chess board!");
        }
        this.column.set(column);
    }

    protected void setRow(int row) throws IndexOutOfBoundsException {
        if (row < 1 || row > 8) {
            throw new IndexOutOfBoundsException("Row " + row + " out of range of the chess board!");
        }
        this.row.set(row);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Position(this.getColumn(), this.getRow());
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass()
                && ((Position) obj).getColumn() == this.getColumn()
                && ((Position) obj).getRow() == this.getRow();
    }

    @Override
    public String toString() {
        char[] chessPosition = {
                (char) (this.getColumn() - 1 + 'A'),
                (char) (this.getRow() + '0')
        };
        return new String(chessPosition);
    }
}
