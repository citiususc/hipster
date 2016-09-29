/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
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

package es.usc.citius.lab.hipster.collections.adapter;

import es.usc.citius.lab.hipster.collections.FibonacciHeap;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Implementation of {@link java.util.Queue} based on the Fibonacci heap
 * concept, described in http://en.wikipedia.org/wiki/Fibonacci_heap.
 *
 * @param <N> type of the nodes
 *
 * @author Pablo Rodríguez Mier
 */
public class PriorityFibonacciQueue<N> extends AbstractQueue<N> {
    private final FibonacciHeap<N> heap = new FibonacciHeap<N>();
    private PriorityEvaluator<N> evaluator;

    public PriorityFibonacciQueue(PriorityEvaluator<N> evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public Iterator<N> iterator() {
        return new Iterator<N>() {
            @Override
            public boolean hasNext() {
                return heap.size() > 0;
            }

            @Override
            public N next() {
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
    public boolean offer(N n) {
        heap.enqueue(n, this.evaluator.getPriority(n));
        return true;
    }

    @Override
    public N poll() {
        return heap.dequeueMin().getValue();
    }

    @Override
    public N peek() {
        return heap.min().getValue();
    }
}
