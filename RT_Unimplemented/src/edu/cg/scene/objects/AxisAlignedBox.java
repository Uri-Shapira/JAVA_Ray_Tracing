package edu.cg.scene.objects;

import edu.cg.UnimplementedMethodException;
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
		double tNear = -1.0E8D;double tFar = 1.0E8D;
		double[] rayP = ray.source().asArray();double[] rayD = ray.direction().asArray();
		double[] minP = minPoint.asArray();double[] maxP = maxPoint.asArray();
		for (int i = 0; i < 3; i++) {
			if (Math.abs(rayD[i]) <= 1.0E-5D) {
				if ((rayP[i] < minP[i]) || (rayP[i] > maxP[i])) {
					return null;
				}
			} else {
				double t1 = find_t(rayD[i], rayP[i], minP[i]);
				double t2 = find_t(rayD[i], rayP[i], maxP[i]);
				if (t1 > t2) {
					double tmp = t1;
					t1 = t2;
					t2 = tmp;
				}
				if ((Double.isNaN(t1)) || (Double.isNaN(t2))) {
					return null;
				}
				if (t1 > tNear) {
					tNear = t1;
				}
				if (t2 < tFar) {
					tFar = t2;
				}
				if ((tNear > tFar) || (tFar < 1.0E-5D))
					return null;
			}
		}
		double minT = tNear;
		boolean isWithin = false;
		if (minT < 1.0E-5D) {
			isWithin = true;
			minT = tFar;
		}
		Vec norm = find_normal(ray.add(minT));
		if (isWithin) {
			norm = norm.neg();
		}
		return new Hit(minT, norm).setIsWithin(isWithin);
	}

	private double find_t(double a, double b, double c) {
		if ((Math.abs(a) < 1.0E-5D) && (Math.abs(b - c) > 1.0E-5D)) {
			return 1.0E8D;
		}
		if ((Math.abs(a) < 1.0E-5D) && (Math.abs(b - c) < 1.0E-5D))
			return 0.0D;
		double t = (c - b) / a;
		return t;
	}

	private Vec find_normal(Point hitPoint){
		Vec normal = null;
		if (Math.abs(hitPoint.z - minPoint.z) <= 1.0E-5D) {
			return new Vec(0.0D, 0.0D, -1.0D);
		}
		if (Math.abs(hitPoint.z - maxPoint.z) <= 1.0E-5D) {
			return new Vec(0.0D, 0.0D, 1.0D);
		}
		if (Math.abs(hitPoint.y - minPoint.y) <= 1.0E-5D) {
			return new Vec(0.0D, -1.0D, 0.0D);
		}
		if (Math.abs(hitPoint.y - maxPoint.y) <= 1.0E-5D) {
			return new Vec(0.0D, 1.0D, 0.0D);
		}
		if (Math.abs(hitPoint.x - minPoint.x) <= 1.0E-5D) {
			return new Vec(-1.0D, 0.0D, 0.0D);
		}
		if (Math.abs(hitPoint.x - maxPoint.x) <= 1.0E-5D) {
			return new Vec(1.0D, 0.0D, 0.0D);
		}
		return null;
	}
	
}
