package treesearch;

import boku.Boku;
import boku.Move;
import tpzsgames.GameType;

public class MiniMax extends TreeSearchEngine {

    public static final String ID = "MiniMax";
    public static final int ALLOWED_MAX_PLY = 3;
    private static final int DEFAULT_MAX_PLY = 1;

    public MiniMax(GameType game) {
        super(game);
        this.maxPly = 1;
    }

    public String getID() {
        return "MiniMax";
    }

    public int getAllowedMaxPly() {
        return 3;
    }

    public int getEvaluation(int colour) {
        int eval;

        if (((Boku) this.game).won(colour)) {
            eval = 4194404 * colour;
        } else {
            eval = miniMax(-colour, this.maxPly);
        }
        return eval;
    }

    private int miniMax(int colour, int ply) {
        if (!Boku.threadRunning) {
            return 0;
        }
        if (ply == 0) {

            return this.game.eval();
        }
        Move[] movesAtThisPly = (Move[]) ((Boku) this.game).generateMoveList(colour);
        if (movesAtThisPly[0] == null) {
            return 0;
        }
        boolean winnerFound = false;
        int bestEval = colour == 1
                ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; (movesAtThisPly[i] != null)
                && (!winnerFound) && (Boku.threadRunning); i++) {
            this.game.make(movesAtThisPly[i]);
            int eval;
            if (((Boku) this.game).won(colour)) {
                eval = (4194304 + ply) * colour;
                clearBestLine(ply);
                winnerFound = true;
            } else {
                eval = miniMax(-colour, ply - 1);
            }
            if (eval * colour > bestEval * colour) {
                bestEval = eval;
                saveBestLine(ply, movesAtThisPly[i]);
            }
            this.game.undoMove();
        }
        return bestEval;
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/treesearch/MiniMax.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
