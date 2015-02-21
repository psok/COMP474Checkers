package com.checkers.kingme.comp474checkers;

/**
 * Created by Richa on 2/21/2015.
 */
public enum Rank {
    checker(1), king(2);

    private int value;

    private Rank(int value){
        this.value = value;
    }
}
