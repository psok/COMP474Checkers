package com.checkers.kingme.comp474checkers.model;

/**
 * Created by Carlos
 */
public interface NetworkUpdateObserver {
    public void onCompleteMove(int from, char type, int to);
}
