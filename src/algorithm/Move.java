package algorithm;

public class Move {
    public int row;
    public int col;
    public char symbol;
    
    public Move(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
