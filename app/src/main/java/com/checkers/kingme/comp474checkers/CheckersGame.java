package com.checkers.kingme.comp474checkers;

import java.util.List;

public class CheckersGame
{
    static class MoveWithoutPickException extends RuntimeException
    {

    }

    private CurrentBoard board;
    private Color turn;
    private List<String> moveList;
    //private Player winner;
    //private CheckersSystem listener;
    
    private int toMove;
    private boolean jumps;

    public CheckersGame()//players? system?
    {
        board = new CurrentBoard();
        turn = Color.BLACK;
    }

    private int between(int here, int there)
    {
        int difference, between;

        if (here < 1
                || here > 32
                || there < 1
                || there > 32) {
            return 0;
        }

        difference = here - there;

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

    private void findJumps()
    {
        int square;
        
        for(square = 1; square <= 32; ++square) {
            if (canJump(square)) {
                jumps = true;
                return;
            }
        }

        jumps = false;
    }

    private boolean existMoves()
    {
        int square;

        for(square = 1; square <= 32; ++square) {
            if (canMove(square)) {
                return true;
            }
        }

        return false;
    }
    
    private int checkUL(int square)
    {
        int location;
        int neighborSquare;

        if (square < 1 || square > 32 ) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;
        
        switch(location) {
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

    private int checkUR(int square)
    {
        int location;
        int neighborSquare;

        if (square < 1 || square > 32 ) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;
        
        switch(location) {
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

    private int checkLL(int square)
    {    
        int location;
        int neighborSquare;

        if (square < 1 || square > 32 ) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;
        
        switch(location) {
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

    private int checkLR(int square)
    {
        int location;
        int neighborSquare;

        if (square < 1 || square > 32 ) {
            return 0;
        }

        location = square % 8;
        neighborSquare = 0;
        
        switch(location) {
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
    
    private boolean canPick(int square)
    {
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
    
    public boolean pickUp(int from)
    {
        if (canPick(from)) {
            toMove = from;
            return true;
        } else {
            return false;
        }
    }

    public boolean isLegalMove(int from, int to) {
        int difference, jumped;
        boolean check = false;
        boolean jump = false;

        if (from < 1
                || from > 32
                || to < 1
                || to > 32) {
            return false;
        }

        difference = to - from;
        jumped = 0;

        switch (difference) {
            // MOVE CASES
            case 4:
                check = (checkLR(from) == to) || (checkLL(from) == to);
                break;
            case 3:
                check = (checkLL(from) == to);
                break;
            case 5:
                check = (checkLR(from) == to);
                break;

            case -4:
                check = (checkUR(from) == to) || (checkUL(from) == to);
                break;
            case -5:
                check = (checkUL(from) == to);
                break;
            case -3:
                check = (checkUR(from) == to);
                break;

            // JUMP CASES
            case 9:
                jumped = checkLR(from);
                check = (checkLR(jumped) == to);
                jump = true;
                break;
            case 7:
                jumped = checkLL(from);
                check = (checkLL(jumped) == to);
                jump = true;
                break;
            case -7:
                jumped = checkUR(from);
                check = (checkUR(jumped) == to);
                jump = true;
                break;
            case -9:
                jumped = checkUL(from);
                check = (checkUL(jumped) == to);
                jump = true;
                break;

            default:
                return false;
        }

        return (!jump && check && !board.isEmpty(from) && board.isEmpty(to))
                || (jump && check && !board.isEmpty(from) && board.isEmpty(to)
                    && board.getPiece(jumped).getColor() != board.getPiece(from).getColor());
    }

    public boolean isJump(int to, int from)
    {
        int difference = from - to;
        return (difference == 9) || (difference == 7) || (difference == -7) || (difference == -9);
    }

    public boolean moveTo(int to) {
        if (toMove == 0) {
            throw new MoveWithoutPickException();
        }

        if (isLegalMove(toMove, to)) {
            board.movePiece(toMove, to);
            if (isJump(toMove, to)) {
                board.removePiece(between(toMove,to));
                logMove(toMove, to, true);
                if (canJump(to)) {
                    toMove = to;
                } else {
                    endTurn();
                }
            } else {
                logMove(toMove, to, false);
                endTurn();
            }
            return true;
        } else {
            return false;
        }
    }

    private void endTurn() {
        changeTurn();
        findJumps();
        if (!jumps && !existMoves()) {
            turn = null;

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

    /**
     * @return
     */
    public Color whoseTurn() {
        return turn;
    }

    /**
     *
     * @param turn
     * @return
     */
    public boolean isPlayerTurn(Color turn) {
        if (this.turn == turn) {
            return true;
        } else {
            return false;
        }
    }

    public void changeTurn(){
        if(turn == Color.BLACK){
            this.turn = Color.RED;
        }else if(turn == Color.RED){
            this.turn = Color.BLACK;
        }
    }
    public CurrentBoard getBoard() {
        return board;
    }
}