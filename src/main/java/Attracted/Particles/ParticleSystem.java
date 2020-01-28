package Attracted.Particles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Particle System Class controls all the motions of the particles through assigning weights to the particles based on potential forces
//found in the array of potentials

public class ParticleSystem {

	List<Particle> particles = new ArrayList<>();
	Potentials potential;

	// Constructor for the Particle System Class takes number of particles and
	// spread between particles.
	// The particles initial location are randomly assigned times the spread.

	// Spread sets the initial distance between particles. This is intend to control
	// the "rest"
	// speeds of the particles once the particles have found an equilibrium momentum
	// This of drawing a bow and arrow and letting go.

	ParticleSystem(Integer numberOfParticles, Double spread, Double difference) {
		Random r = new Random();

		potential = new Potentials( difference);
		for (int i = 0; i < numberOfParticles; i++) {

			// if(i<numberOfParticles/2) {

			particles.add(new Particle(Math.abs(r.nextGaussian() * 10000000.1), // Mass
					("Particle" + i), // Particle Name
					r.nextGaussian() * spread, // X Position
					r.nextGaussian() * spread, // Y Position
					r.nextGaussian() * spread, // Z Position
					.001

			));// }

			/*
			 * else {
			 * 
			 * particles.add(new Particle(Math.abs(r.nextGaussian() * 10.1), // Mass
			 * ("Particle" + i), // Particle Name (r.nextGaussian() * spread)+15000, // X
			 * Position (r.nextGaussian() * spread)+15000, // Y Position (r.nextGaussian() *
			 * spread)+15000, // Z Position .001
			 * 
			 * )); }
			 */

		}

	}

	Particle getParticle(int i) {

		return particles.get(i);
	}

	// Calculate Vector will go into a nested iteration of all of the particles one
	// by one and calculate each momentum vector and
	// add these up to come up with a single motion vector.

	// Then these vectors will be stored into the particle and used to move the
	// particle in the final portion of the code.

	public void calculateVectors() {

		// I compare all the particles with other particles in the particles list in
		// this nested loop.
		// I do all this except for the particle being evaluated against.

		for (int i = 0; i < particles.size(); i++) {
			Double momentumX = 0.0;
			Double momentumY = 0.0;
			Double momentumZ = 0.0;

			for (int j = 0; j < particles.size(); j++) {

				// This insures particles.get(i) doesn't get evaluated, because being at
				// distance 0 from itself, it would have the strongest weight.
				// This would through the other force potentials off and possibility produce a
				// division by zero somewhere.

				if (!particles.get(i).equals(particles.get(j))) {

					// Here I subtract particle.get(i) location components, X, Y, Z with
					// particle.get(j) location components, for some reason it works better by
					// putting a minus sign in
					// front of particle.get(i) and then adding particle.get(j).

					Double dX = -(particles.get(i).getX()) + (particles.get(j).getX());
					Double dY = -(particles.get(i).getY()) + (particles.get(j).getY());
					Double dZ = -(particles.get(i).getZ()) + (particles.get(j).getZ());

					// I calculate DS or distance using the Pythagoras formula c^2=a^2+b^2.
					// This gives me a positive distance between the particle.get(i) particle
					// and the particle.get(j).

					Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));

					// I then re-use the dX to normalize the distance and direction.
					// I multiply it with one of the force Potentials Functions that take a distance
					// unit
					// to assign a weight force between particle.get(i)
					// and particle.get(j) which will be summed up later to create one vector. T

					dX = (dX / 1) * potential.ljPotential(dS) * particles.get(j).mass;
					dY = (dY / 1) * potential.ljPotential(dS) * particles.get(j).mass;
					dZ = (dZ / 1) * potential.ljPotential(dS) * particles.get(j).mass;

					// I add up the resulting vector to the "running count" of vectors, creating one
					// general momentum in directions, X, Y, Z
					// Depending on the result of the Potential function the added vector can have
					// more or less effect on the total momentum.

					// This way even though there are many forces
					// acting on particle.get(i) it will tend in the direction of the most forceful
					// vector.
					// More information can be found here
					// http://mathworld.wolfram.com/VectorAddition.html
					momentumX += dX;
					momentumY += dY;
					momentumZ += dZ;

				}

			}

			// Finally I add the momentum components to the particles.
			// The addMomentum adds the old momentum and this new momentum times the decay.
			// This insures the particle will move in small enough steps and that the energy
			// is conserved.
			particles.get(i).addMomentumX(momentumX);
			particles.get(i).addMomentumY(momentumY);
			particles.get(i).addMomentumZ(momentumZ);

		}

		// After having calculated all the forces on the particles
		// I then change the positions of the particle using the momentum components.

		for (int i = 0; i < particles.size(); i++) {

			particles.get(i).setLocationX(particles.get(i).getX() + (particles.get(i).getMomentumX()));
			particles.get(i).setLocationY(particles.get(i).getY() + (particles.get(i).getMomentumY()));
			particles.get(i).setLocationZ(particles.get(i).getZ() + (particles.get(i).getMomentumZ()));

		}

	}



	// This function will dump the particle locations to a file for later viewing or
	// processing. We are using the .xyz file format.

	void dumpToFile(String filename) {

		
		//Open the file, and first write the number of particles, then a blank space and then append the color, and x, y ,z locations to the file.
		
		try {
			
			//I'm using bufferedWritter because I want to be consistent???
			
			File file = new File(filename);
			FileWriter fr = new FileWriter(file, true);
			BufferedWriter br = new BufferedWriter(fr);
			

			//Write the amount of particles in the system , to indicate start of new dump.
			br.write(particles.size()+"\n");
			//br.write("\n");
			
			for(Particle particle: particles) {
				
				//Append into file particle per particle all separated by white space.
				
				
				br.write(particle.getColor()+" "   //Write particle color
				+ particle.getX()+" "             //Write X location
				+ particle.getY()+" "             //Write Y location
				+ particle.getZ() +"\n" );             //Write Z location
				
			}
			
			
			//Close all opened file writting mechanisms.
			br.close();
			fr.close();
			
			

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
