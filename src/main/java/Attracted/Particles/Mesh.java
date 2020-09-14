package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;

public class Mesh {

	List<List<List<Node>>> network = new ArrayList<>();

	List<Integer[]> activeNodes = new ArrayList<>();

	// Mesh Creator
	void createMesh(int particleCount, Double boxSize, Double decayRatio) {

		Double interval = boxSize / particleCount;

		for (int x = 0; x <= particleCount; x++) {
			// X
			network.add(new ArrayList<>());
			for (int y = 0; y <= particleCount; y++) {
				// Y
				int last = network.size() - 1;
				network.get(last).add(new ArrayList<>());

				for (int z = 0; z <= particleCount; z++) {
					// Z
					int lastOfLast = network.get(last).size() - 1;
					network.get(last).get(lastOfLast).add(new Node(x, y, z, interval, decayRatio));

				}
			}
		}

	}

	private Integer[] findParticle(Particle particle) {

		Integer[] active = { 0 };
		for (int x = 0; x < network.size(); x++) {

			for (int y = 0; y < network.get(x).size(); y++) {

				for (int z = 0; z < network.get(x).get(y).size(); z++) {

					if (network.get(x).get(y).get(z).isInside(particle)) {

						activeNodes.add(new Integer[] { x, y, z });

						return new Integer[] { x, y, z };

					}

				}

			}

		}

		return active;
	}

	private void updateActiveNode(List<Particle> particles) {

		activeNodes.clear();

		for (Particle p : particles) {

			findParticle(p);

		}

	}

	public void calculatePotential(List<Particle> particles) {
		for (Integer[] activeNode : activeNodes) {
			// Clear Energies
			network.get(activeNode[0]).get(activeNode[1]).get(activeNode[2]).clear();
		}

		updateActiveNode(particles);
for (Particle p : particles) {
		for (Integer[] activeNode : activeNodes) {
			
				network.get(activeNode[0]).get(activeNode[1]).get(activeNode[2]).calculatePotential(p);

			}

		}

		for (Particle p : particles) {
			Integer[] activeNode = findParticle(p);
			
			
			
			Double[] vector = network.get(activeNode[0]).get(activeNode[1]).get(activeNode[2]).getVector(p);

			p.addMomentumX(vector[0]);
			p.addMomentumY(vector[1]);
			p.addMomentumZ(vector[2]);
		}
	}

}
