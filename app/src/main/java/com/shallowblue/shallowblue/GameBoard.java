package com.shallowblue.shallowblue;

/**
 * Created by peter on 3/14/2016.
 */
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class GameBoard {

    public static GameBoard activeGameBoard;
    public static Map<Position, Piece> customPositions;
    public Map<Position, Piece> gameBoard;
    public List<Move> gameHistory;
    public Color playerToMove;
    private Stack<Move> redoStack;
    private String explanation;

    // Caches to speed up calculations. Only calculate the legal moves for a given position ONCE!
    private List<Move> legalMovesCache;
    private King whiteKing;
    private King blackKing;

    public GameBoard() {
        if (gameBoard == null) {
            gameBoard = new HashMap<Position, Piece>();
            Position p;
            gameBoard.put((p = new Position(0, 0)), new Rook(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 1)), new Knight(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 2)), new Bishop(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 3)), new Queen(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 4)), new King(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 5)), new Bishop(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 6)), new Knight(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 7)), new Rook(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 0)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 1)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 2)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 3)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 4)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 5)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 6)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 7)), new Pawn(p, Color.WHITE));

            gameBoard.put((p = new Position(6, 0)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 1)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 2)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 3)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 4)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 5)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 6)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 7)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 0)), new Rook(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 1)), new Knight(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 2)), new Bishop(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 3)), new Queen(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 4)), new King(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 5)), new Bishop(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 6)), new Knight(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 7)), new Rook(p, Color.BLACK));
        }

        findKings();
        playerToMove = Color.WHITE;
        gameHistory = new ArrayList<Move>();
        redoStack = new Stack<Move>();
        legalMovesCache = null;
    }

    private void findKings() {
        for (Piece piece : gameBoard.values()) {
            if (piece instanceof King) {
                if (piece.getColor() == Color.WHITE) whiteKing = (King) piece;
                else blackKing = (King) piece;
            }
        }
    }

    public GameBoard(GameBoard in) {
        gameBoard = new HashMap<Position, Piece>();
        for (Map.Entry<Position, Piece> e : in.gameBoard.entrySet())
            gameBoard.put(new Position(e.getKey()), Piece.copy(e.getValue()));
        gameHistory = new ArrayList<Move>();
        for (Move m : in.gameHistory) {
            gameHistory.add(new Move(m));
        }
        findKings();
        playerToMove = in.playerToMove();
        redoStack = new Stack<Move>();
        legalMovesCache = null;
    }

    public GameBoard(Map<Position, Piece> map) {
        gameBoard = map;
        gameHistory = new ArrayList<Move>();
        playerToMove = Color.WHITE;
        redoStack = new Stack<Move>();
        legalMovesCache = null;
        findKings();
    }

    public void switchPlayerToMove() {
        this.playerToMove = (playerToMove == Color.WHITE) ? Color.BLACK : Color.WHITE;
        legalMovesCache = null;
    }

    public static int cacheHits = 0;
    public List<Move> getAllLegalMoves() {
        if (legalMovesCache != null) {
            cacheHits++;
            return legalMovesCache;
        }

        long startTime = System.currentTimeMillis();

        List<Move> legalMoves = new ArrayList<Move>();
        List<Piece> pieces = new ArrayList<Piece>();
        for (Piece piece : gameBoard.values()) {
            pieces.add(piece);
        }
        for (Piece piece : pieces) {
            if (piece.getColor() == playerToMove) {
                List<Position> possibleMoves = piece.possibleMoves();
                for (Position position : possibleMoves) {
                    Move move = new Move(piece, piece.getPosition(), position);
                    if (legalMove(move)) legalMoves.add(move);
                }
            }
        }

        long stopTime = System.currentTimeMillis();
        float elapsedTime = (float) ((stopTime - startTime) / 1000.0);
        Log.i("GameBoard", "Legal Moves Time Taken: " + String.format("%.3f", elapsedTime) + " seconds.");

        legalMovesCache = legalMoves;
        return legalMoves;
    }

    private boolean move(Move m, boolean clearRedoStack) {
        // Check that the move is valid.
        if (gameBoard.get(m.getFrom()) == null) {
            return false;
        }

        // Clear the redo stack now since we know we are proceeding.
        if (clearRedoStack) redoStack.clear();

        // Handle capturing if necessary.
        if (gameBoard.get(m.getTo()) != null) {
            m.setPieceCaptured(gameBoard.get(m.getTo()));
            this.gameBoard.remove(m.getTo());
        } else if (isLegalEnPassant(m)) {
            int direction = playerToMove == Color.WHITE ? -1 : 1;
            Position capturedPosition = new Position(m.getTo().getRow() + direction, m.getTo().getColumn());
            m.setPieceCaptured(gameBoard.get(capturedPosition));
            this.gameBoard.remove(capturedPosition);
        }

        // Update the game board.
        gameBoard.put(m.getTo(), gameBoard.get(m.getFrom()));
        gameBoard.remove(m.getFrom());

        // Update the piece's position in both places to be safe.
        gameBoard.get(m.getTo()).setPosition(m.getTo());
        m.getPieceMoved().setPosition(m.getTo());

        // Handle bookkeeping.
        gameHistory.add(m);
        switchPlayerToMove();
        return true;
    }

    public boolean move(Move m) {
        return move(m, true);
    }

    public void addMove(Move m) {
        gameHistory.add(m);
    }

    public boolean put(Piece p) {
        if (gameBoard.get(p.getPosition()) == null) {
            gameBoard.put(p.getPosition(), p);
            return true;
        }
        return false;
    }

    public String toString() {
        return TextUtils.join("\n", gameHistory);
    }

    public boolean undo() { //example move is ka4_b5
        if (gameHistory.isEmpty()) return false;

        Move m = gameHistory.get(gameHistory.size() - 1);

        gameBoard.remove(m.getTo());
        gameBoard.put(m.getFrom(), m.getPieceMoved());
        m.getPieceMoved().setPosition(m.getFrom());

        if (m.getPieceCaptured() != null) {
            gameBoard.put(m.getPieceCaptured().getPosition(), m.getPieceCaptured());
        }

        gameHistory.remove(gameHistory.size() - 1);
        redoStack.push(m);
        switchPlayerToMove();
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }
        Move m = redoStack.pop();
        move(m, false);
        return true;
    }

    public String getLastExplanation() {
        if (this.explanation == null) return "";
        return this.explanation;
    }

    private boolean isLegalEnPassant(Move m) {
        if (!(m.getPieceMoved() instanceof Pawn)) return false; // must being moving a pawn
        if (m.getFrom().getColumn() == m.getTo().getColumn())
            return false; // must be moving diagonally

        Move test = gameHistory.size() > 0 ? gameHistory.get(gameHistory.size() - 1) : null;
        if (test == null) return false;// must have a move made
        if (!(test.getPieceMoved() instanceof Pawn)) return false; // must be a pawn
        if (test.getTo().getRow() != m.getFrom().getRow())
            return false; // that is currently in the same row as us
        if (Math.abs(test.getTo().getColumn() - m.getFrom().getColumn()) != 1)
            return false; // that is one column away
        if (Math.abs(test.getFrom().getRow() - test.getTo().getRow()) != 2)
            return false; // that just performed a double jump
        if (test.getTo().getColumn() != m.getTo().getColumn())
            return false; // and we are moving toward their column.

        // the move meets all the criteria to be a legal en passant
        return true;
    }

    public boolean legalMove(Move m) {
        if (legalMovesCache != null) {
            cacheHits++;
            for (Move legal : legalMovesCache) {
                if (legal.getTo().equals(m.getTo()) && legal.getFrom().equals(m.getFrom())) return true;
            }
            return false;
        }

        // Check to make sure there is a piece in the square and that it belongs to the player
        // whose turn it is.
        if (gameBoard.get(m.getFrom()) == null) {
            this.explanation = "You can only move from a square that contains a piece.";
            return false;
        }

        if (this.playerToMove != gameBoard.get(m.getFrom()).getColor()) {
            this.explanation = "You can only move a piece that is your color.";
            return false;
        }


        // Check to make sure there is not a friendly piece in the square being moved to.
        if (gameBoard.get(m.getTo()) != null &&
                gameBoard.get(m.getTo()).getColor() == gameBoard.get(m.getFrom()).getColor()) {
            this.explanation = "You cannot capture your own piece.";
            return false;
        }

        // Check to make sure the move is possible.
        if (!gameBoard.get(m.getFrom()).possibleMoves().contains(m.getTo())) {
            // TODO: Make each piece have a method to describe how it moves and call it here.
            this.explanation = "This piece does not move this way!";
            return false;
        }

        // Check to make sure a pawn isn't capturing forward or moving diagonally when not capturing.
        if (m.getPieceMoved() instanceof Pawn) {
            if (m.getFrom().getColumn() == m.getTo().getColumn() && gameBoard.get(m.getTo()) != null) {
                this.explanation = "Pawns can only capture enemy pieces diagonally.";
                return false;
            }

            // Check for En Passant.
            if (m.getFrom().getColumn() != m.getTo().getColumn()) {
                if (isLegalEnPassant(m)) {
                    this.explanation = "This pawn can perform en Passant.";
                } else if (gameBoard.get(m.getTo()) == null) { // otherwise the capture is illegal
                    this.explanation = "Pawns can only move diagonally when capturing.";
                    return false;
                }
            }
        }

        // You cannot move through another piece unless you are a Knight.
        if (!(gameBoard.get(m.getFrom()) instanceof Knight)) {
            if (pieceInPath(m) != null) {
                this.explanation = "Only a knight can move through pieces.";
                return false;
            }
        }

        if (movePutsPlayerInCheck(m)) {
            return false;
        }

        return true;
    }

    public List<Move> getLegalMoves(Position from) {
        List<Move> allLegalMoves = getAllLegalMoves();
        List<Move> legalMoves = new ArrayList<Move>();
        for (Move legal : legalMoves) {
            if (legal.getFrom().equals(from)) legalMoves.add(legal);
        }
        return legalMoves;
    }

    public String pack() {
        int value = 0;
        String temp = "";
        try {
            Position p;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    p = new Position(i, j);
                    if (gameBoard.containsKey(p)) {
                        temp += gameBoard.get(p).toString(true);
                    } else {
                        temp += "_";
                    }
                }
            }

            String history = "";
            if (gameHistory.size() == 0) {

            } else if (gameHistory.size() == 1) {
                history = gameHistory.get(0).toString(true);
            } else if (gameHistory.size() == 2) {
                history = gameHistory.get(0).toString(true) + gameHistory.get(1).toString(true);
            } else if (gameHistory.size() >= 3) {
                history = gameHistory.get(0).toString(true) + gameHistory.get(1).toString(true) + gameHistory.get(2).toString(true);
            }
            for (Piece piece : gameBoard.values()) {

                if (piece instanceof Rook) {
                    if (piece.hasMoved() && ((Rook) piece).leftright()) {
                        value += 2;
                    } else if (piece.hasMoved() && !((Rook) piece).leftright()) {
                        value += 3;
                    }
                } else if (piece instanceof King)
                    if (piece.hasMoved()) {
                        value += 5;
                    }
            }
            temp += "\n" + history + "\n" + value;
            return temp;
        } catch (Exception e) {
            //null exception when on gameHistory available
        }
        return temp;
    }


    public static GameBoard unpack(String packedString) {
        return new GameBoard(packedString);
    }

    public GameBoard(String packedString) {
        legalMovesCache = null;
        gameBoard.clear();
        gameHistory.clear();
        int i = 0;
        Position p;
        int row = 0;
        int column = 0;
        while (packedString.charAt(i) != '\n') {
            if (row == 8) {
                row = 0;
                column++;
            }
            if (packedString.charAt(i) != '_') {
                if (packedString.charAt(i) == 'p') {
                    gameBoard.put((p = new Position(row, column)), new Pawn(p, Color.WHITE));
                } else if (packedString.charAt(i) == 'k') {
                    gameBoard.put((p = new Position(row, column)), new King(p, Color.WHITE));
                } else if (packedString.charAt(i) == 'n') {
                    gameBoard.put((p = new Position(row, column)), new Knight(p, Color.WHITE));
                } else if (packedString.charAt(i) == 'b') {
                    gameBoard.put((p = new Position(row, column)), new Bishop(p, Color.WHITE));
                } else if (packedString.charAt(i) == 'r') {
                    gameBoard.put((p = new Position(row, column)), new Rook(p, Color.WHITE));
                } else if (packedString.charAt(i) == 'q') {
                    gameBoard.put((p = new Position(row, column)), new Queen(p, Color.WHITE));
                } else if (packedString.charAt(i) == 'P') {
                    gameBoard.put((p = new Position(row, column)), new Pawn(p, Color.BLACK));
                } else if (packedString.charAt(i) == 'K') {
                    gameBoard.put((p = new Position(row, column)), new King(p, Color.BLACK));
                } else if (packedString.charAt(i) == 'N') {
                    gameBoard.put((p = new Position(row, column)), new Knight(p, Color.BLACK));
                } else if (packedString.charAt(i) == 'B') {
                    gameBoard.put((p = new Position(row, column)), new Bishop(p, Color.BLACK));
                } else if (packedString.charAt(i) == 'R') {
                    gameBoard.put((p = new Position(row, column)), new Rook(p, Color.BLACK));
                } else if (packedString.charAt(i) == 'Q') {
                    gameBoard.put((p = new Position(row, column)), new Queen(p, Color.BLACK));
                }

            }
            row++;
            i++;
        }
        i++;
        while (packedString.charAt(i) != '\n') {
            Move m;
            char movedPiece = packedString.charAt(i);
            int fromRow = Integer.parseInt(packedString.substring(i + 1, i + 2));
            int fromColumn = Integer.parseInt(packedString.substring(i + 2, i + 3));
            char capturedPiece = packedString.charAt(i + 3);
            int toRow = Integer.parseInt(packedString.substring(i + 4, i + 5));
            int toColumn = Integer.parseInt(packedString.substring(i + 5, i + 6));
            Position from = new Position(fromRow, fromColumn);
            Position to = new Position(toRow, toColumn);
            if (packedString.charAt(i) != '_') {
                if (packedString.charAt(i) == 'p') {

                    gameHistory.add(m = new Move(new Pawn(from, Color.WHITE), from, to));
                } else if (packedString.charAt(i) == 'k') {
                    gameHistory.add(m = new Move(new King(from, Color.WHITE), from, to));
                } else if (packedString.charAt(i) == 'n') {
                    gameHistory.add(m = new Move(new Knight(from, Color.WHITE), from, to));
                } else if (packedString.charAt(i) == 'b') {
                    gameHistory.add(m = new Move(new Bishop(from, Color.WHITE), from, to));
                } else if (packedString.charAt(i) == 'r') {
                    gameHistory.add(m = new Move(new Rook(from, Color.WHITE), from, to));
                } else if (packedString.charAt(i) == 'q') {
                    gameHistory.add(m = new Move(new Queen(from, Color.WHITE), from, to));
                } else if (packedString.charAt(i) == 'P') {
                    gameHistory.add(m = new Move(new Pawn(from, Color.BLACK), from, to));
                } else if (packedString.charAt(i) == 'K') {
                    gameHistory.add(m = new Move(new King(from, Color.BLACK), from, to));
                } else if (packedString.charAt(i) == 'N') {
                    gameHistory.add(m = new Move(new Knight(from, Color.BLACK), from, to));
                } else if (packedString.charAt(i) == 'B') {
                    gameHistory.add(m = new Move(new Bishop(from, Color.BLACK), from, to));
                } else if (packedString.charAt(i) == 'R') {
                    gameHistory.add(m = new Move(new Rook(from, Color.BLACK), from, to));
                } else if (packedString.charAt(i) == 'Q') {
                    gameHistory.add(m = new Move(new Queen(from, Color.BLACK), from, to));
                }

            }
            i += 6;
        }
        findKings();
    }

    public Map<Position, Piece> getGameBoard() {
        return gameBoard;
    }

    public List<Move> getGameHistory() {
        return gameHistory;
    }

    public Color playerToMove() {
        return playerToMove;
    }

    public double sbe() {
        double sum = 0.0;
        for (Piece p : gameBoard.values()) {
            double value;
            if (p instanceof Rook)
                value = 5;
            else if (p instanceof Queen)
                value = 9;
            else if (p instanceof Knight)
                value = 3;
            else if (p instanceof King)
                value = 10000;
            else if (p instanceof Bishop)
                value = 3;
            else if (p instanceof Pawn)
                value = 1;
            else
                continue; // null

            if (p.getColor() == Color.WHITE)
                sum += value;
            else
                sum -= value;
        }

        return sum;
    }

    public List<Move> isThreatened(Piece p) {
        List<Move> ret = new ArrayList<Move>();
        for (Map.Entry<Position, Piece> e : gameBoard.entrySet()) {
            if (!(e.getValue().getColor() == playerToMove))
                ret.addAll(getLegalMoves(e.getKey()));
        }
        for (Move e : ret) {
            if (e.getTo() != p.getPosition()) {
                ret.remove(e);
            }
        }
        return ret;
    }

    //TODO Add 50 move/replay/other ties
    public boolean gameOver() {
        List<Move> legalMoves = getAllLegalMoves();
        if (!legalMoves.isEmpty()) return false;
        if (inCheckMate()) {
            this.explanation = "You are in check and have no legal moves. The game is over by checkmate.";
        } else {
            this.explanation = "You are not in check but have no legal moves. The game ends in a stalemate.";
        }
        return true;
    }

    public boolean inCheck() {
        return inCheck(playerToMove, true);
    }

    public boolean inCheck(Color curr, boolean check) {
        King king = curr == Color.WHITE ? whiteKing : blackKing;
        return isThreatened(king.getPosition());
    }

    public boolean movePutsPlayerInCheck(Move m) {
        boolean answer = false;

        // Handle capturing if necessary.
        Piece captured = null;
        if (gameBoard.get(m.getTo()) != null) {
            captured = gameBoard.get(m.getTo());
            gameBoard.remove(m.getTo());
        } else if (isLegalEnPassant(m)) {
            int direction = playerToMove == Color.WHITE ? -1 : 1;
            Position capturedPosition = new Position(m.getTo().getRow() + direction, m.getTo().getColumn());
            captured = gameBoard.get(capturedPosition);
            gameBoard.remove(capturedPosition);
        }

        // simulate making the move
        Piece moved = gameBoard.get(m.getFrom());
        gameBoard.remove(m.getFrom());
        gameBoard.put(m.getTo(), moved);
        moved.setPosition(m.getTo());

        // check if the king is threatened
        King king = playerToMove == Color.WHITE ? whiteKing : blackKing;
        if (inCheck()) answer = true;

        // undo the simulated move
        gameBoard.remove(m.getTo());
        gameBoard.put(m.getFrom(), moved);
        moved.setPosition(m.getFrom());

        if (captured != null) gameBoard.put(captured.getPosition(), captured);

        return answer;
    }

    private Piece pieceInPath(Move move) {
        if (move == null) return null;

        int deltaRow = 0;
        int deltaColumn = 0;

        if (move.getFrom().getRow() != move.getTo().getRow())
            deltaRow = move.getFrom().getRow() > move.getTo().getRow() ? -1 : 1;
        if (move.getFrom().getColumn() != move.getTo().getColumn())
            deltaColumn = move.getFrom().getColumn() > move.getTo().getColumn() ? -1 : 1;

        Position curr = new Position(move.getFrom().getRow() + deltaRow, move.getFrom().getColumn() + deltaColumn);
        while (!curr.equals(move.getTo())) {
            if (gameBoard.get(curr) != null) return gameBoard.get(curr);
            curr = new Position(curr.getRow() + deltaRow, curr.getColumn() + deltaColumn);
        }

        return null;
    }

    public boolean inCheckMate() {
        List<Move> legalMoves = getAllLegalMoves();
        return legalMoves.isEmpty() && inCheck();
    }

    private boolean isThreatened(Position position) {
        Color byPlayer = playerToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
        return isThreatenedByPlayer(position, byPlayer);
    }

    private boolean isThreatenedByPlayer(Position position, Color byPlayer) {
        // In this function, we try to short circuit at quickly as possible.
        // First check for knights. They are always legal to attack their possible moves.
        for (Piece piece : gameBoard.values()) {
            if (piece instanceof Knight && piece.getColor() == byPlayer) {
                if (piece.possibleMoves().contains(position)) return true; // Quit as fast as possible!
            }
        }

        // Now check each of the directions (row, column, diagonal) for an attacking piece.
        // We give up when we find any piece because only knight can go through other pieces.
        // To facilitate code reuse, we will use the pieceInPath function with
        // fake moves that go to a nonexistent square (kinda hacky, I know).
        Move fakeMove = null;
        Piece threat = null;

        // Start with the row (going up). We only care about kings, rooks, and queens.
        if (position.getRow() < 7) {
            fakeMove = new Move(null, position, new Position(8, position.getColumn()));
            threat = pieceInPath(fakeMove);
            if (threat != null) {
                // We aren't threatened by our own color.
                if (threat.getColor() == byPlayer &&
                // We don't care about pieces that can't move to us.
                        !(threat instanceof Pawn || threat instanceof Bishop || threat instanceof Knight) &&
                // We don't care about Kings if they are more than one square away.
                        !(threat instanceof King && Math.abs(position.getRow() - threat.getPosition().getRow()) > 1)) {
                    // If we made it here, the closest piece is threatening. Stop and return true.
                    return true;
                }
            }
        }

        // Now do the same thing, but in the row going down.
        if (position.getRow() > 0) {
            fakeMove = new Move(null, position, new Position(-1, position.getColumn()));
            threat = pieceInPath(fakeMove);
            if (threat != null) {
                // We aren't threatened by our own color.
                if (threat.getColor() == byPlayer &&
                        // We don't care about pieces that can't move to us.
                        !(threat instanceof Pawn || threat instanceof Bishop || threat instanceof Knight) &&
                        // We don't care about Kings if they are more than one square away.
                        !(threat instanceof King && Math.abs(position.getRow() - threat.getPosition().getRow()) > 1)) {
                    // If we made it here, the closest piece is threatening. Stop and return true.
                    return true;
                }
            }
        }

        // Now do the same thing but for the column going right.
        if (position.getColumn() < 7) {
            fakeMove = new Move(null, position, new Position(position.getRow(), 8));
            threat = pieceInPath(fakeMove);
            if (threat != null) {
                // We aren't threatened by our own color.
                if (threat.getColor() == byPlayer &&
                        // We don't care about pieces that can't move to us.
                        !(threat instanceof Pawn || threat instanceof Bishop || threat instanceof Knight) &&
                        // We don't care about Kings if they are more than one square away.
                        !(threat instanceof King && Math.abs(position.getRow() - threat.getPosition().getRow()) > 1)) {
                    // If we made it here, the closest piece is threatening. Stop and return true.
                    return true;
                }
            }
        }

        // Now the column going left.
        if (position.getColumn() > 0) {
            fakeMove = new Move(null, position, new Position(position.getRow(), -1));
            threat = pieceInPath(fakeMove);
            if (threat != null) {
                // We aren't threatened by our own color.
                if (threat.getColor() == byPlayer &&
                        // We don't care about pieces that can't move to us.
                        !(threat instanceof Pawn || threat instanceof Bishop || threat instanceof Knight) &&
                        // We don't care about Kings if they are more than one square away.
                        !(threat instanceof King && Math.abs(position.getRow() - threat.getPosition().getRow()) > 1)) {
                    // If we made it here, the closest piece is threatening. Stop and return true.
                    return true;
                }
            }
        }

        // Now we check the diagonals. We care about pawns and kings one square away, bishops, and queens.
        for (Position diagonal : getDiagonals(position)) {
            fakeMove = new Move(null, position, diagonal);
            threat = pieceInPath(fakeMove);
            if (threat == null) threat = gameBoard.get(diagonal);
            if (threat != null) {
                // We aren't threatened by our own color.
                if (threat.getColor() == byPlayer && !(threat instanceof Rook || threat instanceof Knight)) {
                    boolean oneAway = Math.abs(threat.getPosition().getRow() - position.getRow()) == 1 &&
                            Math.abs(threat.getPosition().getColumn() - position.getColumn()) == 1;
                    if (threat instanceof Queen || threat instanceof Bishop ||
                            (oneAway && (threat instanceof Pawn || threat instanceof King))) {
                        return true;
                    }
                }
            }
        }

        // We've checked everything; I suppose it's safe.
        return false;
    }

    private List<Position> getDiagonals(Position position) {
        List<Position> diagonals = new ArrayList<Position>(4);
        int difference;
        if (position.getRow() != 0 && position.getColumn() != 0) { // Down and to the left
            difference = position.getRow() < position.getColumn() ? position.getRow() : position.getColumn();
            diagonals.add(new Position(position.getRow() - difference, position.getColumn() - difference));
        }
        if (position.getRow() != 7 && position.getColumn() != 0) { // Up and to the left
            difference = 7 - position.getRow() < position.getColumn() ? 7 - position.getRow() : position.getColumn();
            diagonals.add(new Position(position.getRow() + difference, position.getColumn() - difference));
        }
        if (position.getRow() != 7 && position.getColumn() != 7) { // Up and to the right
            difference = 7 - position.getRow() < 7 - position.getColumn() ? 7 - position.getRow() : 7 - position.getColumn();
            diagonals.add(new Position(position.getRow() + difference, position.getColumn() + difference));
        }
        if (position.getRow() != 0 && position.getColumn() != 7) { // Down and to the right
            difference = position.getRow() < 7 - position.getColumn() ? position.getRow() : 7 - position.getColumn();
            diagonals.add(new Position(position.getRow() - difference, position.getColumn() + difference));
        }
        return diagonals;
    }
}