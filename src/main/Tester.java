package main;

import gamestates.CaroAIEasy;
import gamestates.CaroAIMedium;
import gamestates.CaroAIHard;
import gamestates.CaroBot;
import simulator.GameSimulator;

public class Tester {
    public static void main(String[] args) {
        CaroBot easy = new CaroAIEasy();
        CaroBot medium = new CaroAIMedium();
        CaroBot hard = new CaroAIHard();

        System.out.println("Easy vs Medium:");
        GameSimulator.playMultipleMatches(easy, medium, 100);

        System.out.println("\nMedium vs Hard:");
        GameSimulator.playMultipleMatches(medium, hard, 100);

        System.out.println("\nEasy vs Hard:");
        GameSimulator.playMultipleMatches(easy, hard, 100);
    }
}
