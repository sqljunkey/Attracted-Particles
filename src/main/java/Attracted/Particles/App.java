package Attracted.Particles;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.event.*;

//This is a particle potential simulator.
//
//The reason I'm writing this simulator is to develop some a system that can be used in a macro type of simulation. There are many
//particle simulators out there. In so far as I have research(which isn't far) I have come across many nano-scale particle simulators 
//and not many particle simulators that tries to simulate macro conditions. 
//
//There are many features pending and lacking at this moment, such as general relativity conditions, proper unit system, accurate physical 
//representation such as heat, energy and so forth.
//
//In addition of the lack of these critical features, the simulator is also lacking in the ability to perform a high number of calculations
//at a rapid speed, since it is written in Java, and java is slow. and It currently does not have cluster support.
//
//All being said and told, in the end this is a research project, an exploration of the limits of classical mechanics, and the trying of 
//different variables. The software will rely heavily on potentials, such as Lennard Jones Potential, Newtons General Relativity Potenential
//and custom made potentials.
//
//The goal is to hopefully represent the universe, and it's operations. Operations such as star formation, planetary motions, galaxy formation and shape,
//a black hole or two colliding with each other, and maybe some quantum mechanics. A tall order I know, given the many short comings, yet what
//the heck right?

//The way the software works is that it will get it's different parameters from a .script file, load them.(which is currently not implemented). Run the 
//Simulation using the potentials and weights, dump the particle locations in a file at a set interval and  then close the application.
//Hopefully after a few minutes you will have a .xyz file that you can load in an animation software and look at.

public class App {

	// I will place the temporary Application Settings in this block until I can
	// write a propper .script load code.
	//////////////////////////////////////////////

	// Number of Particles in the system

	private static final int particleCount = 50;

	// Average Distance between generated particles

	private static final double spread = 500;

	// Number of Steps in Simulation the higher this number the more fine rich
	// detail the timesteps will have

	private static final Double steps = 1E+9;

	// Difference between potentials

	private static final Double diff = 1E-3;

	// Dump interval

	private static final int dumpInterval = 20;

	// Dump filename

	static String dumpFilename = "file.xyz";

	///////////////////////////////////////// 
	static ParticleSystem system;

	ParticleSystemConfiguration cfg = new ParticleSystemConfiguration();

	// Use the start to start things.

	public static void startSim(ParticleSystemConfiguration cfg ) {

		// Create particle System

		// Start new Dump File

		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(cfg.getDumpFilename()));
			writer.write("");

			writer.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		// Set tracking Interval to zero, and use an update integer to keep the user
		// updated of the percentage of the simulation that is completed.

		int trackingInterval = 0;
		int update = 0;
		int updateEnergy = 0;
		int iterate =0;
		
		// Loop the simulation by the number of steps.
		for (Double i = 0.0; i < cfg.getSteps(); i++) {

			// increase the update count and create a some formatting for decimals
			update++;
			updateEnergy++;
		//	iterate++;

			DecimalFormat decimalformat = new DecimalFormat("#0.00");

			// I'm using the steps and the quantity in the update in to give the user a
			// continues percent completion
			// like a progress bar of the simulation.

			if (update > cfg.getSteps() * .001) {
				System.out.println("" + decimalformat.format(((i / cfg.getSteps()) * 100)) + "% Done of Calculation.");
				update = 0;
			}
			
			if(updateEnergy>cfg.getSteps() * .000001 ) {
				
				updateEnergy =0;
				System.out.println("Energy: "+system.getEnergy());
				System.out.println("Decay Ratio: "+system.getDecay());
				//system.iterateMeshPotential();
			}
			
		//	if(iterate>cfg.getSteps()*.000001) {
		//		
			//	system.iterateMeshPotential();
		//		iterate =0;
			//}

			// Run one step of the simulation calculation, we calculate every step and dump
			// on file only a portion a small quantity. We do this
			// to get as accurate as an interaction between the particles as possible, and
			// by dumping after a set interval the animations will
			// run smoothly and will not be too slow.

			//system.calculateVectors();
			
			system.calculateMesh();
			// Increase the trackingInterval and check to see if it is higher than the
			// dumpInteval and if it is dump the particle positions onto a file.
			//

			trackingInterval++;

			if (trackingInterval > cfg.getDumpInterval()) {

				system.dumpToFile(cfg.getDumpFilename());

				trackingInterval = 0;
			}
		}

	}

	public static void main(String[] args) {
		// Test Bezier Curve
		Potentials p = new Potentials(.001);



		
		
		ParticleSystemConfiguration cfg = new ParticleSystemConfiguration();
		
		cfg.readFile(args[0]);
		cfg.printValues();
		
		
		system= new ParticleSystem(
				cfg.getParticleCount(), 
				cfg.getSpread(),
				cfg.getDiff(), 
				cfg.getMass(), 
				cfg.getTargetEnergy(),
				cfg.getPoints(),
				cfg.getSprings());
		
		
	
		 startSim(cfg);

	}

}
