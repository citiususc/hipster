package es.usc.citius.lab.hipster.testutils;

import java.util.Iterator;

import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.util.Operable;
import es.usc.citius.lab.hipster.util.Scalable;

public final class SearchIterators {

	private SearchIterators(){};
	
	public static <S,T extends Operable<T>> Iterator<? extends CostNode<S, T>> createAStar(SearchComponentFactory<S, T> componentFactory){
		return new AStarIteratorFactory<S,T>(componentFactory).buildIteratorSearch();
	}
	
	public static <S,T extends Operable<T>> Iterator<? extends CostNode<S, T>> createBellmanFord(SearchComponentFactory<S, T> componentFactory){
		return new BellmanFordIteratorFactory<S,T>(componentFactory).buildIteratorSearch();
	}
	
	public static <S,T extends Scalable<T>> Iterator<? extends CostNode<S, T>> createADStar(SearchComponentFactory<S, T> componentFactory){
		return new ADStarIteratorFactory<S,T>(componentFactory).buildIteratorSearch();
	}

}
