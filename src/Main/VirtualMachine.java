package Main;

public class VirtualMachine {

    // Konstanty
    public static final int maxPocetIntrukcii = 500;

    private final Mapa mapa;
    private final HladacPokladov hladacPokladov;
    private boolean vypisRiesenie = false;

    public VirtualMachine(Mapa mapa, HladacPokladov hladacPokladov) {
        this.mapa = mapa;
        this.hladacPokladov = hladacPokladov;
    }

    public void run(Subject subject) {
        // Operacie VM a byte hodnoty
        int inc =  0;     // 00XX XXXX
        int dec =  64;    // 01XX XXXX
        int jump = 128;     // 10XX XXXX
        int printout =  192;    // 11XX XXXX

        // Klonuj jedinca aby sa zachoval povodny program v jedincovi,
        // kedze virtualny stroj prepisuje hodnoty v bunkach
        Subject vmSubject = subject.cloneNew();

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
            int akt = vmSubject.getBunka(dalsia);

            // Nastavime dalsiu bunku a ak dosiahne maxPocetBuniek tak prejde 0 vdaka modulu
            dalsia = ++dalsia % Subject.numberOfCells;

            // Ziskaj operaciu a cislo bunky
            int operacia = akt & 192;       // 192 => 1100 0000
            int cisloBunky = akt & 63;      // 63  => 0011 1111

            // Vypis
            if (operacia == inc) {
                // Ziska hodnotu pozadovanej bunky jedinca, inkrementuje a zmeni v jedincovi
                int bunka = vmSubject.getBunka(cisloBunky);
                bunka = ++bunka % 256; // ak inkrementujeme 1111 1111 tak dostaneme 0000 0000
                vmSubject.setBunka(cisloBunky, bunka);

            } else if (operacia == dec) {

                // Ziska hodnotu pozadovanej bunky jedinca, dekrementuje a zmeni v jedincovi
                int bunka = vmSubject.getBunka(cisloBunky);
                bunka = --bunka % 256; // Ak dekrementujem 0000 0000 dostanem 1111 1111
                vmSubject.setBunka(cisloBunky, bunka);

            } else if (operacia == jump) {

                // V dalsom kroku programu sa nacita vybrana bunka
                dalsia = cisloBunky;

            } else if (operacia == printout) {

                // Ziska hodnotu pozadovanej bunky jedinca
                int bunka = vmSubject.getBunka(cisloBunky);
                int pohyb = bunka % 4;

                try {
                    Pozicia p = switch (pohyb) {
                        case 0 -> hladacPokladov.vykonajPohyb("H");
                        case 1 -> hladacPokladov.vykonajPohyb("D");
                        case 2 -> hladacPokladov.vykonajPohyb("P");
                        case 3 -> hladacPokladov.vykonajPohyb("L");
                        default -> null;
                    };

                    if(this.vypisRiesenie){
                        subject.pridajNovyPohyb(p);
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

        // Program skoncil, a neporuseny subject sa ohodnoti fitnessom
        // Fitness pozostava z poctu najdenych pokladov * 1000 minus pocet krokov
        // Preto bude mat riesenie s rovnakym poctom najdenych pokladov a


        int fitness = hladacPokladov.getPocNajdenychPokladov()*1000 - hladacPokladov.getPocKrokov();
        subject.setFitness(fitness);
        subject.setTreasuresFound(hladacPokladov.getPocNajdenychPokladov());
        subject.setStepsCount(hladacPokladov.getPocKrokov());

    }

    public void setVypisRiesenie(boolean vypisRiesenie) {
        this.vypisRiesenie = vypisRiesenie;
    }
}
