package GUI;

import ChessEngine.com.company.board.Possible;
import ChessEngine.com.company.lookup.Pieces;

import javax.swing.*;
import java.util.ArrayList;

public class BoardController {

    private byte[][] curBoard;
    private int[][] possibleMoves;
    private int[][] move;
    GUI gui;

    public BoardController(GUI gui){
        resetBoard();
        this.gui = gui;
    }

    //Gets the board
    public byte[][] getBoard(){
        return this.curBoard;
    }
    //Sets the board
    public void setBoard(byte[][] board){
        move = findMove(curBoard,board);
        this.curBoard = board;
    }

    //Returns a calculated move between two different boards
    public int[][] getMove(){
        return this.move;
    }

    //Gets the piece at a location at the board
    public byte getPieceAt(int x, int y){
        return curBoard[x][y];
    }

    //Returns the previous calculated moves for a piece
    public int[][] getPB(){return possibleMoves;}

    //Resets the board
    private void resetBoard(){
        curBoard = SaveAndLoad.loadBoard("data/board.txt");
        if(curBoard == null){
            curBoard = new byte[8][8];
        }
    }

    public String toString(){
        String output = "";
        for(int x = 0; x < curBoard.length; x++){
            output += "\n";
            for(int y = 0; y < curBoard[x].length; y++){
                output += getPieceAt(y,x) + " ";
            }
        }
        return output;
    }

    //Stores and starts the calculation of the possible moves for a specific piece at a specific location
    public void calculatePossibleMoves(byte piece,int curX,int curY){
        possibleMoves = getAvailableMoves(piece,curX,curY);
    }

    //Calculates the possible moves for a specific piece at a specific location
    private int[][] getAvailableMoves(byte piece,int curX,int curY){
        ArrayList<int[]> position = new ArrayList<> ();
        int[][][] moves = getPieceMoves(piece);

        for(int i = 0; i < moves.length; i++){
            for(int x = 0; x < moves[i].length; x++){
                int movX = moves[i][x][0] + curX;
                int movY = moves[i][x][1] + curY;
                if(movX >= 0 && movX <= 7 && movY >= 0 && movY <= 7){
                    int pieceAt = getPieceAt(movX,movY);
                    //System.out.print("\nPieceAt: " + pieceAt + " at " + movX + "," + movY);
                    if(pieceAt >= 1 && pieceAt < 7){
                        if(piece == 3 || piece == 4 || piece == 5){
                            break;
                        }
                        else{
                            continue;
                        }
                    }
                    if(piece == 1){ //Pawn exceptions
                        int diagonalY = curY + 1;
                        if(checkPawnDiagonals(curX - 1, diagonalY)){
                            position.add(new int[]{curX - 1,diagonalY});
                        }
                        if(checkPawnDiagonals(curX + 1, diagonalY)){
                            position.add(new int[]{curX + 1,diagonalY});
                        }
                        if(pieceAt >= 7){
                            continue;
                        }
                        if(curY != 1 && curY + 2 == movY){
                            continue;
                        }
                    }

                    if(piece == 6 && !gui.kingHasMoved && !gui.isInCheck){
                        //Check for castling
                        if(!gui.leftRookHasMoved){
                            //Check space between
                            boolean noSpace = true;
                            for(int k = 0; k < 3; k++){
                                if(getPieceAt(1+k,0) != 0){
                                    noSpace = false;
                                    break;
                                }
                            }
                            if(noSpace){
                                position.add(new int[]{0,0});
                            }
                        }
                        if(!gui.rightRookHasMoved){
                            //Check space between
                            boolean noSpace = true;
                            for(int k = 0; k < 2; k++){
                                if(getPieceAt(5+k,0) != 0){
                                    noSpace = false;
                                    break;
                                }
                            }
                            if(noSpace){
                                position.add(new int[]{7,0});
                            }
                        }
                    }

                    position.add(new int[]{movX,movY});
                    if(pieceAt >= 7){
                        if(piece == 3 || piece == 4 || piece == 5){
                            break;
                        }
                    }
                }
            }
        }

        position.add(new int[]{curX,curY});

        int[][] result = new int[position.size()][2];
        for(int i = 0; i < result.length; i++){
           result[i] = position.get(i);
        }
        return result;
    }

    //Returns the basic moves a piece can do
    private int[][][] getPieceMoves(byte piece){
        //System.out.print("\nPiece: " + piece);
        int pieceVal = piece;
        int[][][] moves;
        switch (pieceVal){
            case 1:
            case 7:
                moves = new int[1][Pieces.PAWN.length][2];
                moves[0] = Pieces.PAWN;
                break;
            case 2:
            case 8:
                moves = new int[1][Pieces.KNIGHT.length][2];
                moves[0] = Pieces.KNIGHT;
                break;
            case 3:
            case 9:
                moves = extendPossibleMoves(Pieces.BISHOP);
                break;
            case 4:
            case 10:
                moves = extendPossibleMoves(Pieces.ROOK);
                break;
            case 5:
            case 11:
                moves = extendPossibleMoves(Pieces.QUEEN);
                break;
            case 6:
            case 12:
                moves = new int[1][Pieces.KING.length][2];
                moves[0] = Pieces.KING;
                break;
            default:
                moves = null;
                break;
        }
        return moves;
    }

    //Returns an extended version of the basic moves a piece can make
    private int[][][] extendPossibleMoves(int[][] moves){
        ArrayList<int[][]> positions = new ArrayList<>();
        for(int k = 0; k < moves.length; k++){
            positions.add(new int[7][2]);
            for(int i = 1; i < 8; i++){
                int[] m = moves[k];
                m = new int[]{m[0]*i,m[1]*i};
                positions.get(k)[i-1] = m;
            }
        }
        int[][][] temp = new int[positions.size()][6][2];
        positions.toArray(temp);
        return temp;
    }

    //Moves a piece on the board, also checks for exceptions
    private void movePiece(int fromX, int fromY, int toX, int toY){
        byte piece = getPieceAt(fromX,fromY);
        curBoard[fromX][fromY] = 0;

        //Checks if pawn has reached the top row
        if(piece == 1 && toY == 7){
            String option = JOptionPane.showInputDialog("Which chesspiece should replace the PAWN?\n1: Queen\nAny key: Knight");
            try{
                int opt = Integer.parseInt(option);
                piece = (opt == 1)? (byte)5 : 2;
            }
            catch(Exception e){
                piece = 2;
            }
        }
        else if(piece == 6){ //Handles castling
            if(toX == 0 && toY == 0){
                curBoard[2][0] = 6;
                curBoard[3][0] = 4;
                piece = 0;
            }
            else if(toX == 7 && toY == 0){
                curBoard[6][0] = 6;
                curBoard[5][0] = 4;
                piece = 0;
            }
        }

        curBoard[toX][toY] = piece;
        //System.out.print(toString());
        if(isGameOver() != 0){
            gui.getController().isGameOver = true;
        }
    }

    //Tries to move a piece, checks if the move is a valid move
    public boolean tryToMovePiece(int fromX, int fromY, int toX, int toY){
        if(fromX == toX && fromY == toY){return false;}
        if(isMovePossible(toX,toY)){
            movePiece(fromX,fromY,toX,toY);
            return true;
        }
        return false;
    }

    //Checks if a position on the board can be found in the list of possible moves
    public boolean isMovePossible(int posX, int posY){
        for(int i = 0; i < possibleMoves.length; i++) {
            int x = possibleMoves[i][0];
            int y = possibleMoves[i][1];
            //System.out.print("\nX: " + x + ", Y: " + y);
            if (posX == x && posY == y) {
                return true;
            }
        }
        return false;
    }

    //Checks if a position diagonally from a pawn is an opponent piece
    private boolean checkPawnDiagonals(int diagonalX, int diagonalY){
        if(diagonalY >= 0 && diagonalY <= 7 && diagonalX >= 0 && diagonalX <= 7){
            int enemyPiece = getPieceAt(diagonalX,diagonalY);
            if(enemyPiece >= 7){
                return true;
            }
        }
        return false;
    }

    public void printBoard(byte[][] board){
        String output = "";
        for(int x = 0; x < board.length; x++){
            output += "\n";
            for(int y = 0; y < board[x].length; y++){
                output += board[x][y] + " ";
            }
        }
        //.out.print(output);
    }

    //Checks if the game is over, basically if one of the kings are missing
    public int isGameOver(){
        boolean whiteKing = false;
        boolean blackKing = false;
        for(int i = 0; i < curBoard.length; i++){
            for(int k = 0; k < curBoard[i].length; k++){
                if(curBoard[i][k] == 6){whiteKing = true;}
                else if(curBoard[i][k] == 12){blackKing = true;}
            }
        }
        return (whiteKing && blackKing)? 0 : !blackKing? 1 : -1;
    }

    //Returns the move difference between two boards
    private int[][] findMove(byte[][] b1,byte[][] b2){
        int[][] move = new int[2][2];
        int cell = 0;
        for(int i = 0; i < b1.length; i++){
            for(int k = 0; k < b1[i].length; k++){
                if(b1[i][k] != b2[i][k]){
                    move[cell] = new int[]{i,k};
                    cell++;
                    if(cell == 2){return move;}
                }
            }
        }
        return move;
    }

    //Checks if the white king is in check
    public void checkForCheck(){
        //Finds the king position
        if(getPieceAt(gui.kingPos[0],gui.kingPos[1]) != 6){
            boolean kingFound = false;
            for(int i = 7; i >= 0; i--){
                for(int k = 0; k < 8; k++){
                    if(curBoard[i][k] == 6){
                        gui.kingPos = new int[]{i,k};
                        kingFound = true;
                        break;
                    }
                    if(kingFound){break;}
                }
            }
        }

        ArrayList<int[]> posMoves = Possible.possible(curBoard,new boolean[]{false, true, true, true, true});
        for(int i = 0; i < posMoves.size(); i++){
            int posMoveX = posMoves.get(i)[2];
            int posMoveY = posMoves.get(i)[3];
            if(posMoveX == gui.kingPos[0] && posMoveY == gui.kingPos[1]){
                gui.isInCheck = true;
                return;
            }
        }
        gui.isInCheck = false;
    }
}
