package com.checkers.kingme.comp474checkers;

/**
 * Created by Richa on 2/21/2015.
 */
public class CurrentBoard {
    Piece[] checkerBoard = new Piece[32];

    public CurrentBoard(Piece[] checkerBoard){
        this.checkerBoard = checkerBoard;
    }

    public Piece[] getCheckerBoard() {
        return checkerBoard;
    }

    public void setCheckerBoard(Piece[] checkerBoard) {
        this.checkerBoard = checkerBoard;
    }

    public Piece getPiece(int square){
        return checkerBoard[square];
    }

}
