package tpzsgames;

public abstract interface GameType {

    public abstract void make(MoveType paramMoveType);

    public abstract void undoMove();

    public abstract void newGame();

    public abstract int eval();

    public abstract boolean won(PlayerType paramPlayerType);

    public abstract MoveType[] generateMoveList();

    public abstract BoardType getBoard();
}


/* Location:              /home/alexandre/Boku1.21.jar!/tpzsgames/GameType.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
