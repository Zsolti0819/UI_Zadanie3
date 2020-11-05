package Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream("txt/input.txt");
        Scanner scanner = new Scanner(fileInputStream);
        int treasureCount = scanner.nextInt();
        int columns = scanner.nextInt();
        int rows = scanner.nextInt();
        int [] treasureXpositions = new int[treasureCount];
        int [] treasureYpositions = new int[treasureCount];

        scanner.nextLine();
        String treasures;
        int secondBuff = 0;
        for (int buff = 0; buff < treasureCount; buff++) {
            treasures = scanner.nextLine();
            String [] coordinates = treasures.split(",");
            treasureXpositions[secondBuff] = Integer.parseInt(coordinates[0]);
            treasureYpositions[secondBuff] = Integer.parseInt(coordinates[1]);
            secondBuff++;
        }

        int startX = scanner.nextInt();
        int startY = scanner.nextInt();
        double mutation = scanner.nextDouble();

        /*
        System.out.println("pocetPokladov:"+pocetPokladov+
                "\nmriezkaX:"+mriezkaX+
                "\nmriezkaY:"+mriezkaY);

        for (int i = 0; i < pocetPokladov; i++)
            System.out.println("pokladX:"+pokladPozX[i]+"pokladY:"+pokladPozY[i]);
        System.out.println("startX:"+startX+"\nstartY:"+startY+"\nmutacieField:"+mutacieField);
        */

        TestScenario testScenario = new TestScenario();
        testScenario.testujScenar(treasureCount,columns,rows,treasureXpositions, treasureYpositions,startX,startY,mutation);

        FindSolution findSolution = new FindSolution();
        findSolution.najdiRiesenie(treasureCount,columns,rows,treasureXpositions, treasureYpositions,startX,startY,mutation);
    }
}