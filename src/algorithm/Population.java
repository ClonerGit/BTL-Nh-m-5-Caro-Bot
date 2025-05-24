package algorithm;

import java.util.*;

public class Population {
    public List<Chromosome> chromosomes;

    public Population() {
        chromosomes = new ArrayList<>();
    }

    public void initialize(int size, int boardSize, int[][] board) {
        Random rand = new Random();
        while (chromosomes.size() < size) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);
            if (board[x][y] == 0) {
                chromosomes.add(new Chromosome(x, y));
            }
        }
    }

    public Chromosome getBest() {
        return Collections.max(chromosomes, Comparator.comparingInt(c -> c.fitness));
    }

    public List<Chromosome> tournamentSelection(int k) {
        List<Chromosome> selected = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            Chromosome a = chromosomes.get(rand.nextInt(chromosomes.size()));
            Chromosome b = chromosomes.get(rand.nextInt(chromosomes.size()));
            selected.add((a.fitness > b.fitness) ? a : b);
        }
        return selected;
    }
}
