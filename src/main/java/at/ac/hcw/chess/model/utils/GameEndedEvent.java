package at.ac.hcw.chess.model.utils;

import javafx.event.Event;
import javafx.event.EventType;

public class GameEndedEvent extends Event {

    public static final EventType<GameEndedEvent> GAME_ENDED =
            new EventType<>(Event.ANY, "GAME_ENDED");

    public GameEndedEvent() {
        super(GAME_ENDED);
    }
}
