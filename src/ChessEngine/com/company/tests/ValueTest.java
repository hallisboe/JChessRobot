package ChessEngine.com.company.tests;

import ChessEngine.com.company.board.Value;
import GUI.SaveAndLoad;
import org.junit.Assert;
import org.junit.Test;

public class ValueTest {

    @Test
    public void balanceTest() {
        byte[][] board = SaveAndLoad.loadBoard("data/board.txt");
        Assert.assertTrue(Value.value(board) == 0);
    }
}
