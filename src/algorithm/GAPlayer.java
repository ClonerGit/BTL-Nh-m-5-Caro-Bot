package algorithm;

import gamestates.GameState;
import java.util.*;

public class GAPlayer {
    private static final int POPULATION_SIZE = 50;
    private static final int GENERATIONS = 100;
    private static final double MUTATION_RATE = 0.1;

    private GameState gameState;
    private char aiSymbol;
    private char opponentSymbol;

    public GAPlayer(GameState gameState, char aiSymbol) {
        this.gameState = gameState;
        this.aiSymbol = aiSymbol;
        this.opponentSymbol = (aiSymbol == 'X') ? 'O' : 'X';
    }

    public Chromosome run() {
        List<Chromosome> population = initializePopulation();

        for (int gen = 0; gen < GENERATIONS; gen++) {
            evaluateFitness(population);

            // Chọn lọc
            List<Chromosome> newPopulation = new ArrayList<>();
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Chromosome parent1 = selectParent(population);
                Chromosome parent2 = selectParent(population);
                Chromosome child = crossover(parent1, parent2);
                mutate(child);
                newPopulation.add(child);
            }

            population = newPopulation;
        }

        evaluateFitness(population);
        return getBestChromosome(population);
    }

    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        int[][] board = gameState.getBoard();
        Random rand = new Random();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            while (true) {
                int x = rand.nextInt(board.length);
                int y = rand.nextInt(board[0].length);
                if (board[x][y] == 0) {
                    population.add(new Chromosome(x, y));
                    break;
                }
            }
        }
        return population;
    }

    private void evaluateFitness(List<Chromosome> population) {
        for (Chromosome c : population) {
            c.evaluateFitness(gameState, aiSymbol, opponentSymbol);
        }
    }

    private Chromosome selectParent(List<Chromosome> population) {
        Random rand = new Random();
        Chromosome a = population.get(rand.nextInt(population.size()));
        Chromosome b = population.get(rand.nextInt(population.size()));
        return a.fitness > b.fitness ? a : b; // Tournament selection
    }

    private Chromosome crossover(Chromosome a, Chromosome b) {
        // Ở đây crossover đơn giản là chọn ngẫu nhiên một trong hai
        Random rand = new Random();
        return rand.nextBoolean() ? new Chromosome(a.x, a.y) : new Chromosome(b.x, b.y);
    }

    private void mutate(Chromosome c) {
        Random rand = new Random();
        if (rand.nextDouble() < MUTATION_RATE) {
            int[][] board = gameState.getBoard();
            while (true) {
                int x = rand.nextInt(board.length);
                int y = rand.nextInt(board[0].length);
                if (board[x][y] == 0) {
                    c.x = x;
                    c.y = y;
                    break;
                }
            }
        }
    }

    private Chromosome getBestChromosome(List<Chromosome> population) {
        return Collections.max(population, Comparator.comparingInt(c -> c.fitness));
    }
}
