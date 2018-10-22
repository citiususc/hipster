package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.function.NodeExpander;
import es.usc.citius.hipster.model.function.impl.ScaleWeightedNodeFactory;

import java.util.*;

public class ARAStar<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {
    protected S start;
    protected S goal;
    protected float epsilon;
    protected NodeExpander<A, S, N> expander;
    protected ScaleWeightedNodeFactory<A, S, C> nodeFactory;

    public ARAStar(S start, S goal, float epsilon, NodeExpander<A, S, N> expander) {
        this.start = start;
        this.goal = goal;
        this.epsilon = epsilon;
        this.expander = expander;
        this.nodeFactory = (ScaleWeightedNodeFactory<A, S, C>) expander.getNodeFactory();
    }

    @Override
    public Iterator iterator() {
        return new Iterator();
    }

    public class Iterator implements java.util.Iterator<N>{
        protected HashMap<S, N> open;
        protected HashMap<S, N> closed;
        protected HashMap<S, N> incons;
        protected Queue<N> openQueue;
        protected N beginNode;
        protected N goalNode;

        public Iterator() {
            //OPEN = CLOSED = INCONS = 0
            this.open = new HashMap<>();
            this.openQueue = new PriorityQueue<>();
            this.closed = new HashMap<>();
            this.incons = new HashMap<>();
            //g(sstart) = 0;
            //insert sstart into OPEN with fvalue(sstart);
            insertOpen(beginNode);
        }

        @Override
        public boolean hasNext() {
            return !open.values().isEmpty();
        }

        @Override
        public N next() {
            N current = takePromising();

            //while(fvalue(sgoal) > mins∈OPEN(fvalue(s))), go inside ImprovePath()
            if(goalNode.compareTo(current) > 0){
                //remove s with the smallest fvalue(s) from OPEN;
                open.remove(current);
                //CLOSED = CLOSED ∪ {s}
                closed.put(current.state(), current);
                //for each successor s' of s
                for(N successor : expander.expand(current)) {
                    //check if visited
                    N successorClosed = closed.get(successor.state());
                    if (successorClosed != null && successorClosed.getCost().compareTo(successor.getCost()) <= 0) {
                        //insert into INCONS
                        incons.put(successor.state(), successor);
                    }
                    // insert in OPEN
                    insertOpen(successor);
                }

            }
            /*else{
                //ε′ = min(ε, g(sgoal)/ mins∈OPEN∪INCONS(g(s)+h(s)));
                double epsilon = 1.0;
                //publish current ε′-suboptimal solution;

                //while ε′ > 1
                if(epsilon > 1){
                    //decrease ε;

                    //Move states from INCONS into OPEN;
                    open.putAll(incons);

                    //Update the priorities for all s ∈ OPEN according to fvalue(s);
                    openQueue.clear();
                    for(N openNode : open.values()){
                        insertOpen(openNode);
                    }

                    //CLOSED = ∅;
                    closed.clear();
                }
            }*/
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return retrieves, but not removes, the first element in the OPEN queue.
         */
        protected N takePromising() {
            // Poll until a valid state is found
            N node = openQueue.peek();
            while (!open.containsKey(node.state())) {
                openQueue.poll();
                node = openQueue.peek();
            }
            return node;
        }

        /**
         * Inserts a new node in OPEN
         *
         * @param node new node
         */
        protected void insertOpen(N node){
            open.put(node.state(), node);
            openQueue.add(node);
        }
    }

}
