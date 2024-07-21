package org.mps.mocks;

import org.mps.mutation.MutationOperator;

public class MockMutationOperator implements MutationOperator {
    @Override
    public int[] mutate(int[] offspring) {
        // No realiza cambios para simplificar
        return offspring;
    }
}
