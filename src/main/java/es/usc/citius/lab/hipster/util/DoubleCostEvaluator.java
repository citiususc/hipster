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

package es.usc.citius.lab.hipster.util;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.node.Node;
import java.util.Iterator;
import java.util.List;

/**
 * This class calculates the total cost of a path from the list of nodes
 * that form it, using the {@link CostFunction} to do the evaluations.
 * 
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 26-03-2013
 * @version 1.0
 */
public class DoubleCostEvaluator<S> implements CostEvaluator<S, Double>{

    /**
     * Evaluates the cost of the path using Double values
     * @param path list of nodes of the path
     * @param costFunction cost function used to evaluate transitions
     * @return total value of the path
     */
    public Double evaluate(List<Node<S>> path, CostFunction<S, Double> costFunction) {
        Double total = 0d;
        for (Iterator<Node<S>> it = path.iterator(); it.hasNext();) {
            Node<S> n = it.next();
            total += costFunction.evaluate(n.transition());
        }
        return total;
    }


}
