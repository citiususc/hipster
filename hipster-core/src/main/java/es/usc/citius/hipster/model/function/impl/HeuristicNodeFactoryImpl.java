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

package es.usc.citius.hipster.model.function.impl;


import es.usc.citius.hipster.model.ActionState;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.impl.HeuristicNodeImpl;

public class HeuristicNodeFactoryImpl<A,S,C extends Comparable<C>> implements NodeFactory<A,S,HeuristicNodeImpl<A,S,C>>{

    private CostFunction<A,S,C> gf;
    private HeuristicFunction<S,C> hf;
    private BinaryOperation<C> costAccumulator;


    public HeuristicNodeFactoryImpl(CostFunction<A,S,C> costFunction, HeuristicFunction<S, C> heuristicFunction, BinaryOperation<C> costAccumulator) {
        this.gf = costFunction;
        this.hf = heuristicFunction;
        this.costAccumulator = costAccumulator;
    }

    public HeuristicNodeFactoryImpl(CostFunction<A,S,C> costFunction, BinaryOperation<C> costAccumulator) {
        this.gf = costFunction;
        this.hf = new HeuristicFunction<S, C>() {
            public C estimate(S state) {
                return HeuristicNodeFactoryImpl.this.costAccumulator.getIdentityElem();
            }
        };
        this.costAccumulator = costAccumulator;
    }

    @Override
    public HeuristicNodeImpl<A, S, C> makeNode(HeuristicNodeImpl<A, S, C> fromNode, ActionState<A, S> actionState) {
        C cost, estimatedDistance, score;

        if (fromNode == null){
            cost = costAccumulator.getIdentityElem();
        } else {
            cost = costAccumulator.apply(fromNode.getCost(), this.gf.evaluate(actionState));
        }
        estimatedDistance = this.hf.estimate(actionState.getState());
        score = costAccumulator.apply(cost, estimatedDistance);

        return new HeuristicNodeImpl<A,S,C>(fromNode, actionState.getState(), actionState.getAction(), cost, estimatedDistance, score);
    }
}
