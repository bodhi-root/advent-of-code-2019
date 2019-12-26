package day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Location;

/**
 * Modification of the breadth-first MazeSearcher2 that speeds up the search
 * by first solving for the shortest paths between labeled spaces and then 
 * using these in the final search (rather than going step-by-step through
 * the maze at each level).
 */
public class MazeSearcher3 {

	Maze maze;
	Mode mode;
	Location start;
	Location end;
	
	CachedPaths cachedPaths = new CachedPaths();
	
	static class CachedPaths {
		
		Map<Location, List<CachedPath>> cache = new HashMap<>(100);
		
		public void cache(CachedPath path) {
			Location start = path.start.location;
			List<CachedPath> paths = cache.get(start);
			if (paths == null) {
				paths = new ArrayList<>();
				cache.put(start, paths);
			}
			
			paths.add(path);
		}
		
		public void ensureStartingLocationExists(Location loc) {
			if (!cache.containsKey(loc))
				cache.put(loc, new ArrayList<CachedPath>());
		}
		
		public List<CachedPath> getPathsFrom(Location loc) {
			return cache.get(loc);
		}
		
	}
	
	static class CachedPath {
		
		PathNode start;
		PathNode end;
		
		public CachedPath(PathNode start, PathNode end) {
			this.start = start;
			this.end = end;
		}
		
		public int getLevelChange() {
			return end.level - start.level; 
		}
		
	}
	
	PathNode rootNode;
	List<PathNode> leaves = new ArrayList<>();
	
	public MazeSearcher3(Maze maze, Mode mode, Location start, Location end) {
		this.maze = maze;
		this.mode = mode;
		this.start = start;
		this.end = end;
		
		initCache();
	}
	
	/**
	 * Initialize the cache with paths to all labeled spaces.  We ignore START and END
	 * labels since the AA and ZZ labels will already capture these.  We use the regular
	 * MazeSearcher to solve this.  This can be the same MazeSearcher as we used for 
	 * Part A.  The only change we make is that we don't let it go through any portals.
	 * We will step through the end portal ourselves and adjust the end of the path
	 * accordingly.  We begin on level 1 so that when we step through the portal we go
	 * to level 0 or level 2 (if we are in Part B mode).  This will help us later when
	 * we apply these short-cuts to know whether to increase or decrease the level.
	 */
	public void initCache() {
		
		List<Location> labeledLocs = maze.getAllLabeledLocations();
		
		for (int i=0; i<labeledLocs.size(); i++) {
			for (int j=0; j<labeledLocs.size(); j++) {
				
				if (i == j)
					continue;
				
				Location startLocation = labeledLocs.get(i);
				Location endLocation = labeledLocs.get(j);
				
				String startLabel = maze.getLabel(startLocation);
				String endLabel = maze.getLabel(endLocation);
				
				if (startLabel.equals("START") || startLabel.equals("END") ||
					endLabel.equals("START") || endLabel.equals("END"))
					continue;
				
				if (endLabel.equals("AA"))	//no reason to go back to where we started. it's not a portal.
					continue;
				
				cachedPaths.ensureStartingLocationExists(startLocation);
				
				startLocation = maze.findAdjacentFloor(startLocation);
				endLocation = maze.findAdjacentFloor(endLocation);
				
				System.out.println("Finding path from '" + startLabel + "' to '" + endLabel + "' (" + startLocation + " to " + endLocation + ")");
				
				MazeSearcher searcher = new MazeSearcher(maze, Mode.PART_A, startLocation, endLocation);
				searcher.allowPortals = false;
				List<PathNode> path = searcher.search();
				
				if (path == null) {
					System.out.println("No path found");
				} else {
					
					//start on level 1 so all portals are active
					for (PathNode node : path)
						node.level = 1;
					
					//step through portal (remember 'AA' and 'ZZ' are not portals)
					if (!(endLabel.equals("AA") || endLabel.equals("ZZ")) ) {
						
						PathNode endNode = path.get(path.size()-1);
						addChildren(endNode);
						
						//find which one goes through portal (assumption: portal does not take
						//us to adjacent cell)
						for (PathNode child : endNode.children) {
							int di = child.location.row - endNode.location.row;
							int dj = child.location.col - endNode.location.col;
							if (Math.abs(di) > 1 || Math.abs(dj) > 1) {
								path.add(child);
								break;
							}
						}
					}
					
					CachedPath toCache = new CachedPath(path.get(0), path.get(path.size()-1));
					cachedPaths.cache(toCache);
					System.out.println("PATH FOUND (end = " + toCache.end.location + ", distance = " + toCache.end.getDistanceFromRoot() + ")");
				}
				
			}
		}
	}

	public List<PathNode> search() {
		this.rootNode = new PathNode(null, start, 0);
		this.leaves.add(rootNode);
		
		List<PathNode> solution = null;
		int iteration = 0;
		while (solution == null) {
			iteration++;
			//if (iteration % 100 == 0)
				System.out.println("Iteration = " + iteration + " (leaf count: " + leaves.size() + ")");
			solution = expandSearch();
		}
		
		return solution;
	}
	
	//cache this so we don't have to keep creating it
	List<PathNode> nextLeaves = new ArrayList<>();
	PathNode bestSolution = null;
	
	public List<PathNode> expandSearch() {
	
		if (leaves.isEmpty())
			throw new IllegalStateException("No more paths to expand.  Maze has no solution.");
		
		this.nextLeaves.clear();
		
		for (PathNode leaf : this.leaves) {

			//System.out.println("Exploring location " + leaf.location + " (level = " + leaf.level + ")");
			
			List<CachedPath> paths = cachedPaths.getPathsFrom(leaf.location);
			if (paths == null)
				throw new IllegalStateException("Arrived on space that is not in cache: " + leaf.location);
			
			for (CachedPath path : paths) {
				
				int endLevel = leaf.level + path.getLevelChange();
				if (endLevel >= 0) {

					List<PathNode> pathAsList = path.end.getPathAsList();
					PathNode nextLeaf = leaf;
					for (int i=1; i<pathAsList.size(); i++) {
						PathNode nextNode = pathAsList.get(i);
						nextLeaf = nextLeaf.addChild(nextNode.action, nextNode.location, leaf.level);
					}
					
					nextLeaf.level = endLevel;
					
					//solution?
					if (nextLeaf.location.equals(this.end) && nextLeaf.level == 0) {
						if (bestSolution == null || nextLeaf.getDistanceFromRoot() < bestSolution.getDistanceFromRoot()) {
							System.out.println("SOLUTION FOUND!");
							System.out.println("Distance = " + nextLeaf.getDistanceFromRoot());
							bestSolution = nextLeaf;
						}
					} else {
						String label = maze.getLabel(nextLeaf.location);
						if (label == null || !(label.equals("START") || label.equals("END"))) {	//don't go to these if not solutions
							if (!nextLeaf.hasAncestor(nextLeaf.location, nextLeaf.level))	//don't circle back
								this.nextLeaves.add(nextLeaf);	
						}
					}
					
				}
				
			}
			
		}
		
		//are we done yet?
		//we can't just return the first solution we find, because there is a chance it's not the best.
		//however, we can remove anybody in nextLeaves who has already traveled further than our
		//current best solution.  This should help us get to the end fast.
		if (bestSolution != null) {
			int bestDistance = bestSolution.getDistanceFromRoot();
			for (int i=nextLeaves.size()-1; i>=0; i--) {
				if (nextLeaves.get(i).getDistanceFromRoot() >= bestDistance)
					nextLeaves.remove(i);
			}
			
			if (nextLeaves.isEmpty())
				return bestSolution.getPathAsList();
		}
		
		//swap nextLeaves into leaves
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
