package algorithm;

import gamestates.GameState;
import java.util.*;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 50;
    private static final int GENERATIONS = 30;
    private static final double MUTATION_RATE = 0.2;

    public Move findBestMove(GameState gameState, char aiSymbol, char opponentSymbol) {
        int[][] intBoard = gameState.getBoard();
        int boardSize = intBoard.length;

        Population population = new Population();
        population.initialize(POPULATION_SIZE, boardSize, intBoard);

        // Evaluate initial population
        for (Chromosome c : population.chromosomes) {
            c.evaluateFitness(gameState, aiSymbol, opponentSymbol);
        }

        for (int gen = 0; gen < GENERATIONS; gen++) {
            List<Chromosome> nextGen = new ArrayList<>();

            // Tournament selection
            List<Chromosome> parents = population.tournamentSelection(POPULATION_SIZE);

            // Crossover
            for (int i = 0; i < parents.size() - 1; i += 2) {
                Chromosome p1 = parents.get(i);
                Chromosome p2 = parents.get(i + 1);

                Chromosome child = crossover(p1, p2, intBoard);
                mutate(child, boardSize, intBoard);

                child.evaluateFitness(gameState, aiSymbol, opponentSymbol);
                nextGen.add(child);
            }

            population.chromosomes = nextGen;
        }

        return population.getBest().getBestMove();
    }

    private Chromosome crossover(Chromosome p1, Chromosome p2, int[][] board) {
        Random rand = new Random();
        int x = rand.nextBoolean() ? p1.x : p2.x;
        int y = rand.nextBoolean() ? p1.y : p2.y;
        if (board[x][y] != 0) return new Chromosome(p1.x, p1.y); // fallback
        return new Chromosome(x, y);
    }

    private void mutate(Chromosome c, int boardSize, int[][] board) {
        Random rand = new Random();
        if (rand.nextDouble() < MUTATION_RATE) {
            int newX, newY;
            do {
                newX = rand.nextInt(boardSize);
                newY = rand.nextInt(boardSize);
            } while (board[newX][newY] != 0);
            c.x = newX;
            c.y = newY;
        }
    }
}
