package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;

//This is the section of a mesh Object, it will contain a limit of 8 particles that will create a block geometry

public class GeometricGrouping {

	// Holds location of gemeotry
	
	String groupName; 
	List<String> block = new ArrayList<>();
	List<Integer> blockLocation = new ArrayList<>();
	
	
	public GeometricGrouping (String groupName) {
		
		this.groupName = groupName;
	}
	
	
	public GeometricGrouping(String groupName, List<Integer> group) {
		
		this.blockLocation = group;
		this.groupName = groupName;
	}
	
	
	//Recursive Grouping

	List<GeometricGrouping>  groups= new ArrayList<>();

	// Mesh Section Potential will define the size and speed of each mesh. This will
	// be used
	// when calculating the rest size of the box by Particle System.

	Potentials meshPotential;

	void assignPotential(Potentials meshPotential) {

		// System.out.println(meshPotential.targetDistance);
		this.meshPotential = new Potentials(meshPotential.diff);
		this.meshPotential.setTargetDistance(meshPotential.targetDistance);

		// System.out.println(this.meshPotential.targetDistance);
	}

	//Add Group
	
	void addGroup(GeometricGrouping group) {
		
		groups.add(group);
		
		
		
	}
	// Insert new particle name into array

	void addParticle(String particleLocation) {

		block.add(particleLocation);
	}

	// Insert new integer location into location array
	void addLocation(Integer location) {

		blockLocation.add(location);
	}


	//Average Potential Out
	
	void averagePotential(List<Double> distances) {
		
		Double averageDistance = 0.0;
		
		for(Double dS: distances) {
			
			averageDistance+=dS;
		}
		
		averageDistance/=distances.size();
		if(!meshPotential.targetDistance.equals(20.0)) {
		Double percentDifference = Math.abs(meshPotential.targetDistance-averageDistance);
		
		percentDifference/=meshPotential.targetDistance;
		if(percentDifference>0.2) {
			meshPotential.setTargetDistance((meshPotential.targetDistance+averageDistance)/2);
			
		}
			
		else {
			meshPotential.setTargetDistance(meshPotential.targetDistance-0.001);
			
		//	meshPotential.setTargetDistance((meshPotential.targetDistance+averageDistance)/2);
		}
		}
		
	}
	





}
