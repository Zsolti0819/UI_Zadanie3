package Main;

import java.awt.*;

public class NajdiRiesenie {
    void najdiRiesenie(int pocetPokladov, int mriezkaX, int mriezkaY, String suradnicePokladov, int startX, int startY, double mutaciaField) {

        Mapa mapa = new Mapa(pocetPokladov, mriezkaX, mriezkaY);

        mapa.parsujPoklady(suradnicePokladov);

        HladacPokladov h = new HladacPokladov(startX, startY, mapa);

        GenetickyAlgoritmus g = new GenetickyAlgoritmus( mapa, h, mutaciaField);

        // Vypisanie riesenia
        Jedinec j = g.proces();
        VirtualnyStroj vm = new VirtualnyStroj(mapa, h);
        vm.setVypisRiesenie(true);
        vm.spustiProgram(j);

        int pohyby = j.getPohybySize();
        System.out.println("Zacina na X=" + h.getStart().x + " , Y=" + h.getStart().y + " \n");
        for(int i=0; i<pohyby; i++){
            Point p = j.vyberPrvyPohyb();
            if(i != (pohyby-1)) {
                System.out.println("hlada na X=" + p.x + " , Y=" + p.y + " \n");
            } else {
                System.out.println("konci na X=" + p.x + " , Y=" + p.y + " \n");

            }
        }

        System.out.println("fitness:"+j.getFitness());
        System.out.println("poklady:"+j.getPocetNajdenychPokladov());
        System.out.println("krokov:"+j.getPocetKrokov());




    }
}