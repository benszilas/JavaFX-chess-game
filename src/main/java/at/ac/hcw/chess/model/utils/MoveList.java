package at.ac.hcw.chess.model.utils;

import java.util.ArrayList;
import java.util.Collection;

public class MoveList extends ArrayList<Position> {

    @Override
    public boolean add(Position position) {
        if (this.contains(position))
            return false;
        super.add(position);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Position> moves) {
        boolean modified = false;
        for (Position move : moves) {
            if (this.add(move))
                modified = true;
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("List of Moves: ");
        for (Position p : this) str.append(p).append(" ");
        return str.toString();
    }
}
