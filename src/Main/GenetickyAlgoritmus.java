package Main;

import java.util.*;

public class GenetickyAlgoritmus {

    // Konstanty
    public static final int pocetJedincov = 100;
    public static final int maxPocetGeneracii = 500;

    // Premenne
    private Subject[] populacia = new Subject[pocetJedincov];
    private final Mapa mapa;
    private final VirtualMachine virtualMachine;
    private final double pravdMutacie; // Pravdepobost mutaci


    public GenetickyAlgoritmus(Mapa mapa, HladacPokladov hladacPokladov, double mutacie){

        this.mapa = mapa;
        this.virtualMachine = new VirtualMachine(mapa, hladacPokladov);
        this.pravdMutacie = mutacie;

        // Vygeneruj pociatocnu populaciu
        for(int i=0; i<pocetJedincov; i++){
            populacia[i] = new Subject();
        }
    }

    public Subject proces(){

        // Vypisovanie fitness hodnot pre analyzu algoritmu
        int pocGeneracii = 0;

        while( pocGeneracii < maxPocetGeneracii) {

            int pocetPokladovGeneracii = 0;

            pocGeneracii++;

            // Ohodnot populaciu pomoou virtualneho stroja a zapis hodnoty fitness
            for(int i=0; i<pocetJedincov; i++){
                virtualMachine.run(populacia[i]);
                pocetPokladovGeneracii = Math.max(pocetPokladovGeneracii, populacia[i].getPocetNajdenychPokladov());
            }

            // Vytvorime novu generaciu, ktorou nahradime po krizeni a mutaciach tu povodnu
            Subject[] novaPopulacia = new Subject[pocetJedincov];

            // Vytvorim prioritny rad a vlozim vsetkych jedincov
            // Porovnam podla fitness hodnoty oboch jedincov
            Comparator<Subject> cmprtr = new FitnessComparator();
            PriorityQueue<Subject> jedinciFronta = new PriorityQueue<>(cmprtr);
            jedinciFronta.addAll(Arrays.asList(populacia).subList(0, pocetJedincov));


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

            List<Subject> ruleta = new LinkedList<>();
            int pocetRulety, pocetTurnaja;
            pocetRulety = pocetTurnaja = pocetNovychPotomkov / 2;


            for(int i=0; i<pocetRulety; i++){
                Subject j = jedinciFronta.remove();
                double n = j.getFitness() / maxFitness;
                int pocet = (int)(n * 100);
                for(int k=0; k<pocet; k++){
                    ruleta.add(j);
                }
            }

            for(int i=0; i<pocetRulety  ; i++){

                Subject j1 = ruleta.get(rand.nextInt(ruleta.size()));
                Subject j2 = ruleta.get(rand.nextInt(ruleta.size()));

                Subject krizenec = new Subject(j1, j2);
                novaPopulacia[i+pocElity] = krizenec;
            }

            // Selekcia rodicov pomocou metody turnaja
            // Nahodne sa vyberu 4 jedinci z populacie
            // a dvaja lokalni vitazi sa skrizia a vytvoria noveho potomka .
            for(int i=0; i<pocetTurnaja; i++){

                Subject j1 = populacia[rand.nextInt(pocetJedincov)];
                Subject j2 = populacia[rand.nextInt(pocetJedincov)];
                Subject rodic1 = suboj(j1, j2);

                Subject j3 = populacia[rand.nextInt(pocetJedincov)];
                Subject j4 = populacia[rand.nextInt(pocetJedincov)];
                Subject rodic2 = suboj(j3, j4);

                Subject krizenec = new Subject(rodic1, rodic2);
                novaPopulacia[i+pocElity+pocetRulety] = krizenec;
            }

            // Mutacie - podla pravdepodobnosti sa zmutuje x percent celej polupulacie
            // Subject mutuje tak, ze sa jedna jeho bunka nahodne nahradi inou hodnotou.
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

    private Subject suboj(Subject j1, Subject j2){
        if(j1.getFitness() >= j2.getFitness()){
            return j1;
        }
        return j2;
    }

}

// Trieda definuje nové porovnávanie v prioritnej rade, podľa atribútu fitness
// cim vacsia fitness, tým prvšia pozícia vo fronte
class FitnessComparator implements Comparator<Subject> {

    @Override
    public int compare(Subject o1, Subject o2) {
        return Integer.compare(o2.getFitness(), o1.getFitness());
    }
}