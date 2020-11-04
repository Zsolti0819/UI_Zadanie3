package Main;

import java.awt.*;

public class HladacPokladov {

    private final Pozicia start;
    private final Pozicia aktPozicia = new Pozicia();
    private int pocPokladov;
    private int pocKrokov;
    private final Mapa mapa;

    public HladacPokladov(int startX, int startY, Mapa mapa) {
        this.mapa = mapa;
        start = new Pozicia();
        start.setStlpec(startX);
        start.setRiadok(startY);
        this.reset();
    }

    // Resetuje hladaca na zaciatocnu poziciu, resetuje pocet pokladov a vynuluje pocet krokov
    public void reset() {
        mapa.resetujMapu();
        aktPozicia.setRiadok(start.getRiadok());
        aktPozicia.setStlpec(start.getStlpec());
        pocPokladov = mapa.jePoklad(start.getStlpec(), start.getRiadok()) ? 1 : 0; // Ak uz stoji na poklade tak zarata 1 poklad;
        pocKrokov = 0;
    }

    // Zaregistruje pohyb cez vypis vo virtualnom stroji a podla pismenka vyvola pohyb na suradnice.
    public Pozicia vykonajPohyb(String pohyb) throws  MimoMapyException{
        return switch (pohyb) {
            case "P" -> pohyb(1, 0);
            case "H" -> pohyb(0, 1);
            case "D" -> pohyb(0, -1);
            case "L" -> pohyb(-1, 0);
            default -> null;
        };
    }

    // Aktualizuj poziciiu hladaca a vyhod vynimku ak siahne mimo mapy
    private Pozicia pohyb(int pohybX, int pohybY) throws MimoMapyException {
        aktPozicia.setStlpec(aktPozicia.getStlpec()+pohybX);
        aktPozicia.setRiadok(aktPozicia.getRiadok()+pohybY);

        // Ak sa ocitne mimo mapy vyhodi vynimku
        if(! mapa.jevMape(aktPozicia.getStlpec(), aktPozicia.getRiadok())){
            throw new MimoMapyException("Hladac sa ocitol mimo mapy.");
        }

        pocKrokov++;

        // Zvysi pocet najdenych pokladov ak nasiel na novej pozicii poklad.
        if(mapa.jePoklad(aktPozicia.getStlpec(), aktPozicia.getRiadok())){
            pocPokladov++;
        }

        try {
            Object aktPoziciaCloned = aktPozicia.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return aktPozicia;

    }

    public int getStartX() {
        return start.getStlpec();
    }

    public int getStartY() {
        return start.getRiadok();
    }

    public int getPocNajdenychPokladov() {
        return pocPokladov;
    }

    public int getPocKrokov() {
        return pocKrokov;
    }
}


