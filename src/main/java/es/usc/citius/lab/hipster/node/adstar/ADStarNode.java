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

import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.Scalable;

/**
 * Basic structure for AD* search iterators.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 16-04-2013
 * @version 1.0
 */
public class ADStarNode<S, T extends Scalable<T>> extends AbstractNode<S> implements Comparable<ADStarNode<S, T>> {

    protected T g;
    protected T v;
    protected Key<T> key;  
    
    /**
     * Default constructor for this class, that requires the parent transition
     * and previous node.
     *
     * @param transition incoming transition
     * @param previousNode parent node
     */
    public ADStarNode(Transition<S> transition, Node<S> previousNode, T g, T v, Key<T> k) {
        super(transition, previousNode);
        this.g = g;
        this.v = v;
        this.key = k;
    }

    public T getG() {
        return g;
    }

    public T getV() {
        return v;
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

    @Override
    public ADStarNode<S, T> previousNode() {
        return (ADStarNode<S, T>) previousNode;
    }
    
    /**
     * Compares {@link ADStarNode} instances attending to their {@link Key}
     * values.
     *
     * @param o other node instance
     * @return comparation result
     */
    public int compareTo(ADStarNode<S, T> o) {
        return this.key.compareTo(o.key);
    }
    
    /**
     * Class defining the key of the state, used to order them
     */
    public static class Key<T extends Scalable<T>> implements Comparable<Key<T>> {

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
        public Key(T g, T v, T h, double e) {
            if (v.compareTo(g) >= 0) {
                this.first = g.add(h.scale(e));
                this.second = g;
            } else {
                this.first = v.add(h);
                this.second = v;
            }
        }
        
        public Key(T first, T second){
            this.first = first;
            this.second = second;
        }

        /**
         * Compares the first value and, if equal, the second one.
         *
         * @param o other Key object
         * @return comparation result
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
