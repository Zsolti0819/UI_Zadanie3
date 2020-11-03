package Main;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Mapa
{
    // Hashujeme poklady pre rychlu kontrolu.

    private HashMap<String, Point> pokladyZaloha;
    private HashMap<String, Point> poklady;
    private int pocetPokladov;
    private int maxDlzka;
    private int maxVyska;

    // Vytvori mapu s pokladmi bez mapy pokladov
    public Mapa(int pocetPokladov, int maxDlzka, int maxVyska){
        this.pocetPokladov = pocetPokladov;
        this.maxDlzka = maxDlzka;
        this.maxVyska = maxVyska;
    }

    // Vytvori hash mapu pokladov (bodov / suradnic) zo stringu podla formatu "x,y"
    public void parsujPoklady(String poklady){
        String[] dvojice = poklady.split("\n");
        this.poklady = new LinkedHashMap<>(pocetPokladov);
        this.pokladyZaloha = new LinkedHashMap<>(pocetPokladov);
        for(int i=0; i<pocetPokladov; i++){
            String[] cisla = dvojice[i].split(",");
            Point p = new Point(
                    Integer.parseInt(cisla[0]),
                    Integer.parseInt(cisla[1])
            );
            this.poklady.put(unikatnyHash(p.x,p.y), p);
            this.pokladyZaloha.put(unikatnyHash(p.x,p.y), p);
        }
    }

    // Vrati true ak je na [x,y] lezi poklad
    public Boolean jePoklad(int x, int y){
        if(poklady.containsKey(unikatnyHash(x,y))) {
            poklady.remove(unikatnyHash(x,y));
            return true;
        }
        return false;
    }

    // Overi ci sa bod nenachadza  mimo mapy
    public Boolean jevMape(int x, int y){
        if(x < this.maxDlzka && x >= 0 && y < maxVyska && y >= 0){
            return true;
        }
        return false;
    }

    public void resetujMapu(){
        poklady = (HashMap<String, Point>) pokladyZaloha.clone();
    }

    // Vytvori unikatny string pre suradnice x a y.
    private String unikatnyHash(int x, int y){
        return x + " " + y;
    }


    public int getPocetPokladov() {
        return pocetPokladov;
    }
}
