package day18;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.Location;

/**
 * Modification of SimpleSearcher to return all paths to all keys
 * at the same time.
 */
public class SimpleSearcher2 {

	Maze maze;
	Location start;
	
	public SimpleSearcher2(Maze maze, Location start) {
		this.maze = maze;
		this.start = start;
	}

	List<PathNode> leaves = new ArrayList<>();
	List<PathNode> nextLeaves = new ArrayList<>();
	Set<String> visitedHashSet = new HashSet<>(50000);
	
	Map<Location, PathNode> solutions = new HashMap<>();
	
	public Collection<PathNode> getBestPathsToKeys() {
		return solutions.values();
	}
	
	public static String hash(PathNode node) {
		return node.gameState + ":" + node.location.row + ":" + node.location.col;
	}
	
	public void search() {
		PathNode root = new PathNode(null, start, "");
		leaves.clear();
		leaves.add(root);
		visitedHashSet.add(hash(root));
		
		while (!leaves.isEmpty()) {
			expandSearch();
		}
	}
	
	public PathNode expandSearch() {
		
		nextLeaves.clear();
		
		for (PathNode leaf : leaves) {
		
			//is this the goal?
			if (maze.isKey(leaf.location)) {
				//not really needed since we don't revisit the same space with a longer path anyway
				PathNode bestPath = solutions.get(leaf.location);
				if (bestPath == null || leaf.getDistanceFromRoot() < bestPath.getDistanceFromRoot())
					solutions.put(leaf.location, leaf);
				
				continue;
			}
				
			addChildren(leaf);
			
			//visit each child:
			for (PathNode child : leaf.children) {
				if (visitedHashSet.add(hash(child))) {  //don't bother re-visiting old spaces 
					nextLeaves.add(child);
				}
			}
		}
		
		List<PathNode> tmp = this.leaves;
		this.leaves = this.nextLeaves;
		this.nextLeaves = tmp;
		
		return null;
	}
	
	static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	public void addChildren(PathNode node) {
	
		Location loc = node.location;
		
		for (Direction dir : Direction.values()) {
			
			int di = 0, dj = 0;
			switch(dir) {
			case UP:    di--; break;
			case DOWN:  di++; break;
			case LEFT:  dj--; break;
			case RIGHT: dj++; break;
			}
			
			Location nextLoc = new Location(loc.row + di, loc.col + dj);
			if (maze.isValidLocation(nextLoc) && 
				maze.get(nextLoc) != Maze.WALL &&
				!maze.isDoor(nextLoc)) {
				MovePlayerAction action = new MovePlayerAction(loc, nextLoc);
				node.addChild(action, nextLoc, "");
			}
			
		}
				
	}
	
}
