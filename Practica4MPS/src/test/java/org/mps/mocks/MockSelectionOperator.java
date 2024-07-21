package org.mps.mocks;


import org.mps.selection.SelectionOperator;

public class MockSelectionOperator implements SelectionOperator {
    @Override
    public int[] select(int[] population) {
        return population;
    }
}
