package Main;

import java.awt.*;

public class VirtualnyStroj {

    // Konstanty
    public static final int maxPocetIntrukcii = 500;


    private Mapa mapa;
    private HladacPokladov hladacPokladov;


    // Operacie VM a byte hodnoty
    private int inkrem =  0;     // 00XX XXXX
    private int dekrem =  64;    // 01XX XXXX
    private int skok = 128;     // 10XX XXXX
    private int vypis =  192;    // 11XX XXXX
    private boolean vypisRiesenie = false;


    public VirtualnyStroj(Mapa mapa, HladacPokladov hladacPokladov) {
        this.mapa = mapa;
        this.hladacPokladov = hladacPokladov;
    }

    public void spustiProgram(Jedinec jedinec) {

        // Klonuj jedinca aby sa zachoval povodny program v jedincovi,
        // kedze virtualny stroj prepisuje hodnoty v bunkach
        Jedinec vmJedinec = jedinec.klonujNovy();


        // Resutujeme hladaca pokladov
        hladacPokladov.reset();

        int pocInstr = 0;

        // Index bunky jedinca, ktora sa nacita v dalsej iteraci
        int dalsia = 0;

        // Prebiehaju instrukcie pokial :
        // 1. zbehne 500 instrukcii
        // 2. najdeme vsetky poklady
        // 3. sa ocitneme mimo mapy
        while (pocInstr < maxPocetIntrukcii) {

            pocInstr++;

            // Nacitamm dalsiu bunku jedinca
            int akt = vmJedinec.getBunka(dalsia);

            // Nastavime dalsiu bunku a ak dosiahne maxPocetBuniek tak prejde 0 vdaka modulu
            dalsia = ++dalsia % vmJedinec.pocetBuniek;

            // Ziskaj operaciu a cislo bunky
            int operacia = akt & 192;       // 192 => 1100 0000
            int cisloBunky = akt & 63;      // 63  => 0011 1111

            // Vypis
            if (operacia == inkrem) {
                // Ziska hodnotu pozadovanej bunky jedinca, inkrementuje a zmeni v jedincovi
                int bunka = vmJedinec.getBunka(cisloBunky);
                bunka = ++bunka % 256; // ak inkrementujeme 1111 1111 tak dostaneme 0000 0000
                vmJedinec.setBunka(cisloBunky, bunka);

            } else if (operacia == dekrem) {

                // Ziska hodnotu pozadovanej bunky jedinca, dekrementuje a zmeni v jedincovi
                int bunka = vmJedinec.getBunka(cisloBunky);
                bunka = --bunka % 256; // Ak dekrementujem 0000 0000 dostanem 1111 1111
                vmJedinec.setBunka(cisloBunky, bunka);

            } else if (operacia == skok) {

                // V dalsom kroku programu sa nacita vybrana bunka
                dalsia = cisloBunky;

            } else if (operacia == vypis) {

                // Ziska hodnotu pozadovanej bunky jedinca
                int bunka = vmJedinec.getBunka(cisloBunky);
                int pohyb = bunka % 4;

                try {
                    Point p = null;
                    switch (pohyb) {
                        case 0:
                            p = hladacPokladov.vykonajPohyb("H");
                            break;
                        case 1:
                            p = hladacPokladov.vykonajPohyb("D");
                            break;
                        case 2:
                            p = hladacPokladov.vykonajPohyb("P");
                            break;
                        case 3:
                            p = hladacPokladov.vykonajPohyb("L");
                            break;
                    }

                    if(this.vypisRiesenie){
                        jedinec.pridajNovyPohyb(p);
                    }

                } catch (MimoMapyException m){
                    break;
                }
            }

            // Over ci nasiel vsetky poklady
            if(hladacPokladov.getPocNajdenychPokladov() == mapa.getPocetPokladov()){
                break;
            }

        }

        // Program skoncil, a neporuseny jedinec sa ohodnoti fitnessom
        // Fitness pozostava z poctu najdenych pokladov * 1000 minus pocet krokov
        // Preto bude mat riesenie s rovnakym poctom najdenych pokladov a


        int fitness = hladacPokladov.getPocNajdenychPokladov()*1000 - hladacPokladov.getPocKrokov();
        jedinec.setFitness(fitness);
        jedinec.setPocetNajdenychPokladov(hladacPokladov.getPocNajdenychPokladov());
        jedinec.setPocetKrokov(hladacPokladov.getPocKrokov());

    }

    public void setVypisRiesenie(boolean vypisRiesenie) {
        this.vypisRiesenie = vypisRiesenie;
    }
}
