package edu.cg.scene.objects;

import edu.cg.UnimplementedMethodException;
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
		double b = Ops.dot(Ops.mult(2, ray.direction()), Ops.sub(ray.source(), this.center));
		double c = Ops.normSqr(Ops.sub(ray.source(), this.center))- Math.pow(radius,2);
		double t = getTvalue(ray, Ops.quadricRoot(1, b, c));
		if (t >= Ops.infinity || t < Ops.epsilon) {
			return null;
		}
		Point intersectionPoint = Ops.add(ray.source(),Ops.mult(t, ray.direction()));
		Vec normal = Ops.normalize(Ops.sub(intersectionPoint, center));
		Hit ans = new Hit(t, normal);
		return ans;
	}

	private double getTvalue(Ray ray, double [] vals) {
		double dis1 = Double.MAX_VALUE;
		double dis2 = Double.MAX_VALUE;
		if (vals[0] != Double.NEGATIVE_INFINITY) {
			Point p1 = Ops.add(ray.source(), Ops.mult(vals[0], ray.direction()));
			dis1 = Ops.dist(p1, ray.source());
		}
		if (vals[1] != Double.NEGATIVE_INFINITY) {
			Point p2 = Ops.add(ray.source(), Ops.mult(vals[1], ray.direction()));
			dis2 = Ops.dist(p2, ray.source());
		}
		if (dis1 < dis2) {
			return vals[0];
		}else {
			return vals[1];
		}
	}
}
