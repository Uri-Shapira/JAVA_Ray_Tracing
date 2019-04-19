package edu.cg.scene.lightSources;

import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class PointLight extends Light {
	protected Point position;
	
	//Decay factors:
	protected double kq = 0.01;
	protected double kl = 0.1;
	protected double kc = 1;
	
	protected String description() {
		String endl = System.lineSeparator();
		return "Intensity: " + intensity + endl +
				"Position: " + position + endl +
				"Decay factors: kq = " + kq + ", kl = " + kl + ", kc = " + kc + endl;
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Point Light:" + endl + description();
	}
	
	@Override
	public PointLight initIntensity(Vec intensity) {
		return (PointLight)super.initIntensity(intensity);
	}
	
	public PointLight initPosition(Point position) {
		this.position = position;
		return this;
	}
	
	public PointLight initDecayFactors(double kq, double kl, double kc) {
		this.kq = kq;
		this.kl = kl;
		this.kc = kc;
		return this;
	}

	//TODO: add some methods
	/**
	 * Constructs a ray originated from the given point to the light.
	 * @param fromPoint - The initial point of the ray
	 * @return a ray origniated from 'fromPoint' to the light source.
	 */
	public Ray rayToLight(Point fromPoint){
		return null;
	}


	/**
	 * Checks if the given surface occludes the light-source. The surface occludes the light source
	 * if the given ray first intersects the surface before reaching the light source.
	 * @param surface -The given surface
	 * @param rayToLight - the ray to the light source
	 * @return true if the ray is occluded by the surface..
	 */
	public boolean isOccludedBy(Surface surface, Ray rayToLight){
		return false;
	}

	/**
	 * Returns the light intensity at the specified point.
	 * @param hittingPoint - The given point
	 * @param rayToLight - A ray to the light source (this is relevant for point-light and spotlight)
	 * @return A vector representing the light intensity (the r,g and b channels).
	 */
	public Vec intensity(Point hittingPoint, Ray rayToLight){
		return null;
	}
}
