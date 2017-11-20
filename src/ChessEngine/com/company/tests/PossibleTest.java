package ChessEngine.com.company.tests;

import ChessEngine.com.company.board.Possible;
import ChessEngine.com.company.board.Value;
import GUI.SaveAndLoad;
import org.junit.Assert;
import org.junit.Test;

public class PossibleTest {

    @Test
    public void moveTest() {
        byte[][] board = SaveAndLoad.loadBoard("data/board.txt");
        Assert.assertTrue(
                Possible.possible(board, new boolean[] {true, true, true, true, true}).size() == 20
        );
        System.out.println("[DONE] First move -> number of moves");
    }
}