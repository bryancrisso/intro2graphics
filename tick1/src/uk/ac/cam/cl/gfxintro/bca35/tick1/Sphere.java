package uk.ac.cam.cl.gfxintro.bca35.tick1;

public class Sphere extends SceneObject {

	// Sphere coefficients
	private final double SPHERE_KD = 0.8;
	private final double SPHERE_KS = 1.2;
	private final double SPHERE_ALPHA = 10;
	private final double SPHERE_REFLECTIVITY = 0.3;

	// The world-space position of the sphere
	protected Vector3 position;

	public Vector3 getPosition() {
		return position;
	}

	// The radius of the sphere in world units
	private double radius;

	public Sphere(Vector3 position, double radius, ColorRGB colour) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = SPHERE_KD;
		this.phong_kS = SPHERE_KS;
		this.phong_alpha = SPHERE_ALPHA;
		this.reflectivity = SPHERE_REFLECTIVITY;
	}

	public Sphere(Vector3 position, double radius, ColorRGB colour, double kD, double kS, double alphaS, double reflectivity, ColorRGB transmittance) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = kD;
		this.phong_kS = kS;
		this.phong_alpha = alphaS;
		this.reflectivity = reflectivity;
		this.transmittance = transmittance;
	}

	/*
	 * Calculate intersection of the sphere with the ray. If the ray starts inside the sphere,
	 * intersection with the surface is also found.     
	 */
	public RaycastHit intersectionWith(Ray ray) {

		// Get ray parameters
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection();
		
		// Get sphere parameters
		Vector3 C = position;
		double r = radius;

		// Calculate quadratic coefficients
		double a = D.dot(D);
		double b = 2 * D.dot(O.subtract(C));
		double c = (O.subtract(C)).dot(O.subtract(C)) - Math.pow(r, 2);
		
		// TODO: Determine if ray and sphere intersect - if not return an empty RaycastHit
		double d = b*b - 4*a*c;
		if (d < 0)
		{
			return new RaycastHit();
		}
        // TODO: If so, work out any point of intersection
		double s1 = (-b + Math.sqrt(d))/(2*a);
		double s2 = (-b - Math.sqrt(d))/(2*a);
		if (s1 <= 0 && s2 <= 0)
		{
			return new RaycastHit();
		}
        // TODO: Then return a RaycastHit that includes the object, ray distance, point, and normal vector
		Vector3 point = ray.getOrigin().add(ray.getDirection().scale(Math.min(s1, s2)));
		double distance = point.subtract(ray.getOrigin()).magnitude();
		Vector3 normal = getNormalAt(point);

		return new RaycastHit(this, distance, point, normal);
	}

	// Get normal to surface at position
	public Vector3 getNormalAt(Vector3 position) {
		return position.subtract(this.position).normalised();
	}
}