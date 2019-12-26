package day20;

import java.util.ArrayList;
import java.util.List;

import common.Location;

/**
 * Ran until iteration 4700. still no solution.
 * 
 * This is a breadth-first searcher that expands the search path 1 step at a time.
 * This ran very slow though (probably because of how much memory the search tree
 * consumes).  I ran it until iteration 4,700 (all possible 4,700 step paths)
 * and still no solution.
 */
public class MazeSearcher2 {

	Maze maze;
	Mode mode;
	Location start;
	Location end;
	
	PathNode rootNode;
	List<PathNode> leaves = new ArrayList<>();
	
	public MazeSearcher2(Maze maze, Mode mode, Location start, Location end) {
		this.maze = maze;
		this.mode = mode;
		this.start = start;
		this.end = end;
	}

	public List<PathNode> search() {
		this.rootNode = new PathNode(null, start, 0);
		this.leaves.add(rootNode);
		
		List<PathNode> solution = null;
		int iteration = 0;
		while (solution == null) {
			iteration++;
			if (iteration % 100 == 0)
				System.out.println("Iteration = " + iteration + " (leaf count: " + leaves.size() + ")");
			solution = expandSearch();
		}
		
		return solution;
	}
	
	//cache this so we don't have to keep creating it
	List<PathNode> nextLeaves = new ArrayList<>();
	
	public List<PathNode> expandSearch() {
	
		if (leaves.isEmpty())
			throw new IllegalStateException("No more paths to expand.  Maze has no solution.");
		
		this.nextLeaves.clear();
		
		for (PathNode leaf : this.leaves) {
			
			//System.out.println("Expanding node: " + leaf.location.toString() + " , level = " + leaf.level);
			addChildren(leaf);
			
			for (PathNode child : leaf.children) {
				
				//is this the goal?
				if (child.location.equals(end) && child.level == 0) {
					return child.getPathAsList();
				}
				
				if (!child.hasAncestor(child.location, child.level))
					this.nextLeaves.add(child);
			}
		}
		
		List<PathNode> tmp = this.leaves;
		this.leaves = this.nextLeaves;
		this.nextLeaves = tmp;
		
		return null;
	}
	
	public void addChildren(PathNode node) {
		if (mode == Mode.PART_A)
			addChildrenPartA(node);
		else
			addChildrenPartB(node);
	}
	
	/**
	 * Adds children according to part A logic. Level will always equal 0 and
	 * all portals are always active.
	 */
	public void addChildrenPartA(PathNode node) {
		
		Location loc = node.location;
		
		for (Action action : Action.values()) {
			int di = 0, dj = 0;
			switch(action) {
			case MOVE_UP:    di--; break;
			case MOVE_DOWN:  di++; break;
			case MOVE_LEFT:  dj--; break;
			case MOVE_RIGHT: dj++; break;
			}
			
			Location nextLoc = new Location(loc.row + di, loc.col + dj);
			char value = maze.get(nextLoc);
			if (value != Maze.WALL) {
				
				//check for portal:
				if (value != Maze.FLOOR) {
					String label = maze.getLabel(nextLoc);
					//System.out.println("Finding portal: " + label);
					
					List<Location> portalLocs = maze.findAllLocationsWithLabel(label);
					if (portalLocs.size() != 2) 
						throw new IllegalStateException("Could not find two portals labeled: " + label);
					
					Location portalLoc = portalLocs.get(0).equals(nextLoc) ? 
							portalLocs.get(1) : portalLocs.get(0);
					
					nextLoc = maze.findAdjacentFloor(portalLoc);
				}
				
				node.addChild(action, nextLoc, node.level);
			}
			
		}
		
	}
	
	/**
	 * Adds children according to part B logic.  In part B we will increase the
	 * level when we go through inner portals and decrease it when we go through
	 * outer portals.  Also, the outer portals are inactive when level = 0.
	 */
	public void addChildrenPartB(PathNode node) {
		
		Location loc = node.location;
		
		for (Action action : Action.values()) {
			int di = 0, dj = 0;
			switch(action) {
			case MOVE_UP:    di--; break;
			case MOVE_DOWN:  di++; break;
			case MOVE_LEFT:  dj--; break;
			case MOVE_RIGHT: dj++; break;
			}
			
			Location nextLoc = new Location(loc.row + di, loc.col + dj);
			int nextLevel = node.level;
			
			char value = maze.get(nextLoc);
			if (value != Maze.WALL) {
				
				//simple space:
				if (value == Maze.FLOOR) {
					node.addChild(action, nextLoc, nextLevel);
				}
				
				//check for portal:
				else {
					String label = maze.getLabel(nextLoc);
					
					boolean isOuterPortal = (nextLoc.row <= 2) ||
							                (nextLoc.col <= 2) ||
							                (nextLoc.row >= maze.height-2) ||
							                (nextLoc.col >= maze.width-2);
					
					//outer portals only work for level > 0
					if (!isOuterPortal || node.level > 0) {
					
						List<Location> portalLocs = maze.findAllLocationsWithLabel(label);
						if (portalLocs.size() != 2) 
							throw new IllegalStateException("Could not find two portals labeled: " + label);
						
						Location portalLoc = portalLocs.get(0).equals(nextLoc) ? 
								portalLocs.get(1) : portalLocs.get(0);
						
						nextLoc = maze.findAdjacentFloor(portalLoc);
						nextLevel += isOuterPortal ? -1 : +1;
						
						node.addChild(action, nextLoc, nextLevel);
					}
				}
				
			}
			
		}
		
	}
	
}
