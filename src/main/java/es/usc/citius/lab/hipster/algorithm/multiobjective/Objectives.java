package es.usc.citius.lab.hipster.algorithm.multiobjective;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Objectives<E extends Comparable<? super E>> extends AbstractList<E> implements Comparable<Objectives<E>>{

	private List<E> objectives = new ArrayList<E>();
	
	public boolean dominates(Objectives<E> objectives){
		if (this.size() != objectives.size()){
			throw new IllegalArgumentException("Uncomparable objectives (different sizes)");
		}
		boolean strictlyBetter = false;
		boolean dominates = true;
		for(E o1 : this){
			for(E o2 : objectives){
					int comparation = o1.compareTo(o2);
					dominates &= (comparation>=0);
					strictlyBetter = (comparation>0);
			}
		}
		return dominates && strictlyBetter;
	}

	@Override
	public E get(int index) {
		return this.objectives.get(index);
	}

	@Override
	public int size() {
		return this.objectives.size();
	}

	public int compareTo(Objectives<E> o) {
		if (this.size() != o.size()){
			throw new IllegalArgumentException("Objectives not comparable");
		}
		for(E a : objectives){
			for(E b : o){
				int compare = a.compareTo(b);
				if (compare != 0){
					return compare;
				}
			}
		}
		return 0;
	}
}
