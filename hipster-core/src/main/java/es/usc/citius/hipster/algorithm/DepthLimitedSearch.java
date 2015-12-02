/*
 * Copyright 2015 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela.
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
package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.NodeExpander;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author Jennnnyz
 */
public class DepthLimitedSearch<A,S,N extends Node<A,S,N>> extends DepthFirstSearch<A,S,N> {
    protected N initialNode;
    protected NodeExpander<A,S,N> expander;
    protected int limit;
    protected int depth = 0;
    
    /**
     *
     * @param initialNode
     * @param expander
     * @param limit
     */
    public DepthLimitedSearch(N initialNode, NodeExpander<A, S, N> expander,int limit) {        
        super(initialNode,expander);
        this.limit = limit;
    }

         public class Iterator extends DepthFirstSearch.Iterator {
 
        protected Iterator(){
            super();
        }


        @Override
        public boolean hasNext() {
            if (depth == limit)
                return false;
            if (next == null){
                // Compute next
                next = nextUnvisited();
                if (next == null) return false;
            }
            return true;
        }

        @Override
        protected StackFrameNode nextUnvisited(){
            StackFrameNode nextNode;
            depth = depth + 1;
            do {
                nextNode = super.processNextNode();
            } while(nextNode != null &&
		            (nextNode.processed ||
				            nextNode.visited || closed.contains(nextNode.getNode().state())));

            if (nextNode != null){
                nextNode.visited = true;
                // For graphs, the DFS needs to keep track of all nodes
                // that were processed and removed from the stack, in order
                // to avoid cycles.
                if (graphSupport) closed.add(nextNode.getNode().state());
            }
            return nextNode;
         }
        }
         
    @Override
    public Iterator iterator() {
        return new Iterator();
    }
}

