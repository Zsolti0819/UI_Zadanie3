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

    // Resetuje hladača na zač. pozíciu, resetuje počet pokladov a vynuluje počet krokov
    public void reset() {
        map.resetMap();
        actualPosition.setCol(start.getCol());
        actualPosition.setRow(start.getRow());
        treasureCount = map.isTreasure(start.getCol(), start.getRow()) ? 1 : 0; // Ak už strojí na poklade tak zaráta 1 poklad;
        stepCount = 0;
        failed = false;
    }

    public Position moveTo(int pohybX, int pohybY) throws CloneNotSupportedException {
        actualPosition.setCol(actualPosition.getCol()+pohybX);
        actualPosition.setRow(actualPosition.getRow()+pohybY);

        // Ak hladač išiel mimo mapy
        if(! map.isOnTheMap(actualPosition.getCol(), actualPosition.getRow()))
            failed = true;

        stepCount++;

        // Zvyšuj počet nájdených nákladov
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


