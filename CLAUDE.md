# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JavaFX-based chess game for two human players. Implements complete chess rules including castling, en passant, pawn promotion, check, checkmate, and stalemate detection.

**Technology Stack:**
- Java 21 (module system)
- JavaFX 21.0.6 (controls, fxml)
- Gradle 8.13 with Kotlin DSL
- JUnit 5.12.1

## Build Commands

**Run the application:**
```bash
./gradlew run
```

**Build the project:**
```bash
./gradlew build
```

**Run tests (JUnit):**
```bash
./gradlew test
```

**Create distributable package:**
```bash
./gradlew jlink
# Output: build/distributions/app-{platform}.zip
```

**Run manual test classes (not JUnit):**
```bash
# These are main() method test classes in src/main/java
./gradlew run --args="at.ac.hcw.chess.model.chessPieces.ChessPieceTest"
./gradlew run --args="at.ac.hcw.chess.controller.GameControllerTest"
```

**Clean build artifacts:**
```bash
./gradlew clean
```

## Architecture

### MVC Pattern

The codebase follows strict Model-View-Controller separation:

**Entry Point:** `ChessBoardOnlyApp.java` creates `GameController`, retrieves the view, and sets up the JavaFX Scene (900x900 window).

**Flow:**
```
ChessBoardOnlyApp → GameController → GameModel (state)
                                  → GameView (UI)
```

**Controller (`controller/GameController.java`):**
- Central mediator between Model and View
- Handles all user interactions via `clickChessBoard()`
- Orchestrates move flow: selection → validation → execution → state update
- Manages check/checkmate detection pipeline via `lookForGameOver()`

**Model (`model/GameModel.java`):**
- Maintains game state: piece list, current player, selected piece, move history
- Provides simple API: `selectPiece()`, `getCurrentPlayer()`, `changePlayer()`
- Initialized with `PopulateBoard.classicGameBoard()` by default

**View (`view/GameView.java`):**
- Implements `Builder<Region>` pattern returning complete UI
- 10x10 GridPane (8x8 board + rank/file labels)
- Responsive layout with percentage-based constraints
- Image assets: `/images/{color}_{piecetype}.png`
- `redraw()` rebuilds entire board, `addHighlight()`/`removeHighlight()` for selection feedback

### Chess Piece Hierarchy

**Base Class:** `chessPieces/ChessPiece.java` (abstract)

**Properties:**
- `position: Position` - Current board location (column/row 1-8)
- `color: Color` - WHITE or BLACK
- `possibleMoves: MoveList` - All reachable squares including opponent pieces
- `hasMoved: boolean` - Tracks first move (castling/pawn rules)
- `piecesOnBoard: ChessPieceList` - Reference for validation

**Two-Tier Move Validation:**

1. **Possible Moves** (`setPossibleMoves()`) - Raw attack squares
   - Includes captures, ignores self-check
   - Calculated for opponent pieces each turn
   - Used for check detection

2. **Legal Moves** (`setLegalMoves()`) - Filtered to prevent self-check
   - Calls `setPossibleMoves()` then `preventSelfCheck()`
   - Considers pins via `pinnedMoves()`
   - Only these can be executed

**Move Generation Methods:**
- `range(colDelta, rowDelta)` - Generates moves in one direction until blocked
- `straightRange()` - Uses `range()` in 4 cardinal directions (Rook movement)
- `diagonalRange()` - Uses `range()` in 4 diagonal directions (Bishop movement)
- `stepOrJump()` - For pieces that jump or step (Knight, King)

**Piece Implementations:**

| Piece | Move Logic | Special Behavior |
|-------|-----------|------------------|
| `Rook.java` | `straightRange()` | - |
| `Bishop.java` | `diagonalRange()` | - |
| `Queen.java` | `straightRange() + diagonalRange()` | - |
| `Knight.java` | `stepOrJump()` with L-shaped offsets | Can jump over pieces |
| `King.java` | `stepOrJump()` in 8 directions | Overrides `preventSelfCheck()`, handles castling via `tryAddCastleMove()`, fires `CastleEvent` |
| `Pawn.java` | Forward 1/2, diagonal captures | `direction` and `targetRank` based on color, overrides `setLegalMoves()` to remove diagonal moves without target |

### Game State Update Cycle

**Move Flow in GameController:**

1. User clicks square → `clickChessBoard()`
2. If piece at position matches current player → `selectPiece()` highlights it
3. If click on legal move square → `moveSelectedPiece()`
   - Calls `take()` to remove captured pieces
   - Calls `piece.moveTo()` with ImageView reference
   - Fires `CastleEvent` if king moved 2+ squares
4. After valid move:
   - `changePlayer()` toggles WHITE/BLACK
   - `lookForGameOver()` recalculates legal moves and checks game status

**Check/Checkmate Detection Pipeline (`lookForGameOver()`):**

```
updateMoves()
  ├─ For opponent pieces: setPossibleMoves() [raw attacks]
  └─ For current player: setLegalMoves() [filtered by pins/checks]

handleCheck()
  ├─ If not in check: addCastleMoves()
  ├─ If single check: tryToBlockCheck() [can piece block/capture?]
  └─ If double check: forceKingToMove() [only king moves allowed]

handleDraw()
  └─ If no legal moves AND not in check: emit draw event
```

**King's `preventSelfCheck()`:**
- Temporarily removes king and pieces it attacks from board
- Recalculates opponent possible moves
- Tests each potential king move by creating temporary King
- Keeps only moves where temp king isn't attacked

### Utility Classes

**`model/utils/Position.java`:**
- Represents board square with `column` and `row` (both 1-8)
- Methods: `isInStraightLine()`, `isDiagonal()`, `columnDelta()`, `rowDelta()`
- `toString()` converts to chess notation (A1, B2, etc.)

**`model/utils/SquareName.java`:**
- Enum of all 64 squares (A1-H8) for convenient Position creation

**`model/utils/Color.java`:**
- Enum: WHITE, BLACK

**`model/utils/MoveList.java`:**
- Extends `ArrayList<Position>`
- Overrides `add()` to prevent duplicate positions

**`model/utils/ChessPieceList.java`:**
- Extends `ArrayList<ChessPiece>`
- Methods: `getPiece(Position)`, `getPiece(column, row)`, `findPieces(Class, Color)`

**`model/utils/PopulateBoard.java`:**
- Static factory methods for board configurations:
  - `classicGameBoard()` - Standard starting position
  - `noPawnsBoard()` - For endgame testing
  - `checkmate1()`, `draw1()`, `mateInOne1()` - Test scenarios

**`model/utils/CastleEvent.java`:**
- Custom JavaFX Event fired by King during `moveTo()` when castling
- Contains old and new rook positions
- Caught by GameView, triggers `controller.castleRook()`

**`model/utils/MoveRecord.java`:**
- Immutable record: piece, oldPosition, newPosition
- Currently defined but not used in gameplay

### Event System

**Piece/Square Clicks:**
```
Square/Piece ImageView clicked
    ↓
view.setOnMouseClicked(e → controller.clickChessBoard(e, node))
```

**Castling:**
```
King.moveTo() detects 2+ square move
    ↓
Fires CastleEvent on board GridPane
    ↓
board.addEventHandler(CastleEvent.CASTLE, e → controller.castleRook(...))
```

### Coordinate System

**Board Layout:**
- Column: 1-8 (displayed as A-H, left to right)
- Row: 1-8 (1 = white's back rank, 8 = black's back rank)
- GridPane indices match column/row directly

**Pawn Direction Convention:**
- WHITE: `direction = +1` (moves from row 2 toward row 8)
- BLACK: `direction = -1` (moves from row 7 toward row 1)

### Testing

**Test Classes (in src/main/java):**
- `model/chessPieces/ChessPieceTest.java` - Manual test runner with `main()`
  - Writes output to `TestMain.output.log`
  - `compareMoves()` validates move lists
  - `rookTest()` tests rook movement

- `controller/GameControllerTest.java` - Manual test runner
  - Tests checkmate detection (`test1`)
  - Tests draw/stalemate (`test2`)
  - Tests mate-in-one puzzle (`test3`)

**Note:** These are NOT JUnit tests despite the naming. They use `main()` methods and custom assertion logic.

### Module System

Module name: `at.ac.hcw.chess`

**Dependencies:**
- `javafx.controls`
- `javafx.fxml`
- `java.desktop`

**Exports:**
- Package `at.ac.hcw.chess` (opens to javafx.fxml)

## Development Conventions

**Image Asset Naming:**
- Path: `/src/main/resources/images/{color}_{piecetype}.png`
- Examples: `white_king.png`, `black_pawn.png`
- Piece type determined dynamically via `piece.getClass().getSimpleName().toLowerCase()`

**Move Validation Pattern:**
Always use the two-tier system:
```java
// For opponent pieces (check detection)
piece.setPossibleMoves(piecesOnBoard);

// For current player pieces (legal moves)
piece.setLegalMoves(piecesOnBoard);
```

**Console Logging:**
Debug output exists in `GameController` methods: `selectPiece()`, `moveSelectedPiece()`, `take()`, `changePlayer()`. These log piece selections and moves during development.

## Common Patterns

**Creating Custom Board Configurations:**
```java
ChessPieceList pieces = new ChessPieceList();
pieces.add(new King(Color.WHITE, new Position(SquareName.E1)));
pieces.add(new Rook(Color.WHITE, new Position(SquareName.A1)));
// ... add more pieces
GameModel model = new GameModel(pieces);
```

**Testing Specific Scenarios:**
Use `PopulateBoard` static methods for predefined test positions (checkmate, draw, puzzles).

**Extending with New Piece Types:**
1. Extend `ChessPiece`
2. Implement `setPossibleMoves(ChessPieceList)`
3. Use base class methods: `range()`, `straightRange()`, `diagonalRange()`, `stepOrJump()`
4. Override `setLegalMoves()` or `preventSelfCheck()` if special rules apply
5. Add piece image to `/src/main/resources/images/`
