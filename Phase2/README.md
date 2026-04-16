# Chess Game - Phase 2 (GUI)

## Team Members
- Martins Edun
- Denzel Juwah

## Class
CS 3354 - Object-Oriented Programming

## Project Description
Phase 2 of the Chess Game project. A fully functional Graphical User Interface (GUI)
chess game built with Java Swing. Two players can play chess on the same computer.

## Features Implemented
- 8x8 chessboard with alternating light and dark squares
- All 32 chess pieces displayed at correct starting positions using Unicode symbols
- Click-to-move: click a piece then click destination to move
- Drag and drop: drag a piece and drop it on the destination square
- Piece capturing: moving onto an opponent's piece removes it from the board
- King capture: a pop-up window declares the winner and the game ends
- **Extra Feature 1 - Menu Bar**: New Game, Save Game, Load Game, Exit
- **Extra Feature 2 - Settings**: Customize board colors and board size in real-time
- **Extra Feature 3 - Game History + Undo**: Move history panel, captured pieces display, Undo button

## How to Compile
Open a terminal and navigate to the Phase2 folder:

```bash
cd ~/Documents/ChessProject/Phase2
mkdir -p out
javac -d out gui/ChessBoardPanel.java gui/ChessFrame.java gui/HistoryPanel.java gui/SettingsDialog.java model/BoardModel.java model/MoveRecord.java pieces/*.java utils/Position.java Main.java
```

## How to Run
After compiling, run:

```bash
java -cp out Main
```

## Project Structure
```
Phase2/
├── Main.java                  - Entry point
├── gui/
│   ├── ChessFrame.java        - Main window and menu bar
│   ├── ChessBoardPanel.java   - Board rendering, click and drag interaction
│   ├── HistoryPanel.java      - Move history and undo button
│   └── SettingsDialog.java    - Board customization settings
├── model/
│   ├── BoardModel.java        - Board state and piece management
│   └── MoveRecord.java        - Stores move data for history and undo
├── pieces/
│   ├── Piece.java             - Abstract base class for all pieces
│   ├── Pawn.java
│   ├── Rook.java
│   ├── Knight.java
│   ├── Bishop.java
│   ├── Queen.java
│   └── King.java
└── utils/
    └── Position.java          - Board coordinate representation
```
