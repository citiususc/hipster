package es.usc.citius.lab.hipster.function;

public final class Operations {

	private Operations() {
	}

	public static Operation<Double> addition() {
		return new Operation<Double>(Operators.accumulator(), 0d,
				Double.POSITIVE_INFINITY);
	}

	public static Operation<Double> multiplication() {
		return new Operation<Double>(Operators.scalator(), 1d,
				Double.POSITIVE_INFINITY);
	}

}
