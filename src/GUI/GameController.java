package GUI;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import ChessEngine.com.company.ChessAI;

public class GameController extends JFrame{

    private DataInputStream in;
    private DataOutputStream out;
    private GUI gui;
    private ChessAI chessEngine;
    public boolean isGameOver = false;

    public GameController(){
        gui = new GUI(this);
        this.add(gui);
        this.setSize(gui.getWidth(),gui.getHeight() + 27);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.addKeyListener(new KeyUsage());
        chessEngine = new ChessAI();
        //setup();
        //update();
    }

    public static void main(String[] args){
        new GameController();
    }

    private void setup(){
        try{
            Socket s = new Socket("10.0.1.1",1111);
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void update(){
        while(true){
            try{
                String input = in.readUTF();
                System.out.println("\n|" + input + "|");
                if(input.equals("Up")){System.out.print("Moving UP"); gui.moveCurPos(0,-1);}
                else if(input.equals("Down")){System.out.print("Moving DOWN");gui.moveCurPos(0,1);}
                else if(input.equals("Left")){gui.moveCurPos(-1,0);}
                else if(input.equals("Right")){gui.moveCurPos(1,0);}
                else if(input.equals("Enter")){
                    if(!gui.isHolding()){
                        gui.pickUp();
                    }
                    else{
                        gui.drop();
                    }
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void chessEngineMove(){
        System.out.print("\nSending board to engine");

       // gui.bc.printBoard(chessEngine.getBoard());
        chessEngine.setBoard(gui.bc.getBoard());
        try{
            chessEngine.iterate();
            System.out.print("\nEngine iterated");
        }
        catch(InterruptedException e){
            e.printStackTrace();
            System.out.print("\nEngine: \"Iterate failed!\"");
        }
        //gui.bc.printBoard(chessEngine.getBoard());
        gui.bc.setBoard(chessEngine.getBoard());
        gui.repaint();
        gui.bc.toString();
    }

    private class KeyUsage implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyChar() == 'w'){
                gui.moveCurPos(0,-1);
            }
            else if(e.getKeyChar() == 's'){
                System.out.print("Down");
                gui.moveCurPos(0,1);
            }
            else if(e.getKeyChar() == 'a'){
                gui.moveCurPos(-1,0);
            }
            else if(e.getKeyChar() == 'd'){
                gui.moveCurPos(1,0);
            }
            else if(e.getKeyCode() == 10){ //Enter
                if(!gui.isHolding()){
                    //PickUp
                    gui.pickUp();
                }
                else{
                    //Drop
                    gui.drop();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
