package Main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Subject {

    // Konstanty
    public static final int numberOfCells = 64;

    private int fitness;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private int rank;
    private int treasuresFound;
    private int stepCount;
    private final Random rand = new Random();
    private int[] memory = new int[numberOfCells];
    private final Queue<Position> moves = new LinkedList<>();

    public Subject(){
        for(int i = 0; i< numberOfCells; i++)
            memory[i] = randomByte();
    }

    // Kríženie
    public Subject(Subject parent1, Subject parent2){

        // Náhodne sa zvoli bod kríženia, podľa ktorého sa časť zdedí od jedného rodiča a druhá od druhého
        int crossingPoint = rand.nextInt(numberOfCells);
        System.arraycopy(parent1.memory, 0, memory, 0, crossingPoint);
        System.arraycopy(parent2.memory, crossingPoint, memory, crossingPoint, numberOfCells - crossingPoint);
    }

    public int randomByte() {
        return rand.nextInt(256);
    }

    public void mutate() {
        this.memory[rand.nextInt(numberOfCells)] =  rand.nextInt(256); // max je 1111 1111 a min 0000 0000
    }

    public int getTreasuresFound() {
        return treasuresFound;
    }

    public void setTreasuresFound(int treasuresFound) {
        this.treasuresFound = treasuresFound;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public Subject cloneNew() {
        Subject newSubject = new Subject();
        newSubject.memory = this.memory.clone();
        return newSubject;
    }

    public int getCell(int i){
        return memory[i];
    }

    public void setCell(int poz, int b){
        memory[poz] = b;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public void addNewMove(Position p){
        moves.add(p);
    }

    public Position removeFirstMove(){
        return moves.remove();
    }

    public int getMovesSize() {
        return moves.size();
    }
}
