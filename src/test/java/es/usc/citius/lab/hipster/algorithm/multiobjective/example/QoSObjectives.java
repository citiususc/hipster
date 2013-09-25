package es.usc.citius.lab.hipster.algorithm.multiobjective.example;

public class QoSObjectives {
	public int throughput;
	public double responseTime;
	
	public QoSObjectives(double responseTime, int throughput) {
		this.throughput = throughput;
		this.responseTime = responseTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(responseTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + throughput;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QoSObjectives other = (QoSObjectives) obj;
		if (Double.doubleToLongBits(responseTime) != Double
				.doubleToLongBits(other.responseTime))
			return false;
		if (throughput != other.throughput)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return responseTime + " ms / " + throughput + " th";
	}
	
	
}
