import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private final int BOARD_SIZE = 3;

    private Tile[][] board;
    private boolean running;
    private Tile currentPlayer;

    public Game() {
        this.setup();
    }

    private void setup() {
        // Initialize attributes
        this.board = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.running = true;
        this.currentPlayer = Tile.X;

        // Populate board-matrix
        for (Tile[] tiles : board) {
            Arrays.fill(tiles, Tile.EMPTY);
        }
    }

    public void start() {
        Scanner scan = new Scanner(System.in);

        this.output("Welcome to Tic tac toe!");
        this.output("To end the game early, input a negative number.");

        while (this.running) {
            this.printBoard();

            // Get user input for row
            int row = this.userInput(scan, "Row");

            if (row < 0) {
                this.running = false;
                break;
            }

            // Get user input for column
            int col = this.userInput(scan, "Column");

            if (col < 0) {
                this.running = false;
                break;
            }

            // Try to set tile
            if (this.setTile(row, col)) {
                this.nextTurn();
            } else {
                this.output("\nThis spot is already taken, please try again...");

                // Skip unnecessary isGameOver-check because the board has not changed.
                continue;
            }

            // After each move, check if someone has won
            if (this.isGameOver()) {
                this.running = false;

                this.printBoard();
                this.output("Game over");
            }
        }

        scan.close();
    }

    private int userInput(Scanner scan, String value) {
        String lowercase = value.toLowerCase();

        System.out.print("(" + this.currentPlayer.toString() + ")" + " - Enter " + lowercase + ": ");

        // Try-catch to make sure that only numbers are inputted
        try {
            int v = scan.nextInt();

            if (v > BOARD_SIZE) {
                this.output(value + " does not exist, please choose a " + lowercase + " between 1-" + BOARD_SIZE);
                return userInput(scan, value);
            }

            return v;
        } catch (Exception e) {
            this.output("That is not a valid number, please try again...");
            scan.nextLine();
            return this.userInput(scan, value);
        }
    }

    private boolean setTile(int r, int c) {
        int row = r - 1, col = c - 1;
        if (this.board[row][col] != Tile.EMPTY)
            return false;

        this.board[row][col] = this.currentPlayer;
        return true;
    }

    private void printBoard() {
        this.output("");
        for (int i = 0; i < board.length; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < this.board[i].length; j++) {

                // Add lines to middle sections
                if (j != 0 && j != this.board[i].length)
                    row.append(" | ");

                // Check value of tile in matrix and add that value to output string
                switch (this.board[i][j]) {
                    case X:
                        row.append(" x ");
                        break;
                    case O:
                        row.append(" o ");
                        break;
                    case EMPTY:
                        row.append("   ");
                        break;
                }
            }

            // Print row
            this.output(row.toString());

            // Add lines to middle sections
            if (i != board.length - 1) {
                for (int j = 0; j < 15; j++) {
                    System.out.print("-");
                }
                this.output("");
            }
        }
        this.output("");
    }

    private boolean isGameOver() {

        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++)
            if (this.checkRowCol(this.board[i][0], this.board[i][1], this.board[i][2]))
                return true;

        // Check columns
        for (int i = 0; i < BOARD_SIZE; i++)
            if (this.checkRowCol(this.board[0][i], this.board[1][i], this.board[2][i]))
                return true;

        // Check diagonal left to right
        if (this.checkRowCol(this.board[0][0], this.board[1][1], this.board[2][2]))
            return true;

        // Check diagonal right to left
        return this.checkRowCol(this.board[0][2], this.board[1][1], this.board[2][0]);
    }

    // Check if three tiles are the same
    private boolean checkRowCol(Tile a, Tile b, Tile c) {
        return a == b && a == c && a != Tile.EMPTY;
    }

    private void nextTurn() {
        this.currentPlayer = this.currentPlayer == Tile.X ? Tile.O : Tile.X;
    }

    private void output(String s) {
        System.out.println(s);
    }
}