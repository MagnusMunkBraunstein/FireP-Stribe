package Org.Example.Game;

public class AIPlayer {
    private final int AI_PLAYER = 2;
    private final int HUMAN_PLAYER = 1;


    //denne metode returnerer det bedste træk for AI'en ved hjælp af Alpha-Beta pruning
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

    // Alpha-Beta pruning implementation
    private int alphaBeta(GameBoard board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        // Tjek først om der er en vinder, så vi ikke evaluerer unødigt
        if (board.isWinningMove(AI_PLAYER)) {
            return 100000; // AI vinder - høj score
        }
        if (board.isWinningMove(HUMAN_PLAYER)) {
            return -100000; // Menneske vinder - lav score
        }

        // Tjek derefter om vi har nået dybdegrænsen eller et fuldt bræt
        if (depth == 0 || board.isFull()) {
            return evaluateBoard(board);
        }

        //ovenstående if-statement tjekker om vi er nået til bunden af træet, eller om der er en vinder
        // den skal deles op for hvis der er en vinder så skal den ikke evaluerer.

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

    // denne metode evaluerer brættet og giver en score baseret på AI'ens position
    private int evaluateBoard(GameBoard board) {
        int[][] grid = board.getBoard();
        int score = 0;

        // Bonus for midterkolonne (kolonne 3)
        int centerColumn = GameBoard.COLS / 2;
        int centerCount = 0;
        for (int row = 0; row < GameBoard.ROWS; row++) {
            if (grid[row][centerColumn] == AI_PLAYER) {
                // Brikker tættere på bunden er mere værd (mere stabile)
                centerCount += (GameBoard.ROWS - row);
            }
        }
        score += centerCount * 3;

        // Horisontale vinduer
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                int[] window = {
                        grid[row][col],
                        grid[row][col + 1],
                        grid[row][col + 2],
                        grid[row][col + 3]
                };
                score += evaluateWindow(window, AI_PLAYER);
            }
        }

        // Vertikale vinduer
        for (int row = 0; row < GameBoard.ROWS - 3; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                int[] window = {
                        grid[row][col],
                        grid[row + 1][col],
                        grid[row + 2][col],
                        grid[row + 3][col]
                };
                score += evaluateWindow(window, AI_PLAYER);
            }
        }

        // Diagonal ↘
        for (int row = 0; row < GameBoard.ROWS - 3; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                int[] window = {
                        grid[row][col],
                        grid[row + 1][col + 1],
                        grid[row + 2][col + 2],
                        grid[row + 3][col + 3]
                };
                score += evaluateWindow(window, AI_PLAYER);
            }
        }

        // Diagonal
        for (int row = 3; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                int[] window = {
                        grid[row][col],
                        grid[row - 1][col + 1],
                        grid[row - 2][col + 2],
                        grid[row - 3][col + 3]
                };
                score += evaluateWindow(window, AI_PLAYER);
            }
        }

        return score;
    }

    // denne metode evaluerer et vindue (4 celler) og giver en score baseret på spillerens position
    private int evaluateWindow(int[] window, int player) {
        int score = 0;
        int opponent = (player == 1) ? 2 : 1;
        int playerCount = 0;
        int opponentCount = 0;
        int emptyCount = 0;

        for (int cell : window) {
            if (cell == player) playerCount++;
            else if (cell == opponent) opponentCount++;
            else emptyCount++;
        }

        if (playerCount == 4) score += 1000;
        else if (playerCount == 3 && emptyCount == 1) score += 100;
        else if (playerCount == 2 && emptyCount == 2) {
            // Tjek om brikkerne er sammenhængende (stærkere position)
            if ((window[0] == player && window[1] == player) ||
                    (window[1] == player && window[2] == player) ||
                    (window[2] == player && window[3] == player)) {
                score += 15; // Sammenhængende brikker er stærkere
            } else {
                score += 10;  // Normal to brikker
            }
        }

        if (opponentCount == 3 && emptyCount == 1) score -= 80;
        else if (opponentCount == 2 && emptyCount == 2) score -= 5;

        return score;
    }
}