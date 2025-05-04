package treesearch;

import boku.Move;
import tpzsgames.GameType;

public abstract class TreeSearchEngine
        extends BokuEngine {

    protected static final int ROOT_LEVEL_BONUS = 100;
    private Move[][] bestLine;

    protected TreeSearchEngine(GameType game) {
        super(game);
        this.bestLine = new Move[7][7];
    }

    protected void saveBestLine(int ply, Move move) {
        this.bestLine[ply][ply] = ((Move) move.clone());
        for (int k = 1; k < ply; k++) {
            Move moveToSave = this.bestLine[(ply - 1)][k];
            if (moveToSave != null) {
                this.bestLine[ply][k] = ((Move) moveToSave.clone());
            } else {
                this.bestLine[ply][k] = null;
            }
        }
    }

    protected void clearBestLine(int ply) {
        for (int k = 1; k < ply; k++) {
            this.bestLine[(ply - 1)][k] = null;
        }
    }

    public String bestLineToString() {
        String bestLineStr = "";
        for (int k = this.maxPly; (k > 0) && (this.bestLine[this.maxPly][k] != null); k--) {
            bestLineStr = bestLineStr + " - " + this.bestLine[this.maxPly][k].toString();
        }
        return bestLineStr;
    }

    protected int[] indexSort(int colour, Move[] moves) {
        int[] sortedEval = new int[moves.length];
        int[] sortedIndex = new int[moves.length];
        int counter = 0;
        while (moves[counter] != null) {
            this.game.make(moves[counter]);
            int eval = this.game.eval();
            this.game.undoMove();

            int i = counter;
            while ((i > 0) && (sortedEval[(i - 1)] * colour < eval * colour)) {
                sortedIndex[i] = sortedIndex[(i - 1)];
                sortedEval[i] = sortedEval[(i - 1)];
                i--;
            }

            sortedIndex[i] = counter;
            sortedEval[i] = eval;
            counter++;
        }
        return sortedIndex;
    }

    protected Move[] sort(int colour, Move[] moves) {
        int[] sortedEval = new int[moves.length];
        Move[] sortedMoves = new Move[moves.length];
        int counter = 0;
        while (moves[counter] != null) {
            this.game.make(moves[counter]);
            int eval = this.game.eval();
            this.game.undoMove();

            int i = counter;
            while ((i > 0) && (sortedEval[(i - 1)] * colour < eval * colour)) {
                sortedMoves[i] = sortedMoves[(i - 1)];
                sortedEval[i] = sortedEval[(i - 1)];
                i--;
            }

            sortedMoves[i] = moves[counter];
            sortedEval[i] = eval;
            counter++;
        }
        return sortedMoves;
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/treesearch/TreeSearchEngine.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
