package UI;

import Handlers.TicTacToe;
import Listeners.StateListener;
import Models.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI extends JFrame implements StateListener, ActionListener {

    private JPanel boardPanel, upPanel;
    private JButton[][] button;
    private JLabel tvResult;
    private JButton Reset;
    private JTextField InputSize;

    private int boardSize = 3;
    private boolean isReset = false;


    public GUI() {

        intiObject();
        displayGUI();

        add(upPanel, BorderLayout.NORTH);
    }

    private void displayGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setTitle("Tic-Tac-Toe");
        setVisible(true);
    }

    private void intiObject() {

        upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(1, 2));

        JLabel label = new JLabel("Enter Board Size:");
        upPanel.add(label);

        InputSize = new JFormattedTextField();
        upPanel.add(InputSize);

        Reset = new JButton("Start Game");
        Reset.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTheGame();
            }
        });
        Reset.setFocusPainted(false);
        upPanel.add(Reset);

    }

    private void addToPanel() {

        boardPanel.removeAll();

        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                boardPanel.add(button[i][j]);
    }


    private void resetTheGame() {
        if (isReset) {

            for (JButton[] jButton : button) {
                for (JButton button : jButton) {
                    button.setText("");
                    button.setEnabled(true);
                    button.setBackground(null);
                }
            }

            tvResult.setText("Game Is Running!!");
            tvResult.setForeground(Color.BLACK);
            Reset.setText("Play Game");
            isReset = false;
            boardPanel.setVisible(false);
        } else {
            Reset.setText("Reset The Game");

            boardSize = Integer.parseInt(InputSize.getText());

            isReset = true;

            boardPanel = new JPanel();
            boardPanel.setLayout(new GridLayout(boardSize, boardSize));

            if (tvResult == null) {
                tvResult = new JLabel("Game Is Running!!");
                add(tvResult, BorderLayout.SOUTH);
            }


            button = null;
            button = new JButton[boardSize][boardSize];
            for (int i = 0; i < boardSize; i++)
                for (int j = 0; j < boardSize; j++) {
                    button[i][j] = new JButton("");
                    button[i][j].addActionListener(this);
                    button[i][j].setFocusPainted(false);
                    button[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                }

            remove(boardPanel);
            add(boardPanel, BorderLayout.CENTER);
            addToPanel();
            boardPanel.setVisible(true);

            TicTacToe.Companion.run(boardSize - 1, this);
        }
    }

    @Override
    public void onStateChange(Move move, char player) {
        System.out.println("Player : " + player + ", Selected : " + move);

        if (player == TicTacToe.player)
            button[move.getRow$TicTacToe()]
                    [move.getCol$TicTacToe()].setForeground(Color.GREEN);
        else button[move.getRow$TicTacToe()]
                [move.getCol$TicTacToe()].setForeground(Color.RED);

        button[move.getRow$TicTacToe()]
                [move.getCol$TicTacToe()].setText(player + "");
    }

    @Override
    public void onDone(char winner) {

        if (winner == TicTacToe.opponent) {
            tvResult.setForeground(Color.RED);
            tvResult.setText("Computer Win!!");
        } else if (winner == TicTacToe.player) {
            tvResult.setForeground(Color.GREEN);
            tvResult.setText("You Win!!");
        } else {
            tvResult.setForeground(Color.BLUE);
            tvResult.setText("Draw!!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (button[i][j] == e.getSource()) {
                    Move m = new Move();
                    m.setRow$TicTacToe(i);
                    m.setCol$TicTacToe(j);
                    TicTacToe.Companion.move(m);
                    break;
                }
            }
        }
    }
}