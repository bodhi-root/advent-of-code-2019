package day18;

import java.util.ArrayList;
import java.util.List;

import common.Location;

/**
 * Tried a breadth-first search.  Slow!!!
 */
public class MazeSearcher3 {

	World startWorld;
	
	static class Leaf {
		
		World world;
		PathNode node;
		
		public Leaf(World world, PathNode node) {
			this.world = world;
			this.node = node;
		}
		
	}
	
	List<Leaf> leaves = new ArrayList<>();
	List<Leaf> nextLeaves = new ArrayList<>();
	
	PathNode bestPath = null;
	int bestDistance;
	
	public MazeSearcher3(World world) {
		this.startWorld = world;
	}

	public List<PathNode> search() {
		this.leaves.clear();
		
		PathNode root = new PathNode(null, startWorld.playerLocation, startWorld.maze.getAllKeyAndDoorSymbolsAsString());
		this.leaves.add(new Leaf(startWorld.copy(), root));
		
		int iteration = 0;
		while (!leaves.isEmpty()) {
			iteration++;
			System.out.println("i = " + iteration + " (leaves = " + leaves.size() + ", sample len = " + leaves.get(0).node.getDistanceFromRoot() + ")");
			expandSearch();
		}
		
		return bestPath == null ? null : bestPath.getPathAsList();
	}
	
	public void expandSearch() {
		
		this.nextLeaves.clear();
		
		for (Leaf leaf : leaves) {
		
			World world = leaf.world;
			
			PathNode lastNode = leaf.node;
			int distance = lastNode.getDistanceFromRoot();
			
			if (bestPath != null && distance > bestDistance)
				continue;
			
			//is this the goal?
			if (lastNode.gameState.isEmpty()) {
				if (this.bestPath == null || distance < this.bestDistance) {
					System.out.println("SOLUTION FOUND! (len = " + distance + ")");
					this.bestPath = lastNode;
					this.bestDistance = distance;
				}
				continue;
			}
			
			//find paths to next keys
			char [] keys = world.maze.getAllKeySymbolsAsString().toCharArray();
			for (int i=0; i<keys.length; i++) {
				
				Location keyLoc = world.maze.find(keys[i]);
				SimpleSearcher searcher = new SimpleSearcher(world.maze, world.playerLocation, keyLoc);
				List<PathNode> pathToKey = searcher.search();
				if (pathToKey != null) {
					
					World nextWorld = world.copy();
					
					//take path, applying changes as we go
					PathNode currentNode = lastNode;
					PathNode nextNode;
					for (int j=1; j<pathToKey.size(); j++) {
						nextNode = pathToKey.get(j);
						nextNode.action.apply(nextWorld);
						
						currentNode = currentNode.addChild(nextNode.action, nextNode.location, nextWorld.maze.getAllKeyAndDoorSymbolsAsString());
					}
					
					//expand
					nextLeaves.add(new Leaf(nextWorld, currentNode));
				}
			}
		}
		
		//swap nextLeaves into leaves
		List<Leaf> tmp = this.leaves;
		this.leaves = this.nextLeaves;
		this.nextLeaves = tmp;
	}
		
}
