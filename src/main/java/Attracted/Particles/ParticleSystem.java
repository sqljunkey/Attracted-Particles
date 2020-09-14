package Attracted.Particles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

//Particle System Class controls all the motions of the particles through assigning weights to the particles based on potential forces
//found in the array of potentials

public class ParticleSystem {

	// List of particles

	List<Particle> testParticles = new ArrayList<>();

	// Mesh Object, is a interconnected system of section formed by particles.
	Mesh mesh = new Mesh();

	// List of potentials to choose from
	List<Potentials> potentialLibrary = new ArrayList<>();

	int iter = 0;
	Double totalMass = 0.0;

	Double averageEnergy = 0.0;
	Double targetEnergy = 0.0;
	Double decayRatio = 1.0;// .99999;
	Double boxSize = 100000.0;
	int numberOfParticles = 0;

	// Constructor for the Particle System Class takes number of particles and
	// spread between particles.
	// The particles initial location are randomly assigned times the spread.

	// Spread sets the initial distance between particles. This is intend to control
	// the "rest"
	// speeds of the particles once the particles have found an equilibrium momentum
	// This of drawing a bow and arrow and letting go.

	ParticleSystem(Integer numberOfParticles, Double spread, Double difference, Integer mass, Double targetEnergy,
			List<List<Point>> points, List<Double> springs) {

		// Iterate throughout list of spring potentials and assign each

		for (Double spring : springs) {

			potentialLibrary.add(new Potentials(difference));
			potentialLibrary.get(potentialLibrary.size() - 1).setTargetDistance(spring);
		}
		// Iterate throughout the list of points and create a potential for each of
		// them.
		for (List<Point> p : points) {

			potentialLibrary.add(new Potentials(difference));
			potentialLibrary.get(potentialLibrary.size() - 1).setPotentialCurve(p);

		}

		// Set Target Energy for system to reach
		this.targetEnergy = targetEnergy;
		// Random location generator
		Random r = new Random();

		// Create Mesh based on numberOfParticles but otherwise mesh that has n mesh
		// sections in each axis.
		this.numberOfParticles = numberOfParticles;
		mesh.createMesh(numberOfParticles, boxSize, decayRatio);

		for (int i = 0; i < 300; i++) {
			testParticles.add(new Particle(Math.abs(2000.0*Math.abs(r.nextGaussian())), // Mass
					("Particle"), // Particle Name
					r.nextGaussian() * spread + 50000.0, // X Position
					r.nextGaussian() * spread + 50000.0, // Y Position
					r.nextGaussian() * spread + 50000.0, // Z Position
					decayRatio // Decay / Lag*

			));}

		testParticles.add(new Particle(Math.abs(2000.0), // Mass
				("Particle"), // Particle Name
				r.nextGaussian() * spread + 50000.0, // X Position
				r.nextGaussian() * spread + 50000.0, // Y Position
				r.nextGaussian() * spread + 50000.0, // Z Position
				decayRatio // Decay / Lag

		));

		for (Particle t : testParticles) {

			totalMass += t.mass;

		}

	}

	// Return a particle

	void calculateMassDistribution() {

		mesh.calculatePotential(testParticles);

		// Insert here

		for (int i = 0; i < testParticles.size(); i++) {

			testParticles.get(i).setLocationX(testParticles.get(i).getX() + testParticles.get(i).getMomentumX());
			testParticles.get(i).setLocationY(testParticles.get(i).getY() + testParticles.get(i).getMomentumY());
			testParticles.get(i).setLocationZ(testParticles.get(i).getZ() + testParticles.get(i).getMomentumZ());
		}

	}

	// Calculate PIC

	// This function will dump the particle locations to a file for later viewing or
	// processing. We are using the .xyz file format.

	void dumpToFile(String filename) {

		// Open the file, and first write the number of particles, then a blank space
		// and then append the color, and x, y ,z locations to the file.

		try {

			// I'm using bufferedWritter because I want to be consistent???

			File file = new File(filename);
			FileWriter fr = new FileWriter(file, true);
			BufferedWriter br = new BufferedWriter(fr);

			// Write the amount of particles in the system , to indicate start of new dump.

			int totalSize = 0;

			totalSize += /* particles.size()) */testParticles.size();

			br.write(totalSize + "\n");
			br.write("\n");

			for (Particle p : testParticles) {
				br.write(p.getColor() + " " // Write particle color
						+ p.getX() + " " // Write X location
						+ p.getY() + " " // Write Y location
						+ p.getZ() + "\n"); // Write Z location

			}

			// Close all opened file writing mechanisms.
			br.close();
			fr.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
