package com.example.final_year_project;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class TicTacToeActivity extends RobotActivity implements RobotLifecycleCallbacks,View.OnClickListener {

    private int playerXScore = 0;
    private int playerOScore = 0;

    private TextView tvScore;

    private Button[][] buttons = new Button[3][3];
    private boolean playerTurn = true;
    private int moveCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        tvScore = findViewById(R.id.titleQueueNum3);

        // Initialize the 2D array to hold the buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = initButton(i, j);
            }
        }
    }

    private Button initButton(int row, int col) {
        int resID = getResources().getIdentifier("btn" + row + col, "id", getPackageName());
        Button button = findViewById(resID);
        button.setOnClickListener(this);
        return button;
    }

    private void makeRandomMove() {
        // Generate a random row and column to place the O
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().toString().isEmpty());

        buttons[row][col].setText("O");
    }

    private void computerPlay() {
        if (moveCount == 0) {
            // If the board is empty, the computer makes the first move in the center
            buttons[1][1].setText("O");
            moveCount++;
        } else {
            // Implement the computer's move (dumb AI)
            makeRandomMove();
            moveCount++;
        }

        if (checkForWin(boardToState(), "O")) {
            showToast("Player O wins!");
            playerOScore++;
            tvScore.setText("Score: " + playerXScore + " - " + playerOScore);
            disableButtons();
            resetGame();
        } else if (moveCount == 9) {
            showToast("It's a draw!");
            disableButtons();
            resetGame();
        } else {
            playerTurn = true; // Update playerTurn to true after the computer's move.
        }
    }

    private String[][] boardToState() {
        String[][] boardState = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = buttons[i][j].getText().toString();
            }
        }
        return boardState;
    }

    private Move minimax(String[][] boardState, String player) {
        List<Move> moves = new ArrayList<>();

        if (checkForWin(boardState, "X")) {
            return new Move(-10);
        } else if (checkForWin(boardState, "O")) {
            return new Move(10);
        } else if (isBoardFull(boardState)) {
            return new Move(0);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j].isEmpty()) {
                    Move move = new Move();
                    move.row = i;
                    move.col = j;

                    boardState[i][j] = player;
                    if (player.equals("O")) {
                        Move result = minimax(boardState, "X");
                        move.score = result.score;
                    } else {
                        Move result = minimax(boardState, "O");
                        move.score = result.score;
                    }

                    boardState[i][j] = "";
                    moves.add(move);
                }
            }
        }

        Move bestMove = null;
        if (player.equals("O")) {
            int bestScore = Integer.MIN_VALUE;
            for (Move move : moves) {
                if (move.score > bestScore) {
                    bestScore = move.score;
                    bestMove = move;
                }
            }
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                if (move.score < bestScore) {
                    bestScore = move.score;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    private boolean checkForWin(String[][] boardState, String player) {
        // Check rows for a win
        for (int i = 0; i < 3; i++) {
            if (boardState[i][0].equals(player) && boardState[i][1].equals(player) && boardState[i][2].equals(player)) {
                return true;
            }
        }

        // Check columns for a win
        for (int j = 0; j < 3; j++) {
            if (boardState[0][j].equals(player) && boardState[1][j].equals(player) && boardState[2][j].equals(player)) {
                return true;
            }
        }

        // Check diagonals for a win
        if (boardState[0][0].equals(player) && boardState[1][1].equals(player) && boardState[2][2].equals(player)) {
            return true;
        }

        if (boardState[0][2].equals(player) && boardState[1][1].equals(player) && boardState[2][0].equals(player)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull(String[][] boardState) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j].isEmpty()) {
                    return false; // If any cell is empty, the board is not full
                }
            }
        }
        return true; // If all cells are filled, the board is full
    }

    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        // Reset the board and player turn after a short delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        buttons[i][j].setText("");
                        buttons[i][j].setEnabled(true);
                    }
                }
                playerTurn = true;
                moveCount = 0;

                // Update the score TextView
                tvScore.setText("Score: " + playerXScore + " - " + playerOScore);
            }
        }, 1500); // Adjust the delay time (in milliseconds) as needed
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        Button button = (Button) v;
        if (button.getText().toString().isEmpty() && playerTurn) {
            button.setText("X");
            moveCount++;

            // Check for win after the player's move
            if (checkForWin(boardToState(), "X")) {
                playerXScore++;
                tvScore.setText("Score: " + playerXScore + " - " + playerOScore);
                showToast("Player X wins!");
                disableButtons();
                resetGame();
            } else if (moveCount == 9) {
                showToast("It's a draw!");
                disableButtons();
                resetGame();
            } else {
                playerTurn = !playerTurn;
                computerPlay(); // Call the computerPlay() method after the player's move.
            }
        }
    }

    private static class Move {
        int row;
        int col;
        int score;

        Move() {
        }

        Move(int score) {
            this.score = score;
        }
    }


    @Override
    public void onRobotFocusGained(QiContext qiContext) {

    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}