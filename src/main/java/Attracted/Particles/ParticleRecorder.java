package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;

public class ParticleRecorder {
	
	
	int position;
	
	List<List <Particle>> particlePositions = new ArrayList<>();
	
	
	// Constructor takes number of frames in record
	
	public ParticleRecorder() {
		
		
		position =0;
		
	}
	
	
	public void addParticles(List<Particle> heavyParticles) {
		
		//Convert the particles to low memory particles
		
		List<Particle> lightParticles = new ArrayList<>(); 
		
		for(Particle p: heavyParticles) {
			
			lightParticles.add(new Particle(p.getX(), p.getY(),p.getZ(),p.particleColor));
		}
		
		
		particlePositions.add(lightParticles);
	}
	
	
	public List<Particle> next(){
		
		//Increment to Next Frame
		
		position++;
		
		//If Frame is above last Frame Reset Frame
		
		if(position >= particlePositions.size()) {
			position = 0;
			return particlePositions.get(position);
		}
		
		//Return list of Particles
		
		return particlePositions.get(position);
		
	}
	
	
	
	

}
