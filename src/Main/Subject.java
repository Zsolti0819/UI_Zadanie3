package Main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Subject {

    // Konstanty
    public static final int numberOfCells = 64;

    private int fitness;
    private int treasuresFound;
    private int stepsCount;
    private final Random rand = new Random();
    private int[] memory = new int[numberOfCells];
    private final Queue<Pozicia> moves = new LinkedList<>();

    public Subject(){
        for(int i = 0; i< numberOfCells; i++){
            memory[i] = randomByte();
        }
    }

    // Krizenie
    // Novu jedinec, ktory vznikne krizenim rodicov 1 a 2
    public Subject(Subject rodic1, Subject rodic2){

        // Dedenie ... nahodne sa zvoli cislo (bod) krizenia, podla ktoreho sa cast zdedi od jedneho rodica a druha od druheho
        int bodKrizenia = rand.nextInt(numberOfCells);
        System.arraycopy(rodic1.memory, 0, memory, 0, bodKrizenia);
        System.arraycopy(rodic2.memory, bodKrizenia, memory, bodKrizenia, numberOfCells - bodKrizenia);
    }

    // Nahodny byte z itervalu <0,255>.
    // Toto cislo sa zmesti do 8-miestneho bytu.
    public int randomByte() {
        return rand.nextInt(256);
    }

    public void mutate(){
        this.memory[rand.nextInt(numberOfCells)] =  rand.nextInt(256); // max je 1111 1111 a min 0000 0000
    }

    public int getTreasuresFound() {
        return treasuresFound;
    }

    public void setTreasuresFound(int treasuresFound) {
        this.treasuresFound = treasuresFound;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }

    public Subject cloneNew(){
        Subject newSubject = new Subject();
        newSubject.memory = this.memory.clone();
        return newSubject;
    }

    public int getBunka(int i){
        return memory[i];
    }

    public void setBunka(int poz, int b){
        memory[poz] = b;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public void pridajNovyPohyb(Pozicia p){
        moves.add(p);
    }

    public Pozicia vyberPrvyPohyb(){
        return moves.remove();
    }

    public int getPohybySize() {
        return moves.size();
    }
}