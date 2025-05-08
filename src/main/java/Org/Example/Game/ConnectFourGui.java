package Org.Example.Game;

import Org.Example.Logic.GameBoard;
import Org.Example.Logic.AIPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class ConnectFourGui {

    private JFrame frame;
    private JPanel boardPanel;
    private JLabel statusLabel;
    private GameBoard board;
    private final AIPlayer ai;
    private int currentPlayer;
    private int aiDepth;
    private Stack<Integer> moveHistory;

    public ConnectFourGui() {
        board = new GameBoard();
        ai = new AIPlayer();
        moveHistory = new Stack<>();
        chooseAIDepth();
        chooseStartingPlayer();
        initializeUI();
    }

    private void chooseAIDepth() {
        String depth = JOptionPane.showInputDialog(null, "Search dept:", "Difficulty", JOptionPane.QUESTION_MESSAGE);
        try {
            aiDepth = Integer.parseInt(depth);
        } catch (NumberFormatException e) {
            aiDepth = 5; // Default value
        }
    }

    private void chooseStartingPlayer() {
        Object[] options = {"Player", "AI"};
        int choice = JOptionPane.showOptionDialog(null, "Who starts?", "Choose Starter",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        currentPlayer = (choice == 0) ? 1 : 2;
    }

    private void initializeUI() {
        frame = new JFrame("Connect Four");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());

        statusLabel = new JLabel("Player 1's Turn", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(GameBoard.ROWS, GameBoard.COLS));
        frame.add(boardPanel, BorderLayout.CENTER);

        //Blå Baggrund
        boardPanel.setBackground(Color.blue);


        JPanel controlPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        JButton undoButton = new JButton("Undo");

        restartButton.addActionListener(e -> restartGame());
        undoButton.addActionListener(e -> undoMove());

        controlPanel.add(restartButton);
        controlPanel.add(undoButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, GameBoard.COLS));
        for (int col = 0; col < GameBoard.COLS; col++) {
            final int column = col;
            JButton btn = new JButton("Drop");
            btn.addActionListener(e -> playerMove(column));
            buttonPanel.add(btn);
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        updateBoard();
        frame.setVisible(true);

        if (currentPlayer == 2) {
            aiMove();
        }
    }

    // Metode til at håndtere spillerens træk - virker men er uden pop op bokse..
    /*
    private void playerMove(int col) {
        if (board.makeMove(col, currentPlayer)) {
            moveHistory.push(col);
            updateBoard();
            if (board.isWinningMove(currentPlayer)) {
                statusLabel.setText("Player " + currentPlayer + " Wins!");
                disableButtons();
                return;
            }
            currentPlayer = 2;
            aiMove();
        }
    }

    private void aiMove() {
        statusLabel.setText("AI is thinking...");
        Timer timer = new Timer(0, e -> {
            int aiMove = ai.getBestMove(board, aiDepth);
            board.makeMove(aiMove, 2);
            moveHistory.push(aiMove);
            updateBoard();
            if (board.isWinningMove(2)) {
                statusLabel.setText("AI Wins!");
                disableButtons();
                return;
            }
            currentPlayer = 1;
            statusLabel.setText("Player 1's Turn");
        });
        timer.setRepeats(false);
        timer.start();
    }
     */

    private void playerMove(int col) {
        if (board.makeMove(col, currentPlayer)) {
            moveHistory.push(col);
            updateBoard();
            if (board.isWinningMove(currentPlayer)) {
                String message = "Player " + currentPlayer + " Wins!";
                statusLabel.setText(message);
                JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                disableButtons();
                return;
            }
            currentPlayer = 2;
            aiMove();
        }
    }

    private void aiMove() {
        statusLabel.setText("AI is thinking...");
        Timer timer = new Timer(0, e -> {
            int aiMove = ai.getBestMove(board, aiDepth);
            board.makeMove(aiMove, 2);
            moveHistory.push(aiMove);
            updateBoard();
            if (board.isWinningMove(2)) {
                String message = "AI Wins!";
                statusLabel.setText(message);
                JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                disableButtons();
                return;
            }
            currentPlayer = 1;
            statusLabel.setText("Player 1's Turn");
        });
        timer.setRepeats(false);
        timer.start();
    }


    private void updateBoard() {
        boardPanel.removeAll();
        int[][] grid = board.getBoard();
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                JLabel cell = new JLabel("●", SwingConstants.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 60));
                if (grid[row][col] == 1) {
                    cell.setForeground(Color.RED);
                } else if (grid[row][col] == 2) {
                    cell.setForeground(Color.YELLOW);
                } else {
                    cell.setForeground(Color.LIGHT_GRAY);
                }
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void undoMove() {
        if (!moveHistory.isEmpty()) {
            int lastMove = moveHistory.pop();
            board.undoMove(lastMove);
            updateBoard();
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            statusLabel.setText("Player " + currentPlayer + "'s Turn");
        }
    }

    private void restartGame() {
        board = new GameBoard();
        moveHistory.clear();
        chooseStartingPlayer();
        updateBoard();
        statusLabel.setText("Player 1's Turn");
        if (currentPlayer == 2) {
            aiMove();
        }
    }

    private void disableButtons() {
        for (Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component button : ((JPanel) comp).getComponents()) {
                    if (button instanceof JButton) {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConnectFourGui::new);
    }
}
