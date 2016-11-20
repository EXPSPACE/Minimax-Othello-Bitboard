import java.util.ArrayList;

/**
 * Created by NSPACE on 11/17/2016.
 */
public class BitBoardOps {

    /**
     * Masks for east and west shift are used to prevent bitboard wraparound
     **/
    private static final long MASK_E = 0b11111110_11111110_11111110_11111110_11111110_11111110_11111110_11111110L;
    private static final long MASK_W = 0b01111111_01111111_01111111_01111111_01111111_01111111_01111111_01111111L;

    /**
     * Methods to shifts a players in one of the 8 possible directions (N,S,E,W,NW,NE,SW,SE)
     */

    private long shiftN(long bb) {
        return bb << 8;
    }

    private long shiftS(long bb) {
        return bb >> 8;
    }

    private long shiftE(long bb) {
        return (bb & MASK_E) >> 1;
    }

    private long shiftW(long bb) {
        return (bb & MASK_W) << 1;
    }

    private long shiftNW(long bb) {
        return shiftN(shiftW(bb));
    }

    private long shiftNE(long bb) {
        return shiftN(shiftE(bb));
    }

    private long shiftSW(long bb) {
        return shiftS(shiftW(bb));
    }

    private long shiftSE(long bb) {
        return shiftS(shiftE(bb));
    }


    /**
     * Shifts a players bitboard in each possible direction and compares to the enemies.
     * When a capture is possible further shifts and compares with non zero bit arrays occur.
     * Final check ensures move is an open square.
     *
     * @returns array of long integer with single bit for each legal move
     */

    public long generateMoves(long bbSelf, long bbEnemy) {
        long moves = 0L;
        long open = ~(bbSelf | bbEnemy);
        long captured;

        //NORTH
        captured = shiftN(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftN(captured) & bbEnemy;
        }
        moves |= shiftN(captured) & open;

        //SOUTH
        captured = shiftS(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftS(captured) & bbEnemy;
        }
        moves |= shiftS(captured) & open;

        //WEST
        captured = shiftW(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftW(captured) & bbEnemy;
        }
        moves |= shiftW(captured) & open;

        //EAST
        captured = shiftE(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftE(captured) & bbEnemy;
        }
        moves |= shiftE(captured) & open;


        //NORTHWEST
        captured = shiftNW(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftNW(captured) & bbEnemy;
        }
        moves |= shiftNW(captured) & open;

        //NORTHEAST
        captured = shiftNE(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftNE(captured) & bbEnemy;
        }
        moves |= shiftNE(captured) & open;

        //SOUTHWEST
        captured = shiftSW(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftSW(captured) & bbEnemy;
        }
        moves |= shiftSW(captured) & open;

        //SOUTHEAST
        captured = shiftSE(bbSelf) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftSE(captured) & bbEnemy;
        }
        moves |= shiftSE(captured) & open;

        return moves;
    }

    /**
     * Converts bitboard representation of all possible legal moves to an array
     * of a single bit long integers representing a single move
     *
     * @param moves long bitboard representation of all possible legal moves moves
     * @return array of single bit long integers representing a single move
     */

    public long[] toBitMoveArray(long moves) {
        int moveIndex = 0;
        long[] bitMoveArray = new long[Long.bitCount(moves)];

        for (int i = 0; i < 64; i++) {
            if (((moves >> i) & 1) == 1) {
                bitMoveArray[moveIndex] = 1L << i;
                moveIndex++;
            }
        }

        return bitMoveArray;
    }

    /**
     * Updates the players bitboard using given move and turn. Shifts provided move in each direction
     * and compares to enemy bitboard. A check on self bitboard ensures existence of capping stone.
     * Assumes that the move provided is valid.
     *
     * @return updated bitboards for both players
     */

    //TODO optimize make move
    public long[] makeMove(long move, long bbSelf, long bbEnemy, int turn) {
        long captured;

        bbSelf |= move;

        //NORTH
        captured = shiftN(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftN(captured) & bbEnemy;
        }
        if ((shiftN(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        //SOUTH
        captured = shiftS(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftS(captured) & bbEnemy;
        }
        if ((shiftS(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        //WEST
        captured = shiftW(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftW(captured) & bbEnemy;
        }
        if ((shiftW(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        //EAST
        captured = shiftE(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftE(captured) & bbEnemy;
        }
        if ((shiftE(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        //NORTHWEST
        captured = shiftNW(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftNW(captured) & bbEnemy;
        }
        if ((shiftNW(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        //NORTHEAST
        captured = shiftNE(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftNE(captured) & bbEnemy;
        }
        if ((shiftNE(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        //SOUTHWEST
        captured = shiftSW(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftSW(captured) & bbEnemy;
        }
        if ((shiftSW(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        //SOUTHEAST
        captured = shiftSE(move) & bbEnemy;
        for (int i = 0; i < 5; i++) {
            captured |= shiftSE(captured) & bbEnemy;
        }
        if ((shiftSE(captured) & bbSelf) > 0) {
            bbSelf |= captured;
            bbEnemy &= ~captured;
        }

        if (turn % 2 == 0) {
            return new long[]{bbSelf, bbEnemy};
        } else {
            return new long[]{bbEnemy, bbSelf};
        }
    }

    /**
     * Greedy heuristic which returns whatever move results in the largest number of disk
     * gains.
     *
     * @return updated long bitboards after choosing a move with the most flipping outcome; same bitboard if no moves possible
     */

    public long[] makeMaxDiskMove(long bbSelf, long bbEnemy, int turn) {

        ArrayList<long[]> childBitBoards = getChildBitboards(bbSelf, bbEnemy, turn);

        //return same board and exit if no moves can be made
        if (childBitBoards.size() == 0) {
            if (turn % 2 == 0) {
                return new long[]{bbSelf, bbEnemy};
            } else {
                return new long[]{bbEnemy, bbSelf};
            }
        }

        long[] maxBitBoard = new long[2];
        int maxDiskCount = 0;

        //count max number of disks accrued in each child board; return largest updated one
        for(long[] childBitBoard : childBitBoards) {
            long selfBoard = childBitBoard[turn & 1];
            if (Long.bitCount(selfBoard) > maxDiskCount) {
                maxDiskCount = Long.bitCount(selfBoard);
                maxBitBoard = childBitBoard;
            }
        }

        return maxBitBoard;
    }

    public ArrayList<long[]> getChildBitboards(long bbSelf, long bbEnemy, int turn) {
        ArrayList<long[]> childBitBoards = new ArrayList<>();

        //generate all candidate moves
        long moves = generateMoves(bbSelf, bbEnemy);
        long[] bitMoveArray = toBitMoveArray(moves);

        //make all child boards given candidate moves
        for (int i = 0; i < bitMoveArray.length; i++) {
            childBitBoards.add(makeMove(bitMoveArray[i], bbSelf, bbEnemy, turn));
        }

        return childBitBoards;
    }

    /**
     * Tests each of the three potential conditions for a game over
     */

    public boolean gameOver(long bbPOne, long bbPTwo, int numMovesPOne, int numMovesPTwo) {
        return ((bbPOne | bbPTwo) == -1L) ||             // All squares are occupied.
                (numMovesPOne + numMovesPTwo == 0) ||    // Neither player has any moves available.
                (bbPOne == 0 || bbPTwo == 0);            // One player has had all chips eliminated.
    }

    /**
     * Mobility heuristic which returns difference between number of self moves and number
     * of enemy moves.
     */

    public int mobilityEvalFunc(long bbSelf, long bbEnemy) {
        return Long.bitCount(generateMoves(bbSelf, bbEnemy)) - Long.bitCount(generateMoves(bbEnemy, bbSelf));
    }

    /**
     * static evaluation table used is taken from reversi program that was included with past versions of Microsoft Windows
     * source : http://www.samsoft.org.uk/reversi/strategy.htm#position
     *
     * 10 distinct square types used for evaluation : s0 - s9
     *
     *      s9 s8 s6 s3 s3 s6 s8 s9
     *      s8 s7 s5 s2 s2 s5 s7 s8
     *      s6 s5 s4 s1 s1 s4 s5 s6
     *      s3 s2 s1 s0 s0 s1 s2 s3
     *      s3 s2 s1 s0 s0 s1 s2 s3
     *      s6 s5 s4 s1 s1 s4 s5 s6
     *      s8 s7 s5 s2 s2 s5 s7 s8
     *      s9 s8 s6 s3 s3 s6 s8 s9
     *
     *
     * crude.
     */

    //bitboard representation of each square type
    public static final long s0_BB = 0b00000000_00000000_00000000_00011000_00011000_00000000_00000000_00000000L;
    public static final long s1_BB = 0b00000000_00000000_00011000_00100100_00100100_00011000_00000000_00000000L;
    public static final long s2_BB = 0b00000000_00011000_00000000_01000010_01000010_00000000_00011000_00000000L;
    public static final long s3_BB = 0b00011000_00000000_00000000_10000001_10000001_00000000_00000000_00011000L;
    public static final long s4_BB = 0b00000000_00000000_00100100_00000000_00000000_00100100_00000000_00000000L;
    public static final long s5_BB = 0b00000000_00100100_01000010_00000000_00000000_01000010_00100100_00000000L;
    public static final long s6_BB = 0b00100100_00000000_10000001_00000000_00000000_10000001_00000000_00100100L;
    public static final long s7_BB = 0b00000000_01000010_00000000_00000000_00000000_00000000_01000010_00000000L;
    public static final long s8_BB = 0b01000010_10000001_00000000_00000000_00000000_00000000_10000001_01000010L;
    public static final long s9_BB = 0b10000001_00000000_00000000_00000000_00000000_00000000_00000000_10000001L;

    //numerical weight assigned to each square type
    public static final int s0_WEIGHT = 0;
    public static final int s1_WEIGHT = 4;
    public static final int s2_WEIGHT = -3;
    public static final int s3_WEIGHT = 6;
    public static final int s4_WEIGHT = 7;
    public static final int s5_WEIGHT = -4;
    public static final int s6_WEIGHT = 8;
    public static final int s7_WEIGHT = -24;
    public static final int s8_WEIGHT = -8;
    public static final int s9_WEIGHT = 99;

    public static final long[] SQUARE_LIST = {s0_BB,s1_BB,s2_BB,s3_BB,s4_BB,s5_BB,s6_BB,s7_BB,s8_BB,s9_BB};
    public static final int[] WEIGHT_LIST = {s0_WEIGHT,s1_WEIGHT,s2_WEIGHT,s3_WEIGHT,s4_WEIGHT,s5_WEIGHT,s6_WEIGHT,s7_WEIGHT,s8_WEIGHT,s9_WEIGHT};

    /**
     * Evaluation function which computes its value based on the fixed positional strength of 10 distinct squares.
     * Counts the number of matches a player has with each square type and multiplies by a fixed weight associated with
     * that square type. The sum over all square types for the player is subtracted by same evaluation for the opponent.
     *
     * @return
     */

    public int positionalEvalFunc(long bbSelf, long bbEnemy) {
        int totalValue = 0;
        for(int i = 0; i < SQUARE_LIST.length; i++) {
            //add self squares
            int numMatch = Long.bitCount(SQUARE_LIST[i] & bbSelf);
            totalValue += (numMatch * WEIGHT_LIST[i]);

            //subtract enemy squares
            numMatch = Long.bitCount(SQUARE_LIST[i] & bbEnemy);
            totalValue -= (numMatch * WEIGHT_LIST[i]);
        }
        return totalValue;
    }
}
