package Main;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Map
{
    // Hashujeme poklady pre rychlu kontrolu.

    private HashMap<String, Point> treasuresBackup;
    private HashMap<String, Point> treasures;
    private final int treasureCount;
    private final int maxLength;
    private final int maxHeight;

    // Vytvori mapu s pokladmi bez mapy pokladov
    public Map(int treasureCount, int maxLength, int maxHeight){
        this.treasureCount = treasureCount;
        this.maxLength = maxLength;
        this.maxHeight = maxHeight;
    }

    // Vytvori hash mapu pokladov (bodov / suradnic) zo stringu podla formatu "x,y"
    public void parseTreasures(String treasures){
        String[] doubles = treasures.split("\n");
        this.treasures = new LinkedHashMap<>(treasureCount);
        this.treasuresBackup = new LinkedHashMap<>(treasureCount);
        for(int i = 0; i< treasureCount; i++){
            String[] numbers = doubles[i].split(",");
            Point p = new Point(
                    Integer.parseInt(numbers[0]),
                    Integer.parseInt(numbers[1])
            );
            this.treasures.put(hashCode(p.x,p.y), p);
            this.treasuresBackup.put(hashCode(p.x,p.y), p);
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
        if(x < this.maxLength && x >= 0 && y < maxHeight && y >= 0){
            return true;
        }
        return false;
    }

    public void resetMap(){
        treasures = (HashMap<String, Point>) treasuresBackup.clone();
    }

    // Vytvori unikatny string pre suradnice x a y.
    private String hashCode(int x, int y){
        return x + " " + y;
    }


    public int getTreasureCount() {
        return treasureCount;
    }
}
