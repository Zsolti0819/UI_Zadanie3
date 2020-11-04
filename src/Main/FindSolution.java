package Main;

import java.awt.*;

public class FindSolution {
    void najdiRiesenie(int pocetPokladov, int mriezkaX, int mriezkaY, String suradnicePokladov, int startX, int startY, double mutaciaField) {

        Map map = new Map(pocetPokladov, mriezkaX, mriezkaY);

        map.parseTreasures(suradnicePokladov);

        TreasureFinder h = new TreasureFinder(startX, startY, map);

        Algorithm g = new Algorithm(map, h, mutaciaField);

        // Vypisanie riesenia
        Subject j = g.proces();
        VirtualMachine vm = new VirtualMachine(map, h);
        vm.setPrintoutSolution(true);
        vm.spustiProgram(j);

        int pohyby = j.getMovesSize();
        System.out.println("Zacina na X=" + h.getStart().x + " , Y=" + h.getStart().y + " \n");
        for(int i=0; i<pohyby; i++){
            Point p = j.removeFirstMove();
            if(i != (pohyby-1)) {
                System.out.println("hlada na X=" + p.x + " , Y=" + p.y + " \n");
            } else {
                System.out.println("konci na X=" + p.x + " , Y=" + p.y + " \n");
            }
        }
        System.out.println("fitness:"+j.getFitness());
        System.out.println("poklady:"+j.getTreasuresFound());
        System.out.println("krokov:"+j.getStepCount());
    }
}
