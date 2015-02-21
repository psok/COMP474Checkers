package com.checkers.kingme.comp474checkers;

import android.graphics.Color;

/**
 * Created by Richa on 2/21/2015.
 */
public class Player {

    private Color myPieceColor;
    private String myName;
    private boolean isWinner;

    Player(Color myPieceColor, String name){
        this.myPieceColor = myPieceColor;
        this.myName = name;
    }

    public Color getMyPieceColor(){
        return myPieceColor;
    }

    public String getMyName(){
        return myName;
    }
    public boolean isWinner(){
        return isWinner;
    }
    public void setMyPieceColor(Color myPieceColor){
        this.myPieceColor = myPieceColor;
    }
    public void setMyName(String myName){
        this.myName = myName;
    }
    public void setWinner(CurrentBoard board){

    }
}
