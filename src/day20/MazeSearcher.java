package day20;

import java.util.ArrayList;
import java.util.List;

import common.Location;

/**
 * This is a depth-first search that will expand each path until it ends 
 * before stepping back and trying other paths.
 * 
 * I used this for Part A.  It won't work for part B because you can go down
 * to lower levels infinitely times.  The first search path will never end.
 * 
 * I did try to modify it and put a limit on the number of levels we could
 * reach (level < 40).  However, it ran for a long time and never ended. 
 */
public class MazeSearcher {

	Maze maze;
	Mode mode;
	Location start;
	Location end;
	boolean allowPortals = true;
	
	List<PathNode> bestPath = null;
	
	public MazeSearcher(Maze maze, Mode mode, Location start, Location end) {
		this.maze = maze;
		this.mode = mode;
		this.start = start;
		this.end = end;
	}

	public List<PathNode> search() {
		return searchDepthFirst();
	}
	
	public List<PathNode> searchDepthFirst() {
		PathNode root = new PathNode(null, start, 0);
		List<PathNode> path = new ArrayList<>();
		path.add(root);
		expandSearch(path);
		return bestPath;
	}
	
	public void expandSearch(List<PathNode> path) {
		
		PathNode lastNode = path.get(path.size()-1);
		//System.out.println("Exploring: " + lastNode.location.toString());
		
		//is this the goal?
		if (lastNode.location.equals(end) && lastNode.level == 0) {
			if (this.bestPath == null || path.size() < this.bestPath.size()) {
				//store a copy of this path
				this.bestPath = new ArrayList<>();
				this.bestPath.addAll(path);
			}
			return;
		}
		
		//make sure children have been initialized
		if (lastNode.children.isEmpty()) {
			addChildren(lastNode);
		}
		
		//visit each child:
		for (PathNode child : lastNode.children) {
			if (!child.hasAncestor(child.location, child.level)) {  //don't bother re-visiting old spaces 
				path.add(child);
				expandSearch(path);
				path.remove(path.size()-1);
			}
		}
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
				
				//simple space:
				if (value == Maze.FLOOR) {
					node.addChild(action, nextLoc, node.level);
				}
				
				//check for portal:
				else if (allowPortals) {
					String label = maze.getLabel(nextLoc);
					//System.out.println("Finding portal: " + label);
					
					List<Location> portalLocs = maze.findAllLocationsWithLabel(label);
					if (portalLocs.size() != 2) 
						throw new IllegalStateException("Could not find two portals labeled: " + label);
					
					Location portalLoc = portalLocs.get(0).equals(nextLoc) ? 
							portalLocs.get(1) : portalLocs.get(0);
					
					nextLoc = maze.findAdjacentFloor(portalLoc);
					node.addChild(action, nextLoc, node.level);
				}
				
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
				else if (allowPortals) {
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
						
						if (nextLevel < 40)
							node.addChild(action, nextLoc, nextLevel);
					}
				}
				
			}
			
		}
		
	}
	
}
