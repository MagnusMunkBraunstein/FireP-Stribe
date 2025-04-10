package Org.Example;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    // Constants for the game board dimensions
    private final int ROWS = 6;
    private final int COLS = 7;
    private int[][] board;


    // Constructor to initialize the game board
    public GameBoard() {
        board = new int[ROWS][COLS];
    }

    //Denne metode forsøger at placere en brik for en spiller i en given kolonne.
    // Den starter fra bunden af kolonnen og går opad for at finde den første ledige plads.
    public boolean makeMove(int col, int player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            // Hvis den aktuelle plads er tom, placeres spilleren der
            if (board[row][col] == 0) {
                board[row][col] = player;
                return true;
            }
        }
        // Hvis kolonnen er fuld, returner false
        return false;
    }


    //Denne metode fjerner den øverste brik i en given kolonne.
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


    public boolean isWinningMove(int player) {
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


    // Denne metode returnerer det nuværende spilleskema som et 2D-array.
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
