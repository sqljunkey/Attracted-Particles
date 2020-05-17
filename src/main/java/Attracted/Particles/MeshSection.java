package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;

//This is the section of a mesh Object, it will contain a limit of 8 particles that will create a block geometry


public class MeshSection {

	//Holds location of gemeotry
	List<String> block = new ArrayList<>();
	List<Integer> blockLocation = new ArrayList<>();
	
	//Mesh Section Potential will define the size and speed of each mesh. This will be used
	//when calculating the rest size of the box by Particle System.
	
	Potentials meshPotential; 
	
	
	void assignPotential(Potentials meshPotential) {
		
		
		//System.out.println(meshPotential.targetDistance);
		this.meshPotential = new Potentials( meshPotential.diff);
		this.meshPotential.setTargetDistance(meshPotential.targetDistance);
		
		//System.out.println(this.meshPotential.targetDistance);
	}
	
	//Insert new particle name into array
	
	void addParticle(String particleLocation) {
		
		block.add(particleLocation);
	}

	//Insert new integer location into location array
	void addLocation(Integer location) {
		
		blockLocation.add(location);
	}
	
	//Make potential bigger
	void expand() {
		
		meshPotential.targetDistance+=1E-8;
	}
	
	
}
