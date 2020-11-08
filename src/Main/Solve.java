package Main;

public class Solve {
    void findSolution(int treasuresCount, int columns, int rows, int [] treasureXpositions, int [] treasureYpositions, int startX, int startY, int maxGenerationCount, double mutation) throws CloneNotSupportedException {

        Map map = new Map(treasuresCount, columns, rows);
        map.hashMapForTreasures(treasureXpositions, treasureYpositions);
        TreasureFinder treasureFinder = new TreasureFinder(startX, startY, map);
        Algorithm algorithm = new Algorithm(map, treasureFinder, mutation);

        // Vypisanie riesenia
        Subject subject = algorithm.proces(maxGenerationCount);
        VirtualMachine virtualMachine = new VirtualMachine(map, treasureFinder);
        virtualMachine.setPrintoutSolution(true);
        virtualMachine.run(subject);

        int moves = subject.getMovesSize();
        System.out.println("Hladac ma zaciatocnu poziciu (x,y): [" + treasureFinder.getStartX() + "," + treasureFinder.getStartY() + "]");
        for (int i=0; i < moves; i++){
            Position p = subject.removeFirstMove();
            if (i != (moves-1) && p != null)
                System.out.println("Hladac je na pozicii (x,y): [" + p.getCol() + "," + p.getRow()+"]");

            else if (p != null)
                System.out.println("Hladac koncil na pozicii (x,y): [" + p.getCol() + "," + p.getRow()+"]");

        }

        System.out.println("Fitness:"+subject.getFitness());
        System.out.println("Poklady najdene:"+subject.getTreasuresFound());
        System.out.println("Krokov:"+subject.getStepCount());
    }
    void testScenario(int treasuresCount, int columns, int rows, int [] treasureXpositions, int [] treasureYpositions, int startX, int startY, int maxGenerationCount, double mutation) throws CloneNotSupportedException {

        Map map = new Map(treasuresCount, columns, rows);
        map.hashMapForTreasures(treasureXpositions, treasureYpositions);
        TreasureFinder treasureFinder = new TreasureFinder(startX, startY, map);

        double averageTreasures = 0;
        double averageSteps = 0;

        for(int i = 0; i < 100; i++) {
            Algorithm algorithm = new Algorithm(map, treasureFinder, mutation);
            // Vypisanie riesenia
            Subject subject = algorithm.proces(maxGenerationCount);
            averageSteps += subject.getStepCount();
            averageTreasures += subject.getTreasuresFound();
        }

        averageSteps /= 100;
        averageTreasures /= 100;

        System.out.println("Priemerne poklady: " +averageTreasures + "\nKroky " + averageSteps);

    }
}
