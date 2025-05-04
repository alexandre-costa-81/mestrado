package bitboard;

import boku.Field;

public class MapField extends Field {

    int xA;
    int yA;
    int xB;
    int yB;
    int xC;
    int yC;
    int n;

    MapField(int x, int y, int n) {
        super(x, y);
        this.n = n;
    }

    public int getN() {
        return this.n;
    }

    public void set_xA(int x) {
        this.xA = x;
    }

    public void set_yA(int y) {
        this.yA = y;
    }

    public int get_xA() {
        return this.xA;
    }

    public int get_yA() {
        return this.yA;
    }

    public void set_xB(int x) {
        this.xB = x;
    }

    public void set_yB(int y) {
        this.yB = y;
    }

    public int get_xB() {
        return this.xB;
    }

    public int get_yB() {
        return this.yB;
    }

    public void set_xC(int x) {
        this.xC = x;
    }

    public void set_yC(int y) {
        this.yC = y;
    }

    public int get_xC() {
        return this.xC;
    }

    public int get_yC() {
        return this.yC;
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/bitboard/MapField.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
