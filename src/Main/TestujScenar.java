package Main;

public class TestujScenar {
    void testujScenar(int pocetPokladov, int mriezkaX, int mriezkaY, String suradnicePokladov, int startX, int startY, double mutaciaField) {

        Mapa mapa = new Mapa(pocetPokladov, mriezkaX, mriezkaY);

        mapa.parsujPoklady(suradnicePokladov);

        HladacPokladov h = new HladacPokladov(startX, startY, mapa);

        double priemerPoklady = 0;
        double priemerPocetKrokov = 0;

        for(int i=0; i<100; i++) {
            GenetickyAlgoritmus g = new GenetickyAlgoritmus(mapa, h, mutaciaField);
            // Vypisanie riesenia
            Subject j = g.proces();
            priemerPocetKrokov += j.getPocetKrokov();
            priemerPoklady += j.getPocetNajdenychPokladov();
        }

        priemerPocetKrokov /= 100;
        priemerPoklady /= 100;

        System.out.println("Testovany scenar || PRIEMER  poklady: " +priemerPoklady + "   kroky " + priemerPocetKrokov);




    }
}
