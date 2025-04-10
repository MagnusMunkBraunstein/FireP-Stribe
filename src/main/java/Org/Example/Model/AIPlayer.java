package Org.Example.Model;

public class AIPlayer {
    private final int AI_PLAYER = 2;
    private final int HUMAN_PLAYER = 1;

    public int getBestMove(GameBoard board, int depth) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int move : board.getAvailableMoves()) {
            board.makeMove(move, AI_PLAYER);
            int score = alphaBeta(board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            board.undoMove(move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int alphaBeta(GameBoard board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.isFull()) return evaluateBoard(board);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int move : board.getAvailableMoves()) {
                board.makeMove(move, AI_PLAYER);
                int eval = alphaBeta(board, depth - 1, alpha, beta, false);
                board.undoMove(move);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int move : board.getAvailableMoves()) {
                board.makeMove(move, HUMAN_PLAYER);
                int eval = alphaBeta(board, depth - 1, alpha, beta, true);
                board.undoMove(move);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private int evaluateBoard(GameBoard board) {
        if (board.isWinningMove(AI_PLAYER)) return 1000;
        if (board.isWinningMove(HUMAN_PLAYER)) return -1000;

        int[][] weights = {
                {3, 4, 5, 7, 5, 4, 3},
                {4, 6, 8,10, 8, 6, 4},
                {5, 8,11,13,11, 8, 5},
                {5, 8,11,13,11, 8, 5},
                {4, 6, 8,10, 8, 6, 4},
                {3, 4, 5, 7, 5, 4, 3},
        };

        int score = 0;
        int[][] grid = board.getBoard();
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                if (grid[row][col] == AI_PLAYER) score += weights[row][col];
                else if (grid[row][col] == HUMAN_PLAYER) score -= weights[row][col];
            }
        }
        return score;
    }
}
