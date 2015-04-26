package com.checkers.kingme.comp474checkers.backend;

import java.util.ArrayList;

/**
 * Created by Richa on 4/12/2015.
 */
public class Move {
    private ArrayList<Integer> to;
    private int from;
    boolean isJump;
    private int priority;

    public Move() {
        to = new ArrayList<Integer>();
    }

    public int to() {
        return to.get(0);
    }

    public void setTo(int to) {
        this.to.add(to);
    }

    public int sizeTo() {
        return to.size();
    }

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean isJump) {
        this.isJump = isJump;
    }

    public int from() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setMove(int from, int to, boolean isJump) {
        this.from = from;
        this.to.add(to);
        this.isJump = isJump;
    }
}
