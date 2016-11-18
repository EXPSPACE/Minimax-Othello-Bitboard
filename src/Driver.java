/**
 * Created by NSPACE on 11/17/2016.
 */
public class Driver {
    public static void main(String[] args) {

        /**
         * othello bitboard encoding - https://github.com/denkspuren/BitboardC4/blob/master/BitboardDesign.md
         *
         *  63 62 61 60 59 58 57 56
         *  55 54 53 52 51 50 49 48
         *  47 46 45 44 43 42 41 40
         *  39 38 37 36 35 34 33 32
         *  31 30 29 28 27 26 25 24
         *  23 22 21 20 19 18 17 16
         *  15 14 13 12 11 10  9  8
         *   7  6  5  4  3  2  1  0
         */

        long bbPOne = 0b00000000_00000000_00000000_00010000_00001000_00000000_00000000_00000000L; //initial black stones
        long bbPTwo = 0b00000000_00000000_00000000_00001000_00010000_00000000_00000000_00000000L; //initial white stones

        BitBoardHelper bbHelper = new BitBoardHelper();
        BitBoardOps bbOps = new BitBoardOps();

        bbHelper.bbPrint(bbPOne,bbPTwo);

        long moveOne = bbOps.generateMoves(bbPOne,bbPTwo);

        bbHelper.bbPrint(moveOne,0);
    }
}
