package Main;

import java.util.Comparator;

public class FitnessComparator implements Comparator<Subject> {

    @Override
    public int compare(Subject subject1, Subject subject2) {
        return Integer.compare(subject2.getFitness(), subject1.getFitness());
    }
}
