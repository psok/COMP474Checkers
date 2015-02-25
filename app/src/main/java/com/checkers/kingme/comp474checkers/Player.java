package com.checkers.kingme.comp474checkers;

/**
 * Created by Richa on 2/21/2015.
 */
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


}
