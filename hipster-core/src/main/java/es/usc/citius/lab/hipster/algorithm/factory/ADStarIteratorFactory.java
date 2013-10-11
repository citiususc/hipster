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
package es.usc.citius.lab.hipster.algorithm.factory;


import es.usc.citius.lab.hipster.algorithm.ADStar;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeUpdater;
import es.usc.citius.lab.hipster.node.CostNode;

import java.util.Iterator;

public class ADStarIteratorFactory<S, T extends Comparable<T>> implements
        AlgorithmIteratorFactory<S, T> {
    private final HeuristicSearchProblem<S, T> f;
    private ScalarFunction<T> scale;
    private T min;
    private T max;
    private double epsilon;

    public ADStarIteratorFactory(HeuristicSearchProblem<S, T> problem, ScalarFunction<T> scale, double epsilon, T min, T max) {
        this.f = problem;
        this.max = max;
        this.min = min;
        this.epsilon = epsilon;
        this.scale = scale;
    }

    public Iterator<? extends CostNode<S, T>> create() {

        NodeFactory<S, ADStarNode<S, T>> defaultBuilder = new ADStarNodeBuilder<S, T>(
                this.min, this.max);

        ADStarNodeUpdater<S, T> updater = new ADStarNodeUpdater<S, T>(
                f.getCostFunction(), f.getHeuristicFunction(), f.getAccumulator(), this.scale,
                this.epsilon);

        return new ADStar<S, T>(f.getInitialState(), f.getGoalState(),
                f.getTransitionFunction(), f.getTransitionFunction(),
                defaultBuilder, updater);
    }

}
