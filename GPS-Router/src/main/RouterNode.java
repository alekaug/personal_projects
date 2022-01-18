package main;

import java.util.ArrayList;
import org.json.JSONArray;

/**
 * It represents the point on the map: its coordinates, id, connections, description.
 * @author Aleksander Augustyniak
 */
public class RouterNode 
{
	/**
	 * An identification number of a node.
	 */
	private String id                         = null;
	
	/**
	 * A list of this node's connections.
	 */
	private ArrayList<RouterNode> connections = null;
	
	/**
	 * It holds a node's coordinates: longitude and latitude.
	 */
	private NodeCoordinates coords            = null;
	
	/**
	 * A node's description. It can be used to provide additional information about the
	 * particular node.
	 */
	private String description                = null;
	
	/**
	 * The node is being created with the information given by the parameters.
	 * @param id    identification name
	 * @param lon   longitude
	 * @param lat   latitude
	 */
	public RouterNode(String id, double lon, double lat)
	{
		this.id                               = id;
		this.coords                           = new NodeCoordinates(lon, lat);
		this.connections                      = new ArrayList<>();
	}
	
	/**
	 * It adds a node to the list of connections.
	 * @param rn node object.
	 */
	public void addConnection(RouterNode rn)
	{
		if(!this.connections.contains(rn))
			this.connections.add(rn);
	}
	
	/**
	 * It returns the id of the node.
	 * @return the identification name.
	 */
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * 
	 * @param  rn node object.
	 * @return a boolean state whether the node exists in the connections list.
	 */
	public boolean isConnected(RouterNode rn)
	{
		return connections.contains(rn);
	}
	
	/**
	 * It returns the list of connections.
	 * @return the list of connections.
	 */
	public ArrayList<RouterNode> getConnections()
	{
		return this.connections;
	}
	
	/**
	 * The description could be changed using that method.
	 * @param description new node's description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * It returns the node's description.
	 * @return the node's description.
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * The node's coordinates could be changed using that method.
	 * @return the node's coordinates.
	 */
	public NodeCoordinates getCoordinates()
	{
		return this.coords;
	}
}
