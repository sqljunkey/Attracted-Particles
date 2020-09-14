package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;

public class Vector {

	public List<Double> vectorMultiplication(List<Double> vector, Double magnitude) {

		List<Double> multiplied = new ArrayList<>();

		if (magnitude < 0) {

			magnitude = 0.0;

		}
		System.out.println(magnitude);
		for (Double x : vector) {

			multiplied.add(x * magnitude);

		}

		return multiplied;

	}

	public List<Double> getNormalized(List<Double> vector) {

		List<Double> normal = new ArrayList<>();

		// Get the magnitude of the vector,

		Double power = 0.0;
		for (Double x : vector) {

			power += Math.pow(x, 2);

		}

		Double magnitude = Math.sqrt(power);

		// Normalize and add to normal vector;

		for (Double x : vector) {
			normal.add(x / magnitude);
		}

		return normal;

	}

	public List<Double> addVector(List<Double> vectorLeft, List<Double> vectorRight) {

		List<Double> vector = new ArrayList<>();
		// Check if vectors are same dimension

		for (int i = 0; i < vectorLeft.size(); i++) {

			vector.add(vectorLeft.get(i) + vectorRight.get(i));

		}

		return vector;

	}

}
