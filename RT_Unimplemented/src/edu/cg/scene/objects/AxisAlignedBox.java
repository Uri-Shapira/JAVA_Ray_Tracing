package edu.cg.scene.objects;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;

public class AxisAlignedBox extends Shape {
	private Point minPoint;
	private Point maxPoint;
	private String name = "";
	static private int CURR_IDX;

	/**
	 * Creates an axis aligned box with a specified minPoint and maxPoint.
	 */
	public AxisAlignedBox(Point minPoint, Point maxPoint) {
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		name = new String("Box " + CURR_IDX);
		CURR_IDX += 1;
		fixBoundryPoints();
	}

	/**
	 * Creates a default axis aligned box with a specified minPoint and maxPoint.
	 */
	public AxisAlignedBox() {
		minPoint = new Point(-1.0, -1.0, -1.0);
		maxPoint = new Point(1.0, 1.0, 1.0);
	}

	/**
	 * This methods fixes the boundary points minPoint and maxPoint so that the values are consistent.
	 */
	private void fixBoundryPoints() {
		double min_x = Math.min(minPoint.x, maxPoint.x), max_x = Math.max(minPoint.x, maxPoint.x),
				min_y = Math.min(minPoint.y, maxPoint.y), max_y = Math.max(minPoint.y, maxPoint.y),
				min_z = Math.min(minPoint.z, maxPoint.z), max_z = Math.max(minPoint.z, maxPoint.z);
		minPoint = new Point(min_x, min_y, min_z);
		maxPoint = new Point(max_x, max_y, max_z);
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return name + endl + "Min Point: " + minPoint + endl + "Max Point: " + maxPoint + endl;
	}

	//Initializers
	public AxisAlignedBox initMinPoint(Point minPoint) {
		this.minPoint = minPoint;
		fixBoundryPoints();
		return this;
	}

	public AxisAlignedBox initMaxPoint(Point maxPoint) {
		this.maxPoint = maxPoint;
		fixBoundryPoints();
		return this;
	}

	@Override
	public Hit intersect(Ray ray) {
		double[] raySource = {ray.source().x, ray.source().y, ray.source().z};
		double[] rayDirection = {ray.direction().x, ray.direction().y, ray.direction().z};
		double[] minBoxPoint ={minPoint.x, minPoint.y, minPoint.z};
		double[] maxBoxPoint = {maxPoint.x, maxPoint.y, maxPoint.z};
		double tMin = 0.0;
		double tMax = Ops.infinity;
		for (int i = 0; i < 3; i++) {
			if (Math.abs(rayDirection[i]) < Ops.epsilon) {
				if ((raySource[i] < minBoxPoint[i]) || (raySource[i] > maxBoxPoint[i])) {
					return null;
				}
			} else {
				double t1 = find_t(rayDirection[i], raySource[i], minBoxPoint[i]);
				double t2 = find_t(rayDirection[i], raySource[i], maxBoxPoint[i]);
				if (t1 > t2) {
					double tmp = t1;
					t1 = t2;
					t2 = tmp;
				}
				if ((Double.isNaN(t1)) || (Double.isNaN(t2))) {
					return null;
				}
				if (t1 > tMin) {
					tMin = t1;
				}
				if (t2 < tMax) {
					tMax = t2;
				}
				if ((tMin > tMax) || (tMax < 1.0E-5D))
					return null;
			}
		}
		double minT = tMin;
		boolean isWithin = false;
		if (minT < Ops.epsilon) {
			isWithin = true;
			minT = tMax;
		}
		Vec norm = getNormalToSurface(ray, minT);
		if (isWithin) {
			norm = norm.neg();
		}
		return new Hit(minT, norm).setIsWithin(isWithin);
	}

	private double find_t(double a, double b, double c) {
		if ((Math.abs(a) < Ops.epsilon) && (Math.abs(b - c) > Ops.epsilon)) {
			return Ops.infinity ;
		}
		if (((Math.abs(a) < Ops.epsilon) && (Math.abs(b - c) <Ops.epsilon))) {
			return 0.0;
		}
		double t = (c - b) / a;
		return t;
	}
	private Vec getNormalToSurface(Ray ray, double t) {
		Vec normalToSurface = null;
		Point hitPoint = ray.add(t);
		if (Math.abs(hitPoint.x - minPoint.x) < Ops.epsilon) {
			normalToSurface = new Vec(-1.0, 0.0, 0.0);
		}
		if (Math.abs(hitPoint.x - maxPoint.x) < Ops.epsilon) {
			normalToSurface = new Vec(1.0, 0.0, 0.0);
		}
		if (Math.abs(hitPoint.y - maxPoint.y) < Ops.epsilon) {
			normalToSurface = new Vec(0.0, 1.0, 0.0);
		}
		if (Math.abs(hitPoint.y - minPoint.y) < Ops.epsilon) {
			normalToSurface = new Vec(0.0, -1.0, 0.0);
		}
		if (Math.abs(hitPoint.z - maxPoint.z) < Ops.epsilon) {
			normalToSurface = new Vec(0.0, 0.0, 1.0);
		}
		if (Math.abs(hitPoint.z - minPoint.z) < Ops.epsilon) {
			normalToSurface = new Vec(0.0, 0.0, -1.0);
		}
		return normalToSurface;
	}

}
