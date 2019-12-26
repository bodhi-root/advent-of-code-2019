package day18;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Location;

/**
 * Exhaustive search.  Works on samples, but too slow for actual input.  I wrote this at first
 * as a depth-first search.  This just wasn't working.  I added the ability to do a breadth-first
 * search instead.  This also wasn't working until I added the 'visitedHashSet' to store a hash
 * of all places we had already visited.  This prevented us from taking longer paths to get
 * to the same location/state.  Finally, I was able to solve it with this!
 * 
 * (MazeSearcher2 and MazeSearcher3 were not needed after this)
 */
public class MazeSearcher {

	World world;
	
	//for depth first search
	PathNode bestPath = null;
	int bestPathDistance;
	
	public MazeSearcher(World world) {
		this.world = world;
	}

	public List<PathNode> search() {
		//return searchDepthFirst();
		return searchBreadthFirst();
	}
	
	public List<PathNode> searchDepthFirst() {
		PathNode root = new PathNode(null, world.playerLocation, world.maze.getAllKeyAndDoorSymbolsAsString());
		expandDepthFirstSearch(root);
		return bestPath == null ? null : bestPath.getPathAsList();
	}
	
	public static String hash(PathNode node) {
		return node.gameState + ":" + node.location.row + ":" + node.location.col;
	}
	
	public List<PathNode> searchBreadthFirst() {
		List<Leaf> leaves = new ArrayList<>();
		List<Leaf> nextLeaves = new ArrayList<>();
		
		PathNode root = new PathNode(null, world.playerLocation, world.maze.getAllKeyAndDoorSymbolsAsString());
		leaves.add(new Leaf(world.copy(), root));
		
		Set<String> visitedHash = new HashSet<>(100000);
		visitedHash.add(hash(root));
		
		int iteration = 0;
		while (!leaves.isEmpty()) {
			iteration++;
			if (iteration % 100 == 0)
				System.out.println("i = " + iteration + ", leaves = " + leaves.size());
			
			PathNode solution = expandBreadthFirstSearch(leaves, nextLeaves, visitedHash);
			if (solution != null)
				return solution.getPathAsList();
		}
		
		return null;
	}
	
	
	public void expandDepthFirstSearch(PathNode lastNode) {
		
		//PathNode lastNode = path.get(path.size()-1);
		//System.out.println("Exploring: " + lastNode.location.toString());
		
		int distance = lastNode.getDistanceFromRoot();
		
		//is this the goal?
		if (lastNode.gameState.isEmpty()) {
			if (this.bestPath == null || distance < this.bestPathDistance) {
				System.out.println("SOLUTION FOUND! (len = " + distance + ")");
				this.bestPath = lastNode;
				this.bestPathDistance = distance;
			}
			return;
		}
		
		//don't keep going if this path is too long
		if (bestPath != null && distance >= bestPathDistance)
			return;
		
		//make sure children have been initialized
		addChildren(lastNode);
		
		//visit each child:
		for (PathNode child : lastNode.children) {
			if (!child.hasAncestor(child.location, child.gameState)) {  //don't bother re-visiting old spaces 
				//path.add(child);
				child.action.apply(world);
				
				expandDepthFirstSearch(child);
				
				//path.remove(path.size()-1);
				child.action.undo(world);
			}
		}
	}
	
	public PathNode expandBreadthFirstSearch(List<Leaf> leaves, List<Leaf> nextLeaves, Set<String> visitedHash) {
		
		for (Leaf leaf : leaves) {
			
			//make sure children have been initialized
			List<Leaf> children = getNextLeaves(leaf);
			
			//visit each child:
			for (Leaf child : children) {
				if (visitedHash.add(hash(child.node))) {  //don't bother re-visiting old spaces (or spaces other paths have gotten to faster) 
			
					//check for solution:
					if (child.node.gameState.isEmpty())
						return child.node;
					
					nextLeaves.add(child);
				}
			}
			
		}
		
		//swap lists
		leaves.clear();
		leaves.addAll(nextLeaves);
		nextLeaves.clear();
		
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
			if (world.maze.isValidLocation(nextLoc) && 
				world.maze.get(nextLoc) != Maze.WALL &&
				!world.maze.isDoor(nextLoc)) {
				MovePlayerAction action = new MovePlayerAction(loc, nextLoc);
				action.apply(world);
				String gameState = world.maze.getAllKeyAndDoorSymbolsAsString();
				action.undo(world);
				
				node.addChild(action, nextLoc, gameState);
			}
			
		}
				
	}
	
	public List<Leaf> getNextLeaves(Leaf leaf) {
		
		List<Leaf> nextLeaves = new ArrayList<>();
		
		Location loc = leaf.node.location;
		
		for (Direction dir : Direction.values()) {
			
			int di = 0, dj = 0;
			switch(dir) {
			case UP:    di--; break;
			case DOWN:  di++; break;
			case LEFT:  dj--; break;
			case RIGHT: dj++; break;
			}
			
			Location nextLoc = new Location(loc.row + di, loc.col + dj);
			if (leaf.world.maze.isValidLocation(nextLoc) && 
				leaf.world.maze.get(nextLoc) != Maze.WALL &&
				!leaf.world.maze.isDoor(nextLoc)) {
				
				MovePlayerAction action = new MovePlayerAction(loc, nextLoc);
				World nextWorld = leaf.world.copy();
				
				action.apply(nextWorld);
				String gameState = nextWorld.maze.getAllKeyAndDoorSymbolsAsString();
				
				PathNode child = leaf.node.addChild(action, nextLoc, gameState);
				nextLeaves.add(new Leaf(nextWorld, child));
			}
			
		}
		
		return nextLeaves;
	}
	
	
	static class Leaf {
		
		World world;
		PathNode node;
		
		public Leaf(World world, PathNode node) {
			this.world = world;
			this.node = node;
		}
		
	}
	
	
}
