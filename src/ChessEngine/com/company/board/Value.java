package ChessEngine.com.company.board;
import ChessEngine.com.company.lookup.PieceValue;

public class Value {

    private static PieceValue values = new PieceValue();

    public static int value(byte[][] position) {
        int v = 0;
        for(int i = 0; i < position.length; i++) {
            for (int j = 0; j < position.length; j++) {
                byte p = position[i][j];
                if(p == 0) continue;
                if(p < 7) {
                    v += values.getValues()[p - 1][7 - j][i];
                } else {
                    v -= values.getValues()[p - 7][j][i];
                }
            }
        }
        return v;
    }

}

