package treesearch;

import tpzsgames.GameType;

public class StaticEval extends BokuEngine {

    public static final String ID = "Static Evaluation";

    public StaticEval(GameType game) {
        super(game);
    }

    public String getID() {
        return "Static Evaluation";
    }

    public int getAllowedMaxPly() {
        return 0;
    }

    public int getEvaluation(int colour) {
        return this.game.eval();
    }

    public String bestLineToString() {
        return new String();
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/treesearch/StaticEval.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
