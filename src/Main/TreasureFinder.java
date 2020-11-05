package Main;

public class TreasureFinder {

    private final Position start;
    private final Position actualPosition = new Position();
    private int treasureCount;
    private int stepCount;
    private int fails;
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
        fails = 0;
    }

    // Funkciu používam v triede VirtualMachine, kde podľa posledných 2 bitov rozhodneme, kam chceme ísť
    public Position whereToMove(String pohyb) throws CloneNotSupportedException {
        return switch (pohyb) {
            case "P" -> moveTo(1, 0);
            case "H" -> moveTo(0, 1);
            case "D" -> moveTo(0, -1);
            case "L" -> moveTo(-1, 0);
            default -> null;
        };
    }

    // Aktualizuj poziciiu hladaca a vyhod vynimku ak siahne mimo mapy
    private Position moveTo(int pohybX, int pohybY) throws CloneNotSupportedException {
        actualPosition.setCol(actualPosition.getCol()+pohybX);
        actualPosition.setRow(actualPosition.getRow()+pohybY);

        // Ak sa ocitne mimo mapy vyhodi vynimku
        if(! map.isOnTheMap(actualPosition.getCol(), actualPosition.getRow())) {
            fails++;
        }

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

    public int getFails() {
        return fails;
    }

    public void setFails(int fails) {
        this.fails = fails;
    }
}


