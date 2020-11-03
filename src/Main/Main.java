package Main;

public class Main {

    public static void main(String[] args) {
        NajdiRiesenie najdiRiesenie = new NajdiRiesenie();
        najdiRiesenie.najdiRiesenie(1,7,7,"1,4\n",1,3,0.15);
        TestujScenar testujScenar = new TestujScenar();
        testujScenar.testujScenar(1,7,7,"1,4\n",1,3,0.15);
    }
}
