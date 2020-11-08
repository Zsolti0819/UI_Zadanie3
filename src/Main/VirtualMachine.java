package Main;

public class VirtualMachine {

    // Konstanty
    public static final int maxInstructionCount = 500;

    private final Map map;
    private final TreasureFinder treasureFinder;
    private boolean printoutSolution = false;

    public VirtualMachine(Map map, TreasureFinder treasureFinder) {
        this.map = map;
        this.treasureFinder = treasureFinder;
    }

    public void run (Subject subject) throws CloneNotSupportedException {

        int instructionCount;
        int inc =  0;     // 00XX XXXX
        int dec =  64;    // 01XX XXXX
        int jump = 128;     // 10XX XXXX
        // Pre výpis netreba deklarovať hodnotu, lebo ak hodnotu bunky násobíme s 11XX XXXX tak to nezmení nič

        // Klonujem subject, aby sa zachoval pôvodny, kvôli VM prepisuje hodnoty
        Subject vmSubject = subject.cloneNew();

        // Resetujem treasurefinder
        treasureFinder.reset();

        // Index bunky jedinca, ktora sa nacita v dalsej iteraci
        int next = 0;

        // Prebiehaju instrukcie pokial :
        // 1. zbehne 500 instrukcii
        // 2. najdeme vsetky poklady
        // 3. sa ocitneme mimo mapy
        for (instructionCount = 0; instructionCount < maxInstructionCount; instructionCount++) {
            // Nacitamm dalsiu bunku jedinca
            int buff = vmSubject.getCell(next);

            // Nastavime dalsiu bunku a ak dosiahne maxPocetBuniek tak prejde 0 vdaka modulu
            next = ++next % Subject.numberOfCells;

            // Ziskaj operaciu a cislo bunky
            int operation = buff & 192;       // 192 => 1100 0000
            int value = buff & 63;      // 63  => 0011 1111

            // Vypis
            if (operation == inc) {
                // Ziska hodnotu pozadovanej bunky jedinca, inkrementuje a zmeni v jedincovi
                int cell = vmSubject.getCell(value);
                cell = ++cell % 256; // ak inkrementujeme 1111 1111 tak dostaneme 0000 0000
                vmSubject.setCell(value, cell);

            } else if (operation == dec) {

                // Ziska hodnotu pozadovanej bunky jedinca, dekrementuje a zmeni v jedincovi
                int cell = vmSubject.getCell(value);
                cell = --cell % 256; // Ak dekrementujem 0000 0000 dostanem 1111 1111
                vmSubject.setCell(value, cell);

            } else if (operation == jump) {

                // V dalsom kroku programu sa nacita vybrana bunka
                next = value;

            } else {

                // Ziska hodnotu pozadovanej bunky jedinca
                int cell = vmSubject.getCell(value);
                int move = cell % 4;


                    Position p = switch (move) {
                        case 0 -> treasureFinder.whereToMove("H");
                        case 1 -> treasureFinder.whereToMove("D");
                        case 2 -> treasureFinder.whereToMove("P");
                        case 3 -> treasureFinder.whereToMove("L");
                        default -> null;
                    };

                    if(this.printoutSolution){
                        subject.addNewMove(p);
                    }

                }

            // Hladac pokladoj ocitil mimi mapy
            if (treasureFinder.isFailed())
                break;


            // Over ci nasiel vsetky poklady
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
