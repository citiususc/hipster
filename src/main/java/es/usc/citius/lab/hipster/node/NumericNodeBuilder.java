/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.node;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;

/**
 * Implementation of {@link NodeBulder} to create instances of
 * {@link NumericNode}.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public class NumericNodeBuilder<S> implements NodeBuilder<S, ComparableNode<S>> {

    private CostFunction<S, Double> cost;
    private HeuristicFunction<S, Double> heuristic;

    /**
     * Complete constructor: requires the {@link CostFunction} and the
     * {@link HeuristicFunction} returning Double values to guide the heuristic
     * search.
     *
     * @param costFunction cost function implementation
     * @param heuristicFunction heuristic function implementation
     */
    public NumericNodeBuilder(CostFunction<S, Double> costFunction,
            HeuristicFunction<S, Double> heuristicFunction) {
        this.cost = costFunction;
        this.heuristic = heuristicFunction;
    }
    
    /**
     * Partial constructor: requires the {@link CostFunction} returning
     * Double values, heuristic will be assigned by default (always 0).
     * @param costFunction cost function implementation
     */
    public NumericNodeBuilder(CostFunction<S, Double> costFunction){
        this.cost = costFunction;
        this.heuristic = defaultHeuristicFunction();
    }
    
    /**
     * Partial constructor: requires the {@link HeuristicFunction} returning
     * Double values, cost will be assigned by default (always 1).
     * @param heuristicFunction 
     */
    public NumericNodeBuilder(HeuristicFunction<S, Double> heuristicFunction){
        this.cost = defaultCostFunction();
        this.heuristic = defaultHeuristicFunction();
    }
    
    /**
     * Default constructor, assigns a default cost function (always 1) 
     * and a default heuristic function (always 0).
     */
    public NumericNodeBuilder(){
        this.cost = defaultCostFunction();
        this.heuristic = defaultHeuristicFunction();
    }

    /**
     * Build method for {@link NumericNode} instances: Internally evaluates the
     * cost and heuristic function to instantiate the Node.
     *
     * @param from incoming node
     * @param transition incoming transition
     * @return new instance of NumericNode
     */
    public ComparableNode<S> node(ComparableNode<S> from, Transition<S> transition) {
        NumericNode<S> fromCast = (NumericNode<S>) from;
        double previousCost = (fromCast != null) ? fromCast.cost() : 0d;
        double g = previousCost + cost.evaluate(transition);
        double h = heuristic.estimate(transition.to());
        double f = g + h;
        return new NumericNode<S>(transition, fromCast, g, f);
    }

    /**
     * Builds a default heuristic function
     * @return heuristic function returning always 0
     */
    private HeuristicFunction<S, Double> defaultHeuristicFunction() {
        return new HeuristicFunction<S, Double>() {
            public Double estimate(S state) {
                return 0d;
            }
        };
    }

    /**
     * Builds a default cost function.
     * @return cost function returning always 1
     */
    private CostFunction<S, Double> defaultCostFunction() {
        return new CostFunction<S, Double>() {
            public Double evaluate(Transition<S> transition) {
                return 1d;
            }
        };
    }
}
