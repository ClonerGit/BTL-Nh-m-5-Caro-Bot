package gamestates;

public class GameState {
    private int[][] board;

    public GameState(int rows, int cols) {
        board = new int[rows][cols];
    }

    public int[][] getBoard() {
        return board;
    }

    public void setCell(int x, int y, int value) {
        board[x][y] = value;
    }

    public int getCell(int x, int y) {
        return board[x][y];
    }

    

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public GameState() {
        // Khởi tạo nếu cần
    }


}
