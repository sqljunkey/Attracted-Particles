package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Particle System Class controls all the motions of the particles through assigning weights to the particles based on potential forces
//found in the array of potentials

public class ParticleSystem {

	List<Particle> particles = new ArrayList<>();

	//Constructor for the Particle System Class takes number of particles and spread between particles.
	//The particles initial location are randomly assigned times the spread.
	
	// Spread sets the initial distance between particles. This is intend to control the "rest"
	// speeds of the particles once the particles have found an equilibrium momentum
	// This of drawing a bow and arrow and letting go.

	

	ParticleSystem(Integer numberOfParticles, Double spread) {
		Random r = new Random();

		for (int i = 0; i < numberOfParticles; i++) {
			
			//if(i<numberOfParticles/2) {

			particles.add(new Particle(Math.abs(r.nextGaussian() * 10000000.1), // Mass
					("Particle" + i), // Particle Name
					r.nextGaussian() * spread, // X Position
					r.nextGaussian() * spread, // Y Position
					r.nextGaussian() * spread, // Z Position
					.001

			));//}
			
		/*	else {
				
				particles.add(new Particle(Math.abs(r.nextGaussian() * 10.1), // Mass
						("Particle" + i), // Particle Name
						(r.nextGaussian() * spread)+15000, // X Position
						(r.nextGaussian() * spread)+15000, // Y Position
						(r.nextGaussian() * spread)+15000, // Z Position
						.001

				));	
			}
			*/

			

		}

	}

	Particle getParticle(int i) {

		return particles.get(i);
	}

	// Calculate Vector will go into a nested iteration of all of the particles one
	// by one and calculate each momentum vector and
	// add these up to come up with a single motion vector. 
	
	//Then these vectors will be stored into the particle and used to move the
	// particle in the final portion of the code.

	public void calculateVectors() {

	
		
		//I compare all the particles with other particles in the particles list in this nested loop. 
		//I do all this except for the particle being evaluated against. 
		
		for (int i = 0; i < particles.size(); i++) {
			Double momentumX = 0.0;
			Double momentumY = 0.0;
			Double momentumZ = 0.0;

			for (int j = 0; j < particles.size(); j++) {

				//This insures particles.get(i) doesn't get evaluated, because being at distance 0 from itself, it would have the strongest weight.
				//This would through the other force potentials off and possibility produce a division by zero somewhere. 
				
				if (!particles.get(i).equals(particles.get(j))) {
					
					
					
					//Here I subtract particle.get(i) location components, X, Y, Z  with
					//particle.get(j) location components, for some reason it works better by putting a minus sign in 
					//front of particle.get(i) and then adding particle.get(j). 

					Double dX = -(particles.get(i).getX()) + (particles.get(j).getX());
					Double dY = -(particles.get(i).getY()) + (particles.get(j).getY());
					Double dZ = -(particles.get(i).getZ()) + (particles.get(j).getZ());
					
					//I calculate DS or distance using the Pythagoras formula c^2=a^2+b^2. 
					//This gives me a positive distance  between the particle.get(i) particle
					//and the particle.get(j).
					
					Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
					
					//I then re-use the dX to normalize the distance and direction.
					//I multiply it with one of the force Potentials Functions that take a distance unit
					//to assign a weight force between particle.get(i)
					// and particle.get(j) which will be summed up later to create one vector. T
					
					

					dX = (dX / 1) * ljPotential(dS)* particles.get(j).mass;
					dY = (dY / 1) * ljPotential(dS)* particles.get(j).mass;
					dZ = (dZ / 1) * ljPotential(dS)* particles.get(j).mass;
					
					//I add up the resulting vector to the "running count" of vectors, creating one general momentum in directions, X, Y, Z
					//Depending on the result of the Potential function the added vector can have more or less effect on the total momentum. 
					
					//This way even though there are many forces 
					// acting on particle.get(i) it will tend in the direction of the most forceful vector. 
					//More information can be found here http://mathworld.wolfram.com/VectorAddition.html
					momentumX += dX;
					momentumY += dY;
					momentumZ += dZ;

				}

			}

			//Finally I add the momentum components to the particles.
			//The addMomentum adds the old momentum and this new momentum times the decay.
			// This insures the particle will move in small enough steps and that the energy is conserved.
			particles.get(i).addMomentumX(momentumX);
			particles.get(i).addMomentumY(momentumY);
			particles.get(i).addMomentumZ(momentumZ);
			
			

		}

		//After having calculated all the forces on the particles 
		//I then change the positions of the particle using the momentum components. 
		

		for (int i = 0; i < particles.size(); i++) {


			particles.get(i).setLocationX(particles.get(i).getX() + (particles.get(i).getMomentumX()));
			particles.get(i).setLocationY(particles.get(i).getY() + (particles.get(i).getMomentumY()));
			particles.get(i).setLocationZ(particles.get(i).getZ() + (particles.get(i).getMomentumZ()));

		}

	}

	// Heated potential adds a random movement to the particles as their distances
	// decrease, thus simulating the friction caused when atoms collide.

	public Double hPotential(Double distance) {

		Random r = new Random();
		Double h = (distance * 0.000001) + r.nextGaussian() * (1 / distance);

		return h;
	}

	// nPotential function takes the distance between two particles and returns a
	// force potential. This is equivalent to the Newton
	// general law of gravitation F = GMm/r^2. Since at low distances the particles
	// have more force and more motion I inverse the potential at some
	// arbitrary distance to cause the particles to repel each other.

	// A random component is added to simulate heat in the particles or erratic motion
	// this will cause the particles to have a non-linear attraction to each other.

	public Double nPotential(Double distance) {

		Double newton = 0.0;
		Random r = new Random();
		newton = 1 / Math.pow(distance, 3) *500;//* Math.abs(r.nextGaussian())*100;

		if (newton > .01) {
			newton *= -1.0;

		}
		
		/*if(newton <.0001) {
			newton = 0.000001;
		}*/

		return newton;
	}

	

	// ljPotential is a simple Potential used in Molecular Dynamics which will repel
	// a particle as it gets close another particle.
	// You can check it out at this link.
	// https://en.wikipedia.org/wiki/Lennard-Jones_potential. But currently one of
	// it's pitfalls is that it will
	// propel the particle to far once the particle gets too close to another
	// particle.

	public Double ljPotential(Double distance) {

		Double ljPot_1 = 0.0;
		Double ljPot_2 = 0.0;
		Double diff=1E-14;

		Double epsilon = 1.1;
		Double sigma = 10.;

		
		ljPot_1 = epsilon* (Math.pow((sigma / (distance-diff)), 4) - 2*Math.pow((sigma / (distance-diff)), 2));
		ljPot_2 =epsilon* (Math.pow((sigma / (distance)), 4) - 2*Math.pow((sigma / (distance)), 2));
	

		return (ljPot_2- ljPot_1);//*1.0E+11 ;
	}

}
