package edu.cg.scene.lightSources;

import edu.cg.algebra.*;
import edu.cg.scene.objects.Surface;


public class Spotlight extends PointLight {
	private Vec direction;

	public Spotlight initDirection(Vec direction) {
		this.direction = direction;
		return this;
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Spotlight: " + endl +
				description() + 
				"Direction: " + direction + endl;
	}
	
	@Override
	public Spotlight initPosition(Point position) {
		return (Spotlight)super.initPosition(position);
	}
	
	@Override
	public Spotlight initIntensity(Vec intensity) {
		return (Spotlight)super.initIntensity(intensity);
	}
	
	@Override
	public Spotlight initDecayFactors(double q, double l, double c) {
		return (Spotlight)super.initDecayFactors(q, l, c);
	}

	/**
	 * Checks if the given surface occludes the light-source. The surface occludes the light source
	 * if the given ray first intersects the surface before reaching the light source.
	 * @param surface -The given surface
	 * @param rayToLight - the ray to the light source
	 * @return true if the ray is occluded by the surface..
	 */
	public boolean isOccludedBy(Surface surface, Ray rayToLight){
		boolean isOccluded = false;
		if (rayToLight.direction().neg().dot(this.direction.normalize()) < Ops.epsilon) {
			isOccluded = true;
		}
		else {
			isOccluded = super.isOccludedBy(surface, rayToLight);
		}
		return isOccluded;
	}

	/**
	 * Returns the light intensity at the specified point.
	 * @param hittingPoint - The given point
	 * @param rayToLight - A ray to the light source (this is relevant for point-light and spotlight)
	 * @return A vector representing the light intensity (the r,g and b channels).
	 */
	public Vec intensity(Point hittingPoint, Ray rayToLight)  {
		Vec D = this.direction.normalize();
		Vec V = rayToLight.direction().neg().normalize();
		if (V.dot(D) < Ops.epsilon) {
			return new Vec(0.0, 0.0, 0.0);
		}
		return super.intensity(hittingPoint, rayToLight).mult(V.dot(D));
	}
}
