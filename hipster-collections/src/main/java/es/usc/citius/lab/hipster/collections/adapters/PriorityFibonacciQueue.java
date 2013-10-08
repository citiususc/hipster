/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package es.usc.citius.lab.hipster.collections.adapters;

import es.usc.citius.lab.hipster.collections.FibonacciHeap;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * @author Pablo Rodríguez Mier
 */
public class PriorityFibonacciQueue<E> extends AbstractQueue<E> {
    private final FibonacciHeap<E> heap = new FibonacciHeap<>();
    private PriorityEvaluator<E> evaluator;

    public PriorityFibonacciQueue(PriorityEvaluator<E> evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return heap.size() > 0;
            }

            @Override
            public E next() {
                return heap.dequeueMin().getValue();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean offer(E e) {
        heap.enqueue(e, this.evaluator.getPriority(e));
        return true;
    }

    @Override
    public E poll() {
        return heap.dequeueMin().getValue();
    }

    @Override
    public E peek() {
        return heap.min().getValue();
    }
}
