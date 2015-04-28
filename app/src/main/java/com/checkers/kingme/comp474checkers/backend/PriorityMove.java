package com.checkers.kingme.comp474checkers.backend;


/**
 * Created by Pichleap on 4/11/2015.
 */
public class PriorityMove {

    public final int POINT_WON = 200000;
    public final int POINT_KING = 2000;
    public final int POINT_NORMAL = 1000;
    public final int POINT_ATTACK = -50;

    public int evaluateBoard(CurrentBoard board, Color color) {
        int boardValue = 0;

        if (color == Color.BLACK) {
            boardValue = evaluateValueofBoardForBlack(board);
        } else {
            boardValue = evaluateValueofBoardForRed(board);
        }

        return boardValue;
    }

    private boolean isRedWinner(CurrentBoard board) {
        return countPieces(board, Color.BLACK) == 0;
    }

    private boolean isBlackWinner(CurrentBoard board) {
        return countPieces(board, Color.RED) == 0;
    }

    private int countPieces(CurrentBoard board, Color color) {
        int pieces = 0;
        for(int i=1; i<=32; i++) {
            if(board.getPiece(i) != null && board.getPiece(i).getColor() == color) {
                pieces ++;
            }
        }
        return pieces;
    }

    private int evaluateValueofBoardForRed(CurrentBoard board) {

        int rValue = 0;
        if (isRedWinner(board)) {
            rValue += POINT_WON;
            return rValue;
        } else {
            rValue = RedBlackPiecesDifferencePoints(board, Color.RED);
        }
        return rValue;
    }

    private int evaluateValueofBoardForBlack(CurrentBoard board) {

        int bValue = 0;
        if (isBlackWinner(board)) {
            bValue += POINT_WON;
            return bValue;
        } else {
            bValue = RedBlackPiecesDifferencePoints(board, Color.BLACK);
        }
        return bValue;
    }

    private int RedBlackPiecesDifferencePoints(CurrentBoard board, Color color) {

        int value = 0;
        // Scan across the board
        for(int i=1; i<=32; i++) {
            if (!board.isEmpty(i) && board.getPiece(i).getColor() == color) {
                if (board.getPiece(i).getRank() == Rank.KING) {
                    value += POINT_KING;
                } else {
                    value += POINT_NORMAL;
                }
            }
        }
        return value;
    }

    public int calcPointsRedBeingAttacked(CurrentBoard board, Move move) {
        int point = 0;
        int UL = CheckersGame.checkUL(move.to());
        int UR = CheckersGame.checkUR(move.to());
        int LL = CheckersGame.checkUL(move.to());
        int LR = CheckersGame.checkUR(move.to());
        if((UL > 0 && !board.isEmpty(UL) && board.getPiece(UL).getColor() == Color.BLACK
                && LR > 0 && board.isEmpty(LR))
               || (UR > 0 && !board.isEmpty(UR) && board.getPiece(UR).getColor() == Color.BLACK
                && LL > 0 && board.isEmpty(LL))) {
            point += POINT_ATTACK;
        }

        if((LL > 0 && !board.isEmpty(LL) && board.getPiece(LL).getColor() == Color.BLACK
                && board.getPiece(LL).getRank() == Rank.KING
                && UR > 0 && board.isEmpty(UR))
                || (LR > 0 && !board.isEmpty(LR) && board.getPiece(LR).getColor() == Color.BLACK
                && board.getPiece(LR).getRank() == Rank.KING
                && UL > 0 && board.isEmpty(UL))) {
            point += POINT_ATTACK;
        }
        return point;
    }
}
