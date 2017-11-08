package GUI;

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

    public byte[][] getBoard(){
        return this.curBoard;
    }

    public void setBoard(byte[][] board){
        move = findMove(curBoard,board);
        this.curBoard = board;
    }

    public int[][] getMove(){
        return this.move;
    }

    public byte getPieceAt(int x, int y){
        return curBoard[x][y];
    }

    public int[][] getPB(){return possibleMoves;}

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

    public void calculatePossibleMoves(byte piece,int curX,int curY){
        possibleMoves = getAvailableMoves(piece,curX,curY);
    }

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

    private int[][][] getPieceMoves(byte piece){
        //System.out.print("Piece: " + piece);
        int pieceVal = piece;
        int[][][] moves;
        switch (pieceVal){
            case 1:
                moves = new int[1][PieceData.PAWN.length][2];
                moves[0] = PieceData.PAWN;
                break;
            case 2:
                moves = new int[1][PieceData.KNIGHT.length][2];
                moves[0] = PieceData.KNIGHT;
                break;
            case 3:
                moves = extendPossibleMoves(PieceData.BISHOP);
                break;
            case 4:
                moves =  extendPossibleMoves(PieceData.ROOK);
                break;
            case 5:
                moves =  extendPossibleMoves(PieceData.QUEEN);
                break;
            case 6:
                moves = new int[1][PieceData.KING.length][2];
                moves[0] = PieceData.KING;
                break;
            default:
                moves = null;
                break;
        }
        return moves;
    }

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
        boolean isKing = (curBoard[toX][toY] == 12);
        curBoard[toX][toY] = piece;
        System.out.print(toString());
        if(isKing || isGameOver() != 0){
            gui.getController().isGameOver = true;
        }
    }

    public boolean tryToMovePiece(int fromX, int fromY, int toX, int toY){
        if(fromX == toX && fromY == toY){return false;}
        if(isMovePossible(toX,toY)){
            movePiece(fromX,fromY,toX,toY);
            return true;
        }
        return false;
    }

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
        System.out.print(output);
    }

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
}
