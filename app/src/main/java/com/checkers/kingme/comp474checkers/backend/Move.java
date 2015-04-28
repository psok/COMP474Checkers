package com.checkers.kingme.comp474checkers.backend;

import java.util.ArrayList;

/**
 * Created by Richa on 4/12/2015.
 */
public class Move {
    private int to;
    private int from;
    boolean isJump;
    private int priority;

    public int to() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
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
        this.to = to;
        this.isJump = isJump;
    }
}
