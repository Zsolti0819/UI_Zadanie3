package Main;

import java.util.*;

public class Algorithm {

    public static final int subjectCount = 100; // Každá generácia má 100 jedincov
    private Subject[] population = new Subject[subjectCount];
    private final Map map;
    private final VirtualMachine virtualMachine;
    private final double probabilityOfMutation;

    public Algorithm(Map map, TreasureFinder treasureFinder, double mutations) {
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
            int buffer = 0;

            // Ohodnot populaciu pomoou virtualneho stroja a zapis hodnoty fitness
            while (buffer < subjectCount) {
                virtualMachine.run(population[buffer]);
                treasuresFoundByGeneration = Math.max(treasuresFoundByGeneration, population[buffer].getTreasuresFound());
                buffer++;
            }

            Subject[] newPopulation = new Subject[subjectCount];

            // Vytvorenie prioritného radu, do ktorého vložím všetkých jedincov
            // Porovnam ich podľa fitness hodnôt
            Comparator<Subject> fitnessComparator = new FitnessComparator();
            PriorityQueue<Subject> frontSubjects = new PriorityQueue<>(fitnessComparator);
            frontSubjects.addAll(Arrays.asList(population).subList(0, subjectCount));

            int eliteSubjectsCount = subjectCount / 5; // Z najlepších 20% stane elitov
            int newSubjectsCount = (subjectCount / 10 ) * 8; // Zvyšných 80% percent
            Subject[] bufferPopulation = new Subject[newSubjectsCount]; // Pomocné pole pre vytvorenie novej generácií

            // Funkcia elitism() vráti index jedinca, ktorý našiel všetky podklady
            // Ak nebol taký jedinec, funkcia vrátí -1
            int richestSubjectIndex = elitism(eliteSubjectsCount, newSubjectsCount, newPopulation, bufferPopulation, frontSubjects, treasuresFoundByGeneration);
            if (richestSubjectIndex != -1)
                return newPopulation[richestSubjectIndex]; // Skončíme cyklus, lebo našli sme jedinca, ktorý našiel všetky poklady

            int rsSubjectsCount = (newSubjectsCount / 4); // 20 jedincov vyberám pomocou rank selection
            int tSubjectsCount = (newSubjectsCount / 4)*3; // 60 jedincov ide na turnaj

            tournament(newPopulation, eliteSubjectsCount, population, tSubjectsCount);

            rankSelection(newPopulation, bufferPopulation, eliteSubjectsCount, tSubjectsCount, rsSubjectsCount);

            mutate(newPopulation);

            population = newPopulation; // Nahradíme pôvodnú populáciu novou

        } while (generationCount < maxGenerationCount);

        return population[0]; // Ak sme nenašli všetky poklady, tak vráti najúspešnejšieho jedinca

    }

    public int elitism (int eliteSubjectsCount, int newSubjectsCount, Subject [] newPopulation, Subject [] bufferPopulation, PriorityQueue<Subject>frontSubjects, int treasuresFoundByGeneration) {
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

    public void rankSelection(Subject[] newPopulation, Subject[] bufferPopulation, int eliteSubjectsCount, int tSubjectsCount, int rsSubjectsCount) {
        // Pomocou bubble sortu zoradíme pomocné pole jedincov
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

        // Nastavíme hodnosť pre všetky jedince (najhorší bude mať hodnosť 1)
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
            Subject parent1 = roulette.get(random.nextInt(roulette.size()));
            Subject parent2 = roulette.get(random.nextInt(roulette.size()));
            Subject hybrid = new Subject(parent1, parent2);
            newPopulation[i+eliteSubjectsCount+tSubjectsCount] = hybrid;
        }
    }

    public void tournament(Subject[] newPopulation, int eliteSubjectsCount, Subject[] population, int tSubjectsCount) {
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
            newPopulation[buffer+eliteSubjectsCount] = hybrid;
            buffer++;
        }
    }

    public void mutate(Subject[] newPopulation) {
        // Podľa pravdepodobnosti sa zmutuje x percent celej populácie
        // Subject mutuje tak, že sa jedna jeho bunka náhodne nahradí inou hodnotou.
        Random rand = new Random();
        for(int i = 0; i < subjectCount; i++) {
            double probability = rand.nextDouble();
            if(probability <= probabilityOfMutation)
                newPopulation[i].mutate();
        }
    }

    private Subject duel (Subject subject1, Subject subject2) {
        if(subject1.getFitness() >= subject2.getFitness())
            return subject1;
        return subject2;
    }

    int GaussFormula (int newSubjectsCount) {
        return ((newSubjectsCount+1) * (newSubjectsCount/2));
    }
}

