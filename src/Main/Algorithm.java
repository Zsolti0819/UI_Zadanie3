package Main;

import java.util.*;

public class Algorithm {

    // Konstanty
    public static final int subjectCount = 100;
    public static final int maxGenerationCount = 500;

    // Premenne
    private Subject[] population = new Subject[subjectCount];
    private final Map map;
    private final VirtualMachine virtualMachine;
    private final double probabilityOfMutation; // Pravdepobost mutaci


    public Algorithm(Map map, TreasureFinder treasureFinder, double mutations){

        this.map = map;
        this.virtualMachine = new VirtualMachine(map, treasureFinder);
        this.probabilityOfMutation = mutations;

        // Vygeneruj pociatocnu populaciu
        for(int i = 0; i< subjectCount; i++){
            population[i] = new Subject();
        }
    }

    public Subject proces(){

        int generationCount = 0;

        while( generationCount < maxGenerationCount) {

            int treasureCountForGenerations = 0;

            generationCount++;

            // Ohodnot populaciu pomoou virtualneho stroja a zapis hodnoty fitness
            for(int i = 0; i< subjectCount; i++){
                virtualMachine.run(population[i]);
                treasureCountForGenerations = Math.max(treasureCountForGenerations, population[i].getTreasuresFound());
            }

            // Vytvorime novu generaciu, ktorou nahradime po krizeni a mutaciach tu povodnu
            Subject[] novaPopulacia = new Subject[subjectCount];

            // Vytvorim prioritny rad a vlozim vsetkych jedincov
            // Porovnam podla fitness hodnoty oboch jedincov
            Comparator<Subject> fitnessComparator = new FitnessComparator();
            PriorityQueue<Subject> jedinciFronta = new PriorityQueue<>(fitnessComparator);
            jedinciFronta.addAll(Arrays.asList(population).subList(0, subjectCount));

            // Ziskam maximalnu velkost fitness funkcie
            assert jedinciFronta.peek() != null;
            double maxFitness = jedinciFronta.peek().getFitness();

            // Elitarizmus - najlepsich 10% jedincov (podla fitness) sa automaticky naklonuje do novej populacie
            int pocElity = subjectCount / 10;
            for(int i=0; i<pocElity; i++){

                novaPopulacia[i] = jedinciFronta.remove();

                // Ak sme nasli vsetky poklady, vratime jedinca a skonci while
                if(treasureCountForGenerations == map.getTreasureCount()) {
                    return novaPopulacia[i];
                }
            }


            // Krizenie
            int pocetNovychPotomkov = (subjectCount / 10 ) * 9; // 90% novej populacie

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

                Subject j1 = population[rand.nextInt(subjectCount)];
                Subject j2 = population[rand.nextInt(subjectCount)];
                Subject rodic1 = suboj(j1, j2);

                Subject j3 = population[rand.nextInt(subjectCount)];
                Subject j4 = population[rand.nextInt(subjectCount)];
                Subject rodic2 = suboj(j3, j4);

                Subject krizenec = new Subject(rodic1, rodic2);
                novaPopulacia[i+pocElity+pocetRulety] = krizenec;
            }


            // Mutacie - podla pravdepodobnosti sa zmutuje x percent celej polupulacie
            // Subject mutuje tak, ze sa jedna jeho bunka nahodne nahradi inou hodnotou.
            for(int i = 0; i< subjectCount; i++){

                // Pravdepobnost medzi 0.0 az 1.0, ktore vracia newxtDouble()
                double pravd = rand.nextDouble();
                if(pravd <= probabilityOfMutation){
                    novaPopulacia[i].mutate();
                }
            }

            // Nahrad povodnu populaciu novou
            population = novaPopulacia;
        }

        // Vrati najuspesnejsieho jedinca ak sme nenasli vsetky poklady
        return population[0];

    }

    private Subject suboj(Subject j1, Subject j2){
        if(j1.getFitness() >= j2.getFitness()){
            return j1;
        }
        return j2;
    }
}

