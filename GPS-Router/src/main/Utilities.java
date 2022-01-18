package main;

/**
 * The class contains the GPS "business logic" – additional tools provided to help the
 * main class with calculations.
 * @author Aleksander Augustyniak
 */
public class Utilities
{
	/**
	 * Pre-computed element used in the distanceGeocentric method.
	 */
	private static final double el1 = Math.PI / 180.0;
	
	/**
	 * Earth's radius in meters.
	 */
	private static final double EARTH_RADIUS = 6371000;
	
	/**
	 * Computes an Euclidian distance between two RouterNode points.
	 * @param node1 the first route node.
	 * @param node2 the second route node.
	 * @return the Euclidian distance between both given nodes.
	 */
	static double distanceEuclidian(RouterNode node1, RouterNode node2)
	{
		// Euclidian distance
		double a = Math.pow(node1.getCoordinates().getLongitude() - node2.getCoordinates().getLongitude(), 2);
		double b = Math.pow(node1.getCoordinates().getLatitude() - node2.getCoordinates().getLatitude(), 2);
		return Math.sqrt(a + b);
	}
	
	/**
	 * Computes the distance between two points. Effective if they are far away from each other.
	 * @param node1 the first route node.
	 * @param node2the second route node.
	 * @return the Geographical distance between both given nodes.
	 */
	static double distanceGeographical(RouterNode node1, RouterNode node2)
	{
		double lat1_rad = node1.getCoordinates().getLatitude() * Utilities.el1;
		double lat2_rad = node2.getCoordinates().getLatitude() * Utilities.el1;
		double delta_lat = (lat2_rad - lat1_rad) * Utilities.el1;
		double delta_lon = (node2.getCoordinates().getLongitude() - node1.getCoordinates().getLongitude()) * Utilities.el1;
		
		double a = Math.pow(Math.sin(delta_lat / 2), 2) +
				Math.cos(lat1_rad) + Math.cos(lat2_rad) +
				Math.pow(delta_lon / 2, 2);
		
		double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Utilities.EARTH_RADIUS * b;
	}
}
