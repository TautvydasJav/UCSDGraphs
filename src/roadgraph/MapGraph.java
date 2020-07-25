package roadgraph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private HashMap<GeographicPoint, MapNode> vertices;
	private HashSet<MapEdge> edges;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// new list
		System.out.println("New MapGraph");
		vertices = new HashMap<GeographicPoint, MapNode>();
		edges = new HashSet<MapEdge>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		// we just return the number
		return vertices.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		// new set
		HashSet<GeographicPoint> set = new HashSet<GeographicPoint>();

		// add from my vertices to this new set
		for (GeographicPoint vertix : vertices.keySet()) {
		    set.add(vertix);
		}
		
		return set;
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		return edges.size();
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		//System.out.println("MapGraph.addVertex: " + location.toString() );
		// check if we have it already or location is null
		if (location == null || vertices.containsKey(location) ) {
			// if we have it return false			
			return false;
			
		} else {				
			// add it
			MapNode n = new MapNode(location);
			
			// put on list/map
			vertices.put(location, n);
			
			// everything ok, return true
			return true;
			
		}
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//System.out.println("MapGraph.addEdge: " + roadName + ": " 
		//			+ from.toString() + " -> " + to.toString() );		
		
		// check arguments
		if (!vertices.containsKey(from) || from == null) {
			throw new IllegalArgumentException();
		} else if (!vertices.containsKey(to) || from == to) {
			throw new IllegalArgumentException();
		} else if (length == 0) {
			throw new IllegalArgumentException();
		}

		MapNode fromNode = vertices.get(from);
		MapNode toNode = vertices.get(to);		
		
		// create edge
		//System.out.println("MapGraph.addEdge: create edge...");
		MapEdge e = new MapEdge(roadName, roadType, length, fromNode, toNode);
		// add to my list of edges/roads
		//System.out.println("MapGraph.addEdge: add edge to list...");
		edges.add(e);
		//System.out.println("MapGraph.addEdge: add edge to start node...");
		// add road to starting node's edges
		fromNode.addEdge(e);
		
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// bfs - we need a fifo queue, visited set and parent map
		
		// check input parameters
		if (!checkSearchParams(start, goal)) {
			return null;
		}
		//System.out.println("MapGraph.bfs: Search: " + start + " -> " + goal );
		
		// get start and goal nodes
		MapNode startNode = vertices.get(start);
		MapNode goalNode = vertices.get(goal);
		
		// keep track of visited nodes and path
		HashSet<MapNode> visited = new HashSet<MapNode>();
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		
		// add first element to queue
		Queue<MapNode> queue = new LinkedList<MapNode>();
		queue.add(startNode);
		visited.add(startNode);
		
		// while queue not empty
		while (!queue.isEmpty()) {
			// get first element
			MapNode currNode = queue.remove();
			//System.out.println("MapGraph.bfs: check node: " + currNode.toString() );
			
			// Hook for visualization.  See writeup.
		    nodeSearched.accept(currNode.getCoords() );			
			
			// if curr == goal, we did it -> return parent map
			if (currNode.equals(goalNode)) {
				//System.out.println("MapGraph.bfs: check node: FOUND!");
				return createPath(startNode, goalNode, parentMap); // TODO
			} else {
				// add unvisited edges to my queue
				//System.out.println("MapGraph.bfs: add node's edges to list.");
				addEdgesForBfs(currNode, queue, visited, parentMap);
			}
			// next queue item	
		}
		
		// no path possible
		return null;
	}
	
	/** Add all edges of a node to the queue
	 * 
	 * @param currNode The node to use for finding edges
	 * @param queue Queue where we add nodes
	 * @param visited A list of visited nodes, so we don't add the same nodes multiple times
	 * @param parentMap A map of parent nodes to facilitate the reconstruction of the path
	 */	
	private void addEdgesForBfs(MapNode currNode, Queue<MapNode> queue, 
			HashSet<MapNode> visited, HashMap<MapNode, MapNode> parentMap) {
		
		// get unvisited roads to other nodes add add them to queue
		HashSet<MapEdge> roads = currNode.getEdges();
		for(MapEdge road : roads) {
			MapNode destNode = road.getTo();
			// if not visited
			if (!visited.contains(destNode)) {
				// add to visited
				visited.add(destNode);
				// add curr as n's parent in parent map
				parentMap.put(destNode, currNode );
				// enqueue n onto queue					
				queue.add(destNode );
			}
		}
	}
	
	/** Check if all parameters are OK
	 * 
	 * @param start The node to use for start
	 * @param goal The node to use for the goal/end
	 * @return True if everything is OK
	 */
	private Boolean checkSearchParams(GeographicPoint start, 
			 					     GeographicPoint goal) {
		if (start == null || goal == null ) {
			return false;
		} else if (!vertices.containsKey(start) || !vertices.containsKey(goal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/** Make a list/path from one point to another
	 * 
	 * @param start The node to use for start
	 * @param goal The node to use for the goal/end
	 * @param parentMap the mapping of parent nodes so we can recreate the path
	 * @return List of geographic points to get from start to goal
	 */
	private List<GeographicPoint> createPath(MapNode start, MapNode goal, 
			HashMap<MapNode, MapNode> parentMap) {
		
		List<GeographicPoint> path = new LinkedList<GeographicPoint>();
		
		// go up from the goal
		MapNode currNode = goal;
		path.add(currNode.getCoords());
		
		// prevent endless loops in case of errors
		int maxIterations = 10000;
		
		// search up to start
		while (!currNode.equals(start)) {
			// find node above me
			MapNode upNode = parentMap.get(currNode);
			// set new current node
			currNode = upNode;
			// add to path
			path.add(currNode.getCoords());
			
			if (path.size()> maxIterations) {
				// too many ... prevent endless loops
				return null;
			}
		}
		
		// we need from start to goal, not the other way around
		Collections.reverse(path);
		
		// ok, we have the path
		return path;
	}
	

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// call method for search
		return priorityQueueSearch(start, goal, nodeSearched, false);
	}

	/** Find the path from start to goal using Dijkstra's or aStar algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @param usedistanceToGoal true = use the help of distance to goal -> A* search, false = Dijkstra search	 * 
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> priorityQueueSearch(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched, 
										  boolean useDistanceToGoal)
	{
		// check input parameters - use bfs function
		if (!checkSearchParams(start, goal)) {
			return null;
		}

		
		// get start and goal nodes
		MapNode startNode = vertices.get(start);
		MapNode goalNode = vertices.get(goal);
		
		// keep track of visited nodes and path
		HashSet<MapNode> visited = new HashSet<MapNode>();
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		
		// create queue
		PriorityQueue<MapNode> queue = new PriorityQueue<MapNode>();
		
		// initialize queue to infinity
		if (!useDistanceToGoal) {
			//System.out.println("MapGraph.search: D. Search: " + start + " -> " + goal );			
			setAllNodesTo(Double.POSITIVE_INFINITY, 0);
			// set distance - priority
			startNode.setDistanceFromStart(0);
			startNode.setDistanceToGoal(0);
		} else {
			//System.out.println("MapGraph.search: A* Search: " + start + " -> " + goal );						
			setAllNodesTo(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			// set distance - priority
			startNode.setDistanceFromStart(0);
			startNode.setDistanceToGoal(goalNode);
		}
		
		// add first el. to queue with priority 0
		startNode.setPriority(startNode.getTotalDistance());
		queue.add(startNode);
		
		// while queue not empty
		while (!queue.isEmpty()) {
			// get first element
			MapNode currNode = queue.remove();
			//System.out.println("");
			//System.out.println("MapGraph.search: check node: " + currNode.toString() );
			//System.out.println("MapGraph.search: distance for node: " + currNode.getTotalDistance() );
			
			// Hook for visualization.  See writeup.
		    nodeSearched.accept(currNode.getCoords() );			
			
		    // if not visited
		    if (!visited.contains(currNode)) {
		    	// add curr to visited
		    	visited.add(currNode);
		    	
		    	// if curr == goal, return parent map
		    	if (currNode.equals(goalNode)) {
		    		int visitedNodes = visited.size();
		    		System.out.println("MapGraph.search: check node: FOUND!, visited n.: " + visitedNodes);
					return createPath(startNode, goalNode, parentMap); // TODO
				} else {
					//System.out.println("MapGraph.search: add edges/roads...");
					addEdgesForPqSearch(currNode, queue, visited, parentMap, useDistanceToGoal, goalNode);
				}
		    }
			// next queue item	
		}
		
		// write somewhere the number of nodes visited
		//System.out.println("MapGraph.search: OPS, couldn't find destination!");
		return null;
	}	
		
	/** Add all edges of a node to the queue
	 * 
	 * @param currNode The node to use for finding edges
	 * @param queue Queue where we add nodes
	 * @param visited A list of visited nodes, so we don't add the same nodes multiple times
	 * @param parentMap A map of parent nodes to facilitate the reconstruction of the path
	 * @param usedistanceToGoal true = use the help of distance to goal -> A* search, false = Dijkstra search
	 * @param goalNode goal node, used only if usedistanceToGoal is true
	 */	
	private void addEdgesForPqSearch(MapNode currNode, Queue<MapNode> queue, 
			HashSet<MapNode> visited, HashMap<MapNode, MapNode> parentMap, 
			boolean usedistanceToGoal, MapNode goalNode) {
		
		// get unvisited roads to other nodes add add them to queue
		HashSet<MapEdge> roads = currNode.getEdges();
		for(MapEdge road : roads) {
			MapNode destNode = road.getTo();
			//System.out.println("MapGraph.search: road: " + road.getName() + " (" + road.getLength() + "), checking...");
			// if not visited
			if (!visited.contains(destNode)) {
				// get previous distance to node
				double beforeDistance = destNode.getDistanceFromStart();
				// get distance to node through current road/edge
				double throughCurrDistance = currNode.getDistanceFromStart() + road.getLength();
	    		// if path through curr to destNode is shorter
				//System.out.println("MapGraph.search: beforeDistance: " + beforeDistance + ", new: " + throughCurrDistance);
				if (throughCurrDistance < beforeDistance) {	
					// update n's distance
					//System.out.println("MapGraph.search: ok, we have a better road with dist.: " + throughCurrDistance);					
					destNode.setDistanceFromStart(throughCurrDistance);
					if (usedistanceToGoal) {
						destNode.setDistanceToGoal(goalNode);
					}
					// update curr as destNode parent in parent map
					parentMap.put(destNode, currNode );
	    			// enqueue (destNode, distance) to queue
					// we use the total distance for priority... 
					// this could be extended to account for speed, traffic, ...
					destNode.setPriority(destNode.getTotalDistance());					
					queue.add(destNode);
				} else {
					//System.out.println("MapGraph.search: dest is on longer road.");
				}
				
			} else {
				//System.out.println("MapGraph.search: dest via that road already visited.");
			}
		}
	}		
	
	
	

	
	
	

	/** Find the path from start to goal using Dijkstra's or aStar algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @param usedistanceToGoal true = use the help of distance to goal -> A* search, false = Dijkstra search	 * 
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> priorityQueueSearchWithTime(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched, 
										  boolean useDistanceToGoal)
	{
		// check input parameters - use bfs function
		if (!checkSearchParams(start, goal)) {
			return null;
		}
		
		// get start and goal nodes
		MapNode startNode = vertices.get(start);
		MapNode goalNode = vertices.get(goal);
		
		// keep track of visited nodes and path
		HashSet<MapNode> visited = new HashSet<MapNode>();
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		
		// create queue
		PriorityQueue<MapNode> queue = new PriorityQueue<MapNode>();
		
		// initialize queue to infinity
		if (!useDistanceToGoal) {
			//System.out.println("MapGraph.search: time D. Search: " + start + " -> " + goal );			
			setAllNodesTo(Double.POSITIVE_INFINITY, 0);
			// set distance - priority
			startNode.setTimeFromStart(0);
			startNode.setTimeToGoal(0);
		} else {
			//System.out.println("MapGraph.search: time A* Search: " + start + " -> " + goal );						
			setAllNodesTo(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			// set distance - priority
			startNode.setTimeFromStart(0);
			startNode.setTimeToGoal(goalNode);
		}
		
		// add first el. to queue with priority 0
		startNode.setPriority(startNode.getTotalTime());
		queue.add(startNode);
		
		// while queue not empty
		while (!queue.isEmpty()) {
			// get first element
			MapNode currNode = queue.remove();
			//System.out.println("");
			//System.out.println("MapGraph.search: check node: " + currNode.toString() );
			//System.out.println("MapGraph.search: distance for node: " + currNode.getTotalTime() );
			
			// Hook for visualization.  See writeup.
		    nodeSearched.accept(currNode.getCoords() );			
			
		    // if not visited
		    if (!visited.contains(currNode)) {
		    	// add curr to visited
		    	visited.add(currNode);
		    	
		    	// if curr == goal, return parent map
		    	if (currNode.equals(goalNode)) {
		    		int visitedNodes = visited.size();
		    		System.out.println("MapGraph.search: check node: FOUND!, visited n.: " + visitedNodes);
					return createPath(startNode, goalNode, parentMap); // TODO
				} else {
					//System.out.println("MapGraph.search: add edges/roads...");
					addEdgesForPqSearchWithTime(currNode, queue, visited, parentMap, useDistanceToGoal, goalNode);
				}
		    }
			// next queue item	
		}
		System.out.println("Not found, visited " + visited.size() + " nodes");
		
		// write somewhere the number of nodes visited
		//System.out.println("MapGraph.search: OPS, couldn't find destination!");
		return null;
	}		
	
	
	

	/** Add all edges of a node to the queue
	 * 
	 * @param currNode The node to use for finding edges
	 * @param queue Queue where we add nodes
	 * @param visited A list of visited nodes, so we don't add the same nodes multiple times
	 * @param parentMap A map of parent nodes to facilitate the reconstruction of the path
	 * @param usedistanceToGoal true = use the help of distance to goal -> A* search, false = Dijkstra search
	 * @param goalNode goal node, used only if usedistanceToGoal is true
	 */	
	private void addEdgesForPqSearchWithTime(MapNode currNode, Queue<MapNode> queue, 
			HashSet<MapNode> visited, HashMap<MapNode, MapNode> parentMap, 
			boolean usedistanceToGoal, MapNode goalNode) {
		
		// get unvisited roads to other nodes add add them to queue
		HashSet<MapEdge> roads = currNode.getEdges();
		for(MapEdge road : roads) {
			MapNode destNode = road.getTo();
			//System.out.println("MapGraph.search: road: " + road.getName() + " (" + road.getTime() + "), checking...");
			// if not visited
			if (!visited.contains(destNode)) {
				// get previous distance to node
				double beforeDistance = destNode.getTimeFromStart();
				// get distance to node through current road/edge
				double throughCurrDistance = currNode.getTimeFromStart() + road.getTime();
	    		// if path through curr to destNode is shorter
				//System.out.println("MapGraph.search: beforeDistance: " + beforeDistance + ", new: " + throughCurrDistance);
				if (throughCurrDistance < beforeDistance) {	
					// update n's distance
					//System.out.println("MapGraph.search: ok, we have a better road with dist.: " + throughCurrDistance);					
					destNode.setTimeFromStart(throughCurrDistance);
					if (usedistanceToGoal) {
						destNode.setTimeToGoal(goalNode);
					}
					// update curr as destNode parent in parent map
					parentMap.put(destNode, currNode );
	    			// enqueue (destNode, distance) to queue
					// we use the total distance for priority... 
					// this could be extended to account for speed, traffic, ...
					destNode.setPriority(destNode.getTotalTime());					
					queue.add(destNode);
				} else {
					//System.out.println("MapGraph.search: dest is on longer road.");
				}
				
			} else {
				//System.out.println("MapGraph.search: dest via that road already visited.");
			}
		}
	}		
	
	
	
	private void setAllNodesTo(double distanceFromStart, double distanceToGoal) {
		// Iterate over vertices / nodes
		for (MapNode v : vertices.values() ) {
			// distance in km
			v.setDistanceFromStart(distanceFromStart);
			v.setDistanceToGoal(distanceToGoal);
			// time
			v.setTimeFromStart(distanceFromStart);
			v.setTimeToGoal(distanceToGoal);
		}
	}
	
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// call method for search
		return priorityQueueSearch(start, goal, nodeSearched, true);
	}

	
	
	/** Find the path from start to goal using A-Star search with time instead of distance
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> timeSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// call method for search
		return priorityQueueSearchWithTime(start, goal, nodeSearched, true);
	}	
	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
	    MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);		
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		/*
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		*/
		
		
		/* Use this code in Week 3 End of Week Quiz */
		/*MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");
		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
		*/
		
	}
	
}