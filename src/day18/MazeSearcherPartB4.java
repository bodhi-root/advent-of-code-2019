package day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Location;

/**
 * OK, we haven't tried a depth first search with taboo list.  Is that the trick?
 *  
 */
public class MazeSearcherPartB4 {

	WorldPartB startWorld;
		
	public MazeSearcherPartB4(WorldPartB startWorld) {
		this.startWorld = startWorld;
	}
	
	Node bestSolution = null;
	int moveCap = 100;	//Artificial move cap.  Even with a cap of 100 the actual input is taking forever
	
	Map<String, Integer> visitedHash;
	
	public int search() {
		
		Node root = new Node(startWorld.copy(), 0);
		
		this.visitedHash = new HashMap<>(100000);
		visitedHash.put(root.world.hashText(), new Integer(0));
		
		expandDepthFirstSearch(root);
		return bestSolution.distance;
	}
	
	public void expandDepthFirstSearch(Node node) {
		
		List<MovePlayerAction> actions = getNextActions(node);
		
		for (MovePlayerAction action : actions) {
			
			action.apply(node.world);
			node.distance++;
			
			String hashText = node.world.hashText();
			
			//check for solution:
			if (hashText.charAt(0) == ':') {	//no keys or doors, straight to locations
				if (bestSolution == null || node.distance < bestSolution.distance) {
					System.out.println("SOLUTION FOUND! (distance = " + node.distance + ")");
					bestSolution = node.copy();
				}
			} else {

				if (node.distance < moveCap) {
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
			}
			
			action.undo(node.world);
			node.distance--;
		}
	}
	
	
	static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	public List<MovePlayerAction> getNextActions(Node node) {
		
		List<MovePlayerAction> nextActions = new ArrayList<>();
		
		for (int playerIndex=0; playerIndex<4; playerIndex++) {
		
			Location loc = node.world.playerLocations[playerIndex];

			for (Direction dir : Direction.values()) {

				int di = 0, dj = 0;
				switch(dir) {
				case UP:    di--; break;
				case DOWN:  di++; break;
				case LEFT:  dj--; break;
				case RIGHT: dj++; break;
				}

				Location nextLoc = new Location(loc.row + di, loc.col + dj);
				if (node.world.maze.isValidLocation(nextLoc) && 
						node.world.maze.get(nextLoc) != Maze.WALL &&
						!node.world.maze.isDoor(nextLoc)) {

					MovePlayerAction action = new MovePlayerAction(loc, nextLoc);
					nextActions.add(action);
				}
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
