package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;

public class Node {

	List<Particle> connection = new ArrayList<>();

	// Maximum distance between particles.

	Double maxX = 0.0;
	Double minX = 0.0;

	Double maxY = 0.0;
	Double minY = 0.0;

	Double maxZ = 0.0;
	Double minZ = 0.0;

	public Node(int x, int y, int z, Double interval, Double decayRatio) {

		for (int xx = 0; xx < 2; xx++) {

			// Y axis of Block

			for (int yy = 0; yy < 2; yy++) {

				// Z axis of Block

				for (int zz = 0; zz < 2; zz++) {

					connection.add(new Particle(1.0, // Math.abs(r.nextGaussian()), // Mass
							("Particle"), // Particle Name

							(double) x * interval + interval * xx, // X Position
							(double) y * interval + interval * yy, // Y Position
							(double) z * interval + interval * zz, // Z Position
							decayRatio // Decay / Lag

					));

				}

			}
		}

		maxX = (double) x * interval + interval * 1;
		minX = (double) x * interval + interval * 0;

		maxY = (double) y * interval + interval * 1;
		minY = (double) y * interval + interval * 0;

		maxZ = (double) z * interval + interval * 1;
		minZ = (double) z * interval + interval * 0;

	}

	public boolean isInside(Particle particle) {

		boolean isInBox = false;

		if (particle.getX() >= minX && particle.getX() <= maxX && particle.getX() >= minY && particle.getX() <= maxY
				&& particle.getX() >= minZ && particle.getX() <= maxZ

		) {

			isInBox = true;

		}

		return isInBox;

	}

	public void clear() {
		for (int x = 0; x < connection.size(); x++) {

			connection.get(x).clearEnergy();
		}

	}

	public void calculatePotential(Particle particle) {

		Potentials potential = new Potentials(0.0);
		
		if(isInside(particle)) {
			for (int x = 0; x < connection.size(); x++) {
				
				Double energy = particle.mass;
				connection.get(x).addEnergy(energy);
				
			}
			
		}else {

		for (int x = 0; x < connection.size(); x++) {

			Double dS = connection.get(x).getDistance(particle);

			Double energy = potential.pdPotential(particle.mass, .9999, dS);
			// Double energy = 50.0;
			connection.get(x).addEnergy(energy);

		}}
	}

	public Double[] getVector(Particle p) {

		List<List<Double>> distances = new ArrayList<>();
		List<Double> energies = new ArrayList<>();
		List<Double> localEnergies = new ArrayList<>();

		// Get Initial Distances
		for (Particle c : connection) {

			distances.add(c.getGaugedDistance(p, .001));
			energies.add(c.getEnergySum());

		}

		for (int i = 0; i < 4; i++) {

			Double totalDistance = 0.0;
			Double totalEnergy = 0.0;
			for (List<Double> distance : distances) {
				totalDistance += distance.get(i);

			}

			for (int k = 0; k < distances.size(); k++) {

				totalEnergy += distances.get(k).get(i) / totalDistance * (energies.get(k));

			}

			localEnergies.add(totalEnergy);
		}

		Double momentumVectorX = (localEnergies.get(1) - localEnergies.get(0))/ p.mass;
		Double momentumVectorY = (localEnergies.get(2) - localEnergies.get(0))/ p.mass;
		Double momentumVectorZ = (localEnergies.get(3) - localEnergies.get(0))/ p.mass;
		System.out.println(momentumVectorX+": mass: "+ p.mass);
		Double[] vector = { momentumVectorX, momentumVectorY, momentumVectorZ };

		System.out.println(momentumVectorX + "  :  " + momentumVectorY + "  :  " + momentumVectorZ + "  :  ");

		return vector;
	}

}
