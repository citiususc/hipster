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

package es.usc.citius.lab.hipster.algorithm.factory;

import es.usc.citius.lab.hipster.algorithm.DepthFirstSearch;
import es.usc.citius.lab.hipster.algorithm.problem.SearchProblem;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.impl.SimpleNode;

import java.util.Iterator;

/**
 * Implementation of {@link es.usc.citius.lab.hipster.algorithm.factory.AlgorithmIteratorFactory}
 * to obtain iterators of {@link es.usc.citius.lab.hipster.algorithm.BreadthFirstSearch}. As the algorithm
 * does not involve cost operations only requires the basic definition of the search problem.
 * 
 * @param <S> class defining the state
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @since 0.1.0
 */
public class BreadthFirstSearchIteratorFactory<S> implements AlgorithmIteratorFactory<S,Node<S>> {
    private SearchProblem<S> problem;

    /**
     * Default constructor for the Breadth First Search factory. Only the definition of the problem is
     * needed to instantiate the iterator.
     * 
     * @param problem search problem definition
     */
    public BreadthFirstSearchIteratorFactory(SearchProblem<S> problem){
        this.problem = problem;
    }

    @Override
    public Iterator<Node<S>> create() {
        return new DepthFirstSearch<S>(problem.getInitialState(), problem.getTransitionFunction(), new NodeFactory<S, Node<S>>() {
            @Override
            public Node<S> node(Node<S> from, Transition<S> transition) {
                return new SimpleNode<S>(transition, from);
            }
        });
    }
}
