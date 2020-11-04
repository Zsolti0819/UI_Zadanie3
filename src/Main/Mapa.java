package Main;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Mapa
{
    // Hashujeme poklady pre rychlu kontrolu.

    private HashMap<String, Pozicia> pokladyZaloha;
    private HashMap<String, Pozicia> poklady;
    private final int pocetPokladov;
    private final int maxDlzka;
    private final int maxVyska;

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
            Pozicia p = new Pozicia();
            p.setStlpec(Integer.parseInt(cisla[0]));
            p.setRiadok(Integer.parseInt(cisla[1]));
            this.poklady.put(unikatnyHash(p.getStlpec(),p.getRiadok()), p);
            this.pokladyZaloha.put(unikatnyHash(p.getStlpec(),p.getRiadok()), p);
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
        return x < this.maxDlzka && x >= 0 && y < maxVyska && y >= 0;
    }

    public void resetujMapu(){
        poklady = (HashMap<String, Pozicia>) pokladyZaloha.clone();
    }

    // Vytvori unikatny string pre suradnice x a y.
    private String unikatnyHash(int x, int y){
        return x + " " + y;
    }


    public int getPocetPokladov() {
        return pocetPokladov;
    }
}
