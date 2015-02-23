public class CheckersGame
{
    private CurrentBoard board;
    private Color turn;
    private List<String> moveList;
    //private Player winner;
    //private CheckersSystem listener
    
    private int toMove;
    private int 
    
    private void findJumps()
    {
        int square;
        
        for(square = 1; square <= 32; ++square) {
            int neighbor;
            Piece piece = board.getPiece(square);
            
            if (piece != null &&
                piece.getColor == turn) {
        
                if (turn == Color.BLACK || piece.getRank == Rank.KING) {
                    try {
                        neighborColor = checkLL(square);
                        piece.getColor == turn
                    } catch (CheckersGame.NoNeighborException ex) {
                        
                    }
                    try {
                        neighborColor = checkLR(square);
                    } catch (CheckersGame.NoNeighborException ex) {
                        
                    }
                }
                
                if (turn == Color.RED || piece.getRank == Rank.KING) {
                    try {
                        neighborColor = checkUL(square);
                    } catch (CheckersGame.NoNeighborException ex) {
                        
                    }
                    try {
                        neighborColorcheckUR(square);
                    } catch (CheckersGame.NoNeighborException ex) {
                        
                    }
                }
                
            }
        }
    }
    
    private int checkUL(int square)
    {    
        int location = square % 8;
        int neighborSquare;
        
        switch(location) {
            case 5:
                return 0;
            case 1:
            case 2:
            case 3:
            case 4:
                neighborSquare = square - 4;
                break;
            case 0:
            case 6:
            case 7:
                neighborSquare = square - 5;
        }
        
        if ((neighborSquare >= 1) {
            return neighborSquare;
        } else {
            return 0;
        }
    }

    private int checkUR(int square)
    {    
        int location = square % 8;
        int neighborSquare;
        
        switch(location) {
            case 4:
                return 0;
            case 1:
            case 2:
            case 3:
                neighborSquare = square - 3;
                break;
            case 0:
            case 5:
            case 6:
            case 7:
                neighborSquare = square - 4;
        }
        
        if ((neighborSquare >= 1) {
            return neighborSquare;
        } else {
            return 0;
        }
    }

    private int checkLL(int square)
    {    
        int location = square % 8;
        int neighborSquare;
        
        switch(location) {
            case 5:
                return 0;
            case 1:
            case 2:
            case 3:
            case 4:
                neighborSquare = square + 4;
                break;
            case 6:
            case 7:
            case 0:
                neighborSquare = square + 3;
        }
        
        if ((neighborSquare <= 32) {
            return neighborSquare;
        } else {
            return 0;
        }
    }

    private int checkLR(int square)
    {
        int location = square % 8;
        int neighborSquare;
        
        switch(location) {
            case 4:
                return 0;
            case 1:
            case 2:
            case 3:
                neighborSquare = square + 5;
                break;
            case 5:
            case 6:
            case 7:
            case 0:
                neighborSquare = square + 4;
        }
        
        if ((neighborSquare <= 32) {
            return neighborSquare
        } else {
            return 0;
        }
    }
    
    private boolean canPick(int square)
    {
        Piece piece = board.getPiece(square);
        
        if (piece == null) {
            return false;
        }
        
        if (piece.getColor != turn) {
            return false;
        }
        
        if (turn == Color.BLACK || piece.getRank == Rank.KING) {
            checkLL(square);
            checkLR(square);
        }
        
        if (turn == Color.RED || piece.getRank == Rank.KING) {
            checkUL(square);
            checkUR(square);
        }
        
        return false;
    }
    
    public boolean pickUp(int from)
    {
        if (canPick(from)) {
            toMove = from;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean moveTo(int to)
    {
        
    }
}