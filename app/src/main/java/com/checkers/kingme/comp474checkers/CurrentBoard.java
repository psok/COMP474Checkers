package com.checkers.kingme.comp474checkers;

import java.util.List;
import java.util.Arrays;


public class CurrentBoard
{
    private List<Piece> checkersBoard;

    public boolean isEmpty(int square)
    {
        return (checkersBoard.get(square - 1) == null);
    }

    public Piece getPiece(int square)
    {
        return checkersBoard.get(square - 1);
    }

    public void movePiece(int from, int to)
    {
        checkersBoard.set(to - 1, getPiece(from));
        removePiece(from);
    }

    public void removePiece(int place)
    {
        checkersBoard.set(place - 1, null);
    }

    public Piece[] getBoard()
    {
        return checkersBoard.toArray(new Piece[32]);
    }

    public CurrentBoard()
    {
        Piece[] parr = {
                new Piece(Color.BLACK), new Piece(Color.BLACK), new Piece(Color.BLACK),
                new Piece(Color.BLACK), new Piece(Color.BLACK), new Piece(Color.BLACK),
                new Piece(Color.BLACK), new Piece(Color.BLACK), new Piece(Color.BLACK),
                new Piece(Color.BLACK), new Piece(Color.BLACK), new Piece(Color.BLACK),
                null, null, null, null, null, null, null, null, new Piece(Color.RED),
                new Piece(Color.RED), new Piece(Color.RED), new Piece(Color.RED),
                new Piece(Color.RED), new Piece(Color.RED), new Piece(Color.RED),
                new Piece(Color.RED), new Piece(Color.RED), new Piece(Color.RED),
                new Piece(Color.RED), new Piece(Color.RED)
        };

        checkersBoard = Arrays.asList(parr);
    }

    // Jessie: Check if the square has black or red piece, or black or red king or empty square
    public boolean isBlackPiece(int square) {
        return !this.isEmpty(square) && this.getPiece(square).getColor() == Color.BLACK;
    }

    public boolean isRedPiece(int square) {
        return !this.isEmpty(square) && this.getPiece(square).getColor() == Color.RED;
    }

    public boolean isKing(int square) {
        return !this.isEmpty(square) && this.getPiece(square).getRank() == Rank.KING;
    }

}