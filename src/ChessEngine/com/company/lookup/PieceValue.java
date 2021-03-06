package ChessEngine.com.company.lookup;

public class PieceValue {

    private final int[] V = {100, 280, 320, 479, 929, 9600};
    private int[][][] VALUES = new int[][][] { // Taken from sunfish
            /*
            Copyright (C) 2014 Thomas Ahle

            This program is free software: you can redistribute it and/or modify
            it under the terms of the GNU General Public License as published by
            the Free Software Foundation, either version 3 of the License, or
            (at your option) any later version.

            This program is distributed in the hope that it will be useful,
            but WITHOUT ANY WARRANTY; without even the implied warranty of
            MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            GNU General Public License for more details.

            You should have received a copy of the GNU General Public License
            along with this program.  If not, see <http://www.gnu.org/licenses/>.
            
            This is taken from sunfish, an open source chess engine. No code is taken, just values used to estimate value of position.
            All credits go to:
            Thomas Ahle
            https://github.com/thomasahle/sunfish
            */
            {
                    {0, 0, 0, 0, 0, 0, 0, 0}, // PAWN
                    {78, 83, 86, 73, 102, 82, 85, 90},
                    {7, 29, 21, 44, 40, 31, 44, 7},
                    {-17, 16, -2, 15, 14, 0, 15, -13},
                    {-26, 3, 10, 9, 6, 1, 0, -23},
                    {-22, 9, 5, -11, -10, -2, 3, -19},
                    {-31, 8, -7, -37, -36, -14, 3, -31},
                    {0, 0, 0, 0, 0, 0, 0, 0}
            },
            {
                    {-66, -53, -75, -75, -10, -55, -58, -70}, // KNIGTH
                    {-3, -6, 100, -36, 4, 62, -4, -14},
                    {10, 67, 1, 74, 73, 27, 62, -2},
                    {24, 24, 45, 37, 33, 41, 25, 17},
                    {-1, 5, 31, 21, 22, 35, 2, 0},
                    {-18, 10, 13, 22, 18, 15, 11, -14},
                    {-23, -15, 2, 0, 2, 0, -23, -20},
                    {-74, -23, -26, -24, -19, -35, -22, -69}
            },
            {
                    {-59, -78, -82, -76, -23, -107, -37, -50}, // BISHOP
                    {-11, 20, 35, -42, -39, 31, 2, -22},
                    {-9, 39, -32, 41, 52, -10, 28, -14},
                    {25, 17, 20, 34, 26, 25, 15, 10},
                    {13, 10, 17, 23, 17, 16, 0, 7},
                    {14, 25, 24, 15, 8, 25, 20, 15},
                    {19, 20, 11, 6, 7, 6, 20, 16},
                    {-7, 2, -15, -12, -14, -15, -10, -10}
            },
            {
                    {35, 29, 33, 4, 37, 33, 56, 50},
                    {55, 29, 56, 67, 55, 62, 34, 60},
                    {19, 35, 28, 33, 45, 27, 25, 15},    //ROOK
                    {0, 5, 16, 13, 18, -4, -9, -6},
                    {-28, -35, -16, -21, -13, -29, -46, -30},
                    {-42, -28, -42, -25, -25, -35, -26, -46},
                    {-53, -38, -31, -26, -29, -43, -44, -53},
                    {-30, -24, -18, 5, -2, -18, -31, -32}
            },
            {
                    {6, 1, -8, -104, 69, 24, 88, 26},
                    {14, 32, 60, -10, 20, 76, 57, 24},
                    {-2, 43, 32, 60, 72, 63, 43, 2}, // Queen
                    {1, -16, 22, 17, 25, 20, -13, -6},
                    {-14, -15, -2, -5, -1, -10, -20, -22},
                    {-30, -6, -13, -11, -16, -11, -16, -27},
                    {-36, -18, 0, -19, -15, -15, -21, -38},
                    {-39, -30, -31, -13, -31, -36, -34, -42}
            },
            {
                    {4, 54, 47, -99, -99, 60, 83, -62},
                    {-32, 10, 55, 56, 56, 55, 10, 3},
                    {-62, 12, -57, 44, -67, 28, 37, -31},
                    {-55, 50, 11, -4, -19, 13, 0, -49},
                    {-55, -43, -52, -28, -51, -47, -8, -50},
                    {-47, -42, -43, -79, -64, -32, -29, -32},
                    {-4, 3, -14, -50, -57, -18, 13, 4},
                    {17, 30, -3, -14, 6, -1, 40, 18}
            }
            // End of code taken from sunfish
    };

    public int[][][] getValues() {
        return VALUES;
    }

    public PieceValue() {
        for(int i = 0; i < VALUES.length; i++) {
            for(int y = 0; y < VALUES[0].length; y++) {
                for(int x = 0; x < VALUES[0][0].length; x++) {
                    VALUES[i][y][x] += V[i] * 5;
                }
            }
        }
    }
}
