package at.ac.hcw.chess.model.utils;

import java.util.ArrayList;

public class MoveList extends ArrayList<Position> {

    @Override
    public boolean add(Position position) {
        if (this.contains(position))
            return false;
        super.add(position);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("List of Moves: ");
        for (Position p : this) str.append(p);
        return str.toString();
    }
}
