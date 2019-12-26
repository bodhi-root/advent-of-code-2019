package day18;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Location;

/**
 * Modification of Part B4 to use the SimpleSearcher2 to find paths to keys
 * and just take those at each step.
 * 
 * HOLY SHIT!  This solved it in under a second.  I've been working on this
 * for fucking ever!!!
 */
public class MazeSearcherPartB5 {

	WorldPartB startWorld;
		
	public MazeSearcherPartB5(WorldPartB startWorld) {
		this.startWorld = startWorld;
	}
	
	Node bestSolution = null;
	
	Map<String, Integer> visitedHash;
	
	public int search() {
		
		Node root = new Node(startWorld.copy(), 0);
		
		this.visitedHash = new HashMap<>(100000);
		visitedHash.put(root.world.hashText(), new Integer(0));
		
		expandDepthFirstSearch(root);
		return bestSolution.distance;
	}
	
	public void expandDepthFirstSearch(Node node) {
		
		List<MovePlayerAction2> actions = getNextActions(node);
		
		for (MovePlayerAction2 action : actions) {
			
			action.apply(node.world);
			node.distance += action.steps;
			
			String hashText = node.world.hashText();
			
			//check for solution:
			if (hashText.charAt(0) == ':') {	//no keys or doors, straight to locations
				if (bestSolution == null || node.distance < bestSolution.distance) {
					System.out.println("SOLUTION FOUND! (distance = " + node.distance + ")");
					bestSolution = node.copy();
				}
			} else {

				//only continue if path is shorter than best solution:
				if (bestSolution == null || node.distance < bestSolution.distance) { 

					//only continue search if we haven't been here
					Integer prevDistance = visitedHash.get(hashText);
					if (prevDistance == null || node.distance < prevDistance.intValue()) {

						visitedHash.put(hashText, node.distance);
						expandDepthFirstSearch(node);

					}

				}
				
			}
			
			action.undo(node.world);
			node.distance -= action.steps;
		}
	}
	
	
	static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	public List<MovePlayerAction2> getNextActions(Node node) {
		
		List<MovePlayerAction2> nextActions = new ArrayList<>();
		
		for (int playerIndex=0; playerIndex<4; playerIndex++) {
		
			Location loc = node.world.playerLocations[playerIndex];
			
			SimpleSearcher2 searcher = new SimpleSearcher2(node.world.maze, loc);
			searcher.search();
			Collection<PathNode> pathsToKeys = searcher.getBestPathsToKeys();
			
			for (PathNode terminalNode : pathsToKeys) {
				nextActions.add(new MovePlayerAction2(loc, terminalNode.location, terminalNode.getDistanceFromRoot()));
			}
		}
		
		return nextActions;
	}
	
	
		
	static class Node {
		
		//Node parent;
		WorldPartB world;
		int distance;
		
		public Node(WorldPartB world, int distance) {
			//this.parent = parent;
			this.world = world;
			this.distance = distance;
		}
		
		public Node copy() {
			return new Node(world.copy(), distance);
		}
		
		/*
		//use cached value instead
		public int getDistanceFromRoot() {
			int distance = 0;
			Node node = this;
			while (node.parent != null) {
				distance++;
				node = node.parent;
			}
			return distance;
		}
		*/		
	}	
	
}
