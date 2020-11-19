package Main;

public class Solve {
    void findSolution(int treasuresCount, int columns, int rows, int [] treasureXpositions, int [] treasureYpositions, int startX, int startY, int maxGenerationCount, double mutation) throws CloneNotSupportedException {

        Map map = new Map(treasuresCount, columns, rows);
        map.hashMapForTreasures(treasureXpositions, treasureYpositions);
        TreasureFinder treasureFinder = new TreasureFinder(startX, startY, map);
        Algorithm algorithm = new Algorithm(map, treasureFinder, mutation);

        Subject subject = algorithm.converge(maxGenerationCount);
        VirtualMachine virtualMachine = new VirtualMachine(map, treasureFinder);
        virtualMachine.setPrintoutSolution(true);
        virtualMachine.run(subject);

        int moves = subject.getMovesSize();
        System.out.println("Start: [" + treasureFinder.getStartX() + "," + treasureFinder.getStartY() + "]");
        for (int bufer = 0; bufer < moves; bufer++){
            Position p = subject.removeFirstMove();
            if (bufer != (moves-1) && p != null)
                System.out.println(""+(bufer+1)+": [" + p.getCol() + "," + p.getRow()+"]");

            else if (p != null)
                System.out.println(""+(bufer+1)+": [" + p.getCol() + "," + p.getRow()+"]");

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
        double averageFitness = 0;

        for(int i = 0; i < 100; i++) {
            Algorithm algorithm = new Algorithm(map, treasureFinder, mutation);
            // Vypisanie riesenia
            Subject subject = algorithm.converge(maxGenerationCount);
            averageSteps += subject.getStepCount();
            averageTreasures += subject.getTreasuresFound();
            averageFitness += subject.getFitness();
        }

        averageSteps /= 100;
        averageTreasures /= 100;
        averageFitness /= 100;

        System.out.println("Priemerne poklady: " +averageTreasures + "\nKroky " + averageSteps+"\nPriemerny fitness: "+averageFitness);

    }
}
