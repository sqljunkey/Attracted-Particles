package Attracted.Particles;

import java.io.FileNotFoundException;
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

/**
 * Hello world!
 *
 */

public class App extends Application {

	// Number of Particles in the system

	private static final int particleCount = 100;

	// Average Distance between generated particles

	private static final double spread = 200;

	// Number of Steps in Simulation

	private static final Double steps = 1E+9;

	// Width of 3D display
	private static final int WIDTH = 1400;
	private static final int HEIGHT = 800;

	// Display Version
	String version = "2D";

	// JavaFX 3D spheres to represent particles in 3d Mode.

	List<Sphere> particles = new ArrayList<>();

	// Field of lines

	List<Line3D> vectorField = new ArrayList<>();

	// Particle System

	ParticleSystem system = new ParticleSystem(particleCount, spread);

	// 2D Renderer
	JRenderer render = new JRenderer("Particle Simulation");

	// Particle Recorder

	ParticleRecorder recorder = new ParticleRecorder();

	// Calculate Vectors for application

	void calculateParticles() {

		system.calculateVectors();

		for (int i = 0; i < particles.size(); i++) {

			particles.get(i).translateXProperty().set(system.getParticle(i).getX());
			particles.get(i).translateYProperty().set(system.getParticle(i).getY());
			particles.get(i).translateZProperty().set(system.getParticle(i).getZ());

		}

	}

	// 2D version display

	public void JframeRender() {

		render.updatePlot(recorder.next());

	}

	// 3D version display

	@Override
	public void start(Stage primaryStage) {

		if (version.contentEquals("3D")) {
			Group group = new Group();
			for (int i = 0; i < particleCount; i++) {

				// Add particles from system to the sphere array

				particles.add(new Sphere(system.particles.get(i).mass));

				// Use a cylinder to create a line connecting two points

				vectorField.add(new Line3D(

						// Start Line Location

						new Point3D(system.getParticle(i).getX(), system.getParticle(i).getY(),
								system.getParticle(i).getZ())

						,

						// End Line Location

						new Point3D(system.getParticle(i).getMomentumX(), system.getParticle(i).getMomentumY(),
								system.getParticle(i).getMomentumZ())

				));

				group.getChildren().add(particles.get(particles.size() - 1));

			}

			Camera camera = new PerspectiveCamera();
			camera.relocate(-WIDTH / 2, -HEIGHT / 2);
			camera.translateZProperty().set(+200.0);
			Scene scene = new Scene(group, WIDTH, HEIGHT);
			scene.setFill(Color.BLACK);
			scene.setCamera(camera);

			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					calculateParticles();
				}
			}));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();

			primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case W:

					camera.translateZProperty().set(camera.getTranslateZ() + 100);

					break;
				case S:
					camera.translateZProperty().set(camera.getTranslateZ() - 100);
					break;
				case A:

					camera.translateXProperty().set(camera.getTranslateX() - 100);
					break;

				case D:

					camera.translateXProperty().set(camera.getTranslateX() + 100);
					break;
				case Q:

					camera.translateYProperty().set(camera.getTranslateY() - 100);
					break;
				case E:

					camera.translateYProperty().set(camera.getTranslateY() + 100);
					break;

				}
			});

			primaryStage.setTitle("Particles");
			primaryStage.setScene(scene);
			primaryStage.show();

		}

		else {

			int interval = 0;
			int update = 0;
			for (Double i = 0.0; i < steps; i++) {

				update++;
				
				DecimalFormat f = new DecimalFormat("#0.00");

				if (update > steps * .001) {
					System.out.println("" + f.format(((i / steps) * 100))+"% Done of Calculation.");
					update = 0;
				}
				calculateParticles();
				interval++;
				if (interval > 50000) {
					recorder.addParticles(system.particles);
					interval = 0;
				}
			}

			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					JframeRender();
				}
			}));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();

		}

	}

	public static void main(String[] args) {

		launch(args);

	}

}
