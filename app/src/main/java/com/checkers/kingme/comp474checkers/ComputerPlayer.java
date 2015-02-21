package com.checkers.kingme.comp474checkers;

/**
 * Created by Richa on 2/21/2015.
 */
public class ComputerPlayer extends Player{

    public ComputerPlayer(Color myPieceColor, String name){
        super(myPieceColor,name);
    }

    boolean getNextMove(){
        return true;
    }
}
