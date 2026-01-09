package at.ac.hcw.chess.model.utils;

import javafx.event.Event;
import javafx.event.EventType;

public class CastleEvent extends Event {
    public static final EventType<CastleEvent> CASTLE =
            new EventType<>(Event.ANY, "CASTLE");
    private final Position newRookPosition;
    private final Position oldRookPosition;

    public CastleEvent(Position oldRookPosition, Position newRookPosition) {
        super(CASTLE);
        this.newRookPosition = newRookPosition;
        this.oldRookPosition = oldRookPosition;
    }

    public Position getNewRookPosition() {
        return newRookPosition;
    }

    public Position getOldRookPosition() {
        return oldRookPosition;
    }
}
