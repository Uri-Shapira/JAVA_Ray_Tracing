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
		double tMin = -Ops.infinity;
		double tMax = Ops.infinity;
		if (Math.abs(ray.direction().x) <= Ops.epsilon) {
			if (ray.source().x < minPoint.x || ray.source().x > maxPoint.x){
				return null;
			}
		}
		else{
			double txMin = find_t(ray.direction().x, minPoint.x, ray.source().x);
			if(Double.isNaN(txMin)){
				return null;
			}
			double txMax = find_t(ray.direction().x, maxPoint.x, ray.source().x);
			if(Double.isNaN(txMax)){
				return null;
			}
			if (txMax < txMin) {
				double tmp = txMax;
				txMax = txMin;
				txMin = tmp;
			}
			tMax = (txMax < tMax) ? txMax : tMax;
			tMin = (txMin > tMin) ? txMin : tMin;
			if(tMin > tMax || tMax < Ops.epsilon){
				return null;
			}
		}
		if (Math.abs(ray.direction().y) <= Ops.epsilon) {
			if (ray.source().y < minPoint.y || ray.source().y > maxPoint.y) {
				return null;
			}
		}
		else {
			double tyMin = find_t(ray.direction().y, minPoint.y, ray.source().y);
			if(Double.isNaN(tyMin)){
				return null;
			}
			double tyMax = find_t(ray.direction().y, maxPoint.y, ray.source().y);
			if(Double.isNaN(tyMax)){
				return null;
			}
			if (tyMax < tyMin) {
				double tmp = tyMax;
				tyMax = tyMin;
				tyMin = tmp;
			}
			tMax = (tyMax < tMax) ? tyMax : tMax;
			tMin = (tyMin > tMin) ? tyMin : tMin;
			if(tMin > tMax || tMax < Ops.epsilon){
				return null;
			}
		}
		if (Math.abs(ray.direction().z) <= Ops.epsilon) {
			if (ray.source().z < minPoint.z || ray.source().z > maxPoint.z) {
				return null;
			}
		}
		else {
			double tzMin = find_t(ray.direction().z, minPoint.z, ray.source().z);
			if(Double.isNaN(tzMin)){
				return null;
			}
			double tzMax = find_t(ray.direction().z, maxPoint.z, ray.source().z);
			if(Double.isNaN(tzMax)){
				return null;
			}
			if (tzMax < tzMin) {
				double tmp = tzMax;
				tzMax = tzMin;
				tzMin = tmp;
			}
			tMax = (tzMax < tMax) ? tzMax : tMax;
			tMin = (tzMin > tMin) ? tzMin : tMin;
			if(tMin > tMax || tMax < Ops.epsilon){
				return null;
			}
		}
		double t_value = tMin;
		boolean isWithin = false;
		if(tMin < Ops.epsilon){
			isWithin = true;
			t_value = tMax;
		}
		Vec normal = calcNormal(ray.add(t_value));
		if(isWithin){
			normal = normal.neg();
		}
		Hit hit = new Hit(t_value, normal).setIsWithin(isWithin);
		return hit;
	}

	private double find_t(double rayDirection, double boxExtreme, double raySource) {
		if ((Math.abs(rayDirection) < Ops.epsilon) && (Math.abs(boxExtreme - raySource) > Ops.epsilon)) {
			return Ops.infinity ;
		}
		if (((Math.abs(rayDirection) < Ops.epsilon) && (Math.abs(boxExtreme - raySource) <Ops.epsilon))) {
			return 0.0;
		}
		double t = (boxExtreme - raySource) / rayDirection;
		return t;
	}

	private Vec calcNormal(Point hitPoint) {
		Vec normalToSurface = null;
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
