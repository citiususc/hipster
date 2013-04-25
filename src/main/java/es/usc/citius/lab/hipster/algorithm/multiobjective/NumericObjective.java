package es.usc.citius.lab.hipster.algorithm.multiobjective;

public class NumericObjective implements Comparable<NumericObjective> {

	private String name = "Undefined";
	private double value = 0d;
	private double maxValue = Double.MAX_VALUE;
	private double minValue = Double.MIN_VALUE;
	private boolean minimize = true;
	
	
	
	public NumericObjective(String name, double value, double maxValue,
			double minValue, boolean minimize) {
		super();
		this.name = name;
		this.value = value;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.minimize = minimize;
	}

	public boolean isFeasible(){
		return value >= minValue && value <= maxValue;
	}

	
	/*
	public int compareTo(Objective<Double> obj) {
		NumericObjective o = (NumericObjective)obj;
		if (this.minimize != o.minimize || this.maxValue != o.maxValue || this.minValue != o.minValue){
			throw new IllegalArgumentException("Objectives are not comparable");
		}
		// TODO: Invert if the goal is to maximize objectives
		return Double.compare(this.value, o.value);
	}*/

	public Double getValue() {
		return this.value;
	}

	
	public int compareTo(NumericObjective o) {
		if (this.minimize != o.minimize || this.maxValue != o.maxValue || this.minValue != o.minValue){
			throw new IllegalArgumentException("Objectives are not comparable");
		}
		// TODO: Invert if the goal is to maximize objectives
		return Double.compare(this.value, o.value);
	}
}
