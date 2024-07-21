package org.mps.mocks;

import org.mps.crossover.CrossoverOperator;

public class MockCrossoverOperator implements CrossoverOperator {
    @Override
    public int[][] crossover(int[] parent1, int[] parent2) {
        // Devuelve los progenitores sin cambios
        return new int[][]{parent1, parent2};
    }
}
