package es.usc.citius.lab.hipster.node;

import java.util.Comparator;

public interface HeuristicNode<S> extends CostNode<S> {
	Comparator<? extends HeuristicNode<S>> scoreComparator();
	<N extends HeuristicNode<S>> int compareByScore(N node);
}
