package es.usc.citius.lab.hipster.util;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.node.Node;
import java.util.List;

/**
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public interface CostEvaluator<S, T> {
    
    public T evaluate(List<Node<S>> path, CostFunction<S, T> costFunction);
    
}
