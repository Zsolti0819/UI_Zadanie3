package Main;

public class TreasureFinder {

    private final Position start;
    private final Position actualPosition = new Position();
    private int treasureCount;
    private int stepCount;
    private boolean failed = false;
    private final Map map;

    public TreasureFinder(int startX, int startY, Map map) {
        this.map = map;
        start = new Position();
        start.setCol(startX);
        start.setRow(startY);
        this.reset();
    }

    // Resetuje hladaca na zaciatocnu poziciu, resetuje pocet pokladov a vynuluje pocet krokov
    public void reset() {
        map.resetMap();
        actualPosition.setCol(start.getCol());
        actualPosition.setRow(start.getRow());
        treasureCount = map.isTreasure(start.getCol(), start.getRow()) ? 1 : 0; // Ak uz stoji na poklade tak zarata 1 poklad;
        stepCount = 0;
        failed = false;
    }

    Position moveTo(int pohybX, int pohybY) throws CloneNotSupportedException {
        actualPosition.setCol(actualPosition.getCol()+pohybX);
        actualPosition.setRow(actualPosition.getRow()+pohybY);

        // Ak sa ocitne mimo mapy
        if(! map.isOnTheMap(actualPosition.getCol(), actualPosition.getRow()))
            failed = true;

        stepCount++;

        // Zvysi pocet najdenych pokladov ak nasiel na novej pozicii poklad.
        if(map.isTreasure(actualPosition.getCol(), actualPosition.getRow())) {
            treasureCount++;
        }
        return (Position) actualPosition.clone();
    }

    public int getStartX() {
        return start.getCol();
    }

    public int getStartY() {
        return start.getRow();
    }

    public int getTreasuresFound() {
        return treasureCount;
    }

    public int getStepCount() {
        return stepCount;
    }

    public boolean isFailed() {
        return failed;
    }

}


