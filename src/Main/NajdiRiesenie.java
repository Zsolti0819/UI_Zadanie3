package Main;

public class NajdiRiesenie {
    void najdiRiesenie(int pocetPokladov, int mriezkaX, int mriezkaY, String suradnicePokladov, int startX, int startY, double mutaciaField) {

        Mapa mapa = new Mapa(pocetPokladov, mriezkaX, mriezkaY);

        mapa.parsujPoklady(suradnicePokladov);

        HladacPokladov h = new HladacPokladov(startX, startY, mapa);

        GenetickyAlgoritmus g = new GenetickyAlgoritmus( mapa, h, mutaciaField);

        // Vypisanie riesenia
        Subject j = g.proces();
        VirtualMachine vm = new VirtualMachine(mapa, h);
        vm.setVypisRiesenie(true);
        vm.run(j);

        int pohyby = j.getPohybySize();
        System.out.println("Zacina na X=" + h.getStartX() + " , Y=" + h.getStartY() + " \n");
        for(int i=0; i<pohyby; i++){
            Pozicia p = j.vyberPrvyPohyb();
            if(i != (pohyby-1)) {
                System.out.println("hlada na X=" + p.getStlpec() + " , Y=" + p.getRiadok() + " \n");
            } else {
                System.out.println("konci na X=" + p.getStlpec() + " , Y=" + p.getRiadok() + " \n");

            }
        }

        System.out.println("fitness:"+j.getFitness());
        System.out.println("poklady:"+j.getPocetNajdenychPokladov());
        System.out.println("krokov:"+j.getPocetKrokov());




    }
}
