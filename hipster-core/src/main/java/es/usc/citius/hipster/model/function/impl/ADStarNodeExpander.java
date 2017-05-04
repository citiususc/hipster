package es.usc.citius.hipster.model.function.impl;

import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.*;
import es.usc.citius.hipster.model.ADStarNode;
import es.usc.citius.hipster.model.problem.SearchComponents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is an implementation of {@link es.usc.citius.hipster.model.function.NodeExpander} for nodes
 * of type {@link es.usc.citius.hipster.model.ADStarNode}. This node expander executes the main
 * function of a node expander: taking as input a node generates the successor nodes using the information
 * of the transition, cost and heuristic functions. This expander, to be used
 * with {@link es.usc.citius.hipster.algorithm.ADStarForward}, also
 * executes the following operations:
 *
 * <li>
 *     <ul>Obtain a set of iterable nodes from the transitions that changed since the last solution found of AD*</ul>
 *     <ul>Update nodes in consistent and inconsistent states</ul>
 *     <ul>Store the nodes visited by the algorithm</ul>
 *     <ul>Get the predecessors nodes of a current one which were visited by the algorithm</ul>
 * </li>
 *
 * @param <A> type of the actions
 * @param <S> type of the state
 * @param <C> type of the cost (must extend {@link java.lang.Comparable})
 * @param <N> node type
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class ADStarNodeExpander<A, S, C extends Comparable<C>, N extends es.usc.citius.hipster.model.ADStarNode<A, S, C, N>>
        implements NodeExpander<A, S, N>{

    private final TransitionFunction<A, S> successorFunction;
    private final TransitionFunction<A, S> predecessorFunction;
    private final CostFunction<A, S, C> costFunction;
    private final HeuristicFunction<S, C> heuristicFunction;
    private final BinaryOperation<C> add;
    private final ScalarFunction<C> scale;
    private final NodeFactory<A, S, N> nodeFactory;
    private Map<S, N> visited;
    private double epsilon;

    /**
     * Builds a node expander from a search components and a node factory. A epsilon value (used to inflate
     * the heuristic and obtain solutions anytime) must be specified, being >= 1.
     *
     *
     * @param components search components of the search
     * @param factory node factory
     * @param epsilon heuristic inflation value (>=1)
     */
    public ADStarNodeExpander(SearchComponents<A, S, C> components, NodeFactory<A, S, N> factory, double epsilon){
        this(components.successorFunction(), components.predecessorFunction(), components.costFunction(),
                components.heuristicFunction(), components.costAlgebra(), components.scaleAlgebra(), factory, epsilon);
    }

    /**
     * Builds a node expander specifying the required components individually. A epsilon value (used to inflate
     * the heuristic and obtain solutions anytime) must be specified, being >= 1.
     *
     * @param successorFunction successor function
     * @param predecessorFunction predecessor function
     * @param costFunction evaluation function
     * @param heuristicFunction heuristic function
     * @param add cost addition function
     * @param scale cost scale function
     * @param nodeFactory node factory
     * @param epsilon heuristic inflation value (>=1)
     */
    public ADStarNodeExpander(TransitionFunction<A, S> successorFunction, TransitionFunction<A, S> predecessorFunction,
                              CostFunction<A, S, C> costFunction, HeuristicFunction<S, C> heuristicFunction,
                              BinaryOperation<C> add, ScalarFunction<C> scale, NodeFactory<A, S, N> nodeFactory,
                              double epsilon) {
        this.successorFunction = successorFunction;
        this.predecessorFunction = predecessorFunction;
        this.costFunction = costFunction;
        this.heuristicFunction = heuristicFunction;
        this.add = add;
        this.scale = scale;
        this.nodeFactory = nodeFactory;
        this.visited = new HashMap<S, N>();
        this.epsilon = epsilon;
    }

    @Override
    public Iterable<N> expand(N node) {
        Collection<N> nodes = new ArrayList<N>();
        boolean consistency = node.isConsistent();
        //if s' not visited before: v(s')=g(s')=Infinity; bp(s')=null
        for (Transition<A, S> transition : successorFunction.transitionsFrom(node.state())) {
            N successorNode = visited.get(transition.getState());
            if (successorNode == null) {
                successorNode = nodeFactory.makeNode(node, transition);
                visited.put(transition.getState(), successorNode);
            }
            //if consistent
            if (consistency) {
                //if g(s') > g(s) + c(s, s')
                // bp(s') = s
                // g(s') = g(s) + c(s, s')
                //set to update queues after this
                successorNode.setDoUpdate(updateConsistent(successorNode, node, transition));
            } else {
                //Generate
                if (transition.getState().equals(node.state())) {
                    // bp(s') = arg min s'' predecessor of s' such that (v(s'') + c(s'', s'))
                    // g(s') = v(bp(s')) + c(bp(s'), s'')
                    updateInconsistent(successorNode, predecessorsMap(transition.getState()));
                    //update queues after this
                    successorNode.setDoUpdate(true);
                }
            }
            nodes.add(successorNode);
        }
        return nodes;
    }

    /**
     * Generates an iterable list of nodes, updated as inconsistent after applying the cost changes in the
     * list of transitions passed as parameter.
     *
     * @param begin beginning state of the search
     * @param current current node of the search
     * @param transitions list of transitions with changed costs
     * @return list of updated nodes
     */
    public Iterable<N> expandTransitionsChanged(N begin, Iterable<Transition<A, S>> transitions){
        Collection<N> nodes = new ArrayList<N>();
        for (Transition<A, S> transition : transitions) {
            S state = transition.getState();
            //if v != start
            if (!state.equals(begin.state())) {
                //if s' not visited before: v(s')=g(s')=Infinity; bp(s')=null
                N node = this.visited.get(state);
                if (node == null) {
                    node = nodeFactory.makeNode(begin, transition);
                    visited.put(state, node);
                }
                // bp(v) = arg min s'' predecessor of v such that (v(s'') + c(s'', v))
                // g(v) = v(bp(v)) + c(bp(v), v)
                updateInconsistent(node, predecessorsMap(transition.getState()));
                nodes.add(node);
            }
        }
        return nodes;
    }

    /**
     * Updates a node in consistent state (V > G) updating the path
     * and the cost if the parent node and the transition improves the current cost of the node.
     *
     * @param node {@link es.usc.citius.hipster.algorithm.ADStarForward} node to update, in consistent state
     * @param parent previous {@link es.usc.citius.hipster.algorithm.ADStarForward} of the node
     * @param transition {@link es.usc.citius.hipster.model.Transition} between the parent and the node
     * @return true if the node has changed its {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl.Key}
     */
    private boolean updateConsistent(N node, N parent, Transition<A, S> transition) {
        // parent.getG().add(this.costFunction.evaluate(transition));
        C accumulatedCost = add.apply(parent.getG(), costFunction.evaluate(transition));
        if (node.getG().compareTo(accumulatedCost) > 0) {
            node.setPreviousNode(parent);
            // node.previousNode = parent;
            node.setG(accumulatedCost);
            node.setState(transition.getState());
            node.setAction(transition.getAction());
            // node.state = transition;
            node.setKey(new es.usc.citius.hipster.model.ADStarNode.Key<C>(node.getG(), node.getV(),
                    heuristicFunction.estimate(transition.getState()), epsilon, add, scale));
            return true;
        }
        return false;
    }

    /**
     * Updates a node in inconsistent state (V <= G), evaluating all the predecessors of the current node
     * and updating the parent to the node which combination of cost and transition is minimal.
     *
     * @param node inconsistent {@link es.usc.citius.hipster.algorithm.ADStarForward} node to update
     * @param predecessorMap map containing the the predecessor nodes and
     * @return true if the node has changed its {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl.Key}
     */
    private boolean updateInconsistent(N node, Map<Transition<A, S>, N> predecessorMap) {
        C minValue = add.getIdentityElem();
        N minParent = null;
        Transition<A, S> minTransition = null;
        for (Map.Entry<Transition<A, S>, N> current : predecessorMap
                .entrySet()) {
            C value = add.apply(current.getValue().getV(), costFunction.evaluate(current.getKey()));
            //T value = current.getValue().v.add(this.costFunction.evaluate(current.getKey()));
            if (value.compareTo(minValue) < 0) {
                minValue = value;
                minParent = current.getValue();
                minTransition = current.getKey();
            }
        }
        node.setPreviousNode(minParent);
        // node.previousNode = minParent;
        node.setG(minValue);
        node.setState(minTransition.getState());
        node.setAction(minTransition.getAction());
        // node.state = minTransition;
        node.setKey(
                new ADStarNode.Key<C>(node.getG(), node.getV(),
                        heuristicFunction.estimate(minTransition.getState()), epsilon, add, scale)
        );
        return true;
    }

    /**
     * Retrieves a map with the predecessors states and the node associated
     * to each predecessor state.
     *
     * @param current current state to calculate predecessors of
     * @return map pairs of <state, node> with the visited predecessors of the state
     */
    private Map<Transition<A, S>, N> predecessorsMap(S current){
        //Map<Transition, Node> containing predecessors relations
        Map<Transition<A, S>, N> mapPredecessors = new HashMap<Transition<A, S>, N>();
        //Fill with non-null pairs of <Transition, Node>
        for (Transition<A, S> predecessor : predecessorFunction.transitionsFrom(current)) {
            N predecessorNode = visited.get(predecessor.getState());
            if (predecessorNode != null) {
                mapPredecessors.put(predecessor, predecessorNode);
            }
        }
        return mapPredecessors;
    }

    /**
     * Assigns the maximum value to V in the current node.
     *
     * @param node {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl} to modify the value of V
     */
    public void setMaxV(N node) {
        node.setV(this.add.getMaxElem());
    }

    /**
     * Assigns the maximum value to G in the current node.
     *
     * @param node {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl} to modify the value of V
     */
    public void setMaxG(N node)  {
        node.setG(this.add.getMaxElem());
    }

    /**
     * Assign a value to the inflation parameter of the heuristic.
     *
     * @param epsilon new value
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Queries the current value of epsilon (sub-optimal bound for anytime solutions).
     *
     * @return current value of epsilon
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * @return map with the states and nodes visited by the algorithm
     */
    public Map<S, N> getVisited() { return visited; }

    /**
     * Clears the set of visited nodes.
     */
    public void clearVisited() { this.visited = new HashMap<S, N>(); }

    /**
     * Creates a new node from the parent and a transition, calling the node factory.
     *
     * @param from parent node
     * @param transition transition between the parent and the new node
     * @return new node created by the node factory
     */
    public N makeNode(N from, Transition<A, S> transition){
        return nodeFactory.makeNode(from, transition);
    }

    /**
     * Updating the priority of a node is required when changing the value of Epsilon.
     */
    public void updateKey(N node){
        node.setKey(new es.usc.citius.hipster.model.ADStarNode.Key<C>(node.getG(), node.getV(),
                heuristicFunction.estimate(node.state()), epsilon, add, scale));
    }

    public void setMaxKey(N node){
        node.setKey(new ADStarNode.Key<C>(add.getMaxElem(), add.getMaxElem()));
    }
}
