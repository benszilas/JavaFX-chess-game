package at.ac.hcw.chess.model.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Position {
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

    public IntegerProperty getColumnProperty() {
        return column;
    }

    public IntegerProperty getRowProperty() {
        return row;
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

    public boolean isInStraightLine(Position otherPosition) {
        return (otherPosition.getRow() - this.getRow() == 0 || otherPosition.getColumn() - this.getColumn() == 0);
    }

    public boolean isDiagonal(Position otherPosition) {
        return (Math.abs(otherPosition.getRow() - this.getRow()) == Math.abs(otherPosition.getColumn() - this.getColumn()));
    }

    /**
     * calculate the column direction from this piece to the other
     *
     * @param other position of the target piece
     * @return -1, 0, 1 depending on whether the target column is smaller, the same, or bigger
     */
    public int columnDelta(Position other) {
        int columnDifference = other.getColumn() - this.getColumn();
        return (columnDifference != 0) ? columnDifference / Math.abs(columnDifference) : 0;
    }

    /**
     * calculate the row direction from this piece to the other
     *
     * @param other position of the target piece
     * @return -1, 0, 1 depending on whether the target row is smaller, the same, or bigger
     */
    public int rowDelta(Position other) {
        int rowDifference = other.getRow() - this.getRow();
        return (rowDifference != 0) ? rowDifference / Math.abs(rowDifference) : 0;
    }

    @Override
    public Position clone() {
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
