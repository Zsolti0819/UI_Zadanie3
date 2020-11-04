package Main;

public class TestScenario {
    void testujScenar(int pocetPokladov, int mriezkaX, int mriezkaY, String suradnicePokladov, int startX, int startY, double mutaciaField) {

        Map map = new Map(pocetPokladov, mriezkaX, mriezkaY);

        map.parseTreasures(suradnicePokladov);

        TreasureFinder h = new TreasureFinder(startX, startY, map);

        double priemerPoklady = 0;
        double priemerPocetKrokov = 0;

        for(int i=0; i<100; i++) {
            Algorithm g = new Algorithm(map, h, mutaciaField);
            // Vypisanie riesenia
            Subject j = g.proces();
            priemerPocetKrokov += j.getStepCount();
            priemerPoklady += j.getTreasuresFound();
        }

        priemerPocetKrokov /= 100;
        priemerPoklady /= 100;

        System.out.println("Testovany scenar || PRIEMER  poklady: " +priemerPoklady + "   kroky " + priemerPocetKrokov);




    }
}
