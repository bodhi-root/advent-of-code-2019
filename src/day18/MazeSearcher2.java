package day18;

import java.util.Arrays;
import java.util.List;

import common.Location;

/**
 * Speeds up the search by using a SimpleSearcher to find the best path to
 * each key.
 * 
 * Ran for about 30 minutes and was still finding solutions:
 * 
 * NOTE: 'len' is lower by 1 than it should be due to an error
 * SOLUTION FOUND! (len = 7357)
SOLUTION FOUND! (len = 7089)
SOLUTION FOUND! (len = 6933)
SOLUTION FOUND! (len = 6469)
SOLUTION FOUND! (len = 6213)
SOLUTION FOUND! (len = 6201)
SOLUTION FOUND! (len = 6045)
SOLUTION FOUND! (len = 5853)
SOLUTION FOUND! (len = 5585)
SOLUTION FOUND! (len = 5429)
SOLUTION FOUND! (len = 5297)
SOLUTION FOUND! (len = 5285)
SOLUTION FOUND! (len = 5129)
SOLUTION FOUND! (len = 5061)
SOLUTION FOUND! (len = 4961)
SOLUTION FOUND! (len = 4893)

 */
public class MazeSearcher2 {

	World world;
	
	PathNode bestPath = null;
	int bestDistance;
	
	public MazeSearcher2(World world) {
		this.world = world;
	}

	public List<PathNode> search() {
		return searchDepthFirst();
	}
	
	public List<PathNode> searchDepthFirst() {
		PathNode root = new PathNode(null, world.playerLocation, world.maze.getAllKeyAndDoorSymbolsAsString());
		expandSearch(root);
		return bestPath == null ? null : bestPath.getPathAsList();
	}
	
	public void expandSearch(PathNode lastNode) {
		
		//no need to go on if we're longer than a solution
		int distance = lastNode.getDistanceFromRoot();
		if (bestPath != null && distance >= bestDistance)
			return;
		
		//is this the goal?
		if (lastNode.gameState.isEmpty()) {
			if (this.bestPath == null || distance < this.bestDistance) {
				System.out.println("SOLUTION FOUND! (len = " + (distance-1) + ")");
				this.bestPath = lastNode;
				this.bestDistance = distance;
			}
			return;
		}
		
		//find paths to next keys
		char [] keys = world.maze.getAllKeySymbolsAsString().toCharArray();
		Arrays.sort(keys);
		for (int i=0; i<keys.length; i++) {
			
			Location keyLoc = world.maze.find(keys[i]);
			SimpleSearcher searcher = new SimpleSearcher(world.maze, world.playerLocation, keyLoc);
			List<PathNode> pathToKey = searcher.search();
			
		//SimpleSearcher2 searcher = new SimpleSearcher2(world.maze, world.playerLocation);
		//searcher.search();
		//Collection<PathNode> pathsToKeys = searcher.getBestPathsToKeys();
		//for (PathNode pathToKeyTerminus : pathsToKeys) {
		
		//	List<PathNode> pathToKey = pathToKeyTerminus.getPathAsList();
			if (pathToKey != null) {
				
				//take path, applying changes as we go
				PathNode currentNode = lastNode;
				PathNode nextNode;
				for (int j=1; j<pathToKey.size(); j++) {
					nextNode = pathToKey.get(j);
					nextNode.action.apply(world);
					
					currentNode = currentNode.addChild(nextNode.action, nextNode.location, world.maze.getAllKeyAndDoorSymbolsAsString());
				}
				
				//expand
				expandSearch(currentNode);
				
				//undo path and changes
				currentNode.action.undo(world);	//NOTE: This one has the state changes
				for (int j=pathToKey.size()-2; j>0; j--) {
					pathToKey.get(j).action.undo(world);
				}
			}
		}
	}
		
}
