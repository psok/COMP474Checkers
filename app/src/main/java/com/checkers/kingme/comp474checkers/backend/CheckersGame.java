package com.checkers.kingme.comp474checkers.backend;

import android.util.Log;

import com.checkers.kingme.comp474checkers.model.GameListener;
import com.checkers.kingme.comp474checkers.model.NetworkUpdateObserver;

import org.apache.http.client.RedirectException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;


/* CheckersGame Class
 * Author: Carlos
 * Description: Implements the game of checkers logic
 */
public class CheckersGame {
    // Exception to be thrown in case of programmer calling moveTo without having called pickUp first
    static class MoveWithoutPickException extends RuntimeException {

    }

    private NetworkUpdateObserver nullNUO = new NetworkUpdateObserver() {
        public void onCompleteMove(int from, char type, int to) {
            return;
        }
    };

    private CurrentBoard board;
    private Color turn;
    private List<String> moveList;
    private GameListener listener;

    private int toMove; // this integer stores the position of the piece currently picked up
    private boolean jumps; // this flag is used to stop non-jump moves when jumps are possible
    private GameMode gameMode;

    private NetworkUpdateObserver nuo = nullNUO;

    // Constructor. Does a fresh start.
    public CheckersGame(GameListener listener)//players? system?
    {
        nuo = nullNUO;
        board = new CurrentBoard(); //
        turn = Color.BLACK;
        moveList = new ArrayList<String>();
        this.listener = listener;
        listener.onBegin(board.getBoard());
        listener.onNewTurn(turn);
    }

    public CheckersGame(GameListener listener, GameMode gameMode)//players? system?
    {
        this(listener);
        this.gameMode = gameMode;
    }

    // Takes two positions diagonally two squares away in the board and returns the position of the
    // square in between
    private int between(int here, int there) {
        int difference, between;

        // off bounds
        if (here < 1
                || here > 32
                || there < 1
                || there > 32) {
            return 0;
        }

        difference = there - here;

        switch (difference) {
            case 9:
                if ((between = checkLR(here)) == checkUL(there))
                    return between;
                break;
            case 7:
                if ((between = checkLL(here)) == checkUR(there))
                    return between;
                break;
            case -7:
                if ((between = checkUR(here)) == checkLL(there))
                    return between;
                break;
            case -9:
                if ((between = checkUL(here)) == checkLR(there))
                    return between;
                break;

            default:
                return 0;
        }

        return 0;
    }

    // Given a position, determines if there's a piece of the current turn colour that can perform
    // a capture right now (returns true if so).
    private boolean canJump(int square) {
        int neighbor, nextNeighbor;
        Piece piece = board.getPiece(square);

        if (piece != null &&
                piece.getColor() == turn) {

            if (turn == Color.BLACK || piece.getRank() == Rank.KING) {
                neighbor = checkLL(square);
                if (neighbor != 0
                        && !board.isEmpty(neighbor)
                        && board.getPiece(neighbor).getColor() != turn
                        && (nextNeighbor = checkLL(neighbor)) != 0
                        && board.isEmpty(nextNeighbor)) {
                    return true;
                }

                neighbor = checkLR(square);
                if (neighbor != 0
                        && !board.isEmpty(neighbor)
                        && board.getPiece(neighbor).getColor() != turn
                        && (nextNeighbor = checkLR(neighbor)) != 0
                        && board.isEmpty(nextNeighbor)) {
                    return true;
                }
            }

            if (turn == Color.RED || piece.getRank() == Rank.KING) {
                neighbor = checkUL(square);
                if (neighbor != 0
                        && !board.isEmpty(neighbor)
                        && board.getPiece(neighbor).getColor() != turn
                        && (nextNeighbor = checkUL(neighbor)) != 0
                        && board.isEmpty(nextNeighbor)) {
                    return true;
                }

                neighbor = checkUR(square);
                if (neighbor != 0
                        && !board.isEmpty(neighbor)
                        && board.getPiece(neighbor).getColor() != turn
                        && (nextNeighbor = checkUR(neighbor)) != 0
                        && board.isEmpty(nextNeighbor)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Given a square number, determines if there's a piece of the current turn color that can
    // move the an empty adjacent square right now (returns true if so)
    private boolean canMove(int square) {
        int neighbor;
        Piece piece = board.getPiece(square);

        if (piece != null &&
                piece.getColor() == turn) {

            if (turn == Color.BLACK || piece.getRank() == Rank.KING) {
                neighbor = checkLL(square);
                if (neighbor != 0
                        && board.isEmpty(neighbor)) {
                    return true;
                }

                neighbor = checkLR(square);
                if (neighbor != 0
                        && board.isEmpty(neighbor)) {
                    return true;
                }
            }

            if (turn == Color.RED || piece.getRank() == Rank.KING) {
                neighbor = checkUL(square);
                if (neighbor != 0
                        && board.isEmpty(neighbor)) {
                    return true;
                }

                neighbor = checkUR(square);
                if (neighbor != 0
                        && board.isEmpty(neighbor)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Sets the jumps flag to true if any jumps can be made at this point
    private void findJumps() {
        int square;

        for (square = 1; square <= 32; ++square) {
            if (canJump(square)) {
                jumps = true;
                return;
            }
        }
        jumps = false;
    }

    // determines if any moves can be made by the current player and returns true if so
    private boolean existMoves() {
        int square;

        for (square = 1; square <= 32; ++square) {
            if (canMove(square)) {
                return true;
            }
        }

        return false;
    }

    /* The following methods implement the checkers board algebra. Given a square, they return the
     * number of the upper left (UL), upper right (UR), lower left (LL) or upper right (UR) neighbour
     */
    public static int checkUL(int square) {
        int location;
        int neighborSquare;

        if (square < 1 || square > 32) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;

        switch (location) {
            case 5:
                return 0;
            case 1:
            case 2:
            case 3:
            case 4:
                neighborSquare = square - 4;
                break;
            case 0:
            case 6:
            case 7:
                neighborSquare = square - 5;
        }

        if ((neighborSquare >= 1)) {
            return neighborSquare;
        } else {
            return 0;
        }
    }

    public static int checkUR(int square) {
        int location;
        int neighborSquare;

        if (square < 1 || square > 32) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;

        switch (location) {
            case 4:
                return 0;
            case 1:
            case 2:
            case 3:
                neighborSquare = square - 3;
                break;
            case 0:
            case 5:
            case 6:
            case 7:
                neighborSquare = square - 4;
        }

        if ((neighborSquare >= 1)) {
            return neighborSquare;
        } else {
            return 0;
        }
    }

    public static int checkLL(int square) {
        int location;
        int neighborSquare;

        if (square < 1 || square > 32) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;

        switch (location) {
            case 5:
                return 0;
            case 1:
            case 2:
            case 3:
            case 4:
                neighborSquare = square + 4;
                break;
            case 6:
            case 7:
            case 0:
                neighborSquare = square + 3;
        }

        if ((neighborSquare <= 32)) {
            return neighborSquare;
        } else {
            return 0;
        }
    }

    public static int checkLR(int square) {
        int location;
        int neighborSquare;

        if (square < 1 || square > 32) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;

        switch (location) {
            case 4:
                return 0;
            case 1:
            case 2:
            case 3:
                neighborSquare = square + 5;
                break;
            case 5:
            case 6:
            case 7:
            case 0:
                neighborSquare = square + 4;
        }

        if ((neighborSquare <= 32)) {
            return neighborSquare;
        } else {
            return 0;
        }
    }

    // Given a position, determines if there's a piece that can be picked up by the current turn
    // player to move at the current state and returns true if so
    protected boolean canPick(int square) {
        if (jumps) {
            if (canJump(square)) {
                return true;
            }
        } else {
            if (canMove(square)) {
                return true;
            }
        }

        return false;
    }

    // Given two position integers, determines if a legal move can be made this turn between them.
   private boolean isLegalMove(int from, int to) {
        int difference, jumped;
        boolean check = false;
        boolean jump = false;

        if (from < 1
                || from > 32
                || to < 1
                || to > 32) {
            return false;
        }

        if (board.isEmpty(from)) {
            return false;
        }

        difference = to - from;
        jumped = 0;

        switch (difference) {
            // MOVE CASES
            case 4:
                check = ((checkLR(from) == to) || (checkLL(from) == to)) && ((board.getPiece(from).getColor() == Color.BLACK) || (board.getPiece(from).getRank() == Rank.KING));
                break;
            case 3:
                check = (checkLL(from) == to) && ((board.getPiece(from).getColor() == Color.BLACK) || (board.getPiece(from).getRank() == Rank.KING));
                break;
            case 5:
                check = (checkLR(from) == to) && ((board.getPiece(from).getColor() == Color.BLACK) || (board.getPiece(from).getRank() == Rank.KING));
                break;

            case -4:
                check = ((checkUR(from) == to) || (checkUL(from) == to)) && ((board.getPiece(from).getColor() == Color.RED) || (board.getPiece(from).getRank() == Rank.KING));
                break;
            case -5:
                check = (checkUL(from) == to) && ((board.getPiece(from).getColor() == Color.RED) || (board.getPiece(from).getRank() == Rank.KING));
                break;
            case -3:
                check = (checkUR(from) == to) && ((board.getPiece(from).getColor() == Color.RED) || (board.getPiece(from).getRank() == Rank.KING));
                break;

            // JUMP CASES
            case 9:
                jumped = checkLR(from);
                check = (checkLR(jumped) == to) && ((board.getPiece(from).getColor() == Color.BLACK) || (board.getPiece(from).getRank() == Rank.KING));
                jump = true;
                break;
            case 7:
                jumped = checkLL(from);
                check = (checkLL(jumped) == to) && ((board.getPiece(from).getColor() == Color.BLACK) || (board.getPiece(from).getRank() == Rank.KING));
                jump = true;
                break;
            case -7:
                jumped = checkUR(from);
                check = (checkUR(jumped) == to) && ((board.getPiece(from).getColor() == Color.RED) || (board.getPiece(from).getRank() == Rank.KING));
                jump = true;
                break;
            case -9:
                jumped = checkUL(from);
                check = (checkUL(jumped) == to) && ((board.getPiece(from).getColor() == Color.RED) || (board.getPiece(from).getRank() == Rank.KING));
                jump = true;
                break;

            default:
                return false;
        }

        return (!jump && check && board.isEmpty(to) && !jumps)
                || (jump && check && board.isEmpty(to)
                && board.getPiece(jumped).getColor() != board.getPiece(from).getColor());
    }

    // determines if a move is a jump (two squares away)
    private boolean isJump(int to, int from) {
        int difference = from - to;
        return (difference == 9) || (difference == 7) || (difference == -7) || (difference == -9);
    }

    // Picks up a piece from square "from". Returns true if successful, false if can't pick up.
    public boolean pickUp(int from) {
        if (canPick(from)) {
            toMove = from;
            listener.onPick(from);
            return true;
        } else {
            return false;
        }
    }

    // Once a piece has been picked up, this method moves it to the "to" place given. Returns true
    // if successful (move is valid), false if not. Ends turn after movement if no more moves can be
    // made.
    public boolean moveTo(int to) {
        boolean crownFlag = false;

        if (toMove == 0) {
            throw new MoveWithoutPickException();
        }

        if (isLegalMove(toMove, to)) {
            board.movePiece(toMove, to);
            if ((((to >= 1) && (to <= 4)) || ((to >= 29) && (to <= 32)))
                && (board.getPiece(to).getRank() == Rank.CHECKER)) {
                board.getPiece(to).crown();
                crownFlag = true;
            }
            if (isJump(toMove, to)) {
                board.removePiece(between(toMove, to));
                listener.onMove(board.getBoard());
                logMove(toMove, to, true);
                if (crownFlag || !pickUp(to)) {
                    endTurn();
                } else { // MULTIPLE JUMP!!!
                    continueTurn();
                }
            } else {
                listener.onMove(board.getBoard());
                logMove(toMove, to, false);
                endTurn();
            }
            return true;
        } else {
            return false;
        }
    }


    private void continueTurn()
    {
        listener.onNewTurn(turn);
    }

    // Ends the current turn.
    private void endTurn() {
        toMove = 0;
        changeTurn();
        findJumps();
        if (!jumps && !existMoves()) {
            listener.onDefeat(turn);
            turn = null;
        } else {
            listener.onNewTurn(turn);
        }

        if(turn == Color.RED && gameMode == GameMode.ONE_PLAYER){
            while(turn == Color.RED) {
                Move move = getBestMove();
                if (move != null) {
                    pickUp(move.from());
                    moveTo(move.to());
                    Log.i("PRIORITY SELECT: ", move.from() + " " + move.to() + " " + move.getPriority());
                } else {
                    Log.i("PRIORITY SELECT", "Null");
                }

            }
        }

    }

    /**
     * @param pickUp
     * @param moveTo
     * @param isJump
     */
    public void logMove(int pickUp, int moveTo, boolean isJump) {

        String log;
        if (isJump) {
            log = pickUp + "*" + moveTo;
            nuo.onCompleteMove(pickUp, 'x', moveTo);
        } else {
            log = pickUp + "-" + moveTo;
            nuo.onCompleteMove(pickUp, '-', moveTo);
        }
        moveList.add(log);

    }

    private void changeTurn() {
        if (turn == Color.BLACK) {
            this.turn = Color.RED;
        } else if (turn == Color.RED) {
            this.turn = Color.BLACK;
        }
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * To get all legal moves of the given color
     */
    public Vector<Move> getAllLegalMoves(Color color) {
        Vector<Move> myMove = new Vector<>();
        if (color == Color.BLACK) {
            myMove = getAllLegalMovesBlack();
        } else if (color == Color.RED) {
            myMove = getAllLegalMovesRed();
        }
        return myMove;
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * To get all legal moves for Black pieces
     */
    public Vector<Move> getAllLegalMovesBlack() {
        Vector<Move> myMove = new Vector<>();

        for (int square = 1; square <= 32; ++square) {
            //Log.i("kkkk", "" + square + board.getPiece(square));
            int neighbor, nextNeighbor;
            if (!board.isEmpty(square) && board.getPiece(square).getColor() == Color.BLACK) {
                Piece piece = board.getPiece(square);
                if(jumps) {
                    if (piece.getRank() == Rank.KING) {
                        neighbor = checkUL(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != Color.BLACK
                                && (nextNeighbor = checkUL(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkUR(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != turn
                                && (nextNeighbor = checkUR(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }
                    }

                    if (turn == Color.BLACK) {
                        neighbor = checkLL(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != turn
                                && (nextNeighbor = checkLL(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkLR(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != turn
                                && (nextNeighbor = checkLR(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }
                    }
                } else {
                    if (piece.getRank() == Rank.KING) {
                        neighbor = checkUL(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkUR(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }
                    }

                    if (turn == Color.BLACK) {
                        neighbor = checkLL(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkLR(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }
                    }
                }
            }
        }
        return myMove;
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * To get all the legal moves for Red pieces
     */
    public Vector<Move> getAllLegalMovesRed() {
        Vector<Move> myMove = new Vector<Move>();

        for (int square = 1; square <= 32; ++square) {
            //Log.i("kkkk", "" + square + board.getPiece(square));
            int neighbor, nextNeighbor;
            if (!board.isEmpty(square) && board.getPiece(square).getColor() == Color.RED) {
                Piece piece = board.getPiece(square);
                if(jumps) {
                    if (piece.getRank() == Rank.KING) {
                        neighbor = checkLL(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != Color.RED
                                && (nextNeighbor = checkLL(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkLR(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != turn
                                && (nextNeighbor = checkLR(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }
                    }

                    if (turn == Color.RED) {
                        neighbor = checkUL(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != turn
                                && (nextNeighbor = checkUL(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkUR(square);
                        if (neighbor != 0
                                && !board.isEmpty(neighbor)
                                && board.getPiece(neighbor).getColor() != turn
                                && (nextNeighbor = checkUR(neighbor)) != 0
                                && board.isEmpty(nextNeighbor)) {
                            Move move = new Move();
                            move.setMove(square, nextNeighbor, true);
                            myMove.add(move);
                        }
                    }
                } else {
                    if (piece.getRank() == Rank.KING) {
                        neighbor = checkLL(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkLR(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }
                    }

                    if (turn == Color.RED) {
                        neighbor = checkUL(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }

                        neighbor = checkUR(square);
                        if (neighbor != 0
                                && board.isEmpty(neighbor)) {
                            Move move = new Move();
                            move.setMove(square, neighbor, true);
                            myMove.add(move);
                        }
                    }
                }
            }
        }
        return myMove;
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * To copy the color and rank of each piece of the current board
     */
    protected void copyBoard (Color[] colors, Rank[] ranks) {
        for(int i=0; i<32; i++) {
            colors[i] = null;
            ranks[i] = null;
            if(!board.isEmpty(i+1)) {
                colors[i] = board.getPiece(i+1).getColor();
                ranks[i] = board.getPiece(i+1).getRank();
            }
        }
    }

    /*
     * JESSIE 04/26/2015*
     * To be used in AI
     * Set the current board to the original board based on the arrays used to store color and rank
     * This is to avoid object reference to the previous object
     */
    protected void setBoard (Color[] colors, Rank[] ranks) {
        for(int i=1; i<=32; i++) {
            board.removePiece(i);
        }
        for(int i=1; i<=32; i++) {
            if(colors[i-1] != null) {
                board.setPiece(i, new Piece(colors[i-1], ranks[i-1]));
            }
        }
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * Update the board based on the chosen move
     */
    public void updateBoard(Move move) {
        board.movePiece(move.from(), move.to());
        if (((move.to() >= 1) && (move.to() <= 4)) || ((move.to() >= 29) && (move.to() <= 32))) {
            board.getPiece(move.to()).crown();
        }
        if (isJump(move.from(), move.to())) {
            board.removePiece(between(move.from(), move.to()));
        }
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * Background game is a game simulation that will stimulate the gameplay for every given move
     * Depth determines how deep the game will go
     */
    public int backgroundGame(Move move, int depth) {
        int score = 0;
        PriorityMove p = new PriorityMove();
        updateBoard(move);
        turn = Color.BLACK;
        findJumps();

        for(int i = 0; i < depth; i++) {
            Vector<Move> myMove = getAllLegalMoves(turn);

            // if there's no more legal move or it reaches the depth, calculate the priority
            if(myMove.size() == 0) {
                score = p.evaluateBoard(board, Color.RED);
                break;
            }

            // if the piece is BLACK, randomly pick the move
            // otherwise, pick the best move for RED
            if(turn == Color.BLACK) {
                Random rnd = new Random();
                int index = rnd.nextInt(myMove.size());
                move = myMove.get(index);
            } else if(turn == Color.RED) {
                move = getBestMove(turn, myMove);
            }

            updateBoard(move);

            if(i == depth - 1) {
                score = p.evaluateBoard(board, Color.RED);
            }

            if(turn == Color.BLACK) {
                turn = Color.RED;
            } else if (turn == Color.RED) {
                turn = Color.BLACK;
            }
        }
        return score;
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * To get the best move among all legal moves of the given piece color
     */
    public Move getBestMove(Color color, Vector<Move> myMove) {
        Color[] colors = new Color[32];
        Rank[] ranks = new Rank[32];
        copyBoard(colors, ranks);
        Move[] bestMoves = new Move[0];
        DecisionTree maxPriorityAtTop = new DecisionTree(bestMoves, 2);
        PriorityMove p =new PriorityMove();
        int priority;
        for (Move m : myMove) {
            updateBoard(m);
            priority = p.evaluateBoard(board, color);
            m.setPriority(priority);
            bestMoves = maxPriorityAtTop.insert(m);
            setBoard(colors, ranks);
        }
        if(bestMoves.length > 0)
            return bestMoves[0];
        else
            return null;
    }

    /*
     * JESSIE 04/26/2015
     * To be used in AI
     * To get the best move of Computer player (RED)
     */
    public Move getBestMove() {
        Color currentTurn = turn;
        Color[] colors = new Color[32];
        Rank[] ranks = new Rank[32];
        copyBoard(colors, ranks);
        Boolean currentJumps = jumps;
        Vector<Move> myMove = getAllLegalMoves(Color.RED);
        Move[] bestMoves = new Move[0];
        DecisionTree maxPriorityAtTop = new DecisionTree(bestMoves, 2);
        PriorityMove pm = new PriorityMove();

        for(Move m: myMove) {
            int priority = pm.calcPointsRedBeingAttacked(board, m);
            int scoreAfterSimulation = backgroundGame(m, 3);
            m.setPriority(priority + scoreAfterSimulation);
            bestMoves = maxPriorityAtTop.insert(m);
            setBoard(colors, ranks);
            turn = currentTurn;
            jumps = currentJumps;
        }

        if(bestMoves.length > 0)
            return bestMoves[0];
        else
            return null;
    }

    public void setNetworkUpdateObserver(NetworkUpdateObserver nuo)
    {
        this.nuo = nuo;
    }

}