package edu.cg.scene.lightSources;

import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class DirectionalLight extends Light {
	private Vec direction = new Vec(0, -1, -1);

	public DirectionalLight initDirection(Vec direction) {
		this.direction = direction;
		return this;
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Directional Light:" + endl + super.toString() +
				"Direction: " + direction + endl;
	}

	@Override
	public DirectionalLight initIntensity(Vec intensity) {
		return (DirectionalLight)super.initIntensity(intensity);
	}
	
	//TODO: add some methods

	/**
	 * Constructs a ray originated from the given point to the light.
	 * @param fromPoint - The initial point of the ray
	 * @return a ray origniated from 'fromPoint' to the light source.
	 */
	public  Ray rayToLight(Point fromPoint){
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
