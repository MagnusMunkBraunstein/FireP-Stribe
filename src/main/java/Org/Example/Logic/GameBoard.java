package Org.Example.Logic;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    // Constants
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private int[][] board;

    // Constructor
    public GameBoard() {
        board = new int[ROWS][COLS];
    }

    // Denne metode forsøger at placere en brik for en spiller i en given kolonne.
    // Den starter fra bunden af kolonnen og går opad for at finde den første ledige plads.
    public boolean makeMove(int col, int player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            // Hvis den aktuelle plads er tom, placeres spillerens brik der
            if (board[row][col] == 0) {
                board[row][col] = player;
                return true;
            }
        }
        // Hvis kolonnen er fuld, returner false
        return false;
    }

    // Denne metode fjerner den øverste brik i en given kolonne.
    // Den starter fra toppen af kolonnen og går nedad for at finde den første brik.
    // Når den finder en brik, fjernes den og metoden afsluttes.
    public void undoMove(int col) {
        for (int row = 0; row < ROWS; row++) {
            if (board[row][col] != 0) {
                board[row][col] = 0;
                break;
            }
        }
    }

    // Denne metode tjekker om en given spiller har vundet spillet
    public boolean isWinningMove(int player) {
        // Tjek horisontalt
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row][col + 1] == player &&
                        board[row][col + 2] == player &&
                        board[row][col + 3] == player) {
                    return true;
                }
            }
        }

        // Tjek vertikalt
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col] == player &&
                        board[row + 2][col] == player &&
                        board[row + 3][col] == player) {
                    return true;
                }
            }
        }

        // Tjek diagonal
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col + 1] == player &&
                        board[row + 2][col + 2] == player &&
                        board[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }

        // Tjek diagonal
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row - 1][col + 1] == player &&
                        board[row - 2][col + 2] == player &&
                        board[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    // Denne metode tjekker om brættet er fyldt.
    // Den gennemgår den øverste række og kontrollerer, om der er nogen tomme pladser.
    public boolean isFull() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == 0) return false;
        }
        return true;
    }

    // Denne metode returnerer det nuværende spilleskema
    public int[][] getBoard() {
        return board;
    }

    // Denne metode returnerer en liste over tilgængelige kolonner, hvor der kan placeres en brik.
    // Den gennemgår den øverste række og tilføjer kolonnen til listen, hvis den er tom.
    public List<Integer> getAvailableMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == 0) {
                moves.add(col);
            }
        }
        return moves;
    }

    // Denne metode nulstiller spillebrættet
    public void resetBoard() {
        board = new int[ROWS][COLS];
    }
}
