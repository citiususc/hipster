/*
* Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package es.usc.citius.hipster.model.node.impl;

import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.ScalarOperation;
import es.usc.citius.hipster.model.node.ADStarNode;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.node.factory.NodeFactory;
import es.usc.citius.hipster.model.node.impl.ADStarNodeImpl;
import es.usc.citius.hipster.model.problem.SearchComponents;

/**
 * The ADStarNodeBuilder is used for instantiate new {@link ADStarNodeImpl}.
 *
 * @param <S> class defining the state
 * @param <C> class defining the cost
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class ADStarNodeFactory<A, S, C extends Comparable<C>> implements NodeFactory<A, S, ADStarNodeImpl<A, S, C>> {

    protected C max;
    protected C min;
    protected BinaryOperation<C> addOperation;
    protected ScalarOperation<C> scaleOperation;
    protected HeuristicFunction<S, C> hf;

    /**
     * Constructor which takes the minimum and
     * maximum cost values managed by the algorithm to initialize the
     * node values properly.
     *
     * @param addOp addition operation
     * @param scaleOp scaling operation
     * @param hf heuristic function
     */
    public ADStarNodeFactory(BinaryOperation<C> addOp, ScalarOperation<C> scaleOp, HeuristicFunction<S, C> hf) {
        this.hf = hf;
        this.addOperation = addOp;
        this.scaleOperation = scaleOp;
        this.min = addOperation.getIdentityElem();
        this.max = addOperation.getMaxElem();
    }

    /**
     * Constructor which takes the search components instead of taking all the separated
     * arguments.
     *
     * @param components search components of the algorithm
     */
    public ADStarNodeFactory(SearchComponents<A, S, C> components){
        this(components.costAlgebra(), components.scaleAlgebra(), components.heuristicFunction());
    }

    @Override
    public ADStarNodeImpl<A, S ,C> makeNode(ADStarNodeImpl<A, S, C> from, Transition<A, S> transition) {
        if (from == null) {
            return new ADStarNodeImpl<A, S, C>(transition, null, min, max, new ADStarNode.Key<C>(min, max, hf.estimate(transition.getState()), 1.0, addOperation, scaleOperation));
        } else {
            return new ADStarNodeImpl<A, S, C>(transition, null, max, max, new ADStarNode.Key<C>(max, max));
        }
    }
}
