package bitboard;

import boku.Field;
import boku.Marble;
import tpzsgames.BoardType;
import tpzsgames.FieldType;
import tpzsgames.PieceType;

public class BokuBoard
        implements BoardType {

    public Map map;
    public static final int H = 5;
    public static final int D = 6;
    public static final int EMPTY = 1;
    public static final int MAX_VALUE = 4194304;
    public static final int WIN_VALUE = 262144;
    public LookupTable lookupTable = new LookupTable();

    private static final int COLOUR_SHIFT = 16;

    private static final int ROW_MASK_BLACK = 992;

    private static final int SHIFT_BLACK = 14;

    private static final int ROW_MASK_WHITE = 31;

    private static final int SHIFT_WHITE = 3;

    public static final int MAX_BITS = 13;

    private static final int FIRST_BIT = 3;

    private int[] viewA;

    private int[] viewB;

    private int[] viewC;

    private int lastEval;

    public BokuBoard() {
        this.map = Map.getInstance(5, 6);
        initialise();
    }

    public BokuBoard(int h, int d)
            throws IllegalArgumentException {
        this.map = Map.getInstance(h, d);
        if (this.map.maxFieldsInLine > 13) {
            throw new IllegalArgumentException("Board size too big");
        }
        initialise();
    }

    private void initialise() {
        this.viewA = new int[this.map.numberOfLines];
        this.viewB = new int[this.map.maxFieldsInLine];
        this.viewC = new int[this.map.maxFieldsInLine];
    }

    public synchronized int get(int x, int y) {
        int colour = 0;
        MapField fieldConst = this.map.getField[x][y];
        int lineView = this.viewA[fieldConst.get_yA()];

        int fieldBit = 1 << 19 + fieldConst.get_xA();
        if ((lineView & fieldBit) > 0) {
            colour = 1;
        } else if ((lineView & fieldBit >> 16) > 0) {
            colour = -1;
        }
        return colour;
    }

    public synchronized void set(int x, int y, int colour) {
        MapField fieldConst = this.map.getField[x][y];
        if (colour == 1) {
            this.viewA[fieldConst.get_yA()]
                    |= 1 << 19 + fieldConst.get_xA();
            this.viewB[fieldConst.get_yB()]
                    |= 1 << 19 + fieldConst.get_xB();
            this.viewC[fieldConst.get_yC()]
                    |= 1 << 19 + fieldConst.get_xC();
        } else if (colour == -1) {
            this.viewA[fieldConst.get_yA()]
                    |= 1 << 3 + fieldConst.get_xA();
            this.viewB[fieldConst.get_yB()]
                    |= 1 << 3 + fieldConst.get_xB();
            this.viewC[fieldConst.get_yC()]
                    |= 1 << 3 + fieldConst.get_xC();
        } else {
            clear(x, y);
        }
    }

    public synchronized void clear(int x, int y) {
        MapField fieldConst = this.map.getField[x][y];
        this.viewA[fieldConst.get_yA()]
                &= (1 << 19 + fieldConst.get_xA() ^ 0xFFFFFFFF);
        this.viewB[fieldConst.get_yB()]
                &= (1 << 19 + fieldConst.get_xB() ^ 0xFFFFFFFF);
        this.viewC[fieldConst.get_yC()]
                &= (1 << 19 + fieldConst.get_xC() ^ 0xFFFFFFFF);
        this.viewA[fieldConst.get_yA()]
                &= (1 << 3 + fieldConst.get_xA() ^ 0xFFFFFFFF);
        this.viewB[fieldConst.get_yB()]
                &= (1 << 3 + fieldConst.get_xB() ^ 0xFFFFFFFF);
        this.viewC[fieldConst.get_yC()]
                &= (1 << 3 + fieldConst.get_xC() ^ 0xFFFFFFFF);
    }

    public final synchronized Field[] getCaptureFields(int x, int y, int colour) {
        int MAX_SANDWICHED_MARBLES = 12;
        Field[] captureFields = new Field[12];
        MapField fieldConst = this.map.getField[x][y];
        int count = 0;
        int BLACK_SANDWICH = 589830;
        int WHITE_SANDWICH = 393225;

        int sandwichMask;

        if (colour == 1) {
            sandwichMask = 589830 << fieldConst.get_xA();
        } else if (colour == -1) {
            sandwichMask = 393225 << fieldConst.get_xA();
        } else {
            return null;
        }
        int lineView = this.viewA[fieldConst.get_yA()];

        if ((lineView & sandwichMask) == sandwichMask) {
            captureFields[(count++)] = new Field(x - 1, y - 1);
            captureFields[(count++)] = new Field(x - 2, y - 2);
        }

        sandwichMask <<= 3;

        if ((lineView & sandwichMask) == sandwichMask) {
            captureFields[(count++)] = new Field(x + 1, y + 1);
            captureFields[(count++)] = new Field(x + 2, y + 2);
        }

        if (colour == 1) {
            sandwichMask = 589830 << fieldConst.get_xB();
        } else {
            sandwichMask = 393225 << fieldConst.get_xB();
        }
        lineView = this.viewB[fieldConst.get_yB()];

        if ((lineView & sandwichMask) == sandwichMask) {
            captureFields[(count++)] = new Field(x + 1, y);
            captureFields[(count++)] = new Field(x + 2, y);
        }

        sandwichMask <<= 3;

        if ((lineView & sandwichMask) == sandwichMask) {
            captureFields[(count++)] = new Field(x - 1, y);
            captureFields[(count++)] = new Field(x - 2, y);
        }

        if (colour == 1) {
            sandwichMask = 589830 << fieldConst.get_xC();
        } else {
            sandwichMask = 393225 << fieldConst.get_xC();
        }
        lineView = this.viewC[fieldConst.get_yC()];

        if ((lineView & sandwichMask) == sandwichMask) {
            captureFields[(count++)] = new Field(x, y - 1);
            captureFields[(count++)] = new Field(x, y - 2);
        }

        sandwichMask <<= 3;

        if ((lineView & sandwichMask) == sandwichMask) {
            captureFields[(count++)] = new Field(x, y + 1);
            captureFields[(count++)] = new Field(x, y + 2);
        }

        return captureFields;
    }

    public Field[] getCaptureFields(Field field, int colour) {
        return getCaptureFields(field.getX(), field.getY(), colour);
    }

    public void set(Field field, int colour) {
        set(field.getX(),
                field.getY(), colour);
    }

    public void set(PieceType marble, FieldType field) {
        set((Field) field, ((Marble) marble).getColour());
    }

    public void clear(FieldType field) {
        clear(((Field) field).getX(), ((Field) field).getY());
    }

    public int get(int fieldNumber) {
        Field f = this.map.getField(fieldNumber);
        return get(f.getX(), f.getY());
    }

    public int get(Field field) {
        return get(field.getX(), field.getY());
    }

    public PieceType getPiece(FieldType field) {
        int colour = get(((Field) field).getX(), ((Field) field).getY());
        if (colour == 0) {
            return null;
        }
        return new Marble(colour);
    }

    public void clear() {
        for (int i = 0; i < this.viewA.length; i++) {
            this.viewA[i] = 0;
        }
        for (int i = 0; i < this.viewB.length; i++) {
            this.viewB[i] = 0;
        }
        for (int i = 0; i < this.viewC.length; i++) {
            this.viewC[i] = 0;
        }
    }

    public void setLookupTable(LookupTable lookupTable) {
        this.lookupTable = lookupTable;
    }

    private final int rowEvalA(int xA, int yA) {
        int row = 0x3E0 & this.viewA[yA] >> 14 + xA;
        row += (0x1F & this.viewA[yA] >> 3 + xA);
        return this.lookupTable.table[row];
    }

    private final int rowEvalB(int xB, int yB) {
        int row = 0x3E0 & this.viewB[yB] >> 14 + xB;
        row += (0x1F & this.viewB[yB] >> 3 + xB);
        return this.lookupTable.table[row];
    }

    private final int rowEvalC(int xC, int yC) {
        int row = 0x3E0 & this.viewC[yC] >> 14 + xC;
        row += (0x1F & this.viewC[yC] >> 3 + xC);
        return this.lookupTable.table[row];
    }

    public synchronized int evalBoard() {
        int eval = 0;
        for (int line = 0; line < this.viewA.length; line++) {
            int rowN = 0;

            do {
                eval += rowEvalA(rowN, line);
                rowN++;
                this.map.getClass();
            } while (rowN <= this.map.lineLength[0][line] - 5);
        }

        for (int line = 0; line < this.viewB.length; line++) {
            int rowN = 0;

            do {
                eval += rowEvalB(rowN, line);

                eval += rowEvalC(rowN, line);
                rowN++;
                this.map.getClass();
            } while (rowN <= this.map.lineLength[1][line] - 5);
        }

        this.lastEval = eval;
        return this.lastEval;
    }

    public int getLastEval() {
        return this.lastEval;
    }

    public synchronized boolean won(int colour) {
        if (colour == 1) {
            return evalBoard() >= 262144;
        }
        if (colour == -1) {
            return evalBoard() <= -262144;
        }
        return false;
    }

    public synchronized Field[] getWinnerFields(int x, int y, int colour) {
        Field[] winnerFields = new Field[5];
        MapField fieldConst = this.map.getField[x][y];
        boolean winnerFound = false;
        int lineA = fieldConst.get_yA();
        int lineB = fieldConst.get_yB();
        int lineC = fieldConst.get_yC();
        int rowN;
        if (colour == 1) {
            rowN = 0;
            do {
                if (rowEvalA(rowN, lineA) >= 262144) {
                    Field startField = this.map.findFieldInA(rowN, lineA);
                    for (int i = 0; i < 5; i++) {
                        winnerFields[i] = new Field(startField.getX() + i,
                                startField.getY() + i);
                    }
                    winnerFound = true;
                    break;
                }
                rowN++;
                this.map.getClass();
            } while (rowN <= this.map.lineLength[0][lineA] - 5);

            if (!winnerFound) {
                rowN = 0;
                do {
                    if (rowEvalB(rowN, lineB) >= 262144) {
                        Field startField = this.map.findFieldInB(rowN, lineB);
                        for (int i = 0; i < 5; i++) {
                            winnerFields[i] = new Field(startField.getX() - i,
                                    startField.getY());
                        }
                        winnerFound = true;
                        break;
                    }
                    rowN++;
                    this.map.getClass();
                } while (rowN <= this.map.lineLength[1][lineB] - 5);
            }

            if (!winnerFound) {
                rowN = 0;
                do {
                    if (rowEvalC(rowN, lineC) >= 262144) {
                        Field startField = this.map.findFieldInC(rowN, lineC);
                        for (int i = 0; i < 5; i++) {
                            winnerFields[i] = new Field(startField.getX(),
                                    startField.getY() + i);
                        }
                        winnerFound = true;
                        break;
                    }
                    rowN++;
                    this.map.getClass();
                } while (rowN <= this.map.lineLength[2][lineC] - 5);

            }

        } else if (colour == -1) {
            rowN = 0;
            do {
                if (-rowEvalA(rowN, lineA) >= 262144) {
                    Field startField = this.map.findFieldInA(rowN, lineA);
                    for (int i = 0; i < 5; i++) {
                        winnerFields[i] = new Field(startField.getX() + i,
                                startField.getY() + i);
                    }
                    winnerFound = true;
                    break;
                }
                rowN++;
                this.map.getClass();
            } while (rowN <= this.map.lineLength[0][lineA] - 5);

            if (!winnerFound) {
                rowN = 0;
                do {
                    if (-rowEvalB(rowN, lineB) >= 262144) {
                        Field startField = this.map.findFieldInB(rowN, lineB);
                        for (int i = 0; i < 5; i++) {
                            winnerFields[i] = new Field(startField.getX() - i,
                                    startField.getY());
                        }
                        winnerFound = true;
                        break;
                    }
                    rowN++;
                    this.map.getClass();
                } while (rowN <= this.map.lineLength[1][lineB] - 5);
            }

            if (!winnerFound) {
                rowN = 0;
                do {
                    if (-rowEvalC(rowN, lineC) >= 262144) {
                        Field startField = this.map.findFieldInC(rowN, lineC);
                        for (int i = 0; i < 5; i++) {
                            winnerFields[i] = new Field(startField.getX(),
                                    startField.getY() + i);
                        }
                        winnerFound = true;
                        break;
                    }
                    rowN++;
                    this.map.getClass();
                } while (rowN <= this.map.lineLength[2][lineC] - 5);
            }
        }

        return winnerFields;
    }

    public Field[] getWinnerFields(Field moveField, int colour) {
        return getWinnerFields(moveField.getX(), moveField.getY(), colour);
    }

    public boolean isPartOfBoard(FieldType field) {
        return this.map.isValid(((Field) field).getX(), ((Field) field).getY());
    }

    public boolean isPartOfBoard(int x, int y) {
        return this.map.isValid(x, y);
    }

    public int getWidth() {
        return this.map.maxFieldsInLine;
    }

    public int getHeight() {
        return this.map.numberOfLines;
    }

    public String internalViewToString() {
        String returnStr = "    ";
        for (int x = 0; x < this.map.maxFieldsInLine; x++) {
            returnStr = returnStr + (x < 10 ? " " : "") + x + " ";
        }
        returnStr = returnStr + "\n";

        for (int y = 0; y < this.map.maxFieldsInLine; y++) {
            returnStr = returnStr + (y < 10 ? " " : "") + y + "  ";
            for (int x = 0; x < this.map.maxFieldsInLine; x++) {
                if (x < this.map.minX(y)) {
                    returnStr = returnStr + ">> ";

                } else if (x >= this.map.maxX(y)) {
                    returnStr = returnStr + "<< ";
                } else {
                    returnStr = returnStr + (get(x, y) < 0 ? "" : " ") + get(x, y) + " ";
                }
            }
            returnStr = returnStr + "\n";
        }
        return returnStr;
    }

    public String toString() {
        String returnStr = "    ";
        int line = 0;
        for (int n = 0; n < this.map.numberOfFields; n++) {
            if (this.map.firstInLine(n)) {
                returnStr = returnStr + "\n";
                int i = 0;
                do {
                    returnStr = returnStr + "  ";
                    i++;
                    this.map.getClass();
                } while (i < this.map.maxFieldsInLine - this.map.lineLength[0][line]);
            }

            returnStr = returnStr + (get(n) < 0 ? "" : " ") + get(n) + "  ";
            if (this.map.lastInLine(n)) {
                line++;
            }
        }

        return returnStr;
    }

    public void scanBoardStdOut() {
        System.out.println("\n\n" + toString());
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/bitboard/BokuBoard.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
