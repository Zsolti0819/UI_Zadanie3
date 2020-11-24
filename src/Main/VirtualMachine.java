package Main;

public class VirtualMachine {

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

        treasureFinder.reset();
        
        int next = 0;

        for (instructionCount = 0; instructionCount < maxInstructionCount; instructionCount++) {

            int buff = vmSubject.getCell(next); // Načítanie ďalšej bunky

            next = ++next % Subject.numberOfCells; // Nastavenie ďalšej bunky

            int operation = buff & 192;       // 192 => 1100 0000
            int value = buff & 63;      // 63  => 0011 1111

            if (operation == inc) {
                int cell = vmSubject.getCell(value);
                cell = ++cell % 256; // ak inkrementujeme 1111 1111 tak dostaneme 0000 0000
                vmSubject.setCell(value, cell);
            }
            else if (operation == dec) {
                int cell = vmSubject.getCell(value);
                cell = --cell % 256; // Ak dekrementujem 0000 0000 dostanem 1111 1111
                vmSubject.setCell(value, cell);
            }
            else if (operation == jump)
                next = value; // V ďalšom kroku sa načíta vybraná bunka

            else {
                int cell = vmSubject.getCell(value);
                int move = cell & 3; // posledné 4 bity

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

            // Ak hladač išiel mimo mapy alebo ak našiel všetky poklady
            if (treasureFinder.isFailed() || treasureFinder.getTreasuresFound() == map.getTreasureCount())
                break;
        }

        // Vypočítanie fitness hodnoty
        int fitness = treasureFinder.getTreasuresFound()*1000 - treasureFinder.getStepCount();
        subject.setFitness(fitness);
        subject.setTreasuresFound(treasureFinder.getTreasuresFound());
        subject.setStepCount(treasureFinder.getStepCount());
    }

    public void setPrintoutSolution(boolean printoutSolution) {
        this.printoutSolution = printoutSolution;
    }
}
