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

/**
 * Concrete implementation of {@link AbstractNode} for nodes comparable by
 * numeric values.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public class AStarDoubleNode<S> extends AbstractNode<S> implements AStarNode<S>{

    protected final double cost; // In A*: g(n)
    protected final double score; // In A*: f(n) = g(n) + h(n)

    /**
     * Default builder
     *
     * @param transition incoming transition to this node
     * @param previous incoming node
     * @param cost total cost value
     * @param score heuristic value
     */
    public AStarDoubleNode(Transition<S> transition,
            AStarDoubleNode<S> previous, double cost, double score) {
        super(transition, previous);
        this.cost = cost;
        this.score = score;
    }

    /**
     * Resturns the total cost for this node
     *
     * @return double value
     */
    public double cost() {
        return this.cost;
    }

    /**
     * Resturns the heuristic value for this node
     *
     * @return double value
     */
    public double score() {
        return this.score;
    }

    /**
     * Comparation implementation between instances of NumericNode.
     *
     * @param o NumericNode object
     * @return int result
     */
    public int compareTo(AStarDoubleNode<S> o) {
        return Double.compare(this.score, o.score);
    }

    @Override
    public String toString() {
        return this.state.to().toString().concat(" (").concat(new Double(this.cost).toString()).concat(", ").concat(new Double(this.score).toString()).concat(")"); //To change body of generated methods, choose Tools | Templates.
    }

	public int compareTo(AStarNode<S> o) {
		return compareByScore(o);
	}

	public int compareByCost(AStarNode<S> o) {
		AStarDoubleNode<S> node = (AStarDoubleNode<S>) o;
		return Double.compare(this.cost, node.cost);
	}

	public int compareByScore(AStarNode<S> o) {
		AStarDoubleNode<S> node = (AStarDoubleNode<S>) o;
		return Double.compare(this.score, node.score);
	}

}
