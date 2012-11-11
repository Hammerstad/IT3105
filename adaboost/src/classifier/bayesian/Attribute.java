package classifier.bayesian;

import java.util.Map;

public class Attribute {
	private Map<Double, Double> attributes;

	public Attribute(Map<Double, Double> attributes) {
		this.attributes = attributes;
	}

	public double getValue(double position) {
		if (attributes.containsKey(position)) {
			return attributes.get(position);
		}
		return 0;
	}
}
