package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JPanel{

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private static final int OFFSET = 0;

    private static final int GRID_SIZE = 8;
    private static final int BOX_SIZE = HEIGHT/GRID_SIZE;

    private static final Color colorA = Color.white;
    private static final Color colorB = Color.gray;

    private int[] curPos = {0,7};
    private int[] selected;
    public int[] kingPos = {0,4};
    private boolean isHolding = false;
    private byte currentPiece = 0;
    private boolean canPlay = true;
    public boolean isInCheck = false;

    private int winner = 0;
    private int moveCount = 0;

    private GameController controller;

    Graphics g;
    BufferedImage[] sprites;

    BoardController bc;

    public GUI(GameController controller){
        initializeSprites();
        this.setSize(WIDTH,HEIGHT + GUI.OFFSET + 5);
        bc = new BoardController(this);
        this.controller = controller;
    }

    public boolean isHolding() {
        return isHolding;
    }

    public boolean canPlay(){return canPlay;}

    public GameController getController(){return this.controller;}

    public void paintComponent(Graphics g) {
        g.clearRect(0,0,8*BOX_SIZE,8*BOX_SIZE);
        super.paintComponents(g);
        this.g = g;
        drawGrid();
        drawBoard(bc.getBoard());

        if(isInCheck){
            drawPosition(kingPos[0],7-kingPos[1],4);
        }

        if(canPlay){
            int[][] move = bc.getMove();
            if(move != null){
                drawPosition(move[0][0],7-move[0][1],3);
                drawPosition(move[1][0],7-move[1][1],3);
            }
        }

        int c = 0;
        if(isHolding && canPlay){
            drawPossibleMoves();
            if(bc.isMovePossible(curPos[0],7-curPos[1])){
                c = 2;
            }
        }

        if(canPlay){
            drawPosition(curPos[0],curPos[1],c);
        }

        drawMoveCount();
        if(controller.isGameOver){
            drawGameOver();
        }
        //drawGameOver();

    }

    private void drawGrid(){
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                int color = (x + y + 1)%2;
                g.setColor(color == 0? colorA : colorB);
                g.fillRect(x*BOX_SIZE, y*BOX_SIZE + OFFSET,BOX_SIZE,BOX_SIZE);
            }
        }
    }

    private void drawPiece(int x, int y, int image){
        image = (image < 0)? 0 : (image >= 12)? 11 : image;
        g.drawImage(sprites[image],x*BOX_SIZE,y*BOX_SIZE + OFFSET, BOX_SIZE, BOX_SIZE,this);
    }

    private void initializeSprites(){
        try{
            BufferedImage image = ImageIO.read(new File("data/ChessPieces.png"));
            final int width = image.getWidth()/6;
            final int height = image.getHeight()/2;
            final int rows = 2, cols = 6;
            sprites = new BufferedImage[rows*cols];

            for(int i = 0; i < rows; i++){
                for(int k = 0; k < cols; k++){
                    sprites[i*cols + k] = image.getSubimage(k*width,i*height,width,height);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void drawBoard(byte[][] board){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length; y++){
                int index = getGridPieceValue(board[x][y]);
                if(index != -1){
                    drawPiece(x,7-y,index);
                }
            }
        }
    }

    private byte getGridPieceValue(byte index){
        switch (index){
            case PieceData.WHITE_PAWN:
                index = 5;
                break;
            case PieceData.WHITE_ROOK:
                index = 4;
                break;
            case PieceData.WHITE_KNIGHT:
                index = 3;
                break;
            case PieceData.WHITE_BISHOP:
                index = 2;
                break;
            case PieceData.WHITE_QUEEN:
                index = 1;
                break;
            case PieceData.WHITE_KING:
                index = 0;
                break;
            case PieceData.BLACK_PAWN:
                index = 11;
                break;
            case PieceData.BLACK_ROOK:
                index = 10;
                break;
            case PieceData.BLACK_KNIGHT:
                index = 9;
                break;
            case PieceData.BLACK_BISHOP:
                index = 8;
                break;
            case PieceData.BLACK_QUEEN:
                index = 7;
                break;
            case PieceData.BLACK_KING:
                index = 6;
                break;
            default:
                index = -1;
                break;
        }
        return index;
    }

    private void drawPosition(int x, int y,int c){
        int rectWidth = BOX_SIZE/3;
        int rectHeight = BOX_SIZE/10;
        Color color = (c == 0)? Color.YELLOW : (c == 1)? Color.ORANGE: (c == 2)? Color.green : (c == 3)? new Color(70,130,180,120) : (c == 4)? new Color(128,0,0) : Color.GREEN;
        g.setColor(color);
        g.fillRect(x*BOX_SIZE,y*BOX_SIZE + OFFSET,rectWidth,rectHeight);
        g.fillRect(x*BOX_SIZE,y*BOX_SIZE + BOX_SIZE - rectHeight + OFFSET,rectWidth,rectHeight);
        g.fillRect(x*BOX_SIZE + BOX_SIZE - rectWidth,y*BOX_SIZE + OFFSET,rectWidth,rectHeight);
        g.fillRect(x*BOX_SIZE + BOX_SIZE - rectWidth,y*BOX_SIZE + BOX_SIZE - rectHeight + OFFSET,rectWidth,rectHeight);

        g.fillRect(x*BOX_SIZE,y*BOX_SIZE + rectHeight + OFFSET,rectHeight,rectHeight);
        g.fillRect(x*BOX_SIZE + BOX_SIZE -rectHeight,y*BOX_SIZE + rectHeight + OFFSET,rectHeight,rectHeight);
        g.fillRect(x*BOX_SIZE,y*BOX_SIZE + BOX_SIZE - 2*rectHeight + OFFSET,rectHeight,rectHeight);
        g.fillRect(x*BOX_SIZE + BOX_SIZE -rectHeight,y*BOX_SIZE + BOX_SIZE - 2*rectHeight + OFFSET,rectHeight,rectHeight);
    }

    public void moveCurPos(int x,int y){
        if(!canPlay){return;}
        int newX = curPos[0] + x;
        int newY = curPos[1] + y;
        newX = (newX <= 0)? 0 : (newX >= 7)? 7 : newX;
        newY = (newY <= 0)? 0 : (newY >= 7)? 7 : newY;
        curPos = new int[]{newX,newY};
        repaint();
    }


    public void pickUp(){
        currentPiece = bc.getPieceAt(curPos[0],7-curPos[1]);
        System.out.print("\nCurrentPiece: " + currentPiece);
        if(currentPiece == 0 || currentPiece >= 7){return;}
        isHolding = true;
        selected = new int[]{curPos[0],7-curPos[1]};
        bc.calculatePossibleMoves(currentPiece,curPos[0],7-curPos[1]);
        drawPossibleMoves();
        repaint();
    }

    public void drop(){
        isHolding = false;
        boolean hasMoved = bc.tryToMovePiece(selected[0],selected[1],curPos[0],7-curPos[1]);
        System.out.print("\nHas Moved: " + hasMoved);
        if(hasMoved){
            togglePlay();
            repaint();
            moveCount++;
            winner = 1;
           if(!controller.isGameOver){
                controller.chessEngineMove();
                bc.checkForCheck();
                int gameStatus = bc.isGameOver();
                if(gameStatus == 0){
                    togglePlay();
                }
                else{
                    controller.isGameOver = true;
                    winner = gameStatus;
                }
            }
        }
        repaint();
    }

    private void drawPossibleMoves(){
        int[][] possibleMoves = bc.getPB();
        for(int i = 0; i < possibleMoves.length; i++){
            drawPosition(possibleMoves[i][0],7-possibleMoves[i][1],1);
        }
    }

    public void togglePlay(){
        canPlay = !canPlay;
    }

    private void drawGameOver(){
        String victor = (winner == 1)? "White" : "Black";
        Font font = new Font("Dialog",Font.BOLD,30);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(victor + " wins!", BOX_SIZE*8 + 20,40);
    }

    private void drawMoveCount(){
        String moves = "Move Count";
        Font font = new Font("Dialog",Font.BOLD,25);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.clearRect(BOX_SIZE*8,0,WIDTH-BOX_SIZE*8,HEIGHT);
        g.drawString(moves,BOX_SIZE*8 + (WIDTH-BOX_SIZE*8)/2-75,100);
        g.drawString(String.valueOf(moveCount),BOX_SIZE*8 + (WIDTH-BOX_SIZE*8)/2 - 12,125);
    }
}
