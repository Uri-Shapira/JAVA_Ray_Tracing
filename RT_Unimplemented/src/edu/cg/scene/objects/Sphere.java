package edu.cg.scene.objects;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Vec;


public class Sphere extends Shape {
	private Point center;
	private double radius;

	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public Sphere() {
		this(new Point(0, -0.5, -6), 0.5);
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Sphere:" + endl + "Center: " + center + endl + "Radius: " + radius + endl;
	}

	public Sphere initCenter(Point center) {
		this.center = center;
		return this;
	}

	public Sphere initRadius(double radius) {
		this.radius = radius;
		return this;
	}

	@Override
	public Hit intersect(Ray ray) {
		Hit hit = null;
		double b = ray.direction().mult(2.0).dot((ray.source().sub(this.center)));
		double c = ray.source().distSqr(this.center) - Math.pow(radius,2);
		double t = Double.NEGATIVE_INFINITY;
		Vec normalToSurface = null;
		double[] t_values = quadraticEquation(1, b, c);
		boolean isWithin = false;
		if(t_values[1] != Double.NEGATIVE_INFINITY && t_values[0] != Double.NEGATIVE_INFINITY){
			if(t_values[1] > Ops.epsilon){
				t = t_values[0];
				normalToSurface = ray.add(t_values[0]).sub(this.center).normalize();
				if(t_values[0] < Ops.epsilon){
					t = t_values[1];
					normalToSurface = ray.add(t_values[0]).sub(this.center).normalize().neg();
					isWithin = true;
				}
			}
			if (t < Ops.infinity && t > Ops.epsilon) {
				hit  = new Hit(t, normalToSurface);
				hit.setIsWithin(isWithin);
			}
		}
		return hit;
	}

	private double [] quadraticEquation(double a, double b, double c) {
		double [] values = {Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY};
		double determinant = Math.pow(b,2) - (4 * a * c);
		if (determinant >= 0) {
			values[0] = (-b + Math.sqrt(determinant)) / (2 * a);
			values[1] = (-b - Math.sqrt(determinant)) / (2 * a);
		}
		return values;
	}

}
