package cinema.entities;

public class Place {
    private int row;
    private int column;

    Place() {
    }

    Place(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
