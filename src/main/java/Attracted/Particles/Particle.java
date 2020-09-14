package Attracted.Particles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point3D;

public class Particle {

	String particleName;
	Double mass;
	Double locationX;
	Double locationY;
	Double locationZ;

	Double momentumX;
	Double momentumY;
	Double momentumZ;

	List<Double> energies = new ArrayList<>();
	
	Double decayRatio;

	String particleColor;

	List<Integer> connections = new ArrayList<>();

	//Copy Particle
	
	public Particle(Particle copy) {
		
		this.mass = copy.mass;
		locationX = copy.locationX;
		locationY = copy.locationY;
		locationZ = copy.locationZ;
		this.decayRatio = copy.decayRatio;
		
		
		momentumX = 0.0;
		momentumY = 0.0;
		momentumZ = 0.0;

		this.particleName = copy.particleName;

		String colors[] = { "RED", "BLUE", "GREEN" };
		particleColor = colors[new Random().nextInt(3)];	
		
		
	}
	
	//Full version of particle

	public Particle(Double mass, String particleName, Double x, Double y, Double z, Double decay

	) {

		this.mass = mass;
		locationX = x;
		locationY = y;
		locationZ = z;
		this.decayRatio = decay;
		
		
		momentumX = 0.0;
		momentumY = 0.0;
		momentumZ = 0.0;

		this.particleName = particleName;

		String colors[] = { "RED", "BLUE", "GREEN" };
		particleColor = colors[new Random().nextInt(3)];
	}

	//Light version of particles
	
	public Particle(Double locationX, Double locationY, Double locationZ, String color) {

		
		this.locationX = locationX;
		this.locationY = locationY;
		this.locationZ = locationZ;
		

//Randomly assign a color to particle

		
		particleColor = color;

	}

	public String getColor() {

		return particleColor;

	}

	public String getParticleName() {

		return particleName;
	}

	public Double getX() {
		return locationX;
	}

	public Double getY() {
		return locationY;
	}

	public Double getZ() {
		return locationZ;
	}

	public Double getMomentumX() {
		return momentumX;
	}
    public void setMomentumX(Double momentumX) {
    	this.momentumX = momentumX;
    }
    
    public void setMomentumY(Double momentumY) {
    	
    	this.momentumY = momentumY;
    }
    
    public void setMomentumZ(Double momentumZ) {
    	this.momentumZ = momentumZ;
    	
    }
	public void addMomentumX(Double momentumX) {

		this.momentumX = this.momentumX* decayRatio  + momentumX;

	}

	public Double getMomentumY() {
		return momentumY;
	}

	
	public void addMomentumY(Double momentumY) {

		this.momentumY = this.momentumY* decayRatio  + momentumY;
	}

	public Double getMomentumZ() {
		return momentumZ;
	}

	public void addMomentumZ(Double momentumZ) {

		this.momentumZ = this.momentumZ* decayRatio  + momentumZ;

	}

	public void setLocationX(Double locationX) {
		this.locationX = locationX;
	}

	public void setLocationY(Double locationY) {
		this.locationY = locationY;
	}

	public void setLocationZ(Double locationZ) {
		this.locationZ = locationZ;
	}
	
	public void setDecay(Double decayRatio) {
		
		this.decayRatio = decayRatio;
	}

	public double[] getVector() {
		
		double []vector = {this.locationX, this.locationY, this.locationZ};
		
		return vector;
		
			
	}
	
	public List<Double> getVector(Particle particle){
		
		//create vector array
		
		List<Double> vector = new ArrayList<>();
		
		//Calculate distances between this particle and the provided particle for each axis.
		
		Double dX = -(this.getX()) + (particle.getX());
		Double dY = -(this.getY()) + (particle.getY());
		Double dZ = -(this.getZ()) + (particle.getZ());
		
		
		
		
		//Add to Vector 
			
		vector.add(dX);
		vector.add(dY);
		vector.add(dZ);
		
		
		return vector; 
		
		
	}
	
	

	
	public Point3D getLocation() {

		return new Point3D(getX(), getY(), getZ());
	}
	
	public Double getDistance(Particle p) {
		

		Double dX = -p.getX() + (this.getX());
		Double dY = -p.getY() + (this.getY());
		Double dZ = -p.getZ() + (this.getZ());

		Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
		return dS; 
		
	}
	
	
	public List<Double> getGaugedDistance(Particle p, Double epsilon) {
		
		List<Double> distances = new ArrayList<>();
		
		Double dX = -p.getX() + (this.getX());
		Double dY = -p.getY() + (this.getY());
		Double dZ = -p.getZ() + (this.getZ());
		
		//No Gauge
		Double dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
		
		distances.add(dS);
		//Gauge in X direction
		
		 dX = -p.getX()+epsilon + (this.getX());
		 dY = -p.getY() + (this.getY());
		 dZ = -p.getZ() + (this.getZ());
		
		dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
		distances.add(dS);
		
		//Gauge in Y direction
		
		 dX = -p.getX() + (this.getX());
		 dY = -p.getY()+epsilon + (this.getY());
		 dZ = -p.getZ() + (this.getZ());
		
		dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
		distances.add(dS);
		
		//Gauge in Z direction
		 dX = -p.getX() + (this.getX());
		 dY = -p.getY() + (this.getY());
		 dZ = -p.getZ()+epsilon + (this.getZ());
		dS = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
		distances.add(dS);
		
		
		
		return distances; 
		
	}
	
	
	
	
	
	
	
	public void  clearMomentum() {
		
		this.momentumX = this.momentumY = this.momentumZ = 0.0;
	}

	public Point3D getMomentum() {

		Double momX = 0.0;
		Double momY = 0.0;
		Double momZ = 0.0;

		momX = momentumX;
		momY = momentumY;
		momZ = momentumZ;


		Point3D point = new Point3D(getX() + momX, getY() + momY, getZ() + momZ

		);

		return point;

	}

	
	public List<Double> getEnergies() {
		return energies;
	}

	public void addEnergy(Double energy) {
		energies.add(energy);
	}
	
	public void clearEnergy() {
		
		energies.clear();
		
	}

	public Double getEnergySum() {
		
		Double sum = 0.0;
		
		
		for(Double energy : energies) {
			
			sum+= energy;
			
		}
		
		return sum;
		
	}
	


}
