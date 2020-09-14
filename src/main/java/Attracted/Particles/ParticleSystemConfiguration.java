package Attracted.Particles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This will store the particle system configuration

public class ParticleSystemConfiguration {

	// Particle mass
	private int mass = 0;

	// Number of particles
	private int particleCount = 0;

	// Average Distance between generated particles

	private double spread = 0;

	// Number of Steps in Simulation the higher this number the more fine rich
	// detail the timesteps will have

	private Double steps = 0.0;

	// Difference between potentials

	private Double diff = 0.0;

	// Dump interval

	private int dumpInterval = 0;

	// Animation file
	private String dumpFilename = "";

	// Target Energy

	private Double targetEnergy = 0.0;

	// Potential Curve
	List<List<Point>> points = new ArrayList<>();

	// Potential Spring distances
	List<Double> springs = new ArrayList<>();

	// Read script into configuration
	public void readFile(String filename) {

		// Create isPoint variable to check if item is a point
		Boolean isPoint = false;

		try {

			// Open file
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;

			// Particle Mass Pattern
			Pattern particleMass = Pattern.compile("mass(\\s+)?(\\d+)");

			// Particle Count Pattern
			Pattern particleCountPattern = Pattern.compile("particle(\\s+)?count(\\s+)?(\\d+)");

			// Particle Spread Pattern
			Pattern spreadPattern = Pattern.compile("(particle)?(\\s+)?spread(\\s+)?(\\d+)");

			// Particle Steps Pattern
			Pattern stepsPattern = Pattern.compile("steps(\\s+)?([-+]?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?)");

			// Particle Diff Pattern
			Pattern diffPattern = Pattern.compile("diff(\\s+)?([-+]?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?)");

			// Particle Diff Pattern
			Pattern springPattern = Pattern.compile("spring(\\s+)?([-+]?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?)");

			// Particle Target Pattern
			Pattern targetPattern = Pattern.compile("target(\\s+)?([-+]?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?)");

			// Particle Dump Interval Pattern
			Pattern dumpPattern = Pattern.compile("dump(\\s+)?(\\d+)");

			// Particle Dump FileName Pattern
			Pattern dumpFilePattern = Pattern.compile("dumpfile(\\s+)?(\\w+\\.xyz)");

			// Particle Curve Points
			Pattern pointPattern = Pattern.compile("point(\\s+)?([-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)(\\s+)?,"
					+ "(\\s+)?([-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)");

			// Matcher
			Matcher match;

			// Read until the file is empty
			while ((line = reader.readLine()) != null) {

				// Skip any commented Lines
				if (line.contains("#")) {
					continue;
				}

				// Find particle Count Match
				match = particleCountPattern.matcher(line);
				if (match.find()) {

					particleCount = Integer.parseInt(match.group(3));

				}

				// Find particle Spread Match
				match = spreadPattern.matcher(line);
				if (match.find()) {

					spread = Double.parseDouble(match.group(4));
				}

				// Find steps Match
				match = stepsPattern.matcher(line);
				if (match.find()) {

					steps = Double.parseDouble(match.group(2));
				}

				// Find diff Match
				match = diffPattern.matcher(line);
				if (match.find()) {

					diff = Double.parseDouble(match.group(2));
				}

				// Find decay Match
				match = targetPattern.matcher(line);
				if (match.find()) {

					targetEnergy = Double.parseDouble(match.group(2));
				}

				// Find dump interval Match
				match = dumpPattern.matcher(line);
				if (match.find()) {

					dumpInterval = Integer.parseInt(match.group(2));
				}

				// Find dump File Match
				match = dumpFilePattern.matcher(line);
				if (match.find()) {

					dumpFilename = match.group(2);
				}

				// Find Spring Match
				match = springPattern.matcher(line);
				if (match.find()) {

					springs.add(Double.parseDouble(match.group(2)));
				}

				// Find point Match, and then read all points until there is a break
				match = pointPattern.matcher(line);
				if (match.find()) {

					// add a new point Array if this is the first time we encountered a point
					//
					if (isPoint == false) {

						points.add(new ArrayList<Point>());
						isPoint = true;
					}
					Double x = Double.parseDouble(match.group(2));
					Double y = Double.parseDouble(match.group(6));

					points.get(points.size() - 1).add(new Point(x, y));
				} else {

					// If there is a new line or anything else that breaks the points cycle this
					// will create a new points
					// Array.
					isPoint = false;
				}

				match = particleMass.matcher(line);
				if (match.find()) {

					mass = Integer.parseInt(match.group(2));
				}

			}

			reader.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// Getters

	// Print all Loaded Values
	void printValues() {

		System.out.println("Target: " + targetEnergy);
		System.out.println("Particle Mass: " + mass);
		System.out.println("Particle Count: " + particleCount);
		System.out.println("Particle Spread: " + spread);
		System.out.println("Steps: " + steps);
		System.out.println("Dump Interval: " + dumpInterval);
		System.out.println("Dump File: " + dumpFilename);

		for (List<Point> point : points) {

			System.out.println(" ");
			for (Point p : point) {

				System.out.println("Points:" + p.getX() + " " + p.getY());
			}
		}

		for (Double spring : springs) {

			System.out.println("Spring: " + spring);

		}
	}

	public int getParticleCount() {
		return particleCount;
	}

	public double getSpread() {
		return spread;
	}

	public Double getSteps() {
		return steps;
	}

	public Double getDiff() {
		return diff;
	}

	public int getDumpInterval() {
		return dumpInterval;
	}

	public String getDumpFilename() {
		return dumpFilename;
	}

	public int getMass() {

		return mass;

	}

	public Double getTargetEnergy() {

		return targetEnergy;
	}

	public List<List<Point>> getPoints() {

		return points;

	}

	public List<Double> getSprings() {

		return springs;
	}

}
