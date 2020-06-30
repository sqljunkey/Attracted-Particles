package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	
	
	
	List<GeometricGrouping> group = new ArrayList<>(); 
	List<Particle> particles = new ArrayList<>();
	
	
	
	public Mesh (List<GeometricGrouping> group , List<Particle> particles) {
		
		this.group = group;
		this.particles =  particles;
		
		
		
		
	}

}
