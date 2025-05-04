package bitboard;

import boku.Field;
import java.io.PrintStream;

public final class Map {

    private static Map instance = null;

    public MapField[] field;

    public MapField[][] getField;

    int[][] lineLength;

    public int h;

    public int d;

    public static final int MIN_H = 1;

    public static final int MIN_D = 1;

    public static final int MAX_DIRECTIONS = 3;

    public final int A = 0;
    public final int B = 1;
    public final int C = 2;

    public int numberOfLines;

    public int maxFieldsInLine;

    public int numberOfFields;

    private Map(int h, int d) {
        if ((h < 1) || (d < 1)) {
            throw new IllegalArgumentException();
        }
        this.h = h;
        this.d = d;
        this.numberOfLines = (d * 2 - 1);
        this.maxFieldsInLine = (h + d - 1);
        this.numberOfFields = (this.maxFieldsInLine * this.maxFieldsInLine - h * (h - 1));
        this.field = new MapField[this.numberOfFields];
        this.getField = new MapField[this.maxFieldsInLine][this.maxFieldsInLine];
        this.lineLength = new int[3][];
        this.lineLength[0] = new int[this.numberOfLines];
        this.lineLength[1] = new int[this.maxFieldsInLine];
        this.lineLength[2] = new int[this.maxFieldsInLine];

        for (int y = 0; y < this.maxFieldsInLine; y++) {
            for (int x = 0; x < this.maxFieldsInLine; x++) {
                this.getField[x][y] = null;
            }
        }
        int n = 0;

        for (int i = 0; i < d - 1; i++) {
            this.lineLength[0][i] = (this.maxFieldsInLine + i + 1 - d);
            for (int k = d - i - 1; k < this.maxFieldsInLine; k++) {
                int x = k;
                int y = i + x - d + 1;
                this.field[n] = new MapField(x, y, n);
                this.getField[x][y] = this.field[n];
                n++;
            }
        }

        for (int i = 0; i < d; i++) {
            this.lineLength[0][(i + d - 1)] = (this.maxFieldsInLine + i + 1 - d);
            for (int k = 0; k < this.maxFieldsInLine - i; k++) {
                int x = k;
                int y = k + i;
                this.field[n] = new MapField(x, y, n);
                this.getField[x][y] = this.field[n];
                n++;
            }
        }

        int maxExtend = this.maxFieldsInLine > this.numberOfLines
                ? this.maxFieldsInLine : this.numberOfLines;
        for (int y = 0; y < maxExtend; y++) {
            int fieldsInLineA = this.maxFieldsInLine;
            int fieldsInLineB = this.maxFieldsInLine;
            int fieldsInLineC = this.maxFieldsInLine;
            for (int x = 0; x < this.maxFieldsInLine; x++) {
                int fxA = y < d ? x + d - y - 1 : x;
                int fyA = y < d ? x : x + y + 1 - d;
                int fxB = y < h ? d + y - x - 1 : h + d - x - 2;
                int fyB = y;
                int fxC = h + d - y - 2;
                int fyC = y < h ? h + x - y - 1 : x;
                if (!isValid(fxA, fyA)) {
                    fieldsInLineA--;
                } else {
                    this.getField[fxA][fyA].set_xA(x);
                    this.getField[fxA][fyA].set_yA(y);
                }
                if (!isValid(fxB, fyB)) {
                    fieldsInLineB--;
                } else {
                    this.getField[fxB][fyB].set_xB(x);
                    this.getField[fxB][fyB].set_yB(y);
                }
                if (!isValid(fxC, fyC)) {
                    fieldsInLineC--;
                } else {
                    this.getField[fxC][fyC].set_xC(x);
                    this.getField[fxC][fyC].set_yC(y);
                }
            }
            if (y < this.numberOfLines) {
                this.lineLength[0][y] = fieldsInLineA;
            }
            if (y < this.maxFieldsInLine) {
                this.lineLength[1][y] = fieldsInLineB;
                this.lineLength[2][y] = fieldsInLineC;
            }
        }
    }

    public static synchronized Map getInstance(int h, int d) {
        if (instance == null) {
            instance = new Map(h, d);
        }
        return instance;
    }

    public Field getField(int n) {
        return new Field(this.field[n].getX(), this.field[n].getY());
    }

    public int getNumber(Field field) {
        return this.getField[field.getX()][field.getY()].getN();
    }

    public Field findFieldInA(int xA, int yA) {
        for (int fieldNumber = 0; fieldNumber < this.numberOfFields; fieldNumber++) {
            if ((this.field[fieldNumber].get_xA() == xA)
                    && (this.field[fieldNumber].get_yA() == yA)) {
                return this.field[fieldNumber];
            }
        }
        return null;
    }

    public Field findFieldInB(int xB, int yB) {
        for (int fieldNumber = 0; fieldNumber < this.numberOfFields; fieldNumber++) {
            if ((this.field[fieldNumber].get_xB() == xB)
                    && (this.field[fieldNumber].get_yB() == yB)) {
                return this.field[fieldNumber];
            }
        }
        return null;
    }

    public Field findFieldInC(int xC, int yC) {
        for (int fieldNumber = 0; fieldNumber < this.numberOfFields; fieldNumber++) {
            if ((this.field[fieldNumber].get_xC() == xC)
                    && (this.field[fieldNumber].get_yC() == yC)) {
                return this.field[fieldNumber];
            }
        }
        return null;
    }

    public int minX(int y) {
        return y <= this.d - 1 ? 0 : y - (this.d - 1);
    }

    public int maxX(int y) {
        return y >= this.maxFieldsInLine - this.d ? this.maxFieldsInLine : y + this.d;
    }

    public int minY(int x) {
        return x <= this.d - 1 ? 0 : x - (this.d - 1);
    }

    public int maxY(int x) {
        return x >= this.maxFieldsInLine - this.d ? this.maxFieldsInLine : x + this.d;
    }

    public boolean firstInLine(int n) {
        return (isValid(n)) && ((this.field[n].getX() == 0) || (this.field[n].getY() == 0));
    }

    public boolean lastInLine(int n) {
        return (isValid(n)) && ((this.field[n].getX() == this.maxFieldsInLine - 1) || (this.field[n].getY() == this.maxFieldsInLine - 1));
    }

    public boolean isMiddleLine(int n) {
        return (isValid(n)) && (this.field[n].getX() == this.field[n].getY());
    }

    public boolean isTopHalf(int n) {
        return (isValid(n)) && (this.field[n].getX() > this.field[n].getY());
    }

    public boolean isBottomHalf(int n) {
        return (isValid(n)) && (this.field[n].getX() < this.field[n].getY());
    }

    public boolean isValid(int x, int y) {
        return (x >= minX(y)) && (x < maxX(y)) && (y >= minY(x)) && (y < maxY(x));
    }

    public final boolean isValid(int n) {
        return (n >= 0) && (n < this.numberOfFields);
    }

    public void printTest() {
        System.out.println("\n\nProperties of the board:\nHorizontal dimension: "
                + this.h + "\n"
                + "Diagonal dimension:   " + this.d + "\n"
                + "Longest line:         " + this.maxFieldsInLine + "\n"
                + "Number of lines:      " + this.numberOfLines + "\n"
                + "Number of fields:     " + this.numberOfFields);

        System.out.println("\nList of fields with co-ordinates:\n");

        for (int n = 0; n < this.numberOfFields; n++) {
            System.out.println(
                    (n < 10 ? " " : "") + (n < 100 ? " " : "")
                    + n + " " + this.field[n].toString() + " \tA: ("
                    + this.field[n].get_xA() + "," + this.field[n].get_yA() + ")\tB: ("
                    + this.field[n].get_xB() + "," + this.field[n].get_yB() + ")\tC: ("
                    + this.field[n].get_xC() + "," + this.field[n].get_yC() + ")");
        }

        System.out.println("\nInternal board representation:\n");

        for (int y = 0; y < this.maxFieldsInLine; y++) {
            for (int x = 0; x < this.maxFieldsInLine; x++) {
                int n = -1;
                if (this.getField[x][y] != null) {
                    n = this.getField[x][y].getN();
                }
                System.out.print(
                        ((n < 10) && (n >= 0) ? "  " : " ") + (n < 100 ? " " : "") + n);
            }
            System.out.println();
        }

        System.out.println("\nPlayer's view of the board:");

        System.out.print("    ");
        int line = 0;
        for (int n = 0; n < this.numberOfFields; n++) {
            if (firstInLine(n)) {
                System.out.println();
                System.out.print((this.field[n].get_yA() < 10 ? " " : "")
                        + this.field[n].get_yA() + "  ");
                for (int i = 0; i < this.maxFieldsInLine - this.lineLength[0][line]; i++) {
                    System.out.print("  ");
                }
            }
            System.out.print(" " + (n < 10 ? " " : "") + n + (n < 100 ? " " : ""));
            if (lastInLine(n)) {
                for (int i = 0; i < this.maxFieldsInLine - this.lineLength[0][line]; i++) {
                    System.out.print("  ");
                }
                System.out.print((this.lineLength[0][line] < 10 ? " " : "")
                        + "  (" + this.lineLength[0][line] + ")");
                line++;
            }
        }
        System.out.println();

        System.out.println("\nInternal representation with co-ordinates:\n");

        System.out.print("    ");
        for (int x = 0; x < this.maxFieldsInLine; x++) {
            System.out.print((x < 10 ? " " : "") + x + "  ");
        }
        System.out.println();

        for (int y = 0; y < this.maxFieldsInLine; y++) {
            System.out.print((y < 10 ? " " : "") + y + "  ");
            for (int x = 0; x < this.maxFieldsInLine; x++) {
                if (x < minX(y)) {
                    System.out.print(" >> ");

                } else if (x >= maxX(y)) {
                    System.out.print(" << ");
                } else {
                    int n = this.getField[x][y].getN();
                    System.out.print(
                            (n < 10 ? " " : "") + (n < 100 ? " " : "")
                            + n + " ");
                }
            }
            System.out.println();
        }
    }

    public void printViews() {
        System.out.println("\nView A");
        for (int y = 0; y < this.numberOfLines; y++) {
            int l = this.lineLength[0][y];
            System.out.print((l < 10 ? " " : "") + l + " ");
            for (int x = 0; x < this.maxFieldsInLine; x++) {
                int fx = y < this.d ? x + this.d - y - 1 : x;
                int fy = y < this.d ? x : x + y + 1 - this.d;
                System.out.print(
                        isValid(fx, fy)
                        ? " (" + fx + "," + fy + ")"
                        : "  --- ");
            }
            System.out.println();
        }
        System.out.println("\nView B");
        for (int y = 0; y < this.maxFieldsInLine; y++) {
            int l = this.lineLength[1][y];
            System.out.print((l < 10 ? " " : "") + l + " ");
            for (int x = 0; x < this.maxFieldsInLine; x++) {
                int fx = y < this.h ? this.d + y - x - 1 : this.h + this.d - x - 2;
                int fy = y;
                System.out.print(
                        isValid(fx, fy)
                        ? " (" + fx + "," + fy + ")"
                        : "  --- ");
            }
            System.out.println();
        }
        System.out.println("\nView C");
        for (int y = 0; y < this.maxFieldsInLine; y++) {
            int l = this.lineLength[2][y];
            System.out.print((l < 10 ? " " : "") + l + " ");
            for (int x = 0; x < this.maxFieldsInLine; x++) {
                int fx = this.h + this.d - y - 2;
                int fy = y < this.h ? this.h + x - y - 1 : x;
                System.out.print(
                        isValid(fx, fy)
                        ? " (" + fx + "," + fy + ")"
                        : "  --- ");
            }
            System.out.println();
        }
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/bitboard/Map.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
