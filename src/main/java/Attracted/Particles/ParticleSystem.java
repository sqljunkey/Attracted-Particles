package Attracted.Particles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

//Particle System Class controls all the motions of the particles through assigning weights to the particles based on potential forces
//found in the array of potentials

public class ParticleSystem {

	// List of particles
	List<Particle> particles = new ArrayList<>();
	List<Particle> testParticles = new ArrayList<>();

	// Mesh Object, is a interconnected system of section formed by particles.
	List<GeometricGrouping> mesh = new ArrayList<>();

	// List of potentials to choose from
	List<Potentials> potentialLibrary = new ArrayList<>();

	int iter = 0;

	Double averageEnergy = 0.0;
	Double targetEnergy = 0.0;
	Double decayRatio = 1.0;

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

		if (false) {

			// Random location generator
			Random r = new Random();

			for (int i = 0; i < numberOfParticles; i++) {

				// Create 5 random connections

				particles.add(new Particle(Math.abs(r.nextGaussian() * (double) mass), // Mass
						("Particle" + i), // Particle Name
						r.nextGaussian() * spread, // X Position
						r.nextGaussian() * spread, // Y Position
						r.nextGaussian() * spread, // Z Position
						decayRatio // Decay / Lag

				));// }

			}
		} else {

			// Create Mesh based on numberOfParticles but otherwise mesh that has n mesh
			// sections in each axis.

			createMesh(numberOfParticles);

		}

	}

	// Return a particle

	Particle getParticle(int i) {

		return particles.get(i);
	}

	// Calculate Vector will go into a nested iteration of all of the particles one
	// by one and calculate each momentum vector and
	// add these up to come up with a single motion vector.

	// Then these vectors will be stored into the particle and used to move the
	// particle in the final portion of the code.

	public void calculateVectors(Double tempDecay) {

		Double maxDist = potentialLibrary.get(0).cutoffPotential();
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

					Double dX = -(particles.get(i).getX() + particles.get(i).mass / 2)
							+ (particles.get(j).getX() + particles.get(j).mass / 2);
					Double dY = -(particles.get(i).getY() + particles.get(i).mass / 2)
							+ (particles.get(j).getY() + particles.get(j).mass / 2);
					Double dZ = -(particles.get(i).getZ() + particles.get(i).mass / 2)
							+ (particles.get(j).getZ() + particles.get(j).mass / 2);

					// I calculate DS or distance using the Pythagoras formula c^2=a^2+b^2.
					// This gives me a positive distance between the particle.get(i) particle
					// and the particle.get(j).

					Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));

					if (dS < maxDist) {
						// I then re-use the dX to normalize the distance and direction.
						// I multiply it with one of the force Potentials Functions that take a distance
						// unit
						// to assign a weight force between particle.get(i)
						// and particle.get(j) which will be summed up later to create one vector. T

						dX = (dX / 1) * potentialLibrary.get(0).bezierPotential(dS) * particles.get(j).mass;
						dY = (dY / 1) * potentialLibrary.get(0).bezierPotential(dS) * particles.get(j).mass;
						dZ = (dZ / 1) * potentialLibrary.get(0).bezierPotential(dS) * particles.get(j).mass;

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

			}
			// Calculate total Energy
			Double totalEnergy = 0.0;
			for (Particle p : particles) {

				Double energy = 0.0;

				energy += Math.abs(p.getMomentumX());
				energy += Math.abs(p.getMomentumY());
				energy += Math.abs(p.getMomentumZ());

				totalEnergy += energy / 3;
			}

			averageEnergy = totalEnergy / particles.size();

			if (averageEnergy > targetEnergy) {

				decayRatio = 1 - tempDecay;// 0.99;
				for (Particle p : particles) {
					p.setDecay(decayRatio);
				}

			} else if (averageEnergy < targetEnergy) {
				decayRatio = 1 + tempDecay;// 1.01;
				for (Particle p : particles) {
					p.setDecay(decayRatio);
				}

			}

			// Finally I add the momentum components to the particles.
			// The addMomentum adds the old momentum and this new momentum times the decay.
			// This insures the particle will move in small enough steps and that the energy
			// is conserved.
			particles.get(i).addMomentumX(momentumX);
			particles.get(i).addMomentumY(momentumY);
			particles.get(i).addMomentumZ(momentumZ);

			particles.get(i).setLocationX(particles.get(i).getX() + particles.get(i).getMomentumX());
			particles.get(i).setLocationY(particles.get(i).getY() + particles.get(i).getMomentumY());
			particles.get(i).setLocationZ(particles.get(i).getZ() + particles.get(i).getMomentumZ());

		}

		// After having calculated all the forces on the particles
		// I then change the positions of the particle using the momentum components.

		for (int i = 1; i < particles.size(); i++) {

			particles.get(i).setLocationX(particles.get(i).getX() + particles.get(i).getMomentumX());
			particles.get(i).setLocationY(particles.get(i).getY() + particles.get(i).getMomentumY());
			particles.get(i).setLocationZ(particles.get(i).getZ() + particles.get(i).getMomentumZ());

		}

	}

	void createMesh(int size) {

		Double distance = 100.0;

		// First create a series of 8 points to represent a cube in the mesh then join
		// the adjoining vertices by comparing their locations.

		// Random Mass
		Random r = new Random();

		for (int x = 0; x < size; x++) {

			for (int y = 0; y < size; y++) {

				for (int z = 0; z < size; z++) {

					// Create Block Mesh Section
					GeometricGrouping sec = new GeometricGrouping("MeshSection");

					// Assign arbitrary potential to block to initialize it.

					sec.assignPotential(potentialLibrary.get(0));

					// X axis of Block

					for (int xx = 0; xx < 2; xx++) {

						// Y axis of Block

						for (int yy = 0; yy < 2; yy++) {

							// Z axis of Block

							for (int zz = 0; zz < 2; zz++) {

								// Create new Particle

								particles.add(new Particle(1.0, // Math.abs(r.nextGaussian()), // Mass
										("Particle" + x + "_" + y + "_" + z + "_" + xx + "_" + yy + "_" + zz), // Particle
																												// Name
										(double) x * distance + distance * xx, // X Position
										(double) y * distance + distance * yy, // Y Position
										(double) z * distance + distance * zz, // Z Position
										decayRatio // Decay / Lag

								));

								sec.addParticle("Particle" + x + "_" + y + "_" + z + "_" + xx + "_" + yy + "_" + zz);

							}
						}
					}

					// Add Block to mesh
					mesh.add(sec);

				}

			}

		}

		System.out.println("Mesh Created");
		System.out.println(particles.size());
		System.out.println("Joining Mesh....");

		List<Integer> deletes = new ArrayList<>();
		// Compare vertices by location and if they are the same delete them and leave
		// only one.

		Iterator<Particle> iterator = particles.iterator();
		List<String> delete = new ArrayList<>();
		while (iterator.hasNext()) {

			Particle p = iterator.next();
			if (delete.contains(p.particleName)) {
				iterator.remove();
			} else {
				for (Particle c : particles) {

					if (!c.equals(p)) {

						if (p.locationX.equals(c.locationX) && p.locationY.equals(c.locationY)
								&& p.locationZ.equals(c.locationZ)) {
							/*
							 * System.out.println(p.locationX + " " + c.locationX + " " + p.locationY + " "
							 * + c.locationY + " " + p.locationZ + " " + c.locationZ + " ");
							 * 
							 * System.out.println(p.particleName); System.out.println(c.particleName);
							 */
							delete.add(c.particleName);

							for (int i = 0; i < mesh.size(); i++) {

								for (int j = 0; j < mesh.get(i).block.size(); j++) {

									if (mesh.get(i).block.get(j).contentEquals(c.particleName)) {

										mesh.get(i).block.set(j, p.particleName);
									}

								}

							}

						}

					}

				}
			}

		}

		System.out.println("Joining Mesh Done.");
		System.out.println(particles.size());

		// Turn particle names in mesh into index locations for easy access
		System.out.println("Normalizing");

		for (int i = 0; i < particles.size(); i++) {

			for (int j = 0; j < mesh.size(); j++) {

				for (int f = 0; f < mesh.get(j).block.size(); f++) {

					if (particles.get(i).particleName.contentEquals(mesh.get(j).block.get(f))) {

						mesh.get(j).addLocation(i);

					}

				}
			}

		}

		/// Create Vertex Based on closest Points.

		System.out.println("Creating Vertices");

		for (GeometricGrouping meshSection : mesh) {

			for (Integer locationx : meshSection.blockLocation) {

				// create a vertex

				List<Integer> vertex = new ArrayList<>();

				for (Integer locationy : meshSection.blockLocation) {

					if (locationx != locationy) {

						Double dX = -(particles.get(locationx).getX()) + (particles.get(locationy).getX());
						Double dY = -(particles.get(locationx).getY()) + (particles.get(locationy).getY());
						Double dZ = -(particles.get(locationx).getZ()) + (particles.get(locationy).getZ());

						Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));

						System.out.println(dS);

						if (dS.equals(distance)) {

							vertex.add(locationy);

						}

					}

				}

				System.out.println("====================");

				// Add new Vertex to MeshSection
				meshSection.addGroup(new GeometricGrouping("vertex", vertex));

			}

		}

		// Copy two test particles

		testParticles.add(new Particle(1.0,"Test 1",  -330.4, -330.3, -330.2, 0.001 ));
		testParticles.add(new Particle(1.0,"Test 2", 1104.3, 1104.0, 1102.3, 0.001 ));
		// Set one Mesh to count the mass or have a different potential

		// mesh.get(mesh.size() /2-50).assignPotential(potentialLibrary.get(1));
		// mesh.get((mesh.size() /2+50)).assignPotential(potentialLibrary.get(1));

	}

	// Mesh deformity test
	void iterateMeshPotential() {

		if (iter < mesh.size() - 2) {
			mesh.get(iter).assignPotential(potentialLibrary.get(0));

			iter++;

			mesh.get(iter).assignPotential(potentialLibrary.get(1));
		} else {

			mesh.get(iter).assignPotential(potentialLibrary.get(0));

			iter++;

			mesh.get(iter).assignPotential(potentialLibrary.get(1));

			iter = 0;

		}

	}

	// search all the meshes for common vertices and find the one with the largest
	// volume and migrate into it.
	List<Integer> searchNextSection(GeometricGrouping sec) {
		List<Integer> adjcents = new ArrayList<>();

		for (Integer p : sec.blockLocation) {

			for (int i = 0; i < mesh.size(); i++) {
				if (!mesh.get(i).equals(sec))
					for (Integer c : mesh.get(i).blockLocation) {
						// found an adj
						if (c == p) {

							adjcents.add(i);
							break;

						}

					}

			}

		}

		return adjcents;

	}

	// Calculate Area based on total length

	Double calculateDistances(int location) {

		Integer first = mesh.get(location).blockLocation.get(0);
		Double totaldS = 0.0;

		for (Integer p : mesh.get(location).blockLocation) {

			Double dS = 0.0;
			// if not the first one in the array
			if (p != first) {

				Double dX = -(particles.get(first).getX()) + (particles.get(p).getX());
				Double dY = -(particles.get(first).getY()) + (particles.get(p).getY());
				Double dZ = -(particles.get(first).getZ()) + (particles.get(p).getZ());

				// I calculate DS or distance using the Pythagoras formula c^2=a^2+b^2.
				// This gives me a positive distance between the particle.get(i) particle
				// and the particle.get(j).

				totaldS += Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
			}

		}

		return totaldS;
	}

	void moveMesh(Integer location) {

		List<Integer> adj = searchNextSection(mesh.get(location));

		Double runningDistance = 0.0;
		Integer biggestMeshSection = 0;
		for (Integer i : adj) {

			Double totalDistance = calculateDistances(i);

			if (totalDistance > runningDistance) {

				runningDistance = totalDistance;
				biggestMeshSection = i;
			}
		}

		// finally switch
		System.out.println("New: " + biggestMeshSection + " Old: " + location);
		mesh.get(location).assignPotential(potentialLibrary.get(0));
		mesh.get(biggestMeshSection).assignPotential(potentialLibrary.get(1));
	}

	void calculateMesh() {

		// iterateMeshPotential() ;
		// Iterate thru all the vertices and adjust distance based on proximity to mass
		// vertex
		// This way we can create elasticity in the space.
		// vertices will try to adjust to the shape.

		// Iterate thru each vertexes and do a reverse move on the inner vertex based on
		// mass and distance.
		for (GeometricGrouping sec : mesh) {

			List<Double> distances = new ArrayList<>();

			for (GeometricGrouping vertex : sec.groups) {

				for (Integer p : vertex.blockLocation) {

					Double momentumX = 0.0;
					Double momentumY = 0.0;
					Double momentumZ = 0.0;

					for (Integer c : vertex.blockLocation) {

						if (c != p) {

							Double dX = -(particles.get(p).getX()) + (particles.get(c).getX());
							Double dY = -(particles.get(p).getY()) + (particles.get(c).getY());
							Double dZ = -(particles.get(p).getZ()) + (particles.get(c).getZ());

							// I calculate DS or distance using the Pythagoras formula c^2=a^2+b^2.
							// This gives me a positive distance between the particle.get(i) particle
							// and the particle.get(j).

							Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));

							// Double pot = sec.meshPotential.nPotential(dS);
							Double pot = 0.0;
							pot = sec.meshPotential.simpleString(dS);

							/*
							 * if (sec.equals(mesh.get(mesh.size() / 2))) {
							 * 
							 * pot = potentialLibrary.get(2).bezierPotential(dS); }
							 * 
							 * else { pot = potentialLibrary.get(3).bezierPotential(dS); }
							 * 
							 */
							dX = (dX / 1) * pot * particles.get(c).mass*.001;
							dY = (dY / 1) * pot * particles.get(c).mass*.001;
							dZ = (dZ / 1) * pot * particles.get(c).mass*.001;

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

							// }

						} // End if(c!=p)
					} // End c

					particles.get(p).addMomentumX(momentumX);
					particles.get(p).addMomentumY(momentumY);
					particles.get(p).addMomentumZ(momentumZ);

				} // End p Loop

			} // End Vertex Loop

		} // End Mesh Loop

		// Calculate Deformation.
		
		
	for(int j =0; j < testParticles.size();j++) {
			for (int i =0; i<particles.size(); i++) {

				
					 
				
					Double dX = -(testParticles.get(j).getX()) + (particles.get(i).getX());
					Double dY = -(testParticles.get(j).getY()) + (particles.get(i).getY());
					Double dZ = -(testParticles.get(j).getZ()) + (particles.get(i).getZ());

					Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
					

					
					Double pot = mesh.get(0).meshPotential.nPotential(dS);
					
					Double momentumX = (dX / 1) * pot * testParticles.get(j).mass *2;
					Double momentumY = (dY / 1) * pot * testParticles.get(j).mass *2;
					Double momentumZ = (dZ / 1) * pot * testParticles.get(j).mass *2;
					

					
					particles.get(i).addMomentumX(momentumX);
					particles.get(i).addMomentumY(momentumY);
					particles.get(i).addMomentumZ(momentumZ);

				}
	}

			
	
 
		Double totalEnergy = 0.0;
		for (Particle p : particles) {

			Double energy = 0.0;

			energy += Math.abs(p.getMomentumX());
			energy += Math.abs(p.getMomentumY());
			energy += Math.abs(p.getMomentumZ());

			totalEnergy += energy / 3;
		}

		averageEnergy = totalEnergy / particles.size();

		if (averageEnergy > targetEnergy) {

			decayRatio = 0.99999;
			for (Particle p : particles) {
				p.setDecay(decayRatio);
			}

		} else if (averageEnergy < targetEnergy) {
			decayRatio = 1.00001;
			for (Particle p : particles) {
				p.setDecay(decayRatio);
			}

		}
		
		
		testParticles.get(0).setLocationX(testParticles.get(0).getX()+0.01);
		testParticles.get(0).setLocationY(testParticles.get(0).getY()+0.01);
		testParticles.get(0).setLocationZ(testParticles.get(0).getZ()+0.01);
		

		testParticles.get(1).setLocationX(testParticles.get(1).getX()-0.01);
		testParticles.get(1).setLocationY(testParticles.get(1).getY()-0.01);
		testParticles.get(1).setLocationZ(testParticles.get(1).getZ()-0.01);

		for (int i = 0; i < particles.size(); i++) {

			particles.get(i).setLocationX(particles.get(i).getX() + particles.get(i).getMomentumX());
			particles.get(i).setLocationY(particles.get(i).getY() + particles.get(i).getMomentumY());
			particles.get(i).setLocationZ(particles.get(i).getZ() + particles.get(i).getMomentumZ());
		}
	}

	Double getEnergy() {

		return averageEnergy;
	}

	Double getDecay() {

		return decayRatio;
	}

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
			br.write((particles.size()+testParticles.size()) + "\n");
			 br.write("\n");

			for (Particle p : particles) {

				// Append into file particle per particle all separated by white space.

				br.write(p.getColor() + " " // Write particle color
						+ p.getX() + " " // Write X location
						+ p.getY() + " " // Write Y location
						+ p.getZ() + "\n"); // Write Z location

			}
			
			for(Particle p: testParticles) {
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
