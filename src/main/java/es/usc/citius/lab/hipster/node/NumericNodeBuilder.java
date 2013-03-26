package es.usc.citius.lab.hipster.node;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;

/**
 * Implementation of {@link NodeBulder} to create instances of
 * {@link NumericNode}.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public class NumericNodeBuilder<S> implements NodeBuilder<S, NumericNode<S>> {

    private CostFunction<S, Double> cost;
    private HeuristicFunction<S, Double> heuristic;

    /**
     * Default constructor: requieres the {@link CostFunction} and the
     * {@link HeuristicFunction} returning Double values to guide the heuristic
     * search.
     *
     * @param costFunction cost function implementation
     * @param heuristicFunction heuristic function implementation
     */
    public NumericNodeBuilder(CostFunction<S, Double> costFunction,
            HeuristicFunction<S, Double> heuristicFunction) {
        this.cost = costFunction;
        this.heuristic = heuristicFunction;
    }

    /**
     * Build method for {@link NumericNode} instances: Internally evaluates the 
     * cost and heuristic function to instantiate the Node.
     *
     * @param from incoming node
     * @param transition incoming transition
     * @return new instance of NumericNode
     */
    public NumericNode<S> node(NumericNode<S> from, Transition<S> transition) {
        double previousCost = (from != null) ? from.cost() : 0d;
        double g = previousCost + cost.evaluate(transition);
        double h = heuristic.estimate(transition.state());
        double f = g + h;
        return new NumericNode<S>(transition, from, g, f);
    }
}
