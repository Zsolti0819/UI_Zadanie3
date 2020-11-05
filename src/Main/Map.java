package Main;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Map
{
    // Hashujeme poklady pre rychlu kontrolu.

    private HashMap<String, Position> treasuresBackup;
    private HashMap<String, Position> treasures;
    private final int treasureCount;
    private final int maxLength;
    private final int maxHeight;

    // Vytvori mapu s pokladmi bez mapy pokladov
    public Map(int treasureCount, int maxLength, int maxHeight){
        this.treasureCount = treasureCount;
        this.maxLength = maxLength;
        this.maxHeight = maxHeight;
    }

    // Vytvori hash mapu pokladov podla suradnic x,y
    public void hashMapForTreasures(int [] pokladyX, int [] pokladyY){
        this.treasures = new LinkedHashMap<>(treasureCount);
        this.treasuresBackup = new LinkedHashMap<>(treasureCount);
        for(int i = 0; i< treasureCount; i++){
            Position p = new Position();
            p.setRow(pokladyY[i]);
            p.setCol(pokladyX[i]);
            this.treasures.put(hashCode(p.getCol(),p.getRow()), p);
            this.treasuresBackup.put(hashCode(p.getCol(),p.getRow()), p);
        }
    }

    // Vrati true ak je na [x,y] lezi poklad
    public Boolean isTreasure(int x, int y){
        if(treasures.containsKey(hashCode(x,y))) {
            treasures.remove(hashCode(x,y));
            return true;
        }
        return false;
    }

    // Overi ci sa bod nenachadza  mimo mapy
    public Boolean isOnTheMap(int x, int y){
        return x < this.maxLength && x >= 0 && y < maxHeight && y >= 0;
    }

    public void resetMap(){
        treasures = (HashMap<String, Position>) treasuresBackup.clone();
    }

    // Vytvori unikatny string pre suradnice x a y.
    private String hashCode(int x, int y){
        return x + " " + y;
    }


    public int getTreasureCount() {
        return treasureCount;
    }
}
