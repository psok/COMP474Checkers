package com.checkers.kingme.comp474checkers.backend;


/**
 * Created by Richa on 4/11/2015.
 */
public class PriorityMove {

    public final int POINT_WON = 200000;
    public final int POINT_JUMP = 3000;
    public final int POINT_KING = 1000;
    public final int POINT_NORMAL = 500;
    public final int POINT_DEFENCE = 30;
    public final int POINT_DEFENCE_ATTACK_NORMAL = 20;
    public final int POINT_DEFENCE_ATTACK_KING = 40;
    public final int POINT_ATTACK_NORMAL = 50;
    public final int POINT_ATTACK_KING = 60;

    public int evaluateValueofBoard(CurrentBoard board, Move move) {
        int points = 0;
        points += calcPointsForKing(board, move);
        points += calcPointsForJump(move);
        points += calcPointsRedBeingAttacked(board, move);
        points += calcPointsRedDefending(board, move);

        return points;
    }

    private int calcPointsForKing(CurrentBoard board, Move move) {
        int points = 0;
        if(!board.isEmpty(move.from())
                && board.getPiece(move.from()).getRank() != Rank.KING
                && move.to() <= 4) {
            points += POINT_KING;
        }
        return points;
    }

    private int calcPointsForJump(Move move) {
        int points = 0;
        if(move.isJump()) {
            points += POINT_JUMP;
        }

        return points;
    }


    /*public int evaluateValueofBoardForRed(CurrentBoard board, Move move) {
        int rValue = 0;
        if (isRedWinner(board)) {
            rValue += POINT_WON;
            return rValue;
        } else {
            rValue = calcPointsForJumping(board, move);
            rValue += calcPointsRedBeingAttacked(board, move);
            rValue += calcPointsRedDefending(board, move);
        }
        return rValue;
    }*/

    /*private boolean isRedWinner(CurrentBoard board) {
        return blackPieces(board) == 0;
    }*/

    /*private int blackPieces(CurrentBoard board) {
        int bPieces = 0;
        for(int i=1; i<=32; i++) {
            if(board.getPiece(i) != null && board.getPiece(i).getColor() == Color.BLACK) {
                bPieces ++;
            }
        }
        return bPieces;
    }
    */

    // cover capture normal piece and king
    /*private int RedBlackPiecesDifferencePoints(CurrentBoard board) {

        int value = 0;
        // Scan across the board
        for (int i = 1; i <= 32; i++) {
                Piece piece = board.getPiece(i);

                if (piece != null && piece.getColor() == Color.RED) {
                    if (piece.getRank() == Rank.KING) {
                        value += POINT_KING;
                    } else {
                            value += POINT_NORMAL;
                    }
                }
        }
        return value;
    }*/

    // check for multiple jump
    /*private int calcPointsForJumping(CurrentBoard board, Move move) {
        int points = 0;

        //if the move is a jump move
        if(move.isJump) {

            points += POINT_JUMP;

            // get all the neighbours of the given square
            int squareLL = CheckersGame.checkLL(move.from());
            int squareLR = CheckersGame.checkLR(move.from());
            int squareUL = CheckersGame.checkUL(move.from());
            int squareUR = CheckersGame.checkUR(move.from());

            int neighbourLL = CheckersGame.checkLL(squareLL);
            int neighbourLR = CheckersGame.checkLR(squareLR);
            int neighbourUL = CheckersGame.checkUL(squareUL);
            int neighbourUR = CheckersGame.checkUR(squareUR);

            int next = 0, nextNeighbour = 0;

            if (move.to() == neighbourLL) {
                squareLL = CheckersGame.checkLL(move.to());
                neighbourLL = CheckersGame.checkLL(squareLL);
                while (squareLL > 0 && squareLL > 0 && board.getPiece(squareLL) != null && board.getPiece(squareLL).getColor() == Color.BLACK && board.getPiece(neighbourLL) == null) {
                    points += POINT_JUMP;
                    squareLL = CheckersGame.checkLL(neighbourLL);
                    neighbourLL = CheckersGame.checkLL(squareLL);
                }
            } else if (move.to() == neighbourLR) {
                squareLR = CheckersGame.checkLR(move.to());
                neighbourLR = CheckersGame.checkLR(squareLR);
                while (squareLR > 0 && squareLR > 0 && board.getPiece(squareLR) != null && board.getPiece(squareLR).getColor() == Color.BLACK && board.getPiece(neighbourLR) == null) {
                    points += POINT_JUMP;
                    squareLR = CheckersGame.checkLR(neighbourLR);
                    neighbourLR = CheckersGame.checkLR(squareLR);
                }
            }

            if (move.to() == neighbourUL) {
                squareUL = CheckersGame.checkUL(move.to());
                neighbourUL = CheckersGame.checkUL(squareUL);
                while (squareUL > 0 && neighbourUL > 0 && board.getPiece(squareUL) != null && board.getPiece(squareUL).getColor() == Color.BLACK && board.getPiece(neighbourUL) == null) {
                    points += POINT_JUMP;
                    squareUL = CheckersGame.checkUL(neighbourUL);
                    neighbourUL = CheckersGame.checkUL(squareUL);
                }
            } else if (move.to() == neighbourUR) {
                squareUR = CheckersGame.checkUR(move.to());
                neighbourUR = CheckersGame.checkUR(squareUR);
                while (squareUR > 0 && neighbourUR > 0 && board.getPiece(squareUR) != null && board.getPiece(squareUR).getColor() == Color.BLACK && board.getPiece(neighbourUR) == null) {
                    points += POINT_JUMP;
                    squareUR = CheckersGame.checkUR(neighbourUR);
                    neighbourUR = CheckersGame.checkUR(squareUR);
                }
            }
        }
        return points;
    }*/

    /*Calculate points red piece is being attacked.
    To be called only by on a cell which has a red piece.
     */
    private int calcPointsRedBeingAttacked(CurrentBoard board, Move move) {

        Piece piece = board.getPiece(move.to());
        int points = 0;

        // get all the neighbours of the given square
        int squareLL = CheckersGame.checkLL(move.to());
        int squareLR = CheckersGame.checkLR(move.to());
        int squareUL = CheckersGame.checkUL(move.to());
        int squareUR = CheckersGame.checkUR(move.to());

        // if the upper right of the square is black and the lower left of the square is empty
        if (squareUR > 0 && !board.isEmpty(squareUR) && board.getPiece(squareUR).getColor() == Color.BLACK
                && squareLL > 0 && board.isEmpty(squareLL)) {
            // if the to-be-attacked piece is a king
            if (piece != null && piece.getRank() == Rank.KING) {
                points -= POINT_ATTACK_KING;
            } else {
                points -= POINT_ATTACK_NORMAL;
            }
        }

        // if the upper left of the square is black and the lower right of the square is empty
        if (squareUL > 0 && !board.isEmpty(squareUL) && board.getPiece(squareUL).getColor() == Color.BLACK
                && squareLR > 0 && board.isEmpty(squareLR)) {
            // if the to-be-attacked piece is a king
            if(piece != null && piece.getRank() == Rank.KING) {
                points -= POINT_ATTACK_KING;
            } else {
                points -= POINT_ATTACK_NORMAL;
            }
        }

        // if the upper right of the square is empty but the lower left of the square is a black king (able to move backward)
        if (squareUR > 0 && board.isEmpty(squareUR)
                && squareLL > 0 && !board.isEmpty(squareLL) && board.getPiece(squareLL).getColor() == Color.BLACK
                && board.getPiece(squareLL).getRank() == Rank.KING) {
            // if the to-be-attacked piece is a king
            if (piece != null && piece.getRank() == Rank.KING) {
                points -= POINT_ATTACK_KING;
            } else {
                points -= POINT_ATTACK_NORMAL;
            }
        }

        // if the upper left of the square is empty but the lower right of the square is a black king (able to move backward)
        if (squareUL > 0 && !board.isEmpty(squareUL) && board.getPiece(squareUL).getColor() == null
                && squareLR > 0 && !board.isEmpty(squareLR) && board.getPiece(squareLR).getColor() == Color.BLACK
                && board.getPiece(squareLR).getRank() == Rank.KING) {
            // if the to-be-attacked piece is a king
            if(piece != null && piece.getRank() == Rank.KING) {
                points -= POINT_ATTACK_KING;
            } else {
                points -= POINT_ATTACK_NORMAL;
            }
        }

        return points;
    }

    /*Calculate points red piece defenses other red piece.
    To be called only by on a cell which has a red piece.
     */
    private int calcPointsRedDefending(CurrentBoard board, Move move) {
        int points = 0;

        int squareLL = CheckersGame.checkLL(move.to());
        int squareLR = CheckersGame.checkLR(move.to());
        int squareUL = CheckersGame.checkUL(move.to());
        int squareUR = CheckersGame.checkUR(move.to());

        int neighbourLL = CheckersGame.checkLL(squareLL);
        int neighbourLR = CheckersGame.checkLR(squareLR);

        // if the piece is at the most bottom or most left or most right of the board
        // no opponent can capture the piece on any of those square
        // such move is rewarded
        if (move.to() >= 29 || move.to() % 8 == 4 || move.to() % 8 == 5) {
            points += POINT_DEFENCE;
        }
        // if the square is at the third two from the bottom
        else if (squareLR > 0 && board.isEmpty(squareLR) && neighbourLR >= 29 && !board.isEmpty(neighbourLR) && board.getPiece(neighbourLR).getColor() == Color.RED) {
            if (squareUR > 0 && !board.isEmpty(squareUR) && board.getPiece(squareUR).getColor() == Color.BLACK) {
                if (board.getPiece(squareUR).getRank() == Rank.KING) {
                    points += POINT_DEFENCE_ATTACK_KING;
                } else {
                    points += POINT_DEFENCE_ATTACK_NORMAL;
                }

            } else if (squareUL > 0 && !board.isEmpty(squareUL) && board.getPiece(squareUL).getColor() == Color.BLACK
                    && squareLR > 0 && board.isEmpty(squareLR)
                    && ( neighbourLR > 0 && (board.isEmpty(neighbourLR) || board.getPiece(neighbourLR).getColor() == Color.BLACK))) {
                points -= POINT_DEFENCE_ATTACK_NORMAL;
            } else {
                points += POINT_DEFENCE;
            }
        }
        // if the piece is at the third two from the bottom
        else if (squareLL > 0 && board.isEmpty(squareLL)
                && neighbourLL >= 29 && !board.isEmpty(neighbourLL) && board.getPiece(neighbourLL).getColor() == Color.RED) {
            if (squareUL > 0 && !board.isEmpty(squareUL) && board.getPiece(squareUL).getColor() == Color.BLACK) {
                if (board.getPiece(squareUL).getRank() == Rank.KING) {
                    points += POINT_DEFENCE_ATTACK_KING;
                } else {
                    points += POINT_DEFENCE_ATTACK_NORMAL;
                }
            } else if (squareUR > 0 && !board.isEmpty(squareUR) && board.getPiece(squareUR).getColor() == Color.BLACK
                    && (squareLL > 0 && board.isEmpty(squareLL))
                    && (neighbourLL > 0 && (board.isEmpty(neighbourLL) || board.getPiece(neighbourLL).getColor() == Color.BLACK))) {
                points -= POINT_DEFENCE_ATTACK_NORMAL;
            } else {
                points += POINT_DEFENCE;
            }
        } else {
            // if the piece is not at the most right column of the board
            if (move.to() % 8 < 4 ) {
                if (squareLR > 0 && !board.isEmpty(squareLR) && board.getPiece(squareLR).getColor() == Color.RED) {
                    if (squareUR > 0 && (board.isEmpty(squareUR) || board.getPiece(squareUR).getColor() == Color.RED)) {
                        points += POINT_DEFENCE;
                    } else {
                        points += POINT_DEFENCE_ATTACK_NORMAL;
                    }
                }
            }
            // if the piece is not at the most left column of the board
            if (move.to() % 8 > 5) {
                if (squareLL > 0 && !board.isEmpty(squareLL) && board.getPiece(squareLL).getColor() == Color.RED) {
                    if (squareUL > 0 && (board.isEmpty(squareUL) || board.getPiece(squareUL).getColor() == Color.RED))
                    points += POINT_DEFENCE;
                } else {
                    points += POINT_DEFENCE_ATTACK_NORMAL;
                }
            }
        }

        return points;
    }
}

