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

        GameSimulator.playMatch(easy, medium);
        GameSimulator.playMatch(medium, hard);
        GameSimulator.playMatch(easy, hard);
    }
}
