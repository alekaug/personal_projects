package main;

/**
 * @author Aleksander Augustyniak
 */
public class NodeCoordinates
{
	/**
	 * Object's longitude value.
	 */
	private double longitude;
	
	/**
	 * Object's latitude value.
	 */
	private double latitude;
	
	/**
	 * Creates a node coordinates object. The method should receive two parameters.
	 * @param lon the value of longitude.
	 * @param lat the value of latitude.
	 */
	public NodeCoordinates(double lon, double lat)
	{
		this.setCoordinates(lon, lat);
	}
	
	/**
	 * Sets the coordinates.
	 * @param lon the longitude value.
	 * @param lat the latitude value.
	 */
	void setCoordinates(double lon, double lat)
	{
		this.longitude = lon;
		this.latitude = lat;
	}
	
	/**
	 * Sets the longitude.
	 * @param lon the longitude value.
	 */
	void setLongitude(double lon)
	{
		this.longitude = lon;
	}
	
	/**
	 * Gets the longitude.
	 * @return the node's longitude value.
	 */
	double getLongitude()
	{
		return this.longitude;
	}
	
	/**
	 * Sets the latitude.
	 * @param lat
	 */
	void setLatitude(double lat)
	{
		this.latitude = lat;
	}
	
	/**
	 *  Gets the latitude.
	 * @return the node's latitude value.
	 */
	double getLatitude()
	{
		return this.latitude;
	}
}
