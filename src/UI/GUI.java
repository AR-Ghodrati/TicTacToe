package UI;

import Handlers.TicTacToe;
import Listeners.StateListener;
import Models.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI extends JFrame implements StateListener, ActionListener {

    private JPanel boardPanel;
    private JButton[][] button = new JButton[3][3];
    private JLabel tvResult;
    private JButton Reset;


    public GUI() {

        intiObject();
        addToPanel();
        displayGUI();

        add(boardPanel, BorderLayout.CENTER);
        add(tvResult, BorderLayout.SOUTH);
        add(Reset, BorderLayout.NORTH);

        TicTacToe.Companion.run(this);
    }

    private void displayGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setTitle("Tic-Tac-Toe");
        setVisible(true);
    }

    private void intiObject() {
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        tvResult = new JLabel("Game Is Running!!");
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                button[i][j] = new JButton("");
                button[i][j].addActionListener(this);
                button[i][j].setFocusPainted(false);
            }

        Reset = new JButton("Reset the Game");
        Reset.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTheGame();
            }
        });
        Reset.setFocusPainted(false);

    }

    private void addToPanel() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                boardPanel.add(button[i][j]);
    }


    private void resetTheGame() {
        for (JButton[] jButton : button) {
            for (JButton button : jButton) {
                button.setText("");
                button.setEnabled(true);
                button.setBackground(null);
            }
        }

        tvResult.setText("Game Is Running!!");
        tvResult.setForeground(Color.BLACK);

        TicTacToe.Companion.run(this);
    }

    @Override
    public void onStateChange(Move move, char player) {
        button[move.getRow$TicTacToe()]
                [move.getCol$TicTacToe()].setText(player + "");
    }

    @Override
    public void onDone(char winner) {

        if (winner == 'O') {
            tvResult.setForeground(Color.RED);
            tvResult.setText("Computer Win!!");
        } else if (winner == 'X') {
            tvResult.setForeground(Color.GREEN);
            tvResult.setText("You Win!!");
        } else {
            tvResult.setForeground(Color.BLUE);
            tvResult.setText("Draw!!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (button[i][j] == e.getSource()) {
                    Move m = new Move();
                    m.setRow$TicTacToe(i);
                    m.setCol$TicTacToe(j);
                    System.out.println(m);
                    TicTacToe.Companion.move(m);
                    break;
                }
            }
        }
    }
}