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
	List<Particle> particles = new ArrayList<>();
	List<Particle> testParticles = new ArrayList<>();

	// Mesh Object, is a interconnected system of section formed by particles.
	List<Mesh> meshes = new ArrayList<>();

	// List of potentials to choose from
	List<Potentials> potentialLibrary = new ArrayList<>();

	int iter = 0;

	Double averageEnergy = 0.0;
	Double targetEnergy = 0.0;
	Double decayRatio = 1.0;//.99999;

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

		if (false) {

			
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

			createMesh(numberOfParticles * 10, -2000.0);

			testParticles.add(new Particle(Math.abs(1.0), // Mass
					("Particle" + 1), // Particle Name
					r.nextGaussian() * spread + 5000, // X Position
					r.nextGaussian() * spread + 5000, // Y Position
					r.nextGaussian() * spread + 5000, // Z Position
					decayRatio // Decay / Lag

			));

		  testParticles.add(new Particle(Math.abs(10.0), // Mass
					("Particle" + 2), // Particle Name
					r.nextGaussian() * spread +5200, // X Position
					r.nextGaussian() * spread+5200, // Y Position
					r.nextGaussian() * spread+5200, // Z Position
					decayRatio // Decay / Lag

			));
			 testParticles.add(new Particle(Math.abs(332946.0487), // Mass
					("Particle" + 3), // Particle Name
					r.nextGaussian() * spread+ 5000, // X Position
					r.nextGaussian() * spread+ 5000, // Y Position
					r.nextGaussian() * spread+ 5000, // Z Position
					decayRatio // Decay / Lag

			));/* */
		/*	testParticles.add(new Particle(Math.abs(10.0), // Mass
					("Particle" + 4), // Particle Name
					r.nextGaussian() * spread+ 5000, // X Position
					r.nextGaussian() * spread+ 5000, // Y Position
					r.nextGaussian() * spread+ 5000, // Z Position
					decayRatio // Decay / Lag

			)); */

			System.out.println("Number of total Particles: " + particles.size());

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

	void createMesh(int size, Double displacement) {

		List<GeometricGrouping> mesh = new ArrayList<>();
		//List<Particle> vertices = new ArrayList<>();
		Double distance = 2000.0;

		// First create a series of 8 points to represent a cube in the mesh then join
		// the adjoining vertices by comparing their locations.

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
										("Particle" + x + "_" + y + "_" + z + "_" + xx + "_" + yy + "_" + zz + "_"
												+ displacement), // Particle
										// Name
										(double) x * distance + distance * xx, // X Position
										(double) y * distance + distance * yy, // Y Position
										(double) z * distance + distance * zz, // Z Position
										decayRatio // Decay / Lag

								));

								sec.addParticle("Particle" + x + "_" + y + "_" + z + "_" + xx + "_" + yy + "_" + zz
										+ "_" + displacement);

							}
						}
					}

					// Add Block to mesh
					mesh.add(sec);

				}

			}

		}

		System.out.println("Mesh Created");
		System.out.println("Joining Mesh....");
		System.out.println(particles.size());

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

		// Create Vertex Based on closest Points.

		System.out.println("Creating Vertices");

		for (GeometricGrouping meshSection : mesh) {

			for (Integer locationx : meshSection.blockLocation) {

				// Create a vertex

				List<Integer> vertex = new ArrayList<>();

				for (Integer locationy : meshSection.blockLocation) {

					if (locationx != locationy) {

						Double dX = -(particles.get(locationx).getX()) + (particles.get(locationy).getX());
						Double dY = -(particles.get(locationx).getY()) + (particles.get(locationy).getY());
						Double dZ = -(particles.get(locationx).getZ()) + (particles.get(locationy).getZ());

						Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));

						if (dS.equals(distance)) {

							vertex.add(locationy);

						}

					}

				}

				// Add new Vertex to MeshSection

				meshSection.addGroup(new GeometricGrouping("vertex", vertex));

			}

		}

		for (Particle p : particles) {

			p.setLocationX(p.locationX + displacement);
			p.setLocationY(p.locationY + displacement);
			p.setLocationZ(p.locationZ + displacement);

		}

		//meshes.add(new Mesh(mesh, vertices));

	}

	// Find closest

	void closest(Particle p) {

		particles.sort(new Comparator<Particle>() {
			@Override
			public int compare(Particle m1, Particle m2) {

				Double val1 = m1.getDistance(p);
				Double val2 = m2.getDistance(p);
				if (val1 > val2) {
					return 1;

				}
				if (val1 < val2) {
					return -1;

				}
				return 0;
			}
		});

	}

	// Calculate PIC

	void calculatePIC() {

		
	
		
		for (Particle t : testParticles) {
	
           for(Particle p: particles) {p.clearMomentum();}
		
			for (Particle m : particles) {

				
				Double dX = -(t.getX()) + (m.getX());
				Double dY = -(t.getY()) + (m.getY());
				Double dZ = -(t.getZ()) + (m.getZ());

				Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
				Double pot = potentialLibrary.get(0).nPotential(dS);
				//System.out.println(pot+"Pot");
				dX = (dX / 1) * pot * t.mass;
				dY = (dY / 1) * pot *  t.mass;
				dZ = (dZ / 1) * pot *  t.mass;

				m.addMomentumX(dX);
				m.addMomentumY(dY);
				m.addMomentumZ(dZ);

			}
			
			
			}
		
		
		for(Particle t1: testParticles) {
				
				
					
					closest(t1);
					Double momentumX = 0.0;
					Double momentumY = 0.0;
					Double momentumZ = 0.0;
					
					
					//System.out.println(particles.size());
					for(int i =0; i < 8; i++) {
						
					//	particles.get(i).setMomentumX(-particles.get(i).getMomentumX());
					//	particles.get(i).setMomentumY(-particles.get(i).getMomentumX());
					//	particles.get(i).setMomentumZ(-particles.get(i).getMomentumX());
						//System.out.println(particles.get(i).getDistance(t1));
						
						
						
						momentumX+=particles.get(i).getMomentumX();
						momentumY+=particles.get(i).getMomentumY();
						momentumZ+=particles.get(i).getMomentumZ();
					//	System.out.println(particles.get(i).getMomentumX());
						
						
					}
					//System.out.println(momentumX+"=======");
					t1.addMomentumX(-momentumX/t1.mass);
					t1.addMomentumY(-momentumY/t1.mass);
					t1.addMomentumZ(-momentumZ/t1.mass);
				
			

		}
		
		
		for (int i = 0; i < testParticles.size(); i++) {

			testParticles.get(i).setLocationX(testParticles.get(i).getX() + testParticles.get(i).getMomentumX());
			testParticles.get(i).setLocationY(testParticles.get(i).getY() + testParticles.get(i).getMomentumY());
			testParticles.get(i).setLocationZ(testParticles.get(i).getZ() + testParticles.get(i).getMomentumZ());
		}
		
		
		

	}

	// Calculate Area based on total length

	void calculateMeshes() {

		// iterateMeshPotential() ;
		// Iterate thru all the vertices and adjust distance based on proximity to mass
		// vertex
		// This way we can create elasticity in the space.
		// vertices will try to adjust to the shape.

		// Iterate thru each vertexes and do a reverse move on the inner vertex based on
		// mass and distance.

		for (Mesh mesh : meshes) {

			for (GeometricGrouping sec : mesh.group) {

				for (GeometricGrouping vertex : sec.groups) {

					for (Integer p : vertex.blockLocation) {

						Double momentumX = 0.0;
						Double momentumY = 0.0;
						Double momentumZ = 0.0;

						for (Integer c : vertex.blockLocation) {

							if (c != p) {

								Double dX = -(mesh.particles.get(p).getX()) + (mesh.particles.get(c).getX());
								Double dY = -(mesh.particles.get(p).getY()) + (mesh.particles.get(c).getY());
								Double dZ = -(mesh.particles.get(p).getZ()) + (mesh.particles.get(c).getZ());

								// I calculate DS or distance using the Pythagoras formula c^2=a^2+b^2.
								// This gives me a positive distance between the particle.get(i) particle
								// and the particle.get(j).

								Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));

								// Double pot = sec.meshPotential.nPotential(dS);
								Double pot = 0.0;
								pot = sec.meshPotential.simpleString(dS);
								// System.out.println(pot);
								/*
								 * if (sec.equals(mesh.get(mesh.size() / 2))) {
								 * 
								 * pot = potentialLibrary.get(2).bezierPotential(dS); }
								 * 
								 * else { pot = potentialLibrary.get(3).bezierPotential(dS); }
								 * 
								 */
								dX = (dX / 1) * pot * mesh.particles.get(c).mass * 1;
								dY = (dY / 1) * pot * mesh.particles.get(c).mass * 1;
								dZ = (dZ / 1) * pot * mesh.particles.get(c).mass * 1;

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

						mesh.particles.get(p).addMomentumX(momentumX);
						mesh.particles.get(p).addMomentumY(momentumY);
						mesh.particles.get(p).addMomentumZ(momentumZ);

					} // End p Loop

				} // End Vertex Loop

			} // End Mesh Loop

		} // End Meshes Loop

		// Calculate Attraction between particles

		List<Particle> tempParticles = new ArrayList<>();
		for (Mesh m : meshes) {

			tempParticles.addAll(m.particles);

		}

		for (Particle p : tempParticles) {
			Double momentumX = 0.0;
			Double momentumY = 0.0;
			Double momentumZ = 0.0;

			for (Particle c : tempParticles) {

				if (!p.equals(c)) {

					Double dX = -(p.getX()) + (c.getX());
					Double dY = -(p.getY()) + (c.getY());
					Double dZ = -(p.getZ()) + (c.getZ());

					Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));

					Double pot = 0.0;
					pot = meshes.get(0).group.get(0).meshPotential.nPotential(dS);

					dX = (dX / 1) * pot * c.mass * .1;
					dY = (dY / 1) * pot * c.mass * .1;
					dZ = (dZ / 1) * pot * c.mass * .1;

					momentumX += dX;
					momentumY += dY;
					momentumZ += dZ;

				}

			}
			p.addMomentumX(momentumX);
			p.addMomentumY(momentumY);
			p.addMomentumZ(momentumZ);

		}

		Double totalEnergy = 0.0;
		for (Particle p : tempParticles) {

			Double energy = 0.0;

			energy += Math.abs(p.getMomentumX());
			energy += Math.abs(p.getMomentumY());
			energy += Math.abs(p.getMomentumZ());

			totalEnergy += energy / 3;
		}

		averageEnergy = totalEnergy / tempParticles.size() + tempParticles.size();

		if (averageEnergy > targetEnergy) {

			decayRatio = 1.0;// 0.99999;
			for (Particle p : tempParticles) {
				p.setDecay(decayRatio);
			}

			for (Particle p : testParticles) {
				p.setDecay(decayRatio);
			}

		} else if (averageEnergy < targetEnergy) {
			decayRatio = 1.0;// 1.00001;
			for (Particle p : tempParticles) {
				p.setDecay(decayRatio);
			}

			for (Particle p : testParticles) {
				p.setDecay(decayRatio);
			}

		}

		// Calculate motion of Particles

		for (int i = 0; i < tempParticles.size(); i++) {

			tempParticles.get(i).setLocationX(tempParticles.get(i).getX() + tempParticles.get(i).getMomentumX());
			tempParticles.get(i).setLocationY(tempParticles.get(i).getY() + tempParticles.get(i).getMomentumY());
			tempParticles.get(i).setLocationZ(tempParticles.get(i).getZ() + tempParticles.get(i).getMomentumZ());
		}

		// Copy back

		for (Particle p : tempParticles) {

			for (Mesh m : meshes) {

				for (Particle par : m.particles) {

					if (par.getParticleName().contentEquals(p.particleName)) {

						par = p;
						break;
					}

				}

			}

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

			int totalSize = 0;

			for (Mesh m : meshes) {
				totalSize += m.particles.size();
			}
			
			totalSize +=/*particles.size() */+ testParticles.size();

			br.write(totalSize + "\n");
			br.write("\n");

	/*	for (Particle p : particles) {

				// Append into file particle per particle all separated by white space.

				br.write(p.getColor() + " " // Write particle color
						+ p.getX() + " " // Write X location
						+ p.getY() + " " // Write Y location
						+ p.getZ() + "\n"); // Write Z location

			}  */

			for (Particle p : testParticles) {
				br.write(p.getColor() + " " // Write particle color
						+ p.getX() + " " // Write X location
						+ p.getY() + " " // Write Y location
						+ p.getZ() + "\n"); // Write Z location

			}

			for (Mesh m : meshes) {

				for (Particle p : m.particles) {
					br.write(p.getColor() + " " // Write particle color
							+ p.getX() + " " // Write X location
							+ p.getY() + " " // Write Y location
							+ p.getZ() + "\n"); // Write Z location

				}

			}

			// Close all opened file writing mechanisms.
			br.close();
			fr.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
