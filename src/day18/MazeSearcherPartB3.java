package day18;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Location;

/**
 * Modification of MazeSearcherPartB to use the SimpleSearcher2.  I also added cacheing and 
 * memory compression to speed things up and avoid memory overflows.  No luck...
 * 
 * Left it running for 8 hours and got:
 * 
 * i = 1, leaves = 1
i = 2, leaves = 4
i = 3, leaves = 15
i = 4, leaves = 57
i = 5, leaves = 213
i = 6, leaves = 775
i = 7, leaves = 2735
i = 8, leaves = 9245
i = 9, leaves = 29555
i = 10, leaves = 87911
i = 11, leaves = 237692
i = 12, leaves = 583666
i = 13, leaves = 1424622
i = 14, leaves = 3822886
i = 15, leaves = 10239120
i = 16, leaves = 26235408
 */
public class MazeSearcherPartB3 {

	WorldPartB startWorld;
		
	public MazeSearcherPartB3(WorldPartB startWorld) {
		this.startWorld = startWorld;
	}
	
	Node bestSolution = null;
	int bestDistance;
	
	List<Node> leaves;
	List<Node> nextLeaves;
	Map<String, Integer> visitedHash;
	
	public int search() {
		this.leaves = new ArrayList<>();
		this.nextLeaves = new ArrayList<>();
		
		Node root = new Node(null, startWorld.copy(), 0);
		leaves.add(root);
		
		this.visitedHash = new HashMap<>(100000);
		visitedHash.put(root.world.hashText(), new Integer(0));
		
		int iteration = 0;
		while (!leaves.isEmpty()) {
			iteration++;
			//if (iteration % 100 == 0)
				System.out.println("i = " + iteration + ", leaves = " + leaves.size());
			
			Node solution = expandBreadthFirstSearch();
			if (solution != null)
				return solution.getDistanceFromRoot();
		}
		
		return -1;
	}
	
	public Node expandBreadthFirstSearch() {
		
		for (Node leaf : leaves) {
			
			leaf.uncompress(this.startWorld);
			
			//get children:
			List<Node> children = getNextLeaves(leaf);
			//System.out.println(children.size() + " children");
			
			//visit each child:
			for (Node child : children) {
				
				int distance = child.getDistanceFromRoot();
				
				String hashText = child.world.hashText();
				
				Integer bestDistanceToState = visitedHash.get(hashText);
				if (bestDistanceToState == null || distance < bestDistanceToState) {
				//if (visitedHash.add(hashText)) {  //don't bother re-visiting old spaces (or spaces other paths have gotten to faster) 
					visitedHash.put(hashText, bestDistanceToState);
					
					//don't bother if a solution exists that is better  (obsolete because of above)
					//if (bestSolution != null && distance >= bestDistance)
					//	continue;
					
					//check for solution:
					if (hashText.charAt(0) == ':') {	//no keys or doors, straight to locations
						if (bestSolution == null || distance < bestDistance) {
							bestSolution = child;
							bestDistance = distance;
							continue;
						}
					}
					
					nextLeaves.add(child);
					child.compress();
				}
			}
			
			leaf.world = null;
		}
		
		if (nextLeaves.isEmpty())
			return bestSolution;
		
		//swap lists
		List<Node> tmp = this.leaves;
		this.leaves = this.nextLeaves;
		this.nextLeaves = tmp;
		this.nextLeaves.clear();
		
		return null;
	}
	
	
	static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	
	public List<Node> getNextLeaves(Node leaf) {
		
		List<Node> nextLeaves = new ArrayList<>();
		
		String [] quadrantStates = calculateQuadrantStates(leaf.world);
		
		for (int playerIndex=0; playerIndex<4; playerIndex++) {
		
			Location loc = leaf.world.playerLocations[playerIndex];
			
			Collection<PathNode> terminalNodes = cachedTerminalNodes[playerIndex];
			if (cachedQuadrantStates[playerIndex] == null ||
				!cachedQuadrantStates[playerIndex].equals(quadrantStates[playerIndex])) {
				
				//System.out.println("Call simple searcher");
				SimpleSearcher2 searcher = new SimpleSearcher2(leaf.world.maze, loc);
				searcher.search();
				terminalNodes = searcher.getBestPathsToKeys();
				//System.out.println("return");

				cachedQuadrantStates[playerIndex] = quadrantStates[playerIndex];
				cachedTerminalNodes[playerIndex] = terminalNodes;
			}
						
			for (PathNode terminus : terminalNodes) {

				Location nextLoc = terminus.location;
				MovePlayerAction action = new MovePlayerAction(loc, nextLoc);
				
				WorldPartB nextWorld = leaf.world.copy();
				action.apply(nextWorld);
				
				nextLeaves.add(new Node(leaf, nextWorld, terminus.getDistanceFromRoot()));
			}
		}
		
		return nextLeaves;
	}
	
	String [] cachedQuadrantStates = new String[4];
	Collection<PathNode>[] cachedTerminalNodes = (Collection<PathNode> [])new Collection[4];
	//List<Collection<PathNode>> cachedTerminalNodes = new ArrayList<>();
	
	protected String [] calculateQuadrantStates(WorldPartB world) {
		String [] states = new String[4];
		
		states[0] = createQuadrantState(
				world.maze, 
				0, 0, 
				startWorld.playerLocations[0].row, startWorld.playerLocations[0].col,
				world.playerLocations[0]);
		
		states[1] = createQuadrantState(
				world.maze, 
				0, startWorld.playerLocations[1].col, 
				startWorld.playerLocations[1].row, world.maze.width-1,
				world.playerLocations[1]);
		
		states[2] = createQuadrantState(
				world.maze, 
				startWorld.playerLocations[2].row, 0, 
				world.maze.height-1, startWorld.playerLocations[2].col,
				world.playerLocations[2]);
		
		states[3] = createQuadrantState(
				world.maze, 
				startWorld.playerLocations[3].row, startWorld.playerLocations[3].col, 
				world.maze.height-1, world.maze.width-1,
				world.playerLocations[3]);
		
		return states;
	}
	protected String createQuadrantState(Maze maze, int fromRow, int fromCol, int toRow, int toCol, Location loc) {
		StringBuilder s = new StringBuilder();
		for (int i=fromRow; i<=toRow; i++) {
			for (int j=fromCol; j<=toCol; j++) {
				char ch = maze.get(i, j);
				if (maze.isDoor(ch) || maze.isKey(ch))
					s.append(ch);
			}
		}
		
		s.append(':').append(loc.row).append(':').append(loc.col);
		
		return s.toString();
	}
	
	static class Node {
		
		Node parent;
		WorldPartB world;
		int steps;
		
		public Node(Node parent, WorldPartB world, int steps) {
			this.parent = parent;
			this.world = world;
			this.steps = steps;
		}
		
		public int getDistanceFromRoot() {
			int distance = 0;
			Node node = this;
			while (node.parent != null) {
				distance += node.steps;
				node = node.parent;
			}
			return distance;
		}
		
		CompressedWorld cw;
		
		public void compress() {
			this.cw = CompressedWorld.compress(this.world);
			this.world = null;
		}
		public void uncompress(WorldPartB template) {
			if (this.world == null) {
				this.world = this.cw.uncompress(template);
				this.cw = null;
			}
		}
		
	}
	
	static class CompressedWorld {
		
		String keysAndDoors;
		Location [] locations;
		
		public static CompressedWorld compress(WorldPartB world) {
			CompressedWorld cw = new CompressedWorld();
			cw.locations = world.playerLocations;
			cw.keysAndDoors = world.maze.getAllKeyAndDoorSymbolsAsString();
			return cw;
		}
		
		public WorldPartB uncompress(WorldPartB template) {
			WorldPartB newWorld = template.copy();
			newWorld.playerLocations = this.locations;
			
			//only keep doors and keys that are in our state
			for (int i=0; i<newWorld.maze.height; i++) {
				for (int j=0; j<newWorld.maze.width; j++) {
					char value = newWorld.maze.get(i, j);
					if (newWorld.maze.isDoor(value) || newWorld.maze.isKey(value)) {
						if (this.keysAndDoors.indexOf(value) < 0)
							newWorld.maze.set(i, j, Maze.FLOOR);
					}
				}
			}
			
			return newWorld;
		}
		
	}
	
	
}
