/**
 * 
 */
package roadgraph;

/**
 * @author Jure Koren
 *
 */
public class MapEdge {
	private String name;
	private double length;
	private String type;
	private MapNode from;
	private MapNode to;
	
	public static final int MAX_TRAVEL_SPEED = 200;	
	
	

	/** get speed for road/edge from road type
	 * 
	 * @return speed for road type
	 */
	public int getSpeedForThisRoad() {
		int speed;
		
		if (type.equals("residential")) {
			speed = 30;
		} else if (type.equals("city street")) {
			speed = 50;
		} else if (type.equals("trunk")) {
			speed = 100;
		} else if (type.equals("motorway") || type.equals("motorway_link")) {
			speed = 130;			
		} else {
			// default speed
			speed = 50;
		}
		return speed;
	}

	/** get time for road/edge from road type
	 * 
	 * @return time for road type
	 */
	public double getTime() {
		// use helper function
		double time = getTime(getSpeedForThisRoad(), length); 
		return time;
	}
	
	/** get time for road/edge for given distance
	 * 
	 * @return time for road type
	 */
	public static double getTime(int speed, double distance) {
		// time to travel in minutes
		double time = 0;
		// calculate time for traveling this distance with given speed in minutes
		time = 1 + (distance / speed * 60); 
		return time;
	}


	
	/* setters and getters */
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MapNode getFrom() {
		return from;
	}
	public void setFrom(MapNode from) {
		this.from = from;
	}
	public MapNode getTo() {
		return to;
	}
	public void setTo(MapNode to) {
		this.to = to;
	}
	
	// empty edge
	public MapEdge() {
		super();
	}
	
	/** Constructor
	 * 
	 * @param length distance of the edge/road
	 * @param type road type
	 * @param from starting node
	 * @param to ending node
	 */	
	public MapEdge(String name, String type, double length, MapNode from, MapNode to) {
		super();
		this.name = name;
		this.length = length;
		this.type = type;
		this.from = from;
		this.to = to;
	}
	
	
}