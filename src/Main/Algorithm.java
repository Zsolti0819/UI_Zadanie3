package Main;

import java.util.*;

public class Algorithm {

    public static final int subjectCount = 100;
    private Subject[] population = new Subject[subjectCount];
    private final Map map;
    private final VirtualMachine virtualMachine;
    private final double probabilityOfMutation;

    public Algorithm(Map map, TreasureFinder treasureFinder, double mutations){

        this.map = map;
        this.virtualMachine = new VirtualMachine(map, treasureFinder);
        this.probabilityOfMutation = mutations;
        generatePopulation();
    }

    public void generatePopulation() {
        int buffer = 0;
        do {
            population[buffer] = new Subject();
            buffer++;
        } while (buffer < subjectCount);
    }

    public Subject proces(int maxGenerationCount) throws CloneNotSupportedException {

        int generationCount = 0;

        do {
            generationCount++;
            int treasuresFoundByGeneration = 0;
            // Ohodnot populaciu pomoou virtualneho stroja a zapis hodnoty fitness
            int buffer = 0;
            while (buffer < subjectCount) {
                virtualMachine.run(population[buffer]);
                treasuresFoundByGeneration = Math.max(treasuresFoundByGeneration, population[buffer].getTreasuresFound());
                buffer++;
            }

            // Vytvorime novu generaciu, ktorou nahradime po krizeni a mutaciach tu povodnu
            Subject[] newPopulation = new Subject[subjectCount];

            // Vytvorim prioritny rad a vlozim vsetkych jedincov
            // Porovnam podla fitness hodnoty oboch jedincov
            Comparator<Subject> fitnessComparator = new FitnessComparator();
            PriorityQueue<Subject> jedinciFronta = new PriorityQueue<>(fitnessComparator);
            jedinciFronta.addAll(Arrays.asList(population).subList(0, subjectCount));

            // Ziskam maximalnu velkost fitness funkcie
            assert jedinciFronta.peek() != null;
            double maxFitness = jedinciFronta.peek().getFitness();

            // Elitarizmus - najlepsich 10% jedincov (podla fitness) sa automaticky naklonuje do novej populacie
            int eliteSubjectsCount = subjectCount / 10;
            for (buffer=0; buffer < eliteSubjectsCount; buffer++) {

                newPopulation[buffer] = jedinciFronta.remove();

                // Ak sme nasli vsetky poklady, vratime jedinca a skonci while
                if(treasuresFoundByGeneration == map.getTreasureCount()) {
                    return newPopulation[buffer];
                }
            }

            // Krizenie
            int newSubjectsCount = (subjectCount / 10 ) * 9; // 90% novej populacie

            Random rand = new Random();

            // Selekcia rodicov pomocou metody rulety

            List<Subject> ruleta = new LinkedList<>();
            int rouletteCount, tournamentCount;
            rouletteCount = tournamentCount = newSubjectsCount / 2;

            roulette(newPopulation, jedinciFronta, maxFitness, eliteSubjectsCount, rand, ruleta, rouletteCount);

            tournament(newPopulation, eliteSubjectsCount, population, rouletteCount, tournamentCount);

            mutate(newPopulation, rand);

            population = newPopulation; // Nahradíme pôvodnú populáciu novou
        } while(generationCount < maxGenerationCount);

        return population[0]; // Ak sme nenašli všetky poklady, tak vráti najúspešnejšieho jedinca

    }

    public void roulette(Subject[] newPopulation, PriorityQueue<Subject> jedinciFronta, double maxFitness, int eliteSubjectsCount, Random rand, List<Subject> ruleta, int rouletteCount) {
        for(int i = 0; i < rouletteCount; i++){
            Subject j = jedinciFronta.remove();
            double n = j.getFitness() / maxFitness;
            int pocet = (int)(n * 100);
            for(int k=0; k<pocet; k++){
                ruleta.add(j);
            }
        }

        for(int i = 0; i < rouletteCount; i++){

            Subject j1 = ruleta.get(rand.nextInt(ruleta.size()));
            Subject j2 = ruleta.get(rand.nextInt(ruleta.size()));

            Subject krizenec = new Subject(j1, j2);
            newPopulation[i+ eliteSubjectsCount] = krizenec;
        }
    }

    public void mutate(Subject[] newPopulation, Random rand) {
        // Mutacie - podla pravdepodobnosti sa zmutuje x percent celej polupulacie
        // Subject mutuje tak, ze sa jedna jeho bunka nahodne nahradi inou hodnotou.
        for(int i = 0; i< subjectCount; i++){

            // Pravdepobnost medzi 0.0 az 1.0, ktore vracia newxtDouble()
            double probability = rand.nextDouble();
            if(probability <= probabilityOfMutation){
                newPopulation[i].mutate();
            }
        }
    }

    public void tournament(Subject[] newPopulation, int eliteSubjectsCount, Subject [] population, int numberOfRoulettes, int numberOfTournaments) {
        // Selekcia rodicov pomocou metody turnaja
        // Nahodne sa vyberu 4 jedinci z populacie
        // a dvaja lokalni vitazi sa skrizia a vytvoria noveho potomka .
        int i = 0;
        while (i < numberOfTournaments) {
            Random rand = new Random();
            Subject j1 = population[rand.nextInt(subjectCount)];
            Subject j2 = population[rand.nextInt(subjectCount)];
            Subject rodic1 = duel(j1, j2);

            Subject j3 = population[rand.nextInt(subjectCount)];
            Subject j4 = population[rand.nextInt(subjectCount)];
            Subject rodic2 = duel(j3, j4);

            Subject krizenec = new Subject(rodic1, rodic2);
            newPopulation[i+eliteSubjectsCount+numberOfRoulettes] = krizenec;
            i++;
        }
    }

    private Subject duel (Subject j1, Subject j2){
        if(j1.getFitness() >= j2.getFitness()){
            return j1;
        }
        return j2;
    }
}

