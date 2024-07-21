package org.mps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mps.crossover.CrossoverOperator;
import org.mps.mocks.MockCrossoverOperator;
import org.mps.mocks.MockMutationOperator;
import org.mps.mocks.MockSelectionOperator;
import org.mps.mutation.MutationOperator;
import org.mps.selection.SelectionOperator;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvolutionaryAlgorithmTest {

    private EvolutionaryAlgorithm ea;
    private SelectionOperator selection;
    private MutationOperator mutation;
    private CrossoverOperator crossover;

    @BeforeEach
    void setUp() throws EvolutionaryAlgorithmException {
        selection = new MockSelectionOperator();
        mutation = new MockMutationOperator();
        crossover = new MockCrossoverOperator();
        ea = new EvolutionaryAlgorithm(selection, mutation, crossover);
    }

    @Test
    void testOptimizeWithValidOperatorsAndPopulation() throws EvolutionaryAlgorithmException {
        int[][] population = {{1, 2, 3}, {4, 5, 6}};
        int[][] optimizedPopulation = ea.optimize(population);
        assertNotNull(optimizedPopulation);
        assertEquals(optimizedPopulation.length, population.length);
    }

    @Test
    void testOptimizeWithNullPopulation() {
        Exception exception = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            ea.optimize(null);
        });
        assertEquals("Poblacion no valida", exception.getMessage());
    }

    @Test
    void testConstructorWithNullOperators() {
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            new EvolutionaryAlgorithm(null, mutation, crossover);
        });
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            new EvolutionaryAlgorithm(selection, null, crossover);
        });
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            new EvolutionaryAlgorithm(selection, mutation, null);
        });
    }

    @Test
    void testOptimizeWithEmptyPopulation() throws EvolutionaryAlgorithmException {
        int[][] population = new int[0][];
        int[][] optimizedPopulation = ea.optimize(population);
        assertNotNull(optimizedPopulation);
        assertEquals(0, optimizedPopulation.length);
    }

    @Test
    void testOptimizeNoChangesInPopulation() throws EvolutionaryAlgorithmException {
        int[][] population = {{1, 1, 1}, {1, 1, 1}};
        int[][] optimizedPopulation = ea.optimize(population);
        assertNotNull(optimizedPopulation);
        assertArrayEquals(population, optimizedPopulation);
    }

    @Test
    void testOptimizeWithSingleIndividualPopulation() throws EvolutionaryAlgorithmException {
        int[][] population = {{1, 2, 3}};
        int[][] optimizedPopulation = ea.optimize(population);
        assertNotNull(optimizedPopulation);
        assertEquals(1, optimizedPopulation.length);
    }

    @Test
    void testOptimizeWithIdenticalPopulationIntroducingVariability() throws EvolutionaryAlgorithmException {
        int[][] population = {{1, 1, 1}, {1, 1, 1}};
        when(mutation.mutate(any(int[].class))).thenAnswer(invocation -> new int[]{2, 2, 2});
        int[][] optimizedPopulation = ea.optimize(population);
        assertNotNull(optimizedPopulation);
        assertNotEquals(Arrays.deepToString(population), Arrays.deepToString(optimizedPopulation));
    }

    @Test
    void testOptimizeThrowsExceptionOnInvalidPopulationFormat() {
        int[][] population = {{1}, {1, 2}}; // Formato irregular
        assertThrows(EvolutionaryAlgorithmException.class, () -> {
            ea.optimize(population);
        });
    }
}
