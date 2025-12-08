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
                (char) this.getRow()
        };
        return new String(chessPosition);
    }

    public enum SquareName {
        A1, B1, C1, D1, E1, F1, G1, H1,
        A2, B2, C2, D2, E2, F2, G2, H2,
        A3, B3, C3, D3, E3, F3, G3, H3,
        A4, B4, C4, D4, E4, F4, G4, H4,
        A5, B5, C5, D5, E5, F5, G5, H5,
        A6, B6, C6, D6, E6, F6, G6, H6,
        A7, B7, C7, D7, E7, F7, G7, H7,
        A8, B8, C8, D8, E8, F8, G8, H8,
    }
}
