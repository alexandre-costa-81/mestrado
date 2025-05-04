package treesearch;

import boku.LogWriter;
import boku.StopWatch;
import tpzsgames.EngineType;
import tpzsgames.GameType;
import tpzsgames.MoveType;

public abstract class BokuEngine extends EngineType {

    protected int maxPly;
    public static final int ALLOWED_MAX_PLY = 6;
    protected boolean randomChoice;
    public StopWatch stopWatch;

    public BokuEngine(GameType game) {
        super(game);
        this.randomChoice = true;
        this.stopWatch = new StopWatch();
    }

    public int[] calculateMove(int colour, MoveType[] moves) {
        this.stopWatch.start();

        if ((moves == null) || (moves.length == 0)) {
            return null;
        }
        if (LogWriter.isPrintModus(4)) {
            LogWriter.out.println(colour == 1
                    ? "Black's line of thought" : "White's line of thought");
        }
        int[] sortedIndex = new int[moves.length];
        int[] eval = new int[moves.length];
        int counter = 0;

        while (moves[counter] != null) {
            this.game.make(moves[counter]);

            eval[counter] = getEvaluation(colour);
            this.game.undoMove();

            int i = counter;
            while ((i > 0)
                    && (eval[sortedIndex[(i - 1)]] * colour < eval[counter] * colour)) {
                sortedIndex[i] = sortedIndex[(i - 1)];
                i--;
            }

            if (this.randomChoice) {
                int k = i;
                int n = 1;

                while ((k > 0) && (eval[sortedIndex[(k - 1)]] == eval[counter])) {
                    n++;
                    k--;
                }

                n = (int) (Math.random() * n);

                while (n > 0) {
                    sortedIndex[i] = sortedIndex[(i - 1)];
                    i--;
                    n--;
                }
            }

            sortedIndex[i] = counter;
            if ((LogWriter.isPrintModus(8)) || ((i == 0) && (LogWriter.isPrintModus(4)))) {
                LogWriter.out.println(
                        this.stopWatch.intermediateTimeToString() + "\t"
                        + evalToString(eval[sortedIndex[i]]) + " "
                        + moves[sortedIndex[i]].toString() + bestLineToString() + (i == 0 ? " *" : ""));
            }
            counter++;
        }
        if (LogWriter.isPrintModus(2)) {
            LogWriter.out.println(
                    this.stopWatch.intermediateTimeToString() + "\t"
                    + evalToString(eval[sortedIndex[0]]) + " "
                    + moves[sortedIndex[0]].toString() + " <<");
        }

        return sortedIndex;
    }

    public abstract String getID();

    public abstract String bestLineToString();

    public int getMaxPly() {
        return this.maxPly;
    }

    public void setMaxPly(int maxPly) {
        if (maxPly <= 6) {
            this.maxPly = maxPly;
        }
    }

    public void setRandomChoice(boolean choice) {
        this.randomChoice = choice;
    }

    public boolean randomChoiceAllowed() {
        return this.randomChoice;
    }

    public int getAllowedMaxPly() {
        return -1;
    }

    protected String evalToString(int eval) {
        return (eval == 0 ? "+-" : eval > 0 ? "+" : "") + eval;
    }

    public String toString() {
        return getID() + " (" + this.maxPly + " ply, random " + (this.randomChoice ? "on" : "off") + ")";
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/treesearch/BokuEngine.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
