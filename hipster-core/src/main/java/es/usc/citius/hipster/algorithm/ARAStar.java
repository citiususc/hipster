package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.NodeExpander;
import es.usc.citius.hipster.model.function.NodeFactoryWithUpdates;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.ScalarOperation;

import java.util.*;

public class ARAStar<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {
    protected S start;
    protected S goal;
    protected float initialEpsilon;
    protected NodeExpander<A, S, N> expander;
    protected Factory<A, S, C, N> nodeFactory;

    public ARAStar(S start, S goal, float initialEpsilon, NodeExpander<A, S, N> expander) {
        this.start = start;
        this.goal = goal;
        this.initialEpsilon = initialEpsilon;
        this.expander = expander;
        this.nodeFactory = (Factory<A, S, C, N>) expander.getNodeFactory();
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

        public Iterator() {
            //OPEN = CLOSED = INCONS = 0
            this.open = new HashMap<>();
            this.openQueue = new PriorityQueue<>();
            this.closed = new HashMap<>();
            this.incons = new HashMap<>();
            nodeFactory.setScaleFactor(initialEpsilon);
            //g(sstart) = 0;
            this.beginNode = nodeFactory.makeNode(null, Transition.<A,S>create(null, null, start));
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
            //if goal is not in OPEN, then fvalue is infinite
            if(open.get(goal) == null || open.get(goal).getScore().compareTo(current.getScore()) > 0){
                //remove s with the smallest fvalue(s) from OPEN;
                open.remove(current.state());
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
            else{
                // retrieve goal node from OPEN/CLOSED queues
                N goalNode = open.get(goal);
                if(goalNode == null){
                    goalNode = closed.get(goal);
                }
                //ε′ = min(ε, g(sgoal)/ mins∈OPEN∪INCONS(g(s)+h(s)));
                N minIncons = Collections.min(incons.values());
                N minNode = (minIncons.compareTo(current) > 0) ? current : minIncons;
                double newEpsilon = Math.min(
                        nodeFactory.getScaleFactor(),
                        nodeFactory.getScalarOperation().div(goalNode.getCost(), nodeFactory.getCostAccumulator().apply(minNode.getCost(), minNode.getEstimation())));
                //publish current ε′-suboptimal solution;

                //while ε′ > 1
                if(newEpsilon > 1){
                    //decrease ε;
                    nodeFactory.setScaleFactor(newEpsilon);

                    //Move states from INCONS into OPEN;
                    open.putAll(incons);

                    //Update the priorities for all s ∈ OPEN according to fvalue(s);
                    openQueue.clear();
                    for(N currentNodeUpdate : open.values()){
                        // re-calculate fvalue(s)
                        nodeFactory.updateNode(currentNodeUpdate);
                        // insert in OPEN
                        openQueue.add(currentNodeUpdate);
                    }

                    //CLOSED = ∅;
                    closed.clear();
                }
            }
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

        public HashMap<S, N> getOpen() {
            return open;
        }

        public HashMap<S, N> getClosed() {
            return closed;
        }

        public HashMap<S, N> getIncons() {
            return incons;
        }
    }

    /**
     * This algorithm, for purposes of calculating the next value of the scaling factor, has to know
     * which is the scaling operation. For this reason we define an interface which must implement every
     * node factory to be used with this algorithm.
     *
     * @param <A> action type
     * @param <S> state type
     * @param <C> cost type
     * @param <N> node type
     */
    public interface Factory<A, S, C extends Comparable<C>, N> extends NodeFactoryWithUpdates<A, S, N> {

        void setScaleFactor(double epsilon);

        double getScaleFactor();

        ScalarOperation<C> getScalarOperation();

        BinaryOperation<C> getCostAccumulator();

    }

}
