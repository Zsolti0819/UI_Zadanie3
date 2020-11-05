package Main;

import java.util.Comparator;

// Trieda definuje nové porovnávanie v prioritnej rade, podľa atribútu fitness
// cim vacsia fitness, tým prvšia pozícia vo fronte
public class FitnessComparator implements Comparator<Subject> {

    @Override
    public int compare(Subject o1, Subject o2) {
        return Integer.compare(o2.getFitness(), o1.getFitness());
    }
}
