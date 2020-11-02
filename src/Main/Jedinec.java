package Main;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Riso on 4/2/2017.
 */
public class Jedinec {

    // Konstanty
    public static final int pocetBuniek = 64;

    private int fitness;
    private int pocetNajdenychPokladov;
    private int pocetKrokov;
    private Random rand = new Random();
    private int[] pamat = new int[pocetBuniek];
    private Queue<Point> pohyby = new LinkedList<>();


    public Jedinec(){
        for(int i= 0;i<pocetBuniek; i++){
            pamat[i] = nahodnyByte();
        }
    }


    // Krizenie
    // Novu jedinec, ktory vznikne krizenim rodicov 1 a 2
    public Jedinec(Jedinec rodic1, Jedinec rodic2){

        // Dedenie ... nahodne sa zvoli cislo (bod) krizenia, podla ktoreho sa cast zdedi od jedneho rodica a druha od druheho
        int bodKrizenia = rand.nextInt(pocetBuniek);
        for(int i=0; i<bodKrizenia; i++){
            pamat[i] = rodic1.pamat[i];
        }
        for(int i=bodKrizenia; i<pocetBuniek; i++){
            pamat[i] = rodic2.pamat[i];
        }
    }





    // Nahodny byte z itervalu <0,255>.
    // Toto cislo sa zmesti do 8-miestneho bytu.
    public int nahodnyByte() {
        int r = rand.nextInt(256);
        return r;
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

    //
//    public void vypisJedinca(){
//        for(int i=0; i<pocetBuniek; i++){
//            System.out.println(pamat[i] & 0xFF);
//        }
//    }
//
//    public void vypisJedincaByte(){
//        for(int i=0; i<pocetBuniek; i++){
//            System.out.println(pamat[i]);
//        }
//    }

    public Jedinec klonujNovy(){
        Jedinec novy = new Jedinec();
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

    public void pridajNovyPohyb(Point p){
        pohyby.add(p);
    }

    public Point vyberPrvyPohyb(){
        return pohyby.remove();
    }

    public int getPohybySize() {
        return pohyby.size();
    }
}
