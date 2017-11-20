package ChessEngine.com.company.tests;

import ChessEngine.com.company.AI;
import ChessEngine.com.company.board.Value;
import GUI.SaveAndLoad;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class AITest {

    private double play(int a, int g, int m, String msg) {
        byte[][] board = SaveAndLoad.loadBoard("data/board.txt");
        AI ai = new AI(a);
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for(int n = 0; n < g; n++) {
            ai.setBoard(board, true, true);
            try {
                for (int i = 0; i < m; i++) {
                    ai.iterate();
                    if(Math.abs(Value.value(ai.getBoard())) >= 929 * 5 * 2) break;
                }
                System.out.println("[DONE] " + msg + " " + (n + 1)  + " of " + g);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            int v = Value.value(ai.getBoard());
            scores.add(Math.abs(v) >= 929 * 5 * 2 ? 0 : v);
        }
        double total = 0;
        for(int s : scores) total += s;
        System.out.println("[BALANCE] " + total / scores.size());
        return total / scores.size();
    }
    @Test
    public void moveTestShallow() {
        Assert.assertEquals(0, play(1000, 20, 40, "Shallow game"), 1250);
    }

    @Test
    public void moveTestDeep() {
        Assert.assertEquals(0, play(5000, 5, 20, "Deep game"), 2500);
    }
}

