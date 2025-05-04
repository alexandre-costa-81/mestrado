package boku;

import tpzsgames.MoveType;

public class Move
        implements MoveType, Cloneable {

    private Field moveField;
    private Field captureField;
    private Marble marble;

    public Move(Field field, Marble marble) {
        this.moveField = field;
        this.captureField = null;
        this.marble = marble;
    }

    public Move(Field moveField, Field captureField, Marble marble) {
        this.moveField = moveField;
        this.captureField = captureField;
        this.marble = marble;
    }

    public Object clone() {
        return new Move(this.moveField, this.captureField, this.marble);
    }

    public Field getField() {
        return this.moveField;
    }

    public Field getCaptureField() {
        return this.captureField;
    }

    public Marble getMarble() {
        return this.marble;
    }

    public String toString() {
        String moveStr = this.moveField.toString();
        if (this.captureField != null) {
            moveStr = moveStr + "x" + this.captureField.toString();
        }
        return moveStr;
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/boku/Move.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
