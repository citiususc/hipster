package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.util.Iterators;

import java.util.Iterator;


public class BidirectionalHPA<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {
    private final AStar<A,S,C,N> forwardAStar;
    private final AStar<A,S,C,N> backwardsAStar;

    public BidirectionalHPA(AStar<A, S, C, N> forwardAStar, AStar<A, S, C, N> backwardsAStar) {
        this.forwardAStar = forwardAStar;
        this.backwardsAStar = backwardsAStar;
    }

    @Override
    public Iterator<N> iterator() {
        AStar<A, S, C, N>.Iterator it0 = forwardAStar.iterator();
        AStar<A, S, C, N>.Iterator it1 = backwardsAStar.iterator();
        C best = null;

        return new Iterators.AbstractIterator<N>(){
            @Override
            protected N computeNext() {

            }
        };
    }
}
