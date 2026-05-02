# Chess Game - Phase 3 (Integrated GUI + Backend Logic)

## Team Members
- Martins Edun
- Denzel Juwah

## Class
CS 3354 - Object-Oriented Programming

## Project Description
Phase 3 is the final phase of the Chess Game project. It fully integrates the backend chess logic with the Phase 2 GUI to create a complete, functional chess game. The game now enforces all standard chess rules including legal move validation, check detection, checkmate, and stalemate. An AI opponent is also included as extra credit.

## Features Implemented

### Core Requirements
- Full chess rule enforcement — pieces can only move to legal squares
- Legal move highlighting — green dots show valid destination squares when a piece is selected
- Check detection — the King is highlighted in red when in check
- Checkmate detection — game ends with a winner popup when checkmate is reached
- Stalemate detection — game ends in a draw when stalemate is reached
- GUI updates correctly after every move reflecting current game state

### Extra Credit — AI Opponent (Extra Credit B)
- Play against an AI opponent using the minimax algorithm with alpha-beta pruning
- AI searches 3 moves ahead to find the best move
- Enable via Mode menu → "Play vs AI (you are White)"

### Carried Over from Phase 2
- Click-to-move and drag-and-drop piece movement
- Move history panel with real-time tracking
- Captured pieces display
- Undo button that restores previous game state
- Menu bar with New Game, Save Game, Load Game
- Settings window for board color, piece style, and board size customization

## AI Usage Log
This project was developed with AI assistance (Claude by Anthropic):
- MoveValidator logic was designed with AI suggestions for check detection and legal move filtering
- ChessAI minimax algorithm with alpha-beta pruning was implemented using AI-suggested depth-3 search strategy
- AI helped debug move simulation and undo logic in the board model
- All code was reviewed, tested, and verified by the development team

## How to Compile
Open a terminal and navigate to the Phase3 folder:

```bash
cd ~/Documents/ChessProject/Phase3
mkdir -p out
javac -d out gui/HistoryPanel.java gui/SettingsDialog.java gui/ChessBoardPanel.java gui/ChessFrame.java model/BoardModel.java model/MoveRecord.java model/MoveValidator.java model/ChessAI.java pieces/*.java utils/Position.java Main.java
```

## How to Run
After compiling, run:

```bash
java -cp out Main
```

## How to Play
- **Two Player mode** — default mode, both players take turns on the same computer
- **vs AI mode** — go to Mode menu → "Play vs AI". You play as White, AI plays as Black
- **Click to move** — click a piece to see legal moves highlighted in green, then click a destination
- **Drag to move** — drag a piece directly to its destination
- **Undo** — click the Undo button in the history panel to revert the last move
- **Save/Load** — use the Game menu to save and load games

## Project Structure
```
Phase3/
├── Main.java                  - Entry point
├── gui/
│   ├── ChessFrame.java        - Main window, menu bar, mode selection
│   ├── ChessBoardPanel.java   - Board rendering, move validation, AI integration
│   ├── HistoryPanel.java      - Move history and undo button
│   └── SettingsDialog.java    - Board customization settings
├── model/
│   ├── BoardModel.java        - Board state and piece management
│   ├── MoveValidator.java     - Chess rule enforcement, check and checkmate detection
│   ├── ChessAI.java           - AI opponent using minimax with alpha-beta pruning
│   └── MoveRecord.java        - Stores move data for history and undo
├── pieces/
│   ├── Piece.java             - Abstract base class
│   ├── Pawn.java, Rook.java, Knight.java
│   ├── Bishop.java, Queen.java, King.java
└── utils/
    └── Position.java          - Board coordinate representation
```
