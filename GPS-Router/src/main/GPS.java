package main;

import java.io.Console;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * A GPS class is used to compute the route based on the nodes.
 * @author Aleksander Augustyniak
 */
public class GPS {
	/**
	 * A data structure, which stores all of the nodes loaded from the external source.
	 */
	private HashMap<String, RouterNode> nodes;
	
	/**
	 * A list of nodes in the computed route. Available after running a setRoute method.
	 */
	private ArrayList<RouterNode> route = new ArrayList<RouterNode>();
	
	// private RouterNode waypoint;
	
	/**
	 * It takes into account if the route should consist of shortcuts.
	 */
	private boolean shortcutsAllowed;
	
	/**
	 * It initiates a GPS object with a default values.
	 */
	public GPS()
	{
		this.nodes = new HashMap<String, RouterNode>();
		this.shortcutsAllowed = true;
	}
	
	/**
	 * It initiates a GPS object with a provided path to a JSON nodes' file.
	 */
	public GPS(String nodes_path)
	{
		this.nodes = new HashMap<String, RouterNode>();
		this.shortcutsAllowed = true;
		this.initStructure(nodes_path);
	}
	
	/**
	 * It reads nodes' information from a JSON file (given by a path):
	 * their id names, coordinates' values and connections between each other.
	 * The nodes' objects are being held in a HashMap – identified by the nodes' id.
	 */
	public void initStructure(String nodes_path)
	{
		try
		{
			String data = new String(Files.readAllBytes(Paths.get(nodes_path)));
			JSONObject json = new JSONObject(data);
			JSONArray nodes_array = json.getJSONArray("nodes");
			System.out.println(nodes_array.length());
			for(int i = 0; i < nodes_array.length(); i++)
			{
				String id = nodes_array.getJSONObject(i).getString("id");
				double longitude = nodes_array.getJSONObject(i).getDouble("lon");
				double latitude = nodes_array.getJSONObject(i).getDouble("lat");
				this.nodes.putIfAbsent(id, new RouterNode(id, longitude, latitude));
			}
			for(int i = 0; i < nodes_array.length(); i++)
			{
				String node_id = nodes_array.getJSONObject(i).getString("id");
				JSONArray connections_array = nodes_array.getJSONObject(i).getJSONArray("connections");
				for(int j = 0; j < connections_array.length(); j++)
				{
					RouterNode node = this.nodes.get(connections_array.get(j));
					this.nodes.get(node_id).addConnection(node);
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * It sets the current route based on the available route in the data structure.
	 * There are 3 available algorithms to choose from to compute the route.
	 * 
	 * @param algorithm  an integer value from 1 to 3. It allows to choose the particular route setting algorithm.
	 *                   The method uses different algorithms: 
	 *                   1 - Bellman-Ford-Moore algorithm, 
	 *                   2 - Dijkstra algorithm (doesn't work),
	 *                   3 - Floyd-Warshall algorithm.
	 * @param startPoint a node object used in the first algorithm (Bellman-Ford-Moore) - a representation of a start
	 *                   point.
	 */
	public void setRoute(int algorithm, RouterNode startPoint)
	{
		// Bellman-Ford-Moore Algorithm - used to compute a route from one node to the other
		if(algorithm == 1)
		{
			HashMap<String, Double> distance = new HashMap<String, Double>();
			HashMap<String, RouterNode> predecessors = new HashMap<String, RouterNode>();
			for(Map.Entry<String, RouterNode> rn : this.nodes.entrySet())
			{
				if(rn.getValue() != startPoint)
					distance.put(rn.getKey(), Double.POSITIVE_INFINITY);
				else
					distance.put(rn.getKey(), 0.0);
			}
			
			// relaxation			
			for(int n = 0; n < this.nodes.size() - 1; n++)
			{
				for(Map.Entry<String, RouterNode> rn1 : this.nodes.entrySet())
				{
					for(RouterNode connected_node : rn1.getValue().getConnections())
					{
						String key = connected_node.getId();
						double dist = Utilities.distanceEuclidian(rn1.getValue(), connected_node);
						if(distance.get(rn1.getKey()) + dist < distance.get(key))
						{
							distance.put(key, distance.get(rn1.getKey()) + dist); // = rn2.getKey() + dist;
							predecessors.put(key, rn1.getValue());
						}
					}
				}
			}
			
			Scanner in = new Scanner(System.in);
			boolean algorithm_loop = true;
			while(algorithm_loop)
			{
				String b, decision;
				do
				{
					System.out.println("Type in ending point: ");
					b = in.nextLine();
				} while (!this.nodes.containsKey(b));
				double dist = distance.get(b);
				RouterNode rn2 = this.nodes.get(b);
				System.out.print("Your route is: ");
				while(rn2 != startPoint)
				{
					this.route.add(0, rn2);
					rn2 = predecessors.get(rn2.getId());
				}
				System.out.print(startPoint.getId() + " ");
				for(RouterNode rn : this.route)
					System.out.print(rn.getId() + " ");
				System.out.println("");
				System.out.println("Distance: " + String.format("%.2f", dist) + " pixels.");
				System.out.println("");
				
				System.out.println("Do you wish to continue? (y - yes, n - no): ");
				do
				{
					decision = in.nextLine();
				} while (!decision.equals("y") && !decision.equals("n"));
				if(decision.equals("y"))
					this.route = new ArrayList<RouterNode>();
				else
					algorithm_loop = false;
			}
			in.close();
		}
		
		else if(algorithm == 2)
		{
			return;
			/*
			int nodesSize = this.nodes.size();
			double[] distance = new double[nodesSize];
			RouterNode[] predecessors = new RouterNode[nodesSize];
			Arrays.fill(distance, Double.POSITIVE_INFINITY);
			distance[Integer.parseInt(startPoint.getId()) - 1] = 0;
			Utilities util = new Utilities();
			
			// relaxation
			ArrayList<RouterNode> S = new ArrayList<RouterNode>();
			PriorityQueue<RouterNode> Q = new PriorityQueue<RouterNode>();
			Q.addAll(this.nodes);
			
			while(!Q.isEmpty())
			{
				RouterNode u = Q.poll();
				S.add(u);
				for(String conn : u.getConnections())
				{
					int v = Integer.parseInt(conn) - 1;
					double dist = util.distance(u, this.nodes.get(v));
					if(distance[Integer.parseInt(u.getId()) - 1] + dist < distance[v])
					{
						distance[v] = distance[Integer.parseInt(u.getId()) - 1] + dist;
						predecessors[v] = u;
					}
					
				}
			} */
		}
		
		else if(algorithm == 3)
		{
			HashMap<String, HashMap<String, Double>> distance = new HashMap<>();
			HashMap<String, HashMap<String, RouterNode>> predecessors = new HashMap<>();
			for(Map.Entry<String, RouterNode> rn1 : this.nodes.entrySet())
			{
				for(Map.Entry<String, RouterNode> rn2 : this.nodes.entrySet())
				{
					// distance map initialization
					HashMap<String, Double> dist;
					if(distance.get(rn1.getKey()) == null)
						dist = new HashMap<String, Double>();
					else
						dist = distance.get(rn1.getKey());
					dist.put(rn2.getKey(), Double.POSITIVE_INFINITY);
					distance.put(rn1.getKey(), dist);
					
					// predecessors map initialization
					HashMap<String, RouterNode> pred;
					if(predecessors.get(rn1.getKey()) == null)
						pred = new HashMap<String, RouterNode>();
					else
						pred = predecessors.get(rn1.getKey());
					pred.put(rn2.getKey(), null);
					predecessors.put(rn1.getKey(), pred);
				}
			}

			for(Map.Entry<String, RouterNode> i : this.nodes.entrySet())
			{
				for(Map.Entry<String, RouterNode> j : this.nodes.entrySet())
				{
					String key1 = i.getKey(), key2 = j.getKey();
					if(key1 == key2)
						distance.get(key1).put(key2, 0.0);
					else if(i.getValue().isConnected(j.getValue()))
						distance.get(key1).put(key2,  Utilities.distanceEuclidian(i.getValue(), j.getValue()));
					else
						distance.get(key1).put(key2,  Double.POSITIVE_INFINITY);
					
					if(distance.get(key1).get(key2) != Double.POSITIVE_INFINITY)
						predecessors.get(key1).put(key2, j.getValue());
					else
						predecessors.get(key1).put(key2, null);
				}
			}
			
			for(Map.Entry<String, RouterNode> k : this.nodes.entrySet())
			{
				for(Map.Entry<String, RouterNode> i : this.nodes.entrySet())
				{
					String key1 = k.getKey(), key2 = i.getKey();
					if(distance.get(key2).get(key1) != Double.POSITIVE_INFINITY)
					{
						for(Map.Entry<String, RouterNode> j : this.nodes.entrySet())
						{
							String key3 = j.getKey();
							if(distance.get(key2).get(key1) + distance.get(key1).get(key3) < distance.get(key2).get(key3))
							{
								distance.get(key2).put(key3, distance.get(key2).get(key1) + distance.get(key1).get(key3));
								predecessors.get(key2).put(key3, predecessors.get(key2).get(key1));
							}
						}
					}
				}
			}
			
			Scanner in = new Scanner(System.in);
			boolean algorithm_loop = true;
			while(algorithm_loop)
			{
				String a, b, decision;
				do
				{
					System.out.println("Type in starting point: ");
					a = in.nextLine();
				} while (!this.nodes.containsKey(a));
				do
				{
					System.out.println("Type in ending point: ");
					b = in.nextLine();
				} while (!this.nodes.containsKey(b));
				double dist = distance.get(a).get(b);
				RouterNode rn1 = this.nodes.get(a);
				RouterNode rn2 = this.nodes.get(b);
				System.out.print("Your route is: ");
				while(!rn1.equals(rn2))
				{
					this.route.add(rn1);
					rn1 = predecessors.get(rn1.getId()).get(rn2.getId());
				}
				this.route.add(rn1);
				for(RouterNode rn : this.route)
					System.out.print(rn.getId() + " ");
				System.out.println("");
				System.out.println("Distance: " + String.format("%.2f", dist) + " pixels.");
				System.out.println("");
				
				System.out.println("Do you wish to continue? (y - yes, n - no): ");
				do
				{
					decision = in.nextLine();
				} while (!decision.equals("y") && !decision.equals("n"));
				if(decision.equals("y"))
					this.route = new ArrayList<>();
				else
					algorithm_loop = false;
			}
		in.close();
		}
	}
	
	public static void main(String[] args)
	{
		int algorithm = 0;
		String start_node = "";
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("alg"))
				algorithm = Integer.parseInt(args[i + 1]);
			else if(args[i].equals("start"))
				start_node = args[i + 1];
		}
		GPS gps = new GPS("src/nodes_db.json");
		RouterNode start = gps.nodes.get(start_node);
		if(start == null || !(algorithm > 0 && algorithm < 4))
			return;
		gps.setRoute(algorithm, start);
	}
}


