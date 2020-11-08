package Main;

public class VirtualMachine {

    // Konstanty
    public static final int maxInstructionCount = 500;

    private final Map map;
    private final TreasureFinder treasureFinder;
    private boolean printoutSolution = false;

    VirtualMachine(Map map, TreasureFinder treasureFinder) {
        this.map = map;
        this.treasureFinder = treasureFinder;
    }

    public void run (Subject subject) throws CloneNotSupportedException {

        int instructionCount;
        int inc =  0;     // 00XX XXXX
        int dec =  64;    // 01XX XXXX
        int jump = 128;     // 10XX XXXX
        // Pre výpis netreba deklarovať hodnotu, lebo ak hodnotu bunky násobíme s 11XX XXXX tak to nezmení nič

        Subject vmSubject = subject.cloneNew(); // Klonujem subject, aby sa zachoval pôvodny, kvôli VM prepisuje hodnoty

        treasureFinder.reset(); // Resetujem treasurefinder


        int next = 0; // Index bunky jedinca, ktora sa nacita v dalsej iteraci

        // Prebiehaju instrukcie pokial :
        // 1. zbehne 500 instrukcii
        // 2. najdeme vsetky poklady
        // 3. sa ocitneme mimo mapy
        for (instructionCount = 0; instructionCount < maxInstructionCount; instructionCount++) {

            int buff = vmSubject.getCell(next); // Nacitamm dalsiu bunku jedinca

            next = ++next % Subject.numberOfCells; // Nastavime dalsiu bunku a ak dosiahne maxPocetBuniek tak prejde 0 vdaka modulu

            int operation = buff & 192;       // 192 => 1100 0000
            int value = buff & 63;      // 63  => 0011 1111

            if (operation == inc) {
                int cell = vmSubject.getCell(value); // ziskanie hodnotu bunky jedinca
                cell = ++cell % 256; // ak inkrementujeme 1111 1111 tak dostaneme 0000 0000
                vmSubject.setCell(value, cell); // inkrementovanu hodnotu ulozime v jedincovi
            }
            else if (operation == dec) {
                int cell = vmSubject.getCell(value);
                cell = --cell % 256; // Ak dekrementujem 0000 0000 dostanem 1111 1111
                vmSubject.setCell(value, cell);
            }
            else if (operation == jump)
                next = value; // V dalsom kroku programu sa nacita vybrana bunka

            else {
                int cell = vmSubject.getCell(value);
                int move = cell % 4;

                    Position position = switch (move) {
                        case 0 -> treasureFinder.moveTo(1, 0); // HORE
                        case 1 -> treasureFinder.moveTo(0, 1); // VPRAVO
                        case 2 -> treasureFinder.moveTo(0, -1); // DOLE
                        case 3 -> treasureFinder.moveTo(-1, 0); // VLAVO
                        default -> null;
                    };

                    if(this.printoutSolution){
                        subject.addNewMove(position);
                    }
                }

            // Ak hladač išiel mimo mapy
            if (treasureFinder.isFailed())
                break;

            // Ak hladač našiel všetky poklady
            if(treasureFinder.getTreasuresFound() == map.getTreasureCount()) {
                break;
            }
        }

        // Program skoncil, a neporuseny subject sa ohodnoti fitnessom
        // Fitness pozostava z poctu najdenych pokladov * 1000 minus pocet krokov
        // Preto bude mat riesenie s rovnakym poctom najdenych pokladov a

        int fitness = treasureFinder.getTreasuresFound()*1000 - treasureFinder.getStepCount();
        subject.setFitness(fitness);
        subject.setTreasuresFound(treasureFinder.getTreasuresFound());
        subject.setStepCount(treasureFinder.getStepCount());
    }

    public void setPrintoutSolution(boolean printoutSolution) {
        this.printoutSolution = printoutSolution;
    }
}
