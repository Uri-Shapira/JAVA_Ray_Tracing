package edu.cg.scene.camera;

import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;
import edu.cg.algebra.Ray;


public class PinholeCamera {
	public Point cameraPosition;
	public Vec rightVec;
	public Vec towardsVec;
	public Vec upVec;
	public double distanceToPlain;
	public double resolutionX;
	public double resolutionY;
	public double viewPlainWidth;
	public Point imageMiddle;


	/**
	 * Initializes a pinhole camera model with default resolution 200X200 (RxXRy) and image width 2.
	 * @param cameraPosition - The position of the camera.
	 * @param towardsVec - The towards vector of the camera (not necessarily normalized).
	 * @param upVec - The up vector of the camera.
	 * @param distanceToPlain - The distance of the camera (position) to the center point of the image-plain.
	 *
	 */
	public PinholeCamera(Point cameraPosition, Vec towardsVec, Vec upVec, double distanceToPlain) {
		this.cameraPosition = cameraPosition;
		this.towardsVec = towardsVec.normalize();
		this.rightVec = this.towardsVec.cross(upVec).normalize();
		this.upVec = upVec.normalize();
		this.distanceToPlain = distanceToPlain;
		this.resolutionX = 200.0;
		this.resolutionY = 200.0;
		this.viewPlainWidth = 2.0;
		//ray.add = p0 + t*direction
		this.imageMiddle = new Ray(cameraPosition, towardsVec).add(distanceToPlain);
	}

	/**
	 * Initializes the resolution and width of the image.
	 * @param height - the number of pixels in the y direction.
	 * @param width - the number of pixels in the x direction.
	 * @param viewPlainWidth - the width of the image plain in world coordinates.
	 */
	public void initResolution(int height, int width, double viewPlainWidth) {
		this.resolutionY = height;
		this.resolutionX = width;
		this.viewPlainWidth = viewPlainWidth;
	}

	/**
	 * Transforms from pixel coordinates to the center point of the corresponding pixel in model coordinates.
	 * @param x - the index of the x direction of the pixel.
	 * @param y - the index of the y direction of the pixel.
	 * @return the middle point of the pixel (x,y) in the model coordinates.
	 */
	public Point transform(int x, int y) {
		double pixelWidth = this.viewPlainWidth / this.resolutionX;
		// Why does height not matter??????
		double pixelHeight = this.viewPlainWidth / this.resolutionY;
		Vec right = this.rightVec.mult((x - (this.resolutionX/2)) * pixelWidth);
		Vec up = this.upVec.mult(-1.0 * (y - (this.resolutionY/2)) * pixelHeight);
		Point transformedPoint = imageMiddle.add(right).add(up);
		return transformedPoint;
	}

	/**
	 * Returns a copy of the camera position
	 * @return a "new" point representing the camera position.
	 */
	public Point getCameraPosition() {
		Point CameraPosition = new Point(this.cameraPosition.x, this.cameraPosition.y, this.cameraPosition.z);
		return CameraPosition;
	}
}