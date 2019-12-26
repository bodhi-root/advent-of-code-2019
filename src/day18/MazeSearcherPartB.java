package day18;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Location;

/**
 * Modification of the MazeSearcher's breadth-first search for part b.
 * This algorithm worked for Part A, but now it's too slow.
 */
public class MazeSearcherPartB {

	WorldPartB startWorld;
		
	public MazeSearcherPartB(WorldPartB startWorld) {
		this.startWorld = startWorld;
	}
	
	public int search() {
		List<Leaf> leaves = new ArrayList<>();
		List<Leaf> nextLeaves = new ArrayList<>();
		
		Leaf leaf = new Leaf(startWorld.copy());
		leaves.add(leaf);
		
		Set<String> visitedHash = new HashSet<>(100000);
		visitedHash.add(leaf.world.hashText());
		
		int iteration = 0;
		while (!leaves.isEmpty()) {
			iteration++;
			//if (iteration % 100 == 0)
				System.out.println("i = " + iteration + ", leaves = " + leaves.size());
			
			Leaf solution = expandBreadthFirstSearch(leaves, nextLeaves, visitedHash);
			if (solution != null)
				return iteration;
		}
		
		return -1;
	}
	
	
	
	public Leaf expandBreadthFirstSearch(List<Leaf> leaves, List<Leaf> nextLeaves, Set<String> visitedHash) {
		
		for (Leaf leaf : leaves) {
			
			//get children:
			List<Leaf> children = getNextLeaves(leaf);
			
			//visit each child:
			for (Leaf child : children) {
				String hashText = child.world.hashText();
				if (visitedHash.add(hashText)) {  //don't bother re-visiting old spaces (or spaces other paths have gotten to faster) 
			
					//check for solution:
					if (hashText.charAt(0) == ':')	//no keys or doors, straight to locations
						return child;
					
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
	
	
	public List<Leaf> getNextLeaves(Leaf leaf) {
		
		List<Leaf> nextLeaves = new ArrayList<>();
		
		for (int playerIndex=0; playerIndex<4; playerIndex++) {
		
			Location loc = leaf.world.playerLocations[playerIndex];

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
					WorldPartB nextWorld = leaf.world.copy();

					action.apply(nextWorld);
					nextLeaves.add(new Leaf(nextWorld));
				}

			}
		}
		
		return nextLeaves;
	}
	
	
	static class Leaf {
		
		WorldPartB world;
		//PathNode node;
		
		public Leaf(WorldPartB world) {//, PathNode node) {
			this.world = world;
			//this.node = node;
		}
		
	}
	
	
}
