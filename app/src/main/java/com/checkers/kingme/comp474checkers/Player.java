package com.checkers.kingme.comp474checkers;

/**
 * Created by Richa on 2/21/2015.
 */
public class Player {

    private Color myPieceColor;
    private String myName;
    private boolean isWinner;

    Player(Color myPieceColor, String name) {
        this.myPieceColor = myPieceColor;
        this.myName = name;
    }

    public Color getMyPieceColor() {
        return myPieceColor;
    }

    public String getMyName() {
        return myName;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setMyPieceColor(Color myPieceColor) {
        this.myPieceColor = myPieceColor;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public boolean getIsWinnerResult(CurrentBoard board) {
        this.isWinner = true;
        int opponentPiece = 0;
        int myPiece = 0;
        Color opponentColor = Color.black;
        if (myPieceColor == Color.black) {
            opponentColor = Color.red;
        } else if (myPieceColor == Color.red) {
            opponentColor = Color.black;
        }
        for (Piece piece : board.getCheckerBoard()) {
            if (piece.getColor() == opponentColor) {
                opponentPiece++;
            } else if (piece.getColor() == myPieceColor) {
                myPiece++;
            }
        }

        if (opponentPiece == 0 && myPiece > 0) {
            this.isWinner = true;
        } else {
            this.isWinner = false;
        }

        return isWinner;
    }
}
