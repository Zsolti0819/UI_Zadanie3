package Main;

import java.util.*;

public class GenetickyAlgoritmus {

    // Konstanty
    public static final int pocetJedincov = 100;
    public static final int maxPocetGeneracii = 500;

    // Premenne
    private Jedinec[] populacia = new Jedinec[pocetJedincov];
    private Mapa mapa;
    private HladacPokladov hladacPokladov;
    private VirtualnyStroj virtualnyStroj;
    private double pravdMutacie; // Pravdepobost mutaci


    public GenetickyAlgoritmus(Mapa mapa, HladacPokladov hladacPokladov, double mutacie){

        this.mapa = mapa;
        this.hladacPokladov = hladacPokladov;
        this.virtualnyStroj = new VirtualnyStroj(mapa, hladacPokladov);
        this.pravdMutacie = mutacie;

        // Vygeneruj pociatocnu populaciu
        for(int i=0; i<pocetJedincov; i++){
            populacia[i] = new Jedinec();
        }
    }

    public Jedinec proces(){

        // Vypisovanie fitness hodnot pre analyzu algoritmu
        int pocGeneracii = 0;

        while( pocGeneracii < maxPocetGeneracii) {

            int pocetPokladovGeneracii = 0;

            pocGeneracii++;

            // Ohodnot populaciu pomoou virtualneho stroja a zapis hodnoty fitness
            for(int i=0; i<pocetJedincov; i++){
                virtualnyStroj.spustiProgram(populacia[i]);
                pocetPokladovGeneracii = Math.max(pocetPokladovGeneracii, populacia[i].getPocetNajdenychPokladov());
            }

            // Vytvorime novu generaciu, ktorou nahradime po krizeni a mutaciach tu povodnu
            Jedinec[] novaPopulacia = new Jedinec[pocetJedincov];

            // Vytvorim prioritny rad a vlozim vsetkych jedincov
            // Porovnam podla fitness hodnoty oboch jedincov
            Comparator<Jedinec> cmprtr = new FitnessComparator();
            PriorityQueue<Jedinec> jedinciFronta = new PriorityQueue<>(cmprtr);
            double priemerFitness = 0;
            for(int i = 0; i<pocetJedincov; i++){
                priemerFitness += populacia[i].getFitness();
                jedinciFronta.add(populacia[i]);
            }

            priemerFitness /= pocetJedincov;


            // Ziskam maximalnu velkost fitness funkcie
            double maxFitness = jedinciFronta.peek().getFitness();
//            System.out.print(""+maxFitness+";\n");

            // Elitarizmus - najlepsich 10% jedincov (podla fitness) sa automaticky naklonuje do novej populacie
            int pocElity = pocetJedincov / 10;
            for(int i=0; i<pocElity; i++){

                novaPopulacia[i] = jedinciFronta.remove();

                // Ak sme nasli vsetky poklady, vratime jedinca a skonci while
                if(pocetPokladovGeneracii == mapa.getPocetPokladov()) {
                    return novaPopulacia[i];
                }
            }

            // Krizenie
            int pocetNovychPotomkov = (pocetJedincov / 10 ) * 9; // 90% novej populacie

            Random rand = new Random();


            // Selekcia rodicov pomocou metody rulety

            List<Jedinec> ruleta = new LinkedList<>();
            int pocetRulety, pocetTurnaja;
            pocetRulety = pocetTurnaja = pocetNovychPotomkov / 2;


            for(int i=0; i<pocetRulety; i++){
                Jedinec j = jedinciFronta.remove();
                double n = j.getFitness() / maxFitness;
                int pocet = (int)(n * 100);
                for(int k=0; k<pocet; k++){
                    ruleta.add(j);
                }
            }

            for(int i=0; i<pocetRulety  ; i++){

                Jedinec j1 = ruleta.get(rand.nextInt(ruleta.size()));
                Jedinec j2 = ruleta.get(rand.nextInt(ruleta.size()));

                Jedinec krizenec = new Jedinec(j1, j2);
                novaPopulacia[i+pocElity] = krizenec;
            }

            // Selekcia rodicov pomocou metody turnaja
            // Nahodne sa vyberu 4 jedinci z populacie
            // a dvaja lokalni vitazi sa skrizia a vytvoria noveho potomka .
            for(int i=0; i<pocetTurnaja; i++){

                Jedinec j1 = populacia[rand.nextInt(pocetJedincov)];
                Jedinec j2 = populacia[rand.nextInt(pocetJedincov)];
                Jedinec rodic1 = suboj(j1, j2);

                Jedinec j3 = populacia[rand.nextInt(pocetJedincov)];
                Jedinec j4 = populacia[rand.nextInt(pocetJedincov)];
                Jedinec rodic2 = suboj(j3, j4);

                Jedinec krizenec = new Jedinec(rodic1, rodic2);
                novaPopulacia[i+pocElity+pocetRulety] = krizenec;
            }

            // Mutacie - podla pravdepodobnosti sa zmutuje x percent celej polupulacie
            // Jedinec mutuje tak, ze sa jedna jeho bunka nahodne nahradi inou hodnotou.
            for(int i = 0; i<pocetJedincov; i++){

                // Pravdepobnost medzi 0.0 az 1.0, ktore vracia newxtDouble()
                double pravd = rand.nextDouble();
                if(pravd <= pravdMutacie){
                    novaPopulacia[i].mutuj();
                }
            }

            // Nahrad povodnu populaciu novou
            populacia = novaPopulacia;
        }

        // Vrati najuspesnejsieho jedinca ak sme nenasli vsetky poklady
        return populacia[0];

    }

    private Jedinec suboj(Jedinec j1, Jedinec j2){
        if(j1.getFitness() >= j2.getFitness()){
            return j1;
        }
        return j2;
    }

}

// Trieda definuje nové porovnávanie v prioritnej rade, podľa atribútu fitness
// cim vacsia fitness, tým prvšia pozícia vo fronte
class FitnessComparator implements Comparator<Jedinec> {

    @Override
    public int compare(Jedinec o1, Jedinec o2) {
        if(o1.getFitness() > o2.getFitness()){
            return -1;
        }
        if(o1.getFitness() < o2.getFitness()){
            return 1;
        }
        return 0;
    }
}