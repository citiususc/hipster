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

import es.usc.citius.lab.hipster.collections.JGraphTFibonacciHeap;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * @author Pablo Rodríguez Mier
 */
public class JGraphTFibonacciQueue<E> extends AbstractQueue<E> {
    private JGraphTFibonacciHeap<E> heap = new JGraphTFibonacciHeap<>();
    private PriorityEvaluator<E> evaluator;

    public JGraphTFibonacciQueue(PriorityEvaluator<E> evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public E next() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void remove() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean offer(E e) {
        double priority = this.evaluator.getPriority(e);
        JGraphTFibonacciHeap.FibonacciHeapNode<E> node =
                new JGraphTFibonacciHeap.FibonacciHeapNode<E>(e, priority);
        heap.insert(node, priority);
        return true;
    }

    @Override
    public E poll() {
        return heap.removeMin().getData();
    }

    @Override
    public E peek() {
        return heap.min().getData();
    }
}
