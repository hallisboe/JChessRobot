package ChessEngine.com.company.tests;

import ChessEngine.com.company.board.Value;
import GUI.SaveAndLoad;
import org.junit.Assert;
import org.junit.Test;

import static ChessEngine.com.company.lookup.Pieces.BLACK_PAWN;
import static ChessEngine.com.company.lookup.Pieces.WHITE_PAWN;

public class ValueTest {

    @Test
    public void balanceTest() {
        byte[][] board = SaveAndLoad.loadBoard("data/board.txt");
        Assert.assertTrue(Value.value(board) == 0);
        board[3][3] = WHITE_PAWN;
        Assert.assertTrue(Value.value(board) > 0);
        board[3][3] = BLACK_PAWN;
        Assert.assertTrue(Value.value(board) < 0);
    }
}
