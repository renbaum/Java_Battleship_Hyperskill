package battleship;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BoardPosition {
    private int row;
    private int col;
    private boolean shot = false;

    public BoardPosition(int row, int col) {
        init(row, col);
    }

    public BoardPosition(String pos){
        int r = pos.charAt(0) - 'A';
        int c = Integer.parseInt(pos.substring(1));
        init(r, c - 1);
    }

    private void init(int row, int col) {
        if (row < 0 || col < 0) throw new IllegalArgumentException();
        if(row > 9 || col > 9) throw new IllegalArgumentException();
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public String toString() {
        return String.format("%c%d", row + 'A', col + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPosition position = (BoardPosition) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public Set<BoardPosition> getAllInbetween(BoardPosition a, BoardPosition b){
        Set<BoardPosition> set = new HashSet<>();
        set.add(a);
        set.add(b);
        int from = Math.min(a.getRow(), b.getRow());
        int to = Math.max(a.getRow(), b.getRow());

        for(int i = from; i < to; i++){
            set.add(new BoardPosition(i, a.getCol()));
        }

        from = Math.min(a.getCol(), b.getCol());
         to = Math.max(a.getCol(), b.getCol());
        for(int i = from; i < to; i++){
            set.add(new BoardPosition(a.getRow(), i));
        }
        return set;
    }

    public boolean near(BoardPosition occupiedCell) {
        int rowDiff = Math.abs(occupiedCell.getRow() - row);
        int colDiff = Math.abs(occupiedCell.getCol() - col);
        return (rowDiff <= 1 && colDiff <= 1);
    }

    public void setShot() {
        this.shot = true;
    }

    public boolean isShot() {
        return shot;
    }
}
