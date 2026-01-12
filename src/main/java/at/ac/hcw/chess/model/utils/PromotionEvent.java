package at.ac.hcw.chess.model.utils;

import at.ac.hcw.chess.model.chessPieces.ChessPiece;
import javafx.event.Event;
import javafx.event.EventType;

public class PromotionEvent extends Event {
    public static final EventType<PromotionEvent> PROMOTION =
            new EventType<>(Event.ANY, "PROMOTION");
    private final ChessPiece pawn;

    public PromotionEvent(ChessPiece pawn) {
        super(PROMOTION);
        this.pawn = pawn;
    }

    public ChessPiece getPawn() {
        return pawn;
    }
}
