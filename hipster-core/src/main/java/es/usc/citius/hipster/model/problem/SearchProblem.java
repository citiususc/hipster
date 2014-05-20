package es.usc.citius.hipster.model.problem;

import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.NodeExpander;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class SearchProblem<A,S,N extends Node<A,S,N>> {
    private N initialNode;
    private NodeExpander<A,S,N> expander;

    public SearchProblem(N initialNode, NodeExpander<A, S, N> expander) {
        this.initialNode = initialNode;
        this.expander = expander;
    }

    public N getInitialNode() {
        return initialNode;
    }

    public NodeExpander<A, S, N> getExpander() {
        return expander;
    }
}
