package com.checkers.kingme.comp474checkers;

/**
 * Created by Richa on 2/21/2015.
 */
public class RemotePlayer extends Player{

    public RemotePlayer(Color myPieceColor, String name){
        super(myPieceColor,name);
    }

    boolean getNextMove(){
        return true;
    }

}
