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

package es.usc.citius.lab.hipster.algorithm.builder;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.informed.HeuristicNode;
import es.usc.citius.lab.hipster.node.informed.HeuristicNodeImplFactory;

public class HeuristicAlgorithmBuilder<S> {
    private S initialState;
    private TransitionFunction<S> transition;
    private HeuristicFunction<S, Double> heuristic = new HeuristicFunction<S, Double>() {
        public Double estimate(S state) {
            return 0d;
        }
    };
    private CostFunction<S, Double> cost = new CostFunction<S, Double>() {
        public Double evaluate(Transition<S> transition) {
            return 1d;
        }
    };
    private NodeFactory<S, HeuristicNode<S, Double>> nodeFactory;

    public HeuristicAlgorithmBuilder(S initialState, TransitionFunction<S> transition){
        this.initialState = initialState;
        this.transition = transition;
    }

    public HeuristicAlgorithmBuilder<S> cost(CostFunction<S, Double> costFunction){
        this.cost = costFunction;
        return this;
    }

    public HeuristicAlgorithmBuilder<S> heuristic(HeuristicFunction<S, Double> heuristicFunction){
        this.heuristic = heuristicFunction;
        return this;
    }

    public Iterable<HeuristicNode<S, Double>> build(HeuristicAlgorithmFactory<S> factory){
        this.nodeFactory = new HeuristicNodeImplFactory<>(cost, heuristic, BinaryOperation.doubleAdditionOp());
        return factory.create(this);
    }

    public S getInitialState() {
        return initialState;
    }

    public NodeFactory<S, HeuristicNode<S, Double>> getNodeFactory() {
        return nodeFactory;
    }

    public TransitionFunction<S> getTransition() {
        return transition;
    }


    public HeuristicFunction<S, Double> getHeuristic() {
        return heuristic;
    }


    public CostFunction<S, Double> getCost() {
        return cost;
    }

}
