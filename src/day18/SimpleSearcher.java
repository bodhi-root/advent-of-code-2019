package day18;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Location;

/**
 * Simplified searcher that looks for the best path between two points
 * on the maze.  It does this without picking up any keys (unless it's
 * on the last space) to avoid altering the game state.
 * 
 * Originally this was a depth-first search, but I modified it to 
 * a breadth-first search because I think that will be faster (and
 * not go down so many dead ends).
 */
public class SimpleSearcher {

	Maze maze;
	Location start;
	Location end;
	
	public SimpleSearcher(Maze maze, Location start, Location end) {
		this.maze = maze;
		this.start = start;
		this.end = end;
	}

	List<PathNode> leaves = new ArrayList<>();
	List<PathNode> nextLeaves = new ArrayList<>();
	Set<String> visitedHashSet = new HashSet<>(100000);
	
	public static String hash(PathNode node) {
		return node.gameState + ":" + node.location.row + ":" + node.location.col;
	}
	
	public List<PathNode> search() {
		PathNode root = new PathNode(null, start, "");
		leaves.clear();
		leaves.add(root);
		
		visitedHashSet.clear();
		visitedHashSet.add(hash(root));
		
		PathNode solution = null;
		//int iteration = 0;
		while (solution == null) {
			//iteration++;
			//if (iteration % 100 == 0)
			//	System.out.println("Iteration = " + iteration + " (leaf count: " + leaves.size() + ")");
			
			if (this.leaves.isEmpty())
				return null;
			
			solution = expandSearch();
		}
		
		return solution.getPathAsList();
	}
	
	public PathNode expandSearch() {
		
		nextLeaves.clear();
		
		for (PathNode leaf : leaves) {
		
			//is this the goal?
			if (leaf.location.equals(this.end))
				return leaf;
			
			addChildren(leaf);
			
			//visit each child:
			for (PathNode child : leaf.children) {
				//if (!child.hasAncestor(child.location, child.gameState)) { //don't bother re-visiting old spaces
				if (visitedHashSet.add(hash(child))) {  // (or spaces someone else got to faster)
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
			if (nextLoc.equals(this.end) || 
				(
				  maze.isValidLocation(nextLoc) && 
				  maze.get(nextLoc) != Maze.WALL &&
				  !maze.isDoor(nextLoc) &&
				  !maze.isKey(nextLoc)
				 )
				) {
				MovePlayerAction action = new MovePlayerAction(loc, nextLoc);
				node.addChild(action, nextLoc, "");
			}
			
		}
				
	}
	
}
