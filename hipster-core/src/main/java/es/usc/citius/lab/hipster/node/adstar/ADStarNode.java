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

package es.usc.citius.lab.hipster.node.adstar;


import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * Implementation of {@link es.usc.citius.lab.hipster.node.HeuristicNode} to be used with 
 * the {@link es.usc.citius.lab.hipster.algorithm.ADStar} algorithm. The ADStar node defines the cost
 * (called G) and the score (called V) to keep the nomenclature introduced in the 
 * <a href="http://www.cis.upenn.edu/~maximl/files/ad_icaps05.pdf">article describing
 * the algorithm</a>. This type of node also includes a method to calculate the key for a node, which
 * is a tuple of cost elements obtained from the cost and the score and allows the comparison between
 * nodes. The theoretical details about the comparison between nodes can be consulted in the article.
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public class ADStarNode<S, T extends Comparable<T>> extends AbstractNode<S> implements Comparable<ADStarNode<S, T>>, HeuristicNode<S, T> {

    protected T g;
    protected T v;
    protected Key<T> key;
    
    /**
     * Default constructor for ADStarNode. Requires the transition used
     * to reach the new one and the previous node. The current cost (G), 
     * score (V) and key to compare between ADStarNode instances are also required.
     * 
     * @param transition incoming transition
     * @param previousNode parent node
     * @param g accumulated cost from begin
     * @param v score to goal
     * @param k key value evaluated over G and V
     */
    public ADStarNode(Transition<S> transition, Node<S> previousNode, T g, T v, Key<T> k) {
        super(transition, previousNode);
        this.g = g;
        this.v = v;
        this.key = k;
    }

    /**
     * Cost from beginning state.
     * 
     * @return object representing the current cost
     */
    public T getG() {
        return g;
    }

    /**
     * Score to goal given as heuristic.
     * 
     * @return object representing the estimated cost to goal
     */
    public T getV() {
        return v;
    }
    
    /**
     * Same as getG()
     * 
     * @see #getG()
     */
	public T getCost() {
		return this.g;
	}

	/**
	 * Same as getV()
	 * 
	 * @see #getV()
	 */
	public T getScore() {
		return this.v;
	}

    public void setG(T g) {
        this.g = g;
    }

    public void setV(T v) {
        this.v = v;
    }

    public void setKey(Key<T> key) {
        this.key = key;
    }

    /**
     * Method to retrieve the parent ADStarNode, the same
     * retrieved by {@link es.usc.citius.lab.hipster.node.Transition#from()}.
     * 
     * @return parent ADSTarNode, 
     */
    @SuppressWarnings("unchecked") //suppress warnings to return an ADStarNode instead of Node, which is the inherited return type from parent
	@Override
	public ADStarNode<S, T> previousNode() {
        return (ADStarNode<S, T>) previousNode;
    }
    
    /**
     * Compares ADSTarNode instances attending to their {@link Key}
     * values.
     *
     * @param o ADStarNode instance
     * @return usual comparison value
     */
    public int compareTo(ADStarNode<S, T> o) {
        return this.key.compareTo(o.key);
    }
    
    /**
     * Inner class defining the key of the node, which depends on the values of G and V. The
     * key of the node is the comparison criterion for ADStar to order the open queue.
     */
    public static class Key<T extends Comparable<T>> implements Comparable<Key<T>> {

        private T first;
        private T second;

        /**
         * Constructor to calculate a the key to order the nodes in the Open
         * queue.
         *
         * @param g g value of the node
         * @param v v value of the node
         * @param h
         * @param e
         */
        public Key(T g, T v, T h, double e, BinaryOperation<T> add, ScalarFunction<T> scale) {
            if (v.compareTo(g) >= 0) {
                this.first = add.apply(g, scale.scale(h, e)); //g + h*e
                this.second = g;
            } else {
                this.first = add.apply(v, h); //v + h
                this.second = v;
            }
        }
        
        /**
         * Instantiates a new Key given its first and second value instead of 
         * calculating them.
         * 
         * @param first first cost value
         * @param second second cost value
         */
        public Key(T first, T second){
            this.first = first;
            this.second = second;
        }

        /**
         * Compares by the first value and, if equal, by the second one.
         *
         * @param o other Key object
         * @return comparison result
         */
        public int compareTo(Key<T> o) {
            int firstCompare = this.first.compareTo(o.first);
            if (firstCompare == 0) {
                return this.second.compareTo(o.second);
            } else {
                return firstCompare;
            }
        }
    }
}
