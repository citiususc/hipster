package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ComparableNode;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.NumericNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Iterator to execute an AD* search algorithm.
 *
 * @author Adrián González Sieira
 * @param <S> class that defines the states
 * @since 26-03-2013
 * @version 1.0
 */
public class ADStarIterator<S> implements Iterator<S> {

    private final S beginState;
    private Map<S, ComparableNode<S>> open;
    private Map<S, ComparableNode<S>> closed;
    private Map<S, ComparableNode<S>> incons;
    private Queue<S> openQueue;
    private TransitionFunction<S> predecessorFunction;
    private TransitionFunction<S> successorFunction;
    private NodeBuilder<S, ComparableNode<S>> nodeBuilder;

    /**
     * Internal public static class to define a builder to create this iterator
     * with the parameters specified in a transparent way.
     *
     * @param <S>
     */
    public static class Builder<S> {

        private S begin;
        private Iterable<S> goals;
        private Queue<ComparableNode<S>> queue;
        private CostFunction<S, Double> costFunction;
        private HeuristicFunction<S, Double> heuristicFunction;
        private NodeBuilder<S, ComparableNode<S>> nodeBuilder;
        private TransitionFunction<S> predecessorFunction;
        private TransitionFunction<S> successorFunction;

        public Builder(S beginState, TransitionFunction<S> succesorFunction, TransitionFunction<S> predecesorFunction) {
            /*Mandatory elements assigned to the Builder instance.*/
            this.begin = beginState;
            this.predecessorFunction = predecesorFunction;
            this.successorFunction = succesorFunction;

            /*Default open queue used: PriorityQueue*/
            this.queue = new PriorityQueue<ComparableNode<S>>();

            /*Default cost function used: Assigns cost 1 to all transitions*/
            this.costFunction = new CostFunction<S, Double>() {
                public Double evaluate(Transition<S> transition) {
                    return 1.0;
                }
            };

            /*Default heuristic function used: always returns 0*/
            this.heuristicFunction = new HeuristicFunction<S, Double>() {
                public Double estimate(S state) {
                    return 0.0;
                }
            };
            
            this.nodeBuilder = new NumericNodeBuilder<S>(this.costFunction, this.heuristicFunction);
        }
    }
}
