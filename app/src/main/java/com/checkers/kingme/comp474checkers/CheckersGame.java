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
    //private CheckersSystem listener
    
    private int toMove;
    private boolean jumps;

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
    
    private int checkUL(int square)
    {    
        int location = square % 8;
        int neighborSquare = 0;
        
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
        int location = square % 8;
        int neighborSquare = 0;
        
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
        int location = square % 8;
        int neighborSquare = 0;
        
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
        int location = square % 8;
        int neighborSquare = 0;
        
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
    
    public boolean moveTo(int to)
    {
        int difference = to - toMove;

        if (difference == to) {
            throw new MoveWithoutPickException();
        } else {
            switch (difference) {
                case 9:
                case 7:
                case -7:
                case -9:
            }
        }
    }
}