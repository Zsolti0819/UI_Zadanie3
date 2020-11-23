package Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {

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
        int maxGenerationCount = scanner.nextInt();
        double mutation = scanner.nextDouble();

        // Pomocný výpis
        /*
        for (int i = 0; i < treasureCount; i++) {
            System.out.println(""+(i+1)+".treasure:");
            System.out.println("X:"+treasureXpositions[i]+" Y:"+treasureYpositions[i]+"\n");
        }
        System.out.println("Starting position\nX:"+startX+" Y:"+startY+"\nMutation:"+mutation+"\n");
         */

        Solve solve = new Solve();
        solve.benchmark(treasureCount, columns, rows, treasureXpositions, treasureYpositions, startX, startY, maxGenerationCount, mutation);
        solve.findSolution(treasureCount, columns, rows, treasureXpositions, treasureYpositions, startX, startY, maxGenerationCount, mutation);
    }
}