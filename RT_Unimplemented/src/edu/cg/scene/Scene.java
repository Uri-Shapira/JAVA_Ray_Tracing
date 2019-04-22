package edu.cg.scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.cg.Logger;
import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Vec;
import edu.cg.scene.camera.PinholeCamera;
import edu.cg.scene.lightSources.Light;
import edu.cg.scene.objects.Surface;
import edu.cg.scene.objects.Intersectable;

public class Scene {
	private String name = "scene";
	private int maxRecursionLevel = 1;
	private int antiAliasingFactor = 1; //gets the values of 1, 2 and 3
	private boolean renderRefarctions = false;
	private boolean renderReflections = false;
	
	private PinholeCamera camera;
	private Vec ambient = new Vec(1, 1, 1); //white
	private Vec backgroundColor = new Vec(0, 0.5, 1); //blue sky
	private List<Light> lightSources = new LinkedList<>();
	private List<Surface> surfaces = new LinkedList<>();
	
	
	//MARK: initializers
	public Scene initCamera(Point eyePoistion, Vec towardsVec, Vec upVec,  double distanceToPlain) {
		this.camera = new PinholeCamera(eyePoistion, towardsVec, upVec,  distanceToPlain);
		return this;
	}
	
	public Scene initAmbient(Vec ambient) {
		this.ambient = ambient;
		return this;
	}
	
	public Scene initBackgroundColor(Vec backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	
	public Scene addLightSource(Light lightSource) {
		lightSources.add(lightSource);
		return this;
	}
	
	public Scene addSurface(Surface surface) {
		surfaces.add(surface);
		return this;
	}
	
	public Scene initMaxRecursionLevel(int maxRecursionLevel) {
		this.maxRecursionLevel = maxRecursionLevel;
		return this;
	}
	
	public Scene initAntiAliasingFactor(int antiAliasingFactor) {
		this.antiAliasingFactor = antiAliasingFactor;
		return this;
	}
	
	public Scene initName(String name) {
		this.name = name;
		return this;
	}
	
	public Scene initRenderRefarctions(boolean renderRefarctions) {
		this.renderRefarctions = renderRefarctions;
		return this;
	}
	
	public Scene initRenderReflections(boolean renderReflections) {
		this.renderReflections = renderReflections;
		return this;
	}
	
	//MARK: getters
	public String getName() {
		return name;
	}
	
	public int getFactor() {
		return antiAliasingFactor;
	}
	
	public int getMaxRecursionLevel() {
		return maxRecursionLevel;
	}
	
	public boolean getRenderRefarctions() {
		return renderRefarctions;
	}
	
	public boolean getRenderReflections() {
		return renderReflections;
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator(); 
		return "Camera: " + camera + endl +
				"Ambient: " + ambient + endl +
				"Background Color: " + backgroundColor + endl +
				"Max recursion level: " + maxRecursionLevel + endl +
				"Anti aliasing factor: " + antiAliasingFactor + endl +
				"Light sources:" + endl + lightSources + endl +
				"Surfaces:" + endl + surfaces;
	}
	
	private transient ExecutorService executor = null;
	private transient Logger logger = null;
	
	private void initSomeFields(int imgWidth, int imgHeight, Logger logger) {
		this.logger = logger;
		//TODO: initialize your additional field here.
		//      You can also change the method signature if needed.
	}

	public BufferedImage render(int imgWidth, int imgHeight, double viewPlainWidth,Logger logger)
			throws InterruptedException, ExecutionException {
		// TODO: Please notice the following comment.
		// This method is invoked each time Render Scene button is invoked.
		// Use it to initialize additional fields you need.
		initSomeFields(imgWidth, imgHeight, logger);
		
		BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		camera.initResolution(imgHeight, imgWidth, viewPlainWidth);
		int nThreads = Runtime.getRuntime().availableProcessors();
		nThreads = nThreads < 2 ? 2 : nThreads;
		this.logger.log("Intitialize executor. Using " + nThreads + " threads to render " + name);
		executor = Executors.newFixedThreadPool(nThreads);
		
		@SuppressWarnings("unchecked")
		Future<Color>[][] futures = (Future<Color>[][])(new Future[imgHeight][imgWidth]);
		
		this.logger.log("Starting to shoot " +
			(imgHeight*imgWidth*antiAliasingFactor*antiAliasingFactor) +
			" rays over " + name);

		for(int y = 0; y < imgHeight; ++y)
			for(int x = 0; x < imgWidth; ++x)
				futures[y][x] = calcColor(x, y);
		
		this.logger.log("Done shooting rays.");
		this.logger.log("Wating for results...");
		
		for(int y = 0; y < imgHeight; ++y)
			for(int x = 0; x < imgWidth; ++x) {
				Color color = futures[y][x].get();
				img.setRGB(x, y, color.getRGB());
			}
		
		executor.shutdown();
		
		this.logger.log("Ray tracing of " + name + " has been completed.");
		
		executor = null;
		this.logger = null;
		
		return img;
	}
	
	private Future<Color> calcColor(int x, int y) {
		return executor.submit(() -> {
			// TODO: You need to re-implement this method if you want to handle
			//       super-sampling. You're also free to change the given implementation as you like.
			Point centerPoint = camera.transform(x, y);
			Ray ray = new Ray(camera.getCameraPosition(), centerPoint);
			Vec color = calcColor(ray, 0);
			return color.toColor();
		});
	}
	
	private Vec calcColor(Ray ray, int recursionLevel) {
//		if(recursionLevel <= maxRecursionLevel){
			// calculate colorVector regularly as done below
			// If reflection intensity != 0 ....
				// starting point = hit point of the ray to the surface
				// Ray ray = new Ray(starting point, direction of reflect direction / retract direction
//				// return colorVector.add(I_R.mult(calcColor(new ray, ++recursionLevel)))
			// else return colorVector
//		}
		Hit closest_hit = closestHit(ray);
		if(closest_hit != null){
			Vec colorVector = new Vec(0.0);
			Point startingPoint = ray.source();
			Point hitPoint = ray.getHittingPoint(closest_hit);
			Surface surface_hit = closest_hit.getSurface();
			Vec Ka = surface_hit.Ka();
			Vec Kd = surface_hit.Kd();
			Vec Ks = surface_hit.Ks();
			Vec ambient = Ka.mult(this.ambient);
			colorVector = colorVector.add(ambient);
			for (Light lightSource : this.lightSources){
				Ray rayToLight = lightSource.rayToLight(hitPoint);
				if(!lightIsOccluded(rayToLight, lightSource)){
					Vec I_L = lightSource.intensity(hitPoint, rayToLight);
					Vec diffuse = Kd.mult(closest_hit.getNormalToSurface().dot(rayToLight.direction())).mult(I_L);
					colorVector = colorVector.add(diffuse);
					Vec R_hat = Ops.reflect(rayToLight.direction(),closest_hit.getNormalToSurface()).normalize();
					Vec V = hitPoint.sub(startingPoint).normalize();
					double cosine_alpha = V.dot(R_hat);
					Vec specular = Ks.mult(Math.pow(cosine_alpha, surface_hit.shininess())).mult(I_L);
					colorVector = colorVector.add(specular);
				}
			}
			return colorVector;
		}
		else {
			return this.backgroundColor;

		}
	}

	private Hit closestHit(Ray ray){
		double closestT = Ops.infinity;
		Hit closestHit = null;
		for (Intersectable surface : surfaces){
			Hit hit = surface.intersect(ray);
			if (hit != null) {
				if (hit.t() < closestT ){ // Maybe need to add here && hit.t() > 0
					closestHit = hit;
					closestT = hit.t();
				}
			}
		}
		return closestHit;
	}

	private boolean lightIsOccluded(Ray rayToLight, Light light){
		for(Surface surface : surfaces){
			//if(surface != hit_surface) - not sure if this is necessary????
			if (light.isOccludedBy(surface, rayToLight)){
				return true;
			}
		}
		return false;
	}

}
