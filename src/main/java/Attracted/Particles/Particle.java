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

	Double decayRatio;

	String particleColor;


	
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

	public void addMomentumX(Double momentumX) {

		this.momentumX = this.momentumX  + momentumX* decayRatio;

	}

	public Double getMomentumY() {
		return momentumY;
	}

	public void addMomentumY(Double momentumY) {

		this.momentumY = this.momentumY  + momentumY* decayRatio;
	}

	public Double getMomentumZ() {
		return momentumZ;
	}

	public void addMomentumZ(Double momentumZ) {

		this.momentumZ = this.momentumZ  + momentumZ* decayRatio;

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

	public Point3D getLocation() {

		return new Point3D(getX(), getY(), getZ());
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

}
