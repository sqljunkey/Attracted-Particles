package Attracted.Particles;

import java.util.List;
import java.util.Random;

//The reason I'm writing this class is to have a dedicated class that holds all of the potentials.
//This will make my Particle Systems code look cleaner and will give me a place to store any potential
//related variables.
//
//Currently it will hold the general newton potential, lennard jones potential, bezier curve potential


public class Potentials {
	
	
	//This is the difference to calculate the gradient of any potential by calculating one potential and then 
	// calculating a small distance from the original distance and subtracting the two to get the gradient. 
	
	Double diff; 
	
	
	public Potentials(Double diff) {
		
		this.diff = diff;
	}
	
	//This is the general form of the Bezier Curve, you will be able to find more information about this here:
	//https://en.wikipedia.org/wiki/B%C3%A9zier_curve. 
	//It can be used to create custom made potential curves it will take the distance between two particles, 
	//a cutoffDistance to figure out the interpolation ratio
	//(t) and points in 3D space that will control the dimensions of the curve. 
	//
	//Please note that this potential will increase the simulation time.
	
	Double bezierCurve(Double t, List<Double>Points) {
		
		
		
		
		
		//Math can be found at https://en.wikipedia.org/wiki/B%C3%A9zier_curve
		//but essentially this function will return B(t) where t is a number between 0 and 1
		//and where the points are "anchors" of the curve. 
				
		int n = Points.size()-1;
		
		Double b=0.0;
		
		for(int i =0; i <=n ;i++) {
			
			b+= binomialCoefficient(n,i) *  Math.pow((1-t),(n-i)) * Math.pow(t, i) * Points.get(i) ;
			
		//	System.out.println(Math.pow(2, 0));
		}
		
		
		
		
		return b;
	}
	
	
	//To calculate the general form of the Bezier curve we need to use the binomial coefficient
	//So I am writing a function that will take care of this. This can be a private function though
	//because it is technically not a potential. You can find more information on the math here:
	//https://en.wikipedia.org/wiki/Binomial_coefficient
	
	private int binomialCoefficient(int n, int k) {
	
	        if ((n == k) || (k == 0))
	            return 1;
	        else
	            return binomialCoefficient(n - 1, k) + binomialCoefficient(n - 1, k - 1);
		
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

	// A random component is added to simulate heat in the particles or erratic
	// motion
	// this will cause the particles to have a non-linear attraction to each other.

	public Double nPotential(Double distance) {

		Double newton = 0.0;
		Random r = new Random();
		newton = 1 / Math.pow(distance, 2) * 500;// * Math.abs(r.nextGaussian())*100;

		if (newton > .01) {
			newton *= -1.0;

		}

		/*
		 * if(newton <.0001) { newton = 0.000001; }
		 */

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
		//Double diff = 1E-14;

		Double epsilon = 1.1;
		Double sigma = 10.;

		ljPot_1 = epsilon * (Math.pow((sigma / (distance - diff)), 2) - 2 * Math.pow((sigma / (distance - diff)), 1));
		ljPot_2 = epsilon * (Math.pow((sigma / (distance)), 2) - 2 * Math.pow((sigma / (distance)), 1));

		return (ljPot_2 - ljPot_1);// *1.0E+11 ;
	}
	
	
	
	
	
	
	

}
