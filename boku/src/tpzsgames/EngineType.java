package tpzsgames;

public abstract class EngineType {

    protected GameType game;

    public EngineType(GameType game) {
        this.game = game;
    }

    public abstract int[] calculateMove(int paramInt, MoveType[] paramArrayOfMoveType);

    public abstract int getEvaluation(int paramInt);
}


/* Location:              /home/alexandre/Boku1.21.jar!/tpzsgames/EngineType.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
