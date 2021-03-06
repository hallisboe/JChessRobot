package ChessEngine.com.company;

import ChessEngine.com.company.board.Possible;
import ChessEngine.com.company.board.Value;

import java.util.ArrayList;
import java.util.Arrays;

import static ChessEngine.com.company.lookup.Pieces.*;

public class Node{

    final byte[][] position; // Brettet
    final boolean[] data; // Data tilhørende brettet

    Node[] children; // Posisjoner som kommer etter

    boolean unchanged = false; // Optimalisering som lagrer verdi av node til senere bruk
    int last_value;

    Node(byte[][] position, boolean[] data) {
        this.position = position;
        this.data = data;
    }

    /*
    Funksjon:
    Returnerer matematisk verdi av posisjonen på brettet.
     */
    int value() {
        return Value.value(position);
    }

    /*
    Funksjon:
    Fyller ut children listen ved å finne alle mulige trekk.
     */
    void expand(int loop) {
        unchanged = false;
        if(Math.abs(Value.value(position)) < 5000 * 5) {
            ArrayList<Node> c = new ArrayList();
            for (int[] move : Possible.possible(position, data)) {
                byte[][] newPosition = new byte[8][8];
                for (int i = 0; i < position.length; i++)
                    newPosition[i] = Arrays.copyOf(position[i], position[i].length);

                boolean expand = false;
                if (newPosition[move[2]][move[3]] > 0) {
                    expand = true;
                }
                newPosition[move[2]][move[3]] = newPosition[move[0]][move[1]];
                newPosition[move[0]][move[1]] = 0;

                for (int i = 0; i < position.length; i++) {
                    if (position[i][0] == BLACK_PAWN) position[i][0] = BLACK_QUEEN;
                    if (position[i][position.length - 1] == WHITE_PAWN) position[i][position.length - 1] = WHITE_QUEEN;
                }

                boolean[] newData = Arrays.copyOf(data, data.length);
                newData[0] = !newData[0];

                if(data[0] && position[move[0]][move[1]] == WHITE_KING) {
                    if(move[2] - move[0] == 2) {
                        newData[1] = false;
                        newData[2] = false;
                        newPosition[7][0] = 0;
                        newPosition[4][0] = WHITE_ROOK;
                    }
                    if(move[2] - move[0] == -2) {
                        newData[1] = false;
                        newData[2] = false;
                        newPosition[0][0] = 0;
                        newPosition[3][0] = WHITE_ROOK;
                    }
                } else if(position[move[0]][move[1]] == BLACK_KING) {
                    if(move[2] - move[0] == 2) {
                        newData[3] = false;
                        newData[4] = false;
                        newPosition[7][7] = 0;
                        newPosition[4][7] = BLACK_ROOK;
                    }
                    if(move[2] - move[0] == -2) {
                        newData[3] = false;
                        newData[4] = false;
                        newPosition[0][7] = 0;
                        newPosition[3][7] = BLACK_ROOK;
                    }
                }

                Node child = new Node(newPosition, newData);
                if (expand && loop > 0) child.expand(loop - 1);
                c.add(child);

            }
            children = c.toArray(new Node[c.size()]);
        }
    }

    ArrayList<Node> getAllChildren() {
        ArrayList<Node> c = new ArrayList<>();
        if(children != null) {
            for (Node child : children) {
                c.addAll(child.getAllChildren());
            }
        } else {
            c.add(this);
        }
        return c;
    }

    /*
    Funksjon:
    Returner node som String.
     */
    public String toString() {
        return "Node (value = " + value() + ")";
    }
}