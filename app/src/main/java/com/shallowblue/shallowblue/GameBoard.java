package com.shallowblue.shallowblue;

/**
 * Created by peter on 3/14/2016.
 */
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public King whiteKing;
    public King blackKing;

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

    public void findKings() {
        for (Piece piece : gameBoard.values()) {
            if (piece instanceof King) {
                if (piece.getColor() == Color.WHITE) whiteKing = (King) piece;
                else blackKing = (King) piece;
            }
        }
    }

    public GameBoard(GameBoard in) {
        synchronized (GameBoard.class) {
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
        synchronized (GameBoard.class) {
            if (legalMovesCache != null) {
                cacheHits++;
                return legalMovesCache;
            }

            List<Move> legalMoves = new ArrayList<Move>();
            List<Piece> pieces = new ArrayList<Piece>();
            for (Piece piece : gameBoard.values()) {
                if (piece != null)
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

            legalMovesCache = legalMoves;
            return legalMoves;
        }
    }

    private boolean move(Move m, boolean clearRedoStack) {
        // Check that the move is valid.
        if (m == null) return false;
        if (gameBoard.get(m.getFrom()) == null) {
            return false;
        }

        // Clear the redo stack now since we know we are proceeding.
        if (clearRedoStack) redoStack.clear();

        // Handle the GameBoard
        Piece moved = gameBoard.get(m.getFrom());
        m = new Move(moved, m.getFrom(), m.getTo());
        executeMoveOnBoard(m);

        if (isCastle(m)) {
            boolean movingToHigherColumn = (m.getFrom().getColumn() < m.getTo().getColumn()) ? true : false;
            Position currentRookPosition = movingToHigherColumn ? new Position(m.getFrom().getRow(), 7) :
                    new Position(m.getFrom().getRow(), 0);
            Position newRookPosition = movingToHigherColumn ? new Position(m.getTo().getRow(), m.getTo().getColumn() - 1)
                    : new Position(m.getTo().getRow(), m.getTo().getColumn() + 1);
            Rook rook = (Rook) gameBoard.get(currentRookPosition);
            if (rook != null) {
                gameBoard.remove(currentRookPosition);
                gameBoard.put(newRookPosition, rook);
                rook.setPosition(newRookPosition);
                rook.incrementNumMoves(1);//rook.setFirstMove(m);
            } else {
                //Log.i("ShallowBlue", "Test");
            }
        }

        // Handle bookkeeping.
        gameHistory.add(m);
        switchPlayerToMove();
        m.getPieceMoved().incrementNumMoves(1);
        return true;
    }

    private void executeMoveOnBoard(Move m) {
        // Handle capturing if necessary.
        if (gameBoard.get(m.getTo()) != null) {
            Piece captured = gameBoard.get(m.getTo());
            m.setPieceCaptured(captured);
            gameBoard.remove(m.getTo());
        } else if (isLegalEnPassant(m)) {
            int direction = playerToMove == Color.WHITE ? -1 : 1;
            Position capturedPosition = new Position(m.getTo().getRow() + direction, m.getTo().getColumn());
            Piece captured = gameBoard.get(capturedPosition);
            m.setPieceCaptured(captured);
            gameBoard.remove(capturedPosition);
        }

        // Update the game board.
        Piece moved = gameBoard.get(m.getFrom());
        gameBoard.put(m.getTo(), moved);
        gameBoard.remove(m.getFrom());

        // Update the piece's position
        m.getPieceMoved().setPosition(m.getTo());
    }

    public boolean isCastle(Move m) {
        return m.getPieceMoved() instanceof King &&
                m.getFrom().getRow() - m.getTo().getRow() == 0 &&
                Math.abs(m.getFrom().getColumn() - m.getTo().getColumn()) > 1;
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
        executeUndoOnBoard(m);

        if (isCastle(m)) {
            boolean movingToHigherColumn = (m.getFrom().getColumn() < m.getTo().getColumn()) ? true : false;
            Position currentRookPosition = movingToHigherColumn ? new Position(m.getTo().getRow(), m.getTo().getColumn() - 1) :
                    new Position(m.getTo().getRow(), m.getTo().getColumn() + 1);
            Rook rook = (Rook) gameBoard.get(currentRookPosition);
            Position oldRookPosition = movingToHigherColumn ? new Position(m.getFrom().getRow(), 7) :
                    new Position(m.getFrom().getRow(), 0);
            gameBoard.remove(currentRookPosition);
            gameBoard.put(oldRookPosition, rook);
            rook.setPosition(oldRookPosition);
            rook.incrementNumMoves(-1);
        }

        gameHistory.remove(gameHistory.size() - 1);
        redoStack.push(m);
        switchPlayerToMove();
        m.getPieceMoved().incrementNumMoves(-1);
        return true;
    }

    private void executeUndoOnBoard(Move m) {
        Piece moved = gameBoard.get(m.getTo());
        gameBoard.remove(m.getTo());
        gameBoard.put(m.getFrom(), moved);
        moved.setPosition(m.getFrom());
        m.getPieceMoved().setPosition(m.getFrom());

        if (m.getPieceCaptured() != null) {
            gameBoard.put(m.getPieceCaptured().getPosition(), m.getPieceCaptured());
        }

        /*
        if (moved instanceof Rook) {
            Rook rook = (Rook) moved;
            if (m.equals(rook.getFirstMove())) rook.setFirstMove(null);
        } else if (moved instanceof King) {
            King king = (King) moved;
            if (m.equals(king.getFirstMove())) king.setFirstMove(null);
        }*/
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

    public boolean isLegalEnPassant(Move m) {
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

        // Handle castling.
        if (isCastle(m)) {
            if (gameBoard.get(m.getFrom()).hasMoved()) {
                this.explanation = "You cannot castle because your king has previously moved.";
                return false;
            }
            boolean movingToHigherColumn = (m.getFrom().getColumn() < m.getTo().getColumn()) ? true : false;
            Position rookPosition = movingToHigherColumn ? new Position(m.getFrom().getRow(), 7) :
                    new Position(m.getFrom().getRow(), 0);
            Piece rook = gameBoard.get(rookPosition);
            if (rook == null || !(rook instanceof Rook) || rook.hasMoved()) {
                this.explanation = "You cannot castle because your rook has previously moved.";
                return false;
            }
            Move rookMove = new Move(rook, rookPosition, m.getFrom());
            if (!movingToHigherColumn && pieceInPath(rookMove) != null) {
                this.explanation = "You cannot castle if there are pieces between your rook and your king.";
                return false;
            } else if ((movingToHigherColumn && isThreatened(new Position(m.getFrom().getRow(), m.getFrom().getColumn() + 1))) ||
                    (!movingToHigherColumn && isThreatened(new Position(m.getFrom().getRow(), m.getFrom().getColumn() - 1)))) {
                this.explanation = "You can castle if you would be moving through a square in which you would be in check.";
                return false;
            } else {
                this.explanation = "You can legally castle.";
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
        for (Move legal : allLegalMoves) {
            if (legal.getFrom().equals(from)) legalMoves.add(legal);
        }
        return legalMoves;
    }

    public String pack() {
        String whiteRook = "";
        String blackRook = "";
        int whiteKingValue = 0;
        int blackKingValue = 0;


        String temp = "";
        try {
            Position p;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
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
            int whiteRookCount = 0;
            int blackRookCount = 0;

            for (Piece piece : gameBoard.values()) {
                    if (piece instanceof Rook ||piece instanceof King) {
                        whiteRook = whiteRook + piece.getNumMoves() + "/";
                    }
            }
            temp += "/" + history;
            temp +=  "/" + whiteRook + "!";
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
        int whiteRookValue1 = 0;
        int blackRookValue1 = 0;
        int whiteRookValue2 = 0;
        int blackRookValue2 = 0;
        int whiteKingValue = 0;
        int blackKingValue = 0;
        legalMovesCache = null;
        gameBoard = new HashMap<Position, Piece>();
        Position p;
        int i = 0;
        gameHistory = new ArrayList<Move>();
        int row = 0;
        int column = 0;
        while (packedString.charAt(i) != '/') {
            if (column == 8) {
                column = 0;
                row++;
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
            column++;
            i++;
        }
        i++;
        while (packedString.charAt(i) != '/') {
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
        i++;
        //temp += whiteRookValue1 + "/" + whiteRookValue2 + "/" + whiteKingValue;
        //temp += "/" + blackRookValue1 + "/" + blackRookValue2 + "/" + blackKingValue;
        /*while(packedString.charAt(i)!='/'){
            whiteRookValue1 = whiteRookValue1 * 10;
            System.out.print(whiteRookValue1 + " * 10 + " + (packedString.charAt(i)-48) + " = ");
            whiteRookValue1 = whiteRookValue1 + packedString.charAt(i)-48;
            i++;
            System.out.print(whiteRookValue1 + "_");
        }
        i++;
        while(packedString.charAt(i)!='/'){
            whiteRookValue2 = whiteRookValue2 * 10;
            System.out.print(whiteRookValue2 + " * 10 + " + (packedString.charAt(i)-48) + " = ");
            whiteRookValue2 = whiteRookValue2 + packedString.charAt(i)-48;
            i++;
            System.out.print(whiteRookValue2 + "_");
        }
        i++;
        while(packedString.charAt(i)!='/'){
            whiteKingValue = whiteKingValue * 10;
            System.out.print(whiteKingValue + " * 10 + " + (packedString.charAt(i)-48) + " = ");
            whiteKingValue = whiteKingValue + packedString.charAt(i)-48;
            i++;
            System.out.print(whiteKingValue + "_");
        }
        i++;
        while(packedString.charAt(i)!='/'){
            whiteRookValue1 = blackRookValue1 * 10;
            whiteRookValue1 += packedString.charAt(i)-48;
            i++;
        }
        i++;
        while(packedString.charAt(i)!='/'){
            whiteRookValue1 = blackRookValue2 * 10;
            whiteRookValue1 += packedString.charAt(i)-48;
            i++;
        }
        i++;
        while(packedString.charAt(i)!='/'){
            whiteRookValue1 = blackKingValue * 10;
            whiteRookValue1 += packedString.charAt(i)-48;
            i++;
        }*/
        for (Piece piece : gameBoard.values()) {
            if (packedString.charAt(i-1)!='!') {

                int value = 0;


                if (piece instanceof Rook || piece instanceof King) {
                    while (packedString.charAt(i) != '/') {
                        value = value * 10;
                        value += packedString.charAt(i) - 48;

                        i++;
                    }
                    i++;
                    piece.setNumMoves(value);
                }
            }
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

    /*
    public double sbe() {
        int wrookC = 0;
        int wqueenC = 0;
        int wknightC = 0;
        int wbishopC = 0;
        int wpawnC = 0;
        int wkingC = 0;

        int brookC = 0;
        int bqueenC = 0;
        int bknightC = 0;
        int bbishopC = 0;
        int bpawnC = 0;
        int bkingC = 0;

        List<Piece> allPiecesOnBoard = new ArrayList<>();
        List<Piece> whitePieces = new ArrayList<>();
        List<Piece> blackPieces = new ArrayList<>();

        double sum = 0.0;
        for (Position p : gameBoard.keySet()){
            allPiecesOnBoard.add(gameBoard.get(p));
            if (gameBoard.get(p).getColor() == Color.WHITE){
                whitePieces.add(gameBoard.get(p));
            } else {
                blackPieces.add(gameBoard.get(p));
            }
        }

        for (Piece p : whitePieces) {
            if (p instanceof Rook)
                wrookC++;
            else if (p instanceof Queen)
                wqueenC++;
            else if (p instanceof Knight)
                wknightC++;
            else if (p instanceof King)
                wkingC++;
            else if (p instanceof Bishop)
                wbishopC++;
            else if (p instanceof Pawn)
                wpawnC++;
            else
                continue; // null
        }

        for (Piece p : blackPieces){
            if (p instanceof Rook)
                brookC++;
            else if (p instanceof Queen)
                bqueenC++;
            else if (p instanceof Knight)
                bknightC++;
            else if (p instanceof King)
                bkingC++;
            else if (p instanceof Bishop)
                bbishopC++;
            else if (p instanceof Pawn)
                bpawnC++;
            else
                continue; // null
        }
        int playerEval = 0;
        if (playerToMove == Color.WHITE){
            playerEval = -1;
        } else {
            playerEval = 1;
        }


        return sum = playerEval * (1000000*(wkingC - bkingC) + 9*(wqueenC - bqueenC) +
                5*(wrookC - brookC) + 3*(wbishopC - bbishopC + wknightC - bknightC) +
                0.5*(wpawnC - bpawnC));
    }
    */

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

        // Simulate making the move
        executeMoveOnBoard(m);

        // check if the king is threatened
        King king = playerToMove == Color.WHITE ? whiteKing : blackKing;
        if (inCheck()) answer = true;

        // undo the simulated move
        executeUndoOnBoard(m);

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

    public boolean inStaleMate(){
        boolean answer = false;
        if (!inCheck() && getAllLegalMoves().isEmpty()){
            answer = true;
        }
        List<Piece> allPieces = new ArrayList<>();
        for (Position p : gameBoard.keySet()){
            if (gameBoard.get(p) != null){
                Piece piece = gameBoard.get(p);
                allPieces.add(piece);
            }
        }
        if (allPieces.size() == 2) answer = true;
        return answer;
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