package ChessEngine.com.company.tests;

import ChessEngine.com.company.AI;
import ChessEngine.com.company.board.Possible;
import ChessEngine.com.company.board.Value;
import GUI.SaveAndLoad;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;

public class AITest {

    @Test
    public void moveTestShallow() {
        byte[][] board = SaveAndLoad.loadBoard("data/board.txt");
        AI ai = new AI(1000);
        int b = Value.value(board);
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for(int n = 0; n < 30; n++) {
            ai.setBoard(board, true, true);
            try {
                for (int i = 0; i < 40; i++) ai.iterate();
                if(Value.value(ai.getBoard()) >= 5000 * 5) break;
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            scores.add(Value.value(ai.getBoard()));
        }
        double total = 0;
        for(int s : scores) total += s;
        total /= scores.size();

        Assert.assertEquals(0, total, 2000);
    }

    @Test
    public void moveTestDeep() {
        byte[][] board = SaveAndLoad.loadBoard("data/board.txt");
        AI ai = new AI(5000);
        int b = Value.value(board);
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for(int n = 0; n < 5; n++) {
            ai.setBoard(board, true, true);
            try {
                for (int i = 0; i < 50; i++) {
                    ai.iterate();
                    if(Value.value(ai.getBoard()) >= 5000 * 5) break;
                }
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            scores.add(Value.value(ai.getBoard()));
        }
        double total = 0;
        for(int s : scores) total += s;
        total /= scores.size();

        Assert.assertEquals(0, total, 1000);
    }
}
