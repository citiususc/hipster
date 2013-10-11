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

import es.usc.citius.lab.hipster.algorithm.AStar;
import es.usc.citius.lab.hipster.node.HeuristicNode;

/**
 * @author Pablo Rodríguez Mier
 *         Date: 9/10/13
 */
public class AStarFactory<S> implements HeuristicAlgorithmFactory<S> {

    @Override
    public Iterable<HeuristicNode<S, Double>> create(HeuristicAlgorithmBuilder<S> builder) {
        return new AStar<S, Double>(builder.getInitialState(), builder.getTransition(), builder.getNodeFactory());
    }
}
