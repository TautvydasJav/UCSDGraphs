package roadgraph;

import java.util.HashSet;

import geography.GeographicPoint;

/**
 * @author Jure Koren
 *
 */
public class MapNode implements Comparable<MapNode> {
	private GeographicPoint coords;
	private HashSet<MapEdge> edges = new HashSet<MapEdge>();;
	private String nodeName;
	private double distanceToGoal;
	private double distanceFromStart;
	private double timeFromStart;
	private double timeToGoal;
	private double priority;

	
	// empty node
	public MapNode() {
		super();
	}
	
	/**
	 * @param coords
	 */
	public MapNode(GeographicPoint coords) {
		super();
		this.coords = coords;
	}
	
	/** Add edge to this node
	 * 
	 * @param edge the edge to add to the node
	 */	
	public void addEdge(MapEdge edge) {
		//System.out.println("MapNode.addEdge: add edge to my list...");
		edges.add(edge);
	}
	
	@Override
	public String toString() {
		return "MapNode [coords=" + coords + ", nodeName=" + nodeName + "]";
	}
	
    @Override
    public int compareTo(MapNode otherNode)
    {
        /* The compareTo method compares the receiving object with the specified 
         * object and returns a negative integer, 0, or a positive integer depending 
         * on whether the receiving object is less than, equal to, or greater than 
         * the specified object. If the specified object cannot be compared to the 
         * receiving object, the method throws a ClassCastException.
         * */
    	
    	// if this is less than otherNode return -1
    	// if it's equal return 0
    	// if it's greater than otherNode return 1
    	
    	// compare distance to goal
    	
    	double thisValue = this.getPriority();
    	double otherValue = otherNode.getPriority();
    	
    	// not ok, we loose the decimals
    	//Double diff = thisDistance - otherDistance;
    	//return diff;
    	
    	if (thisValue < otherValue){
    		return -1;
    	} else if (thisValue > otherValue){
    		return 1;
    	} else {
    		return 0;
    	}
    	
    }	
    
    // get total distance (from start + to goal)
    public double getTotalDistance() {
    	return (distanceFromStart + distanceToGoal);
    }
    
    // get total time (from start + to goal)
    public double getTotalTime() {
    	return (timeFromStart + timeToGoal);
    }    
    
    
    // set total distance to goal automatically from goal node
	public void setDistanceToGoal(MapNode goalNode) {
		// get coords from goal
		GeographicPoint goalCoords = goalNode.getCoords();
		// update my distance
		this.distanceToGoal = this.getCoords().distance(goalCoords);
	}	
	
	// set total time to goal automatically from goal node
	public void setTimeToGoal(MapNode goalNode) {
		// get coords from goal
		GeographicPoint goalCoords = goalNode.getCoords();
		// update my distance by using fastest speed
		
		double distance = this.getCoords().distance(goalCoords);
		double time = MapEdge.getTime(MapEdge.MAX_TRAVEL_SPEED, distance);
		
		this.setTimeToGoal(time);
	}
    

    

	/*  getters / setters */
	
	public GeographicPoint getCoords() {
		return coords;
	}
	public void setCoords(GeographicPoint coords) {
		this.coords = coords;
	}
	public HashSet<MapEdge> getEdges() {
		return edges;
	}
	public void setEdges(HashSet<MapEdge> edges) {
		this.edges = edges;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public double getDistanceToGoal() {
		return distanceToGoal;
	}
	public void setDistanceToGoal(double distanceToGoal) {
		this.distanceToGoal = distanceToGoal;
	}
	public double getDistanceFromStart() {
		return distanceFromStart;
	}
	public void setDistanceFromStart(double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}
	public double getPriority() {
		return priority;
	}
	public void setPriority(double priority) {
		this.priority = priority;
	}

	public double getTimeFromStart() {
		return timeFromStart;
	}

	public void setTimeFromStart(double timeFromStart) {
		this.timeFromStart = timeFromStart;
	}

	public double getTimeToGoal() {
		return timeToGoal;
	}

	public void setTimeToGoal(double timeToGoal) {
		this.timeToGoal = timeToGoal;
	}
	
	/* end getters / setters */    
	
	
}