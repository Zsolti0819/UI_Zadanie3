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
            PriorityQueue<Subject> frontSubjects = new PriorityQueue<>(fitnessComparator);
            frontSubjects.addAll(Arrays.asList(population).subList(0, subjectCount));

            int eliteSubjectsCount = subjectCount / 10;
            int newSubjectsCount = (subjectCount / 10 ) * 9; // 90% novej populacie

            // Funkcia vráti index jedinca, ktorý našiel všetky podklady
            // Ak nebol taký jedinec, funkcia vrátí -1
            int allTreasuresFound = elitism(eliteSubjectsCount, newSubjectsCount, newPopulation, frontSubjects, treasuresFoundByGeneration);
            if (allTreasuresFound != -1)
                return newPopulation[allTreasuresFound]; // Skončíme cyklus, lebo našli sme jedinca, ktorý našiel všetky poklady

            int test = newSubjectsCount/2;

            // rankSelection(generationCount, newPopulation, eliteSubjectsCount, test);

            tournament(newPopulation, eliteSubjectsCount, population, test);

            mutate(newPopulation);

            population = newPopulation; // Nahradíme pôvodnú populáciu novou

        } while (generationCount < maxGenerationCount);

        return population[0]; // Ak sme nenašli všetky poklady, tak vráti najúspešnejšieho jedinca

    }

    public int elitism (int eliteSubjectsCount, int newSubjectsCount, Subject [] newPopulation, PriorityQueue<Subject>frontSubjects, int treasuresFoundByGeneration) {
        // Elitarizmus - najlepsich 10% jedincov (podla fitness) sa automaticky naklonuje do novej populacie

        for (int buffer=0; buffer < eliteSubjectsCount; buffer++) {
            newPopulation[buffer] = frontSubjects.remove();

            if(treasuresFoundByGeneration == map.getTreasureCount()) // Ak sme nasli vsetky poklady, vratime jedinca a skonci while
                return buffer;
        }

        for (int buffer = 0; buffer < newSubjectsCount; buffer++) {
            Subject subject = frontSubjects.remove();
            newPopulation[buffer+ eliteSubjectsCount] = subject;
        }
        return -1;
    }

    public void rankSelection(int generationCount, Subject[] newPopulation, int eliteSubjectsCount, int newSubjectsCount) {
        int i, j;

        for (i = 0; i < newSubjectsCount -1; i++) {
            for (j = 0; j < newSubjectsCount -i-1; j++) {
                if (newPopulation[j].getFitness() > newPopulation[j+1].getFitness()) {
                    int temp = newPopulation[j].getFitness();
                    newPopulation[j].setFitness(newPopulation[j+1].getFitness());
                    newPopulation[j+1].setFitness(temp);
                }
            }
        }

        for (i = 0; i < newSubjectsCount; i++)
            newPopulation[i].setRank(i+1);

        for (i = 0; i < newSubjectsCount/2; i++) {
            System.out.println("" + generationCount + ". generacia, " + (i + 1) + ". jedinec, rank:" + newPopulation[i].getRank() + ", fitness: " + newPopulation[i].getFitness());
        }

        List<Subject> roulette = new LinkedList<Subject>();
        Random random = new Random();

        for (i = 1; i <= newSubjectsCount; i++)
        {
            int probablityTwo = (i / GaussFormula(newSubjectsCount))*100;
            for (j = 0; j < probablityTwo; j++)
                roulette.add(newPopulation[i]);
        }

        i = 0;
        while (i < newSubjectsCount) {
            i++;
            Subject t1 = roulette.get(random.nextInt(roulette.size()));
            Subject t2 = roulette.get(random.nextInt(roulette.size()));
            Subject hybrid = new Subject(t1, t2);
            newPopulation[i+eliteSubjectsCount] = hybrid;
        }
    }

    int GaussFormula (int newSubjectsCount) {
        return ((newSubjectsCount+1) * (newSubjectsCount/2));
    }

    public void tournament(Subject[] newPopulation, int eliteSubjectsCount, Subject[] population, int newSubjectsCount) {
        // Selekcia rodicov pomocou metody turnaja
        // Nahodne sa vyberu 4 jedinci z populacie
        // a dvaja lokalni vitazi sa skrizia a vytvoria noveho potomka .
        int buffer = 0;
        while (buffer < newSubjectsCount) {
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
        // Mutacie - podla pravdepodobnosti sa zmutuje x percent celej polupulacie
        // Subject mutuje tak, ze sa jedna jeho bunka nahodne nahradi inou hodnotou.
        Random rand = new Random();
        for(int i = 0; i< subjectCount; i++){

            // Pravdepobnost medzi 0.0 az 1.0, ktore vracia newxtDouble()
            double probability = rand.nextDouble();
            if(probability <= probabilityOfMutation){
                newPopulation[i].mutate();
            }
        }
    }

    private Subject duel (Subject j1, Subject j2) {
        if(j1.getFitness() >= j2.getFitness()){
            return j1;
        }
        return j2;
    }
}

