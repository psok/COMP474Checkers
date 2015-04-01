package com.checkers.kingme.comp474checkers;

/**
 * Created by JESSIE on 3/27/15.
 */
public abstract class Player {
    private Color color;
    private String name;

    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public abstract void receiveMove(int squareID);
    public abstract void sendMove(int squareID);
    public String getName() {
        return name;
    }
}
