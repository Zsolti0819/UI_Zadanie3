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

    // Vytvorenie populácie
    public void generatePopulation() {
        int buffer = 0;
        do {
            population[buffer] = new Subject();
            buffer++;
        } while (buffer < subjectCount);
    }

    public Subject converge (int maxGenerationCount) throws CloneNotSupportedException {
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
            PriorityQueue<Subject> frontSubjects = new PriorityQueue<>(fitnessComparator);
            frontSubjects.addAll(Arrays.asList(population).subList(0, subjectCount));

            int eliteSubjectsCount = subjectCount / 5;
            int newSubjectsCount = (subjectCount / 10 ) * 8; // 80% novej populacie
            Subject[] bufferPopulation = new Subject[newSubjectsCount];

            // Funkcia vráti index jedinca, ktorý našiel všetky podklady
            // Ak nebol taký jedinec, funkcia vrátí -1
            int richestSubjectIndex = elitism(eliteSubjectsCount, newSubjectsCount, newPopulation, bufferPopulation, frontSubjects, treasuresFoundByGeneration);
            if (richestSubjectIndex != -1)
                return newPopulation[richestSubjectIndex]; // Skončíme cyklus, lebo našli sme jedinca, ktorý našiel všetky poklady

            int rsSubjectsCount = (newSubjectsCount / 4) * 3;
            int tSubjectsCount = (newSubjectsCount / 4);

            rankSelection(newPopulation, bufferPopulation, eliteSubjectsCount, rsSubjectsCount);

            tournament(newPopulation, eliteSubjectsCount, population, tSubjectsCount, rsSubjectsCount);

            mutate(newPopulation);

            population = newPopulation; // Nahradíme pôvodnú populáciu novou

        } while (generationCount < maxGenerationCount);

        return population[0]; // Ak sme nenašli všetky poklady, tak vráti najúspešnejšieho jedinca

    }

    public int elitism (int eliteSubjectsCount, int newSubjectsCount, Subject [] newPopulation, Subject [] bufferPopulation, PriorityQueue<Subject>frontSubjects, int treasuresFoundByGeneration) {
        // Elitarizmus - najlepsich 20% jedincov (podla fitness) sa automaticky naklonuje do novej populacie
        for (int buffer = 0; buffer < eliteSubjectsCount; buffer++) {
            newPopulation[buffer] = frontSubjects.remove();

            if(treasuresFoundByGeneration == map.getTreasureCount()) // Ak sme našli všetky poklady, tak vráti index jedinca
                return buffer;
        }

        for (int buffer = 0; buffer < newSubjectsCount; buffer++) {
            Subject subject = frontSubjects.remove();
            bufferPopulation[buffer] = subject;
        }
        return -1;
    }

    public void rankSelection(Subject[] newPopulation, Subject[] bufferPopulation, int eliteSubjectsCount, int rsSubjectsCount) {
        // Pomocou bubble sortu zoradíme pole jedincov
        // Jedinec s najnižšou fitness hodnotou je na 0. pozícií
        int i, j;
        for (i = 0; i < rsSubjectsCount - 1; i++) {
            for (j = 0; j < rsSubjectsCount - i - 1; j++) {
                if (bufferPopulation[j].getFitness() > bufferPopulation[j + 1].getFitness()) {
                    int temp = bufferPopulation[j].getFitness();
                    bufferPopulation[j].setFitness(bufferPopulation[j + 1].getFitness());
                    bufferPopulation[j + 1].setFitness(temp);
                }
            }
        }

        // Nastavíme hodnosť pre všetky jedince
        for (i = 0; i < rsSubjectsCount; i++) {
            bufferPopulation[i].setRank(i + 1);
        }


        // Podľa Gaussovej formuly vypočítame pravdepodobnosť výskytu jedinca
        // Čím väčšia hodnosť má jedinec, tým väčšiu pravdepodobnosť má na to, aby sme ho vybrali
        List<Subject> roulette = new LinkedList<>();
        int probability;
        for (i = 0; i < rsSubjectsCount; i++) {
            probability = (bufferPopulation[i].getRank() * 1000) / GaussFormula(rsSubjectsCount);
            // System.out.println("" + (i) + ". jedinec, hodnost: " + bufferPopulation[i].getRank() + ", fitness: " + bufferPopulation[i].getFitness() + ", pravdepodobnost vyskytu: " + probability);
            for (j = 0; j < probability; j++)
                roulette.add(bufferPopulation[i]);
        }

        // Kríženie
        Random random = new Random();
        for (i = 0; i < rsSubjectsCount; i++) {
            Subject t1 = roulette.get(random.nextInt(roulette.size()));
            Subject t2 = roulette.get(random.nextInt(roulette.size()));
            Subject hybrid = new Subject(t1, t2);
            newPopulation[i+eliteSubjectsCount] = hybrid;
        }
    }

    public void tournament(Subject[] newPopulation, int eliteSubjectsCount, Subject[] population, int tSubjectsCount, int rsSubjectsCount) {
        // Selekcia rodicov pomocou metody turnaja
        // Nahodne sa vyberu 4 jedinci z populacie
        // a dvaja lokalni vitazi sa skrizia a vytvoria noveho potomka .
        int buffer = 0;
        while (buffer < tSubjectsCount) {
            Random rand = new Random();
            Subject subject1 = population[rand.nextInt(subjectCount)];
            Subject subject2 = population[rand.nextInt(subjectCount)];
            Subject parent1 = duel(subject1, subject2);

            Subject subject3 = population[rand.nextInt(subjectCount)];
            Subject subject4 = population[rand.nextInt(subjectCount)];
            Subject parent2 = duel(subject3, subject4);

            Subject hybrid = new Subject(parent1, parent2);
            newPopulation[buffer+eliteSubjectsCount+rsSubjectsCount] = hybrid;
            buffer++;
        }
    }

    public void mutate(Subject[] newPopulation) {
        // Mutacie - podla pravdepodobnosti sa zmutuje x percent celej polupulacie
        // Subject mutuje tak, ze sa jedna jeho bunka nahodne nahradi inou hodnotou.
        Random rand = new Random();
        for(int i = 0; i < subjectCount; i++) {
            double probability = rand.nextDouble();
            if(probability <= probabilityOfMutation)
                newPopulation[i].mutate();
        }
    }

    private Subject duel (Subject j1, Subject j2) {
        if(j1.getFitness() >= j2.getFitness())
            return j1;
        return j2;
    }

    int GaussFormula (int newSubjectsCount) {
        return ((newSubjectsCount+1) * (newSubjectsCount/2));
    }
}

