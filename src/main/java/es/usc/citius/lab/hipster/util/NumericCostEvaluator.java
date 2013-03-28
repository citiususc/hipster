package es.usc.citius.lab.hipster.util;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.node.Node;
import java.util.Iterator;
import java.util.List;

/**
 * This class calculates the total cost of a path from the list of nodes
 * that form it, using the {@link CostFunction} to do the evaluations.
 * 
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 26-03-2013
 * @version 1.0
 */
public class NumericCostEvaluator<S> implements CostEvaluator<S, Double>{

    /**
     * Evaluates the cost of the path using Double values
     * @param path list of nodes of the path
     * @param costFunction cost function used to evaluate transitions
     * @return total value of the path
     */
    public Double evaluate(List<Node<S>> path, CostFunction<S, Double> costFunction) {
        Double total = 0d;
        for (Iterator<Node<S>> it = path.iterator(); it.hasNext();) {
            Node<S> n = it.next();
            total += costFunction.evaluate(n.transition());
        }
        return total;
    }


}
