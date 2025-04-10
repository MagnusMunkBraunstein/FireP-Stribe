package Org.Example.Controller;
import Org.Example.Model.GameBoard;
import Org.Example.Model.AIPlayer;
import java.util.*;

public class GameController {
    private final GameBoard board;
    private final AIPlayer ai;
    private int currentPlayer;

    public GameController() {
        board = new GameBoard();
        ai = new AIPlayer();
        currentPlayer = 1;
        startGame();
    }

    private void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (!board.isFull()) {
            printBoard();

            if (currentPlayer == 1) {
                System.out.print("Vælg kolonne (0-6): ");
                int col = scanner.nextInt();
                if (board.makeMove(col, 1)) {
                    if (board.isWinningMove(1)) {
                        printBoard();
                        System.out.println("Spilleren vinder!");
                        return;
                    }
                    currentPlayer = 2;
                }
            } else {
                System.out.println("AI tænker...");
                int aiMove = ai.getBestMove(board, 5);
                board.makeMove(aiMove, 2);
                if (board.isWinningMove(2)) {
                    printBoard();
                    System.out.println("AI vinder!");
                    return;
                }
                currentPlayer = 1;
            }
        }

        printBoard();
        System.out.println("Spillet endte uafgjort.");
    }

    private void printBoard() {
        int[][] grid = board.getBoard();
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6");
    }
}
