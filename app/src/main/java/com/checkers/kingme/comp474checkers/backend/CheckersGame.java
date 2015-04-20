package com.checkers.kingme.comp474checkers.backend;

import android.util.Log;

import com.checkers.kingme.comp474checkers.model.GameListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/* CheckersGame Class
 * Author: Carlos
 * Description: Implements the game of checkers logic
 */
public class CheckersGame {
    // Exception to be thrown in case of programmer calling moveTo without having called pickUp first
    static class MoveWithoutPickException extends RuntimeException {

    }

    private CurrentBoard board;
    private Color turn;
    private List<String> moveList;
    private GameListener listener;

    private int toMove; // this integer stores the position of the piece currently picked up
    private boolean jumps; // this flag is used to stop non-jump moves when jumps are possible

    // Constructor. Doesn't take any arguments (thus far). Does a fresh start.
    public CheckersGame(GameListener listener)//players? system?
    {
        board = new CurrentBoard(); //
        turn = Color.BLACK;
        moveList = new ArrayList<String>();
        this.listener = listener;
        listener.onBegin(board.getBoard());
        listener.onNewTurn(turn);
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
        if (toMove == 0) {
            throw new MoveWithoutPickException();
        }

        if (isLegalMove(toMove, to)) {
            board.movePiece(toMove, to);
            if (((to >= 1) && (to <= 4)) || ((to >= 29) && (to <= 32))) {
                board.getPiece(to).crown();
            }
            if (isJump(toMove, to)) {
                board.removePiece(between(toMove, to));
                listener.onMove(board.getBoard());
                logMove(toMove, to, true);
                if (!pickUp(to)) {
                    endTurn();
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

        if(turn == Color.RED){
            Move move = getBestMove();
            if(move != null) {
                Log.i("PRIORITY SELECT from", "" + move.getFrom());
                Log.i("PRIORITY SELECT to", "" + move.getTo());
                Log.i("PRIORITY SELECT priority", "" + move.getPriority());
            } else {
                Log.i("Null", "");
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
        } else {
            log = pickUp + "-" + moveTo;
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

    public Vector<Move> getAllLegalMoves() {
        Vector<Move> myMove = new Vector<Move>();

        for (int square = 1; square <= 32; ++square) {
            Piece piece = board.getPiece(square);
            Log.i("kkkk", "" + square + piece);
            if (piece != null && piece.getColor() == Color.RED) {
                if(canMove(square)) {
                    //Left move
                    int UL = CheckersGame.checkUL(square);
                    int UR = CheckersGame.checkUR(square);
                    int LL = CheckersGame.checkLL(square);
                    int LR = CheckersGame.checkLR(square);

                    if(isLegalMove(square, UL)) {
                        Move move = new Move();
                        move.setFrom(square);
                        move.setTo(UL);
                        move.setJump(false);
                    }

                    if(isLegalMove(square, UR)) {
                        Move move = new Move();
                        move.setFrom(square);
                        move.setTo(UR);
                        move.setJump(false);
                    }

                    if(board.getPiece(square).getRank() == Rank.KING) {
                        if (isLegalMove(square, LL)) {
                            Move move = new Move();
                            move.setFrom(square);
                            move.setTo(LL);
                            move.setJump(false);
                        }

                        if (isLegalMove(square, LR)) {
                            Move move = new Move();
                            move.setFrom(square);
                            move.setTo(LR);
                            move.setJump(false);
                        }
                    }
                    if(canJump(square)) {
                        if (isJump(square, CheckersGame.checkUL(UL))) {
                            Move move = new Move();
                            move.setFrom(square);
                            move.setTo(CheckersGame.checkUL(UL));
                            move.setJump(true);
                        }

                        if (isJump(square, CheckersGame.checkUR(UR))) {
                            Move move = new Move();
                            move.setFrom(square);
                            move.setTo(CheckersGame.checkUR(UR));
                            move.setJump(true);
                        }

                        if(board.getPiece(square).getRank() == Rank.KING) {
                            if (isJump(square, CheckersGame.checkLL(LL))) {
                                Move move = new Move();
                                move.setFrom(square);
                                move.setTo(CheckersGame.checkLL(LL));
                                move.setJump(true);
                            }

                            if (isJump(square, CheckersGame.checkLR(LR))) {
                                Move move = new Move();
                                move.setFrom(square);
                                move.setTo(CheckersGame.checkLR(LR));
                                move.setJump(true);
                            }
                        }
                    }
                }
            }
        }
        return myMove;
    }

    public Move getBestMove() {

        Vector<Move> myMove = getAllLegalMoves();


        Move[] bestmoves = new Move[0];
        DecisionTree maxPriorityAtTop = new DecisionTree(bestmoves, 2);
        int priority;
        for (Move m : myMove) {
            //priority = PriorityMove.getPriority(m);
            PriorityMove p =new PriorityMove();
            priority = p.evaluateValueofBoardForRed(board,m);
            m.setPriority(priority);
            Log.i("PRIORITY FROM: ", "" + m.from);
            Log.i("PRIORITY TO: ", "" + m.to);
            Log.i("PRIORITY PRI: ", "" + m.priority);
            Log.i("================\n", "");
            bestmoves = maxPriorityAtTop.insert(m);
        }

        if(bestmoves.length > 0)
            return bestmoves[0];
        else
            return null;
    }

}