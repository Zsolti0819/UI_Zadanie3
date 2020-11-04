package Main;

public class Main {

    public static void main(String[] args) {
        TestScenario testScenario = new TestScenario();
        testScenario.testujScenar(5,7,7,"1,4\n2,2\n4,1\n4,5\n6,3\n",1,3,0.15);

        FindSolution findSolution = new FindSolution();
        findSolution.najdiRiesenie(5,7,7,"1,4\n2,2\n4,1\n4,5\n6,3\n",1,3,0.15);
    }
}