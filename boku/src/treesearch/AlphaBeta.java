package treesearch;

import boku.Boku;
import boku.LogWriter;
import boku.Move;
import tpzsgames.GameType;
import tpzsgames.MoveType;

public class AlphaBeta
        extends TreeSearchEngine {

    public static final String ID = "AlphaBeta";
    public static final int ALLOWED_MAX_PLY = 6;
    private static final int DEFAULT_MAX_PLY = 3;
    protected int selectivePly;
    protected int maxSelectiveMoves;
    public int keepIndex;

    public AlphaBeta(GameType game) {
        super(game);
        this.maxPly = 3;
        this.selectivePly = 0;
        this.maxSelectiveMoves = 10;
        this.randomChoice = true;
    }

    public String getID() {
        return "AlphaBeta";
    }

    public int getAllowedMaxPly() {
        return 6;
    }

    public int[] calculateMove(int colour, MoveType[] moves) {
        this.stopWatch.start();
        if (moves == null) {
            return new int[]{-1};
        }
        if (LogWriter.isPrintModus(4)) {
            LogWriter.out.println(colour == 1
                    ? "Black's line of thought" : "White's line of thought");
        }

        int[] sortedIndex = new int[moves.length];

        int[] eval = new int[moves.length];

        int[] idx = indexSort(colour, (Move[]) moves);

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        boolean winnerFound = false;
        int counter = 0;

        while ((moves[counter] != null) && (!winnerFound)) {
            int next = idx[counter];
            this.game.make(moves[next]);
            if (((Boku) this.game).won(colour)) {
                eval[next] = (4194404 * colour);
                winnerFound = true;
            } else {
                eval[next] = alphaBeta(-colour, this.maxPly, alpha, beta);
            }
            this.game.undoMove();

            if ((colour == 1) && (eval[next] > alpha)) {
                if (this.randomChoice) {
                    alpha = eval[next] - 1;
                } else {
                    alpha = eval[next];
                }
            } else if ((colour == -1) && (eval[next] < beta)) {
                if (this.randomChoice) {
                    beta = eval[next] + 1;
                } else {
                    beta = eval[next];
                }
            }

            int i = counter;
            while ((i > 0) && (eval[sortedIndex[(i - 1)]] * colour < eval[next] * colour)) {
                sortedIndex[i] = sortedIndex[(i - 1)];
                i--;
            }

            if (this.randomChoice) {
                int k = i;
                int n = 1;

                while ((k > 0) && (eval[sortedIndex[(k - 1)]] == eval[next])) {
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

            sortedIndex[i] = next;
            if (i == 0) {
                this.keepIndex = counter;
            }
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
                    + "(" + this.keepIndex + ")\t"
                    + evalToString(eval[sortedIndex[0]]) + " "
                    + moves[sortedIndex[0]].toString() + " <<");
        }
        return sortedIndex;
    }

    public int getEvaluation(int colour) {
        int eval;

        if (((Boku) this.game).won(colour)) {
            eval = 4194404 * colour;
        } else {
            eval = alphaBeta(-colour, this.maxPly, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        return eval;
    }

    private int alphaBeta(int colour, int ply, int alpha, int beta) {
        if (!Boku.threadRunning) {
            return 0;
        }
        Move[] movesAtThisPly;

        if (ply > 1) {
            movesAtThisPly
                    = sort(colour, (Move[]) ((Boku) this.game).generateMoveList(colour));
        } else {
            movesAtThisPly = (Move[]) ((Boku) this.game).generateMoveList(colour);
        }
        if (movesAtThisPly[0] == null) {
            return 0;
        }
        boolean winnerFound = false;
        for (int i = 0; ((ply > 0)
                || (ply > this.selectivePly)
                || (i < this.maxSelectiveMoves))
                && (movesAtThisPly[i] != null)
                && (!winnerFound) && (Boku.threadRunning); i++) {
            this.game.make(movesAtThisPly[i]);
            int eval;
            if (((Boku) this.game).won(colour)) {
                eval = (4194304 + ply) * colour;
                clearBestLine(ply);
                winnerFound = true;
            } else {
                if (ply <= 1) {
                    eval = ((Boku) this.game).getLastEval();
                } else {
                    eval = alphaBeta(-colour, ply - 1, alpha, beta);
                }
            }
            this.game.undoMove();

            if ((colour == 1) && (eval > alpha)) {
                alpha = eval;
                saveBestLine(ply, movesAtThisPly[i]);

            } else if ((colour == -1) && (eval < beta)) {
                beta = eval;
                saveBestLine(ply, movesAtThisPly[i]);
            }

            if (alpha >= beta) {
                return eval;
            }
        }
        if (colour == 1) {
            return alpha;
        }
        return beta;
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/treesearch/AlphaBeta.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
