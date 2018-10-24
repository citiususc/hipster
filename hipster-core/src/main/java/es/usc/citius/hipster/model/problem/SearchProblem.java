/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.model.problem;

import es.usc.citius.hipster.model.node.Node;
import es.usc.citius.hipster.model.node.factory.NodeExpander;

/**
 * Search problem definition for Anytime search algorithms, containing the following information:
 * <ul>
 *     <li>Initial and final states</li>
 *     <li>Expander component, which creates the successors nodes from the current one</li>
 *     <li>Heuristic scale factor to obtain sub-optimal solutions in anytime Algorithms (like ARA*)</li>
 * </ul>
 *
 * {@see ProblemBuilder} for further information and assisted creation of instances of this class
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class SearchProblem<A,S,N extends Node<A,S,N>> {

    protected N initialNode;
    protected N finalNode;
    protected float scaleFactor;
    protected NodeExpander<A,S,N> expander;

    public SearchProblem(N initialNode, NodeExpander<A, S, N> expander) {
        this(initialNode, null, expander, 1f);
    }

    public SearchProblem(N initialNode, N finalNode, NodeExpander<A, S, N> expander) {
        this(initialNode, finalNode, expander, 1f);
    }

    public SearchProblem(N initialNode, NodeExpander<A, S, N> expander, float scaleFactor) {
        this(initialNode, null, expander, scaleFactor);
    }

    public SearchProblem(N initialNode, N finalNode, NodeExpander<A, S, N> expander, float scaleFactor) {
        this.initialNode = initialNode;
        this.finalNode = finalNode;
        this.expander = expander;
        this.scaleFactor = scaleFactor;
    }

    public N getInitialNode() {
        return initialNode;
    }

    public NodeExpander<A, S, N> getExpander() {
        return expander;
    }

    public N getFinalNode() {
        return finalNode;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    /**
     * @deprecated will disappear in next release
     */
    public void setFinalNode(N finalNode) {
        this.finalNode = finalNode;
    }
}
