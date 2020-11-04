package Main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Subject {

    // Konstanty
    public static final int pocetBuniek = 64;

    private int fitness;
    private int pocetNajdenychPokladov;
    private int pocetKrokov;
    private final Random rand = new Random();
    private int[] pamat = new int[pocetBuniek];
    private final Queue<Pozicia> pohyby = new LinkedList<>();

    public Subject(){
        for(int i= 0;i<pocetBuniek; i++){
            pamat[i] = nahodnyByte();
        }
    }

    // Krizenie
    // Novu jedinec, ktory vznikne krizenim rodicov 1 a 2
    public Subject(Subject rodic1, Subject rodic2){

        // Dedenie ... nahodne sa zvoli cislo (bod) krizenia, podla ktoreho sa cast zdedi od jedneho rodica a druha od druheho
        int bodKrizenia = rand.nextInt(pocetBuniek);
        System.arraycopy(rodic1.pamat, 0, pamat, 0, bodKrizenia);
        System.arraycopy(rodic2.pamat, bodKrizenia, pamat, bodKrizenia, pocetBuniek - bodKrizenia);
    }

    // Nahodny byte z itervalu <0,255>.
    // Toto cislo sa zmesti do 8-miestneho bytu.
    public int nahodnyByte() {
        return rand.nextInt(256);
    }

    public void mutuj(){
        this.pamat[rand.nextInt(pocetBuniek)] =  rand.nextInt(256); // max je 1111 1111 a min 0000 0000
    }

    public int getPocetNajdenychPokladov() {
        return pocetNajdenychPokladov;
    }

    public void setPocetNajdenychPokladov(int pocetNajdenychPokladov) {
        this.pocetNajdenychPokladov = pocetNajdenychPokladov;
    }

    public int getPocetKrokov() {
        return pocetKrokov;
    }

    public void setPocetKrokov(int pocetKrokov) {
        this.pocetKrokov = pocetKrokov;
    }

    public Subject klonujNovy(){
        Subject novy = new Subject();
        novy.pamat = this.pamat.clone();
        return novy;
    }

    public int getBunka(int i){
        return pamat[i];
    }

    public void setBunka(int poz, int b){
        pamat[poz] = b;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public void pridajNovyPohyb(Pozicia p){
        pohyby.add(p);
    }

    public Pozicia vyberPrvyPohyb(){
        return pohyby.remove();
    }

    public int getPohybySize() {
        return pohyby.size();
    }
}
