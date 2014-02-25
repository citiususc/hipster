package es.usc.citius.lab.hipster.util.map;

import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Transition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Definition for a generic graph-based problem, where the connectivity is defined using a Map<S, Iterable<S>>,
 * the costs are defined with a Map<S, Map<S, Double>> and the heuristic (optional) is defined as a Map<S, Double>.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 24/02/2014
 */
public class MapBasedGraphSearchProblem<S> implements HeuristicSearchProblem<S, Double> {

    private S begin;
    private S goal;
    private TransitionFunction<S> transitionFunction;
    private CostFunction<S, Double> costFunction;
    private HeuristicFunction<S, Double> heuristicFunction;

    /**
     * Default constructor for MapBasedGraphSearchProblem. The problem is defined as a set
     * of connections (which defines the transition function) and a map of costs between
     * states (which defines the cost function).
     *
     * @param begin beginning state
     * @param goal goal state
     * @param connectivity map defining the set of neighbor states per state in the graph
     * @param costs map containing for each pair of states the corresponding cost
     */
    public MapBasedGraphSearchProblem(S begin, S goal, Map<S, Collection<S>> connectivity, Map<S, Map<S, Double>> costs){
        //assign begin and ending states
        this.begin = begin;
        this.goal = goal;

        //assign transition and cost functions and the default heuristic function
        this.transitionFunction = transitionFunction(connectivity);
        this.costFunction = costFunction(costs);
        /*
         * Default implementation for the heuristic, that returns 0 in all cases (to perform a Dijkstra exploration
         * of the state space.
         */
        this.heuristicFunction = new HeuristicFunction<S, Double>() {
            @Override
            public Double estimate(S state) {
                return 0d;
            }
        };
    }

    /**
     * Constructor that initializes the transition function, the cost function and additionally the
     * heuristic function with the values that are stored in the maps passed as parameters.
     *
     * @param connectivity map defining the set of neighbor states per state in the graph
     * @param costs table containing for each pair of states the corresponding cost
     * @param heuristics map containing the heuristic value per state
     */
    public MapBasedGraphSearchProblem(S begin, S goal, Map<S, Collection<S>> connectivity, Map<S, Map<S, Double>> costs,
                                      Map<S, Double> heuristics){
        this(begin, goal, connectivity, costs);
        //assign heuristic function
        this.heuristicFunction = heuristicFunction(heuristics);
    }

    @Override
    public HeuristicFunction<S, Double> getHeuristicFunction() {
        return heuristicFunction;
    }

    @Override
    public CostFunction<S, Double> getCostFunction() {
        return costFunction;
    }

    @Override
    public S getInitialState() {
        return begin;
    }

    @Override
    public S getGoalState() {
        return goal;
    }

    @Override
    public TransitionFunction<S> getTransitionFunction() {
        return transitionFunction;
    }

    /**
     * Generates a transition function which takes the connectivity definition and generates the set of outgoing
     * transitions for each incoming state.
     *
     * @param connectivity connectivity definition as a Map with multiple elements per key
     * @param <S> class defining the state
     * @return transition function according to the definition of the connectivity taken as input
     */
    public static <S> TransitionFunction<S> transitionFunction(final Map<S, Collection<S>> connectivity){
        return new TransitionFunction<S>() {
            @Override
            public Iterable<? extends Transition<S>> from(S current) {
                //initialize variable to store outgoing transitions
                ArrayList<Transition<S>> outgoingTransitions = new ArrayList<Transition<S>>();
                //iterate over connections to generate the transitions
                for(S neighbor : connectivity.get(current)){
                    outgoingTransitions.add(new Transition<S>(current, neighbor));
                }
                return outgoingTransitions;
            }
        };
    }

    /**
     * Generates a cost function which takes the cost definition for each pair of states. Elements not present
     * in the cost function or with null at the beginning of the transition are evaluated as 0. Assumes the cost
     * to be expressed as a Double value.
     *
     * @param costs map defining the cost for each pair of elements
     * @param <S> class defining the state
     * @return cost function according to the definition of the cost map taken as input
     */
    public static <S> CostFunction<S, Double> costFunction(final Map<S, Map<S, Double>> costs){
        return new CostFunction<S, Double>() {
            @Override
            public Double evaluate(Transition<S> transition) {
                try{
                    return costs.get(transition.from()).get(transition.to());
                } catch (NullPointerException ex){
                    /*
                     * returns 0 when no entry is present in the cost definition map, or one of the elements
                     * of the transition is null
                     */
                    return 0d;
                }
            }
        };
    }

    /**
     * Generates a heuristic function which takes the heuristic definition for each state. Elements not present
     * in the heuristic definition are evaluated as 0. Assumes the heuristic to be expressed as Double.
     *
     * @param heuristics map defining the heuristic value for each state
     * @param <S> class defining the state
     * @return heuristic function according to the definition of the information in the input map
     */
    public static <S> HeuristicFunction<S, Double> heuristicFunction(final Map<S, Double> heuristics){
        return new HeuristicFunction<S, Double>() {
            @Override
            public Double estimate(S state) {
                try{
                    return heuristics.get(state);
                } catch (NullPointerException ex){
                    //returns 0 when no information is found in the heuristic definition
                    return 0d;
                }
            }
        };
    }
}
