package ChessEngine.com.company.tests;

import ChessEngine.com.company.AI;
import ChessEngine.com.company.board.Possible;
import ChessEngine.com.company.board.Value;
import GUI.SaveAndLoad;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AITest {
    @Test
    public void moveTest() {
        byte[][] board = SaveAndLoad.loadBoard("data/board.txt");
        AI ai = new AI(1000);
        int b = Value.value(board);
        ai.setBoard(board, true, true);
        try {
            for(int i = 0; i < 20; i++) ai.iterate();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        int a = Value.value( ai.getBoard());
        System.out.println(a + " - " + b);
        Assert.assertTrue(a >= b);
    }
}
