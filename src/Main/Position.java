package Main;

public class Position implements Cloneable{
    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    private int col, row;

    @Override
    public Object clone() throws CloneNotSupportedException {
        Position cloned = (Position) super.clone();
        cloned.setRow(cloned.getRow());
        cloned.setCol(cloned.getCol());
        return cloned;
    }

}