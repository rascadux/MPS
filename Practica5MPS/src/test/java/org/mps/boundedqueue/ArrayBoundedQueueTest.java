package org.mps.boundedqueue;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

/**
 * Pruebas para la clase ArrayBoundedQueue utilizando AssertJ para asegurar que las operaciones de la cola
 * se comporten como se espera. Estas pruebas verifican la funcionalidad básica de la cola acotada,
 * incluyendo la inicialización de la cola, la inserción y eliminación de elementos, y el manejo de excepciones
 * para operaciones que exceden la capacidad de la cola o intentan operar sobre una cola vacía.
 * También se incluyen pruebas para verificar la funcionalidad del buffer circular, el manejo de operaciones mixtas,
 * y el comportamiento del iterador y la verificación de estados internos del array.
 */

public class ArrayBoundedQueueTest {

    private ArrayBoundedQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayBoundedQueue<>(5);
    }

    @Test
    void newQueueCheckedIsEmptyAndSizeIsZero() {
        assertThat(queue.isEmpty()).isTrue();
        assertThat(queue.size()).isZero();
    }

    @Test
    void newQueueElementPutQueueNotEmptyAndSizeIncreases() {
        queue.put(10);
        assertThat(queue.isEmpty()).isFalse();
        assertThat(queue.size()).isEqualTo(1);
        assertThat(queue).contains(10);
    }

    @Test
    void queueWithElementGetCalledElementIsRemovedAndQueueIsEmpty() {
        queue.put(20);
        Integer element = queue.get();
        assertThat(element).isEqualTo(20);
        assertThat(queue.isEmpty()).isTrue();
        assertThat(queue.size()).isZero();
    }

    @Test
    void fullQueuePutCalledThrowsFullBoundedQueueException() {
        for (int i = 0; i < 5; i++) {
            queue.put(i);
        }
        assertThat(queue.isFull()).isTrue();
        assertThatThrownBy(() -> queue.put(6))
                .isInstanceOf(FullBoundedQueueException.class)
                .hasMessageContaining("put: full bounded queue");
    }

    @Test
    void emptyQueueGetCalledThrowsEmptyBoundedQueueException() {
        assertThat(queue.isEmpty()).isTrue();
        assertThatThrownBy(() -> queue.get())
                .isInstanceOf(EmptyBoundedQueueException.class)
                .hasMessageContaining("get: empty bounded queue");
    }

    @Test
    void fullQueueElementRemovedAndNewPutWrapsAround() {
        for (int i = 0; i < 5; i++) {
            queue.put(i);
        }
        assertThat(queue.isFull()).isTrue();
        queue.get();
        queue.put(5);
        assertThat(queue).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void queueWithElementsMixedOperationsPerformedCorrectOrderMaintained() {
        queue.put(1);
        queue.put(2);
        queue.get();
        queue.put(3);
        queue.get();
        queue.put(4);
        assertThat(queue).containsExactly(3, 4);
    }

    @Test
    void queueNullPutThrowsIllegalArgumentException() {
        queue.put(1);
        queue.put(2);
        assertThatThrownBy(() -> queue.put(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("put: element cannot be null");
    }

    @Test
    void queueWithElementsIteratorUsedElementsIteratedCorrectly() {
        queue.put(1);
        queue.put(2);
        queue.put(3);
        assertThat(queue).containsExactly(1, 2, 3);

        Iterator<Integer> it = queue.iterator();
        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(1);
        it.next();
        assertThat(it.next()).isEqualTo(3);
    }
}
