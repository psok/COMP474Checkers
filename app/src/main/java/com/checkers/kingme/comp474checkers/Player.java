package com.checkers.kingme.comp474checkers;


public class Player {

   private Color myPieceColor;
    private String myName;
    private boolean isWinner;

    /**
     *
     * @param myPieceColor
     * @param name
     */
    Player(Color myPieceColor, String name) {
        this.myPieceColor = myPieceColor;
        this.myName = name;
    }

    /**
     *
     * @return
     */
    public Color getMyPieceColor() {
        return myPieceColor;
    }

    /**
     *
     * @return
     */
    public String getMyName() {
        return myName;
    }

    /**
     *
     * @return
     */
    public boolean isWinner() {
        return isWinner;
    }

    /**
     *
     * @param myPieceColor
     */
    public void setMyPieceColor(Color myPieceColor) {
        this.myPieceColor = myPieceColor;
    }

    /**
     *
     * @param myName
     */
    public void setMyName(String myName) {
        this.myName = myName;
    }

    /**
     *
     * @param isWinner
     */
    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    /**
     *
     * @param board
     * @return
     */
    public boolean getIsWinnerResult(CurrentBoard board) {
        this.isWinner = true;
        int opponentPiece = 0;
        int myPiece = 0;
        Color opponentColor = Color.BLACK;
        if (myPieceColor == Color.BLACK) {
            opponentColor = Color.RED;
        } else if (myPieceColor == Color.RED) {
            opponentColor = Color.BLACK;
        }
        for (Piece piece : board.getBoard()) {
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
