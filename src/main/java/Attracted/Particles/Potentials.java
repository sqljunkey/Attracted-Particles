package Attracted.Particles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//The reason I'm writing this class is to have a dedicated class that holds all of the potentials.
//This will make my Particle Systems code look cleaner and will give me a place to store any potential
//related variables.
//
//Currently it will hold the general newton potential, lennard jones potential, bezier curve potential

public class Potentials {

	// This is the difference to calculate the gradient of any potential by
	// calculating one potential and then
	// calculating a small distance from the original distance and subtracting the
	// two to get the gradient.

	Double diff;

	// Target distance
	Double targetDistance;

	// Coordinates for the Bezier Curve
	List<Point> bezierCurve;

	// Constructor
	public Potentials(Double diff) {

		this.diff = diff;
	}
//Simple Spring potential will give you a simple gradient based on distance from target distance

	public Double simpleString(Double dS) {
	


		Double	difference = Math.abs(targetDistance) - dS;
	

		// targetDistance+=difference*0.000001;

		if (difference < 0)

			return -difference * diff * .01;
		else
			return -difference * diff;

	}
	// Set target distance for simple spring

	public void setTargetDistance(Double targetDistance) {

		this.targetDistance = targetDistance;

	}

	// Set curve
	public void setPotentialCurve(List<Point> bezierCurve) {

		// this.bezierCurve.clear();
		this.bezierCurve = bezierCurve;

	}

	// This function will find the largest point along the x axis. This will help
	// when we are calculating the t between 0 and 1 for the bezier curve;
	Double cutoffPotential() {

		Double largest = 0.0;

		for (Point point : bezierCurve) {
			if (point.getX() > largest) {

				largest = point.getX();

			}

		}

		return largest;
	}

	Double bezierPotential(Double distance) {

		Double bzPot1 = 0.0;
		Double bzPot2 = 0.0;
		// Get Largest Point in bezierCurve.

		// Get Inverse 0-1
		Double t = (distance / cutoffPotential());
		Double test = t;
		if (t > 1) {
			t = 1.0;
		}
		if (t < 0) {
			t = 0.0;
		}
		// System.out.println(t);

		bzPot1 = bezierCurve(t).getY();

		// Get Inverse 0-1 with diff
		t = (distance + diff) / cutoffPotential();
		if (t > 1) {
			t = 1.0;
		}
		if (t < 0) {
			t = 0.0;
		}

		bzPot2 = bezierCurve(t).getY();

		/*
		 * if ((bzPot2 - bzPot1) > 0.0) { System.out.println("t" + t + " " + test);
		 * System.out.println("pot " + (bzPot2- bzPot1)); }
		 */

		return (bzPot2 - bzPot1);
	}

	// This is the general form of the Bezier Curve, you will be able to find more
	// information about this here:
	// https://en.wikipedia.org/wiki/B%C3%A9zier_curve. It can take a list of
	// bezierCurve that will define the
	// dimensions of the curve. t is a number between 0 and 1.

	Point bezierCurve(Double t) {

		// Math can be found at https://en.wikipedia.org/wiki/B%C3%A9zier_curve
		// but essentially this function will return B(t) where t is a number between 0
		// and 1
		// and where the bezierCurve are "anchors" of the curve.

		int n = bezierCurve.size() - 1;

		Double x = 0.0;
		Double y = 0.0;

		for (int i = 0; i <= n; i++) {

			x += binomialCoefficient(n, i) * Math.pow((1 - t), (n - i)) * Math.pow(t, i) * bezierCurve.get(i).getX();
			y += binomialCoefficient(n, i) * Math.pow((1 - t), (n - i)) * Math.pow(t, i) * bezierCurve.get(i).getY();

		}

		return new Point(x, y);
	}

	// To calculate the general form of the Bezier curve we need to use the binomial
	// coefficient
	// So I am writing a function that will take care of this. This can be a private
	// function though
	// because it is technically not a potential. You can find more information on
	// the math here:
	// https://en.wikipedia.org/wiki/Binomial_coefficient

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

	
	// this will cause the particles to have a non-linear attraction to each other.

	public Double nPotential(Double distance) {

		Double newton = 0.0;
		
	
		if(distance>1.0) {
		newton = 1 / Math.pow(distance, 2);// (G)Mm/R^2

		}
		
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
		// Double diff = 1E-14;

		Double epsilon = 1.1;
		Double sigma = 10.;

		ljPot_1 = epsilon * (Math.pow((sigma / (distance - diff)), 2) - 2 * Math.pow((sigma / (distance - diff)), 1));
		ljPot_2 = epsilon * (Math.pow((sigma / (distance)), 2) - 2 * Math.pow((sigma / (distance)), 1));

		return (ljPot_2 - ljPot_1);// *1.0E+11 ;
	}

}
