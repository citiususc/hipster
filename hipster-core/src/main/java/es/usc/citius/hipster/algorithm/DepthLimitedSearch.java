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
package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.node.Node;
import es.usc.citius.hipster.model.node.factory.NodeExpander;
import es.usc.citius.hipster.model.node.impl.UnweightedNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
/**
 * Copyright 2015 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>
 * In computer science maximumDepth-limited search is an algorithm to explore the vertices of a graph.
 * It is a modification of maximumDepth-first search and is used for example in the iterative deepening
 * maximumDepth-first search algorithm.
 * </p>
 *
 * For more information see <a href="http://en.wikipedia.org/wiki/Depth-limited_search">this article of the Wikipedia</a> about DLS.
 *
 * @author Gabriella Zekany
 */
public class DepthLimitedSearch <A,S,N extends Node<A,S,N>> extends Algorithm<A,S,N> {
    protected N initialNode;
    protected N finalNode;
    protected NodeExpander nodeExpander;
    protected int maximumDepth;
    protected int currentDepth;
    protected ArrayList<S> path;

    public DepthLimitedSearch(N initialNode, N finalNode, NodeExpander nodeExpander, int maximumDepth) {
        this.initialNode = initialNode;
        this.finalNode = finalNode;
        this.nodeExpander = nodeExpander;
        this.maximumDepth = maximumDepth;
        this.currentDepth = 0;
        this.path = new ArrayList<>();
    }

    public int getMaximumDepth() {
        return this.maximumDepth;
    }

    public int getCurrentDepth() {
        return this.currentDepth;
    }

    public ArrayList<S> getPath() {
        return path;
    }

    public void incrementCurrentDepth() {
        this.currentDepth ++;
    }

    public boolean execute() {
        Stack<StackNode> nodeStack = new Stack();
        StackNode tempStackNode = new StackNode(this.initialNode);
        nodeStack.add(tempStackNode);

        while(!nodeStack.isEmpty()) {
            if(this.currentDepth <= this.maximumDepth) {
                StackNode temp = nodeStack.pop();
                if(!path.contains(temp.getNode()) && ((UnweightedNode) temp.getNode()).state().equals(((UnweightedNode)this.finalNode).state())){
                    this.path.add((S) temp.getNode().state());
                    return true;
                }  else {
                    this.path.add((S) temp.getNode().state());
                    for(StackNode child : temp.getChildren()) {
                        if(!this.path.contains(child.getNode().state())) {
                            nodeStack.add(child);
                        }
                    }
                    this.incrementCurrentDepth();
                }
            } else {
                return false;
            }
        }
        return false;
    }

    private class StackNode {
        private N node;
        private java.util.Iterator<N> children;

        public StackNode(N node) {
            this.node = node;
            this.children = nodeExpander.expand(node).iterator();
        }

        public N getNode() {
            return node;
        }

        public void setNode(N node) {
            this.node = node;
        }

        public List<StackNode> getChildren() {
            ArrayList<StackNode> result = new ArrayList<>();
            while(this.children.hasNext()) {
                StackNode temp = new StackNode(this.children.next());
                result.add(temp);
            }
            return result;
        }

        public void setChildren(java.util.Iterator<N> children) {
            this.children = children;
        }
    }

    @Override
    public Iterator<N> iterator() {
        return null;
    }
}
