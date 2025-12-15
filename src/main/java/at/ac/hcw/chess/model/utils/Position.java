package at.ac.hcw.chess.model.utils;

import javafx.beans.property.SimpleIntegerProperty;

public class Position implements Cloneable {
    private final SimpleIntegerProperty column;
    private final SimpleIntegerProperty row;
    public static int MIN = 1;
    public static int MAX = 8;

    public Position(int column, int row) throws IndexOutOfBoundsException {
        this.column = new SimpleIntegerProperty();
        this.setColumn(column);
        this.row = new SimpleIntegerProperty();
        this.setRow(row);
    }

    public Position(SquareName squareName) {
        int ordinal = squareName.ordinal();
        this.column = new SimpleIntegerProperty((ordinal % Position.MAX) + Position.MIN);
        this.row = new SimpleIntegerProperty((ordinal / Position.MAX) + Position.MIN);
    }

    public int getColumn() {
        return column.get();
    }

    public int getRow() {
        return row.get();
    }

    protected void setColumn(int column) throws IndexOutOfBoundsException {
        if (column < Position.MIN || column > Position.MAX) {
            throw new IndexOutOfBoundsException("Column " + column + " out of range of the chess board!");
        }
        this.column.set(column);
    }

    protected void setRow(int row) throws IndexOutOfBoundsException {
        if (row < Position.MIN || row > Position.MAX) {
            throw new IndexOutOfBoundsException("Row " + row + " out of range of the chess board!");
        }
        this.row.set(row);
    }

    @Override
    public Object clone() {
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
                (char) (this.getColumn() - Position.MIN + 'A'),
                (char) (this.getRow() + '0')
        };
        return new String(chessPosition);
    }
}
