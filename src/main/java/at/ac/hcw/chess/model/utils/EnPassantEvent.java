package at.ac.hcw.chess.model.utils;

import javafx.event.Event;
import javafx.event.EventType;

public class EnPassantEvent extends Event {

    public static final EventType<EnPassantEvent> EN_PASSANT =
            new EventType<>(Event.ANY, "EN_PASSANT");
    private final Position targetPawn;

    public EnPassantEvent(Position targetPawn) {
        super(EN_PASSANT);
        this.targetPawn = targetPawn;
    }

    public Position getTargetPawn() {
        return targetPawn;
    }
}
