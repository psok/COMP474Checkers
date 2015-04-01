package com.checkers.kingme.comp474checkers.backend;

import java.util.List;
import java.util.Arrays;


class CurrentBoard
{
    private List<Piece> checkersBoard;

    protected boolean isEmpty(int square)
    {
        return (checkersBoard.get(square - 1) == null);
    }

    protected Piece getPiece(int square)
    {

            return checkersBoard.get(square - 1);

    }

    protected void movePiece(int from, int to)
    {
        checkersBoard.set(to - 1, getPiece(from));
        removePiece(from);
    }

    protected void removePiece(int place)
    {
        checkersBoard.set(place - 1, null);
    }

    protected Piece[] getBoard()
    {
        return checkersBoard.toArray(new Piece[32]);
    }

    protected CurrentBoard()
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

}