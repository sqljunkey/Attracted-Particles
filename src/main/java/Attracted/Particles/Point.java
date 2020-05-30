package Attracted.Particles;


//Simple Point Class  with X Y coordinates for 2D points
//or X Y Z for 3D points.

public class Point {

	Double x;
	Double y;
	Double z;

	public Point(Double x, Double y) {

		this.x = x;
		this.y = y;
	}
	
	public Point(Double x, Double y, Double z) {
		this.x = x;
		this.y = y;
		this.z = z; 
		
		
	}

	Double getX() {
		return x;
	}

	Double getY() {
		return y;
	}
	
	Double getZ() {
		
		return z;
	}

}
