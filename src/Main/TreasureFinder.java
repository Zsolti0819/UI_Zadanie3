package Main;

import java.awt.*;

public class TreasureFinder {

    private final Point start;
    private final Point actualPosition = new Point();
    private int treasureCount;
    private int stepCount;
    private final Map map;


    public TreasureFinder(int startX, int startY, Map map) {
        this.map = map;
        start = new Point(startX, startY);
        this.reset();
    }


    // Resetuje hladaca na zaciatocnu poziciu, resetuje pocet pokladov a vynuluje pocet krokov
    public void reset() {
        map.resetMap();
        actualPosition.setLocation(start.x, start.y);
        treasureCount = map.isTreasure(start.x, start.y) ? 1 : 0; // Ak uz stoji na poklade tak zarata 1 poklad;
        stepCount = 0;
    }

    // Zaregistruje moveTo cez vypis vo virtualnom stroji a podla pismenka vyvola moveTo na suradnice.
    public Point whereToMove(String pohyb) throws OutsideOfTheMapException {
        return switch (pohyb) {
            case "P" -> moveTo(1, 0);
            case "H" -> moveTo(0, 1);
            case "D" -> moveTo(0, -1);
            case "L" -> moveTo(-1, 0);
            default -> null;
        };
    }

    // Aktualizuj poziciiu hladaca a vyhod vynimku ak siahne mimo mapy
    private Point moveTo(int pohybX, int pohybY) throws OutsideOfTheMapException {
        actualPosition.x += pohybX;
        actualPosition.y += pohybY;

        // Ak sa ocitne mimo mapy vyhodi vynimku
        if(! map.isOnTheMap(actualPosition.x, actualPosition.y)){
            throw new OutsideOfTheMapException("Hladac sa ocitol mimo mapy.");
        }

        stepCount++;

        // Zvysi pocet najdenych pokladov ak nasiel na novej pozicii poklad.
        if(map.isTreasure(actualPosition.x, actualPosition.y)){
            treasureCount++;
        }

        return (Point) actualPosition.clone();


         //System.out.println("X > " + aktPozicia.x + ", Y >" + aktPozicia.y + ", POKLADY = " + pocPokladov);

    }

    public Point getStart() {
        return start;
    }

    public int getTreasuresFound() {
        return treasureCount;
    }

    public int getStepCount() {
        return stepCount;
    }
}


