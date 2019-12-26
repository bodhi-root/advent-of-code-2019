package day15;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.Location;
import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;

public class RepairDroid implements ComputerOutput, ComputerInput {

	public static final int MOVE_NORTH = 1;
	public static final int MOVE_SOUTH = 2;
	public static final int MOVE_WEST = 3;
	public static final int MOVE_EAST = 4;
	
	public static final int REPLY_HIT_WALL = 0;
	public static final int REPLY_SUCCESS = 1;
	public static final int REPLY_SUCCESS_AND_GOAL = 2;
	
	public static final int SPACE_UNKNOWN = 0;
	public static final int SPACE_EMPTY = 1;
	public static final int SPACE_WALL = 2;
	public static final int SPACE_GOAL = 3;
	
	int [][] map;
	int height;
	int width;
	
	Location location;
	
	Computer computer;
	ComputerInput computerInput;
	int lastCommand;
	int inputCount = 0;
	
	public RepairDroid(int height, int width, Location loc) {
		this.map = new int[height][width];
		this.height = height;
		this.width = width;
		
		this.location = loc;
		//this.computerInput = new ManualInput(this);
		//this.computerInput = new HugTheWallAutomatedInput(this);
		//this.computerInput = new RecursiveExplorerInput(this);
		this.computerInput = new PartBSolver(this);
		
		computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day15/input.txt"));
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
		computer.setInput(this);
		computer.setOutput(this);
	}
	
	public Computer getComputer() {
		return computer;
	}
	
	public int getMapValue(int row, int col) {
		return map[row][col];
	}
	public int getMapValue(Location location) {
		return getMapValue(location.row, location.col);
	}
	
	public void printMap() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				char ch;
				if (i == location.row && j == location.col)
					ch = 'D';
				else {
					switch(map[i][j]) {
					case SPACE_UNKNOWN: ch = ' '; break;
					case SPACE_EMPTY:   ch = '.'; break;
					case SPACE_WALL:    ch = '#'; break;
					case SPACE_GOAL:    ch = '2'; break;
					default: throw new IllegalStateException("Unknown space code: " + map[i][j]);
					}
				}
				System.out.print(ch);
			}
			System.out.println();
		}
	}

	public int getKnownSpaceCount() {
		int count = 0;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (map[i][j] != SPACE_UNKNOWN)
					count++;
			}
		}
		
		return count;
	}
	
	@Override
	public void write(long value) {
		
		int di = 0, dj = 0;
		switch(lastCommand) {
		case MOVE_NORTH: di = -1; break;
		case MOVE_SOUTH: di = +1; break;
		case MOVE_EAST:  dj = +1; break;
		case MOVE_WEST:  dj = -1; break;
		}
		
		int nextRow = location.row + di;
		int nextCol = location.col + dj;
		
		if (value == REPLY_HIT_WALL) {
			map[nextRow][nextCol] = SPACE_WALL;
		} 
		else if (value == REPLY_SUCCESS) {
			map[nextRow][nextCol] = SPACE_EMPTY;
			location = new Location(nextRow, nextCol);
		} 
		else if (value == REPLY_SUCCESS_AND_GOAL) {
			map[nextRow][nextCol] = SPACE_GOAL;
			location = new Location(nextRow, nextCol);
			printMap();
			System.out.println("YOU WIN!!!");
			//System.exit(0);
		}
	}
	
	
	public long getInput() {
		long value = computerInput.getInput();
		this.lastCommand = (int)value;
		this.inputCount++;
		return value;
	}

	/**
	 * Accepts manual input from the user.  The keypad is used (8 = UP, 2 = DOWN, 4 = LEFT, 6 = RIGHT)
	 */
	static class ManualInput implements ComputerInput {

		RepairDroid droid;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		public ManualInput(RepairDroid droid) {
			this.droid = droid;
		}
		
		public long getInput() {
			System.out.println();
			droid.printMap();
			System.out.print("Enter Input: ");
			
			while(true) {
				String line;
				char ch;
				try {
					line = in.readLine();
					ch = line.isEmpty() ? ' ' : line.charAt(0);
				}
				catch(IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
				System.out.println();
				
				switch(ch) {
				case '4': return MOVE_WEST;
				case '6': return MOVE_EAST;
				case '8': return MOVE_NORTH;
				case '2': return MOVE_SOUTH;
				}
				
				System.out.println("Invalid input: " + ch);
			}
		}
		
	}
	
	/**
	 * Always tries to turn left before continuing in its current direction 
	 * (or turning right if there is an obstacle).  This will "hug" the left 
	 * wall and should enumerate the entire map.
	 * 
	 * (If you start in an open area, I think there is a chance you will go in a 
	 * circle though...)
	 * 
	 * This algorithm revealed the following maze:
	 * 
	################# ####### #############      
    #.................#.......#.............#     
    #.#####.###########.###.###.#.#########.#     
    #...#...#.........#.#.#.....#...#.....#.#     
    #.#.#####.#######.#.#.#########.###.#.#.#     
    #.#...#...#...#...#.#.........#...#.#...#     
     ####.#.###.###.#.#.#.###.#### ##.#.####      
    #.....#.#...#...#.#...#...#...#...#.#...#     
    #.###.#.#.#.#.#######.#####.#.#.###.#.#.#     
    #...#.#.#.#.#.........#.....#...#.#.#.#.#     
    #.#.###.###.###########.#########.#.#.#.#     
    #.#...#.....#.....#...#...#.....#...#.#.#     
    #.###.#####.#.###.#.#.###.#.#.###.#####.#     
    #...#.....#.#.#.....#.....#.#...#.......#     
     ##D#####.#.#.#.#####.#### ####.#######.#     
    #...#...#...#.#.#...#.#...#.....#.......#     
    #.###.#.#####.###.#.###.#.#.#####.######      
    #.#...#...#.#.....#.#...#.#.......#.....#     
    #.###.###.#.#######.#.###.#####.###.####      
    #...#...#.#.....#...#.#.#.....#...#.....#     
     ##.###.#.#####.#.###.#.#####.###.#.###.#     
    #...#...#.......#...#.#.......#.#.#...#.#     
    #.###.#########.###.###.#######.#.#####.#     
    #.....#...#.....#.#...#.....#.....#...#.#     
     ######.#.#.#####.###.#####.#.#####.#.#.#     
    #.#.....#.#.......#.#.....#.#.......#...#     
    #.#.###.## ######.#.#.###.#.######## ##.#     
    #...#.#...#.....#.#.#.#...#.#.......#...#     
    #.###.###.#.#.###.#.#.###.#.###.###.#.##      
    #.#.....#...#.#...#.#...#.#...#...#.#...#     
    #.#.#########.#.###.###.## ##.#.#.#####.#     
    #.#.#.........#.....#.#...#...#.#.......#     
    #.#.#.#######.#####.#.###.#.###.########      
    #.#.#...#.........#.#...#.#...#.#.......#     
    #.#.###.#.#######.#.#.###.###.#.###.###.#     
    #...#...#.#...#...#.#.#...#...#...#.#...#     
     ##.#.## ##.#.#####.#.#.###.#####.#.#.##      
    #...#...#...#.......#.#...#.#...#.#.#...#     
    #.#####.#.###########.###.#.#.###.#.###.#     
    #.....#.................#...#.......#2..#     
     ##### ################# ### ####### ###    
	 * 
	 */
	static class HugTheWallAutomatedInput implements ComputerInput {

		RepairDroid droid;
		
		public HugTheWallAutomatedInput(RepairDroid droid) {
			this.droid = droid;
		}
		
		int [] TEST_COMMANDS = new int [] {MOVE_WEST, MOVE_NORTH, MOVE_EAST, MOVE_SOUTH};
		int lastCommand = MOVE_NORTH;
		
		public long getInput() {
			
			if (droid.inputCount % 100 == 0) {
				droid.printMap();
				System.out.println();
			}
			
			//if (test(lastCommand))
			//	return lastCommand;
			
			int lastCommandIndex = -1;
			for (int i=0; i<TEST_COMMANDS.length; i++) {
				if (TEST_COMMANDS[i] == lastCommand) {
					lastCommandIndex = i;
					break;
				}
			}
			
			for (int i=-1; i<3; i++) {
				int testCommandIndex = (lastCommandIndex + i) % 4;
				if (testCommandIndex < 0)
					testCommandIndex += 4;
				else if (testCommandIndex >= 4)
					testCommandIndex -= 4;
				
				if (test(TEST_COMMANDS[testCommandIndex])) {
					lastCommand = TEST_COMMANDS[testCommandIndex];
					return lastCommand;
				}
			}
			
			throw new IllegalStateException("Should not happen");
		}
		
		protected boolean test(int command) {
			int di = 0, dj = 0;
			
			switch(command) {
			case MOVE_NORTH: di = -1; break;
			case MOVE_SOUTH: di = +1; break;
			case MOVE_EAST:  dj = +1; break;
			case MOVE_WEST:  dj = -1; break;
			}
			
			int nextRow = droid.location.row + di;
			int nextCol = droid.location.col + dj;
			
			if (droid.map[nextRow][nextCol] == SPACE_WALL)
				return false;
			
			return true;
		}
		
	}
	
	static class PathNode {
		
		int command = -1;	//command that brought us here (-1 for root node)
		Location location;
		boolean hasVisited;
		
		List<PathNode> children = new ArrayList<>();
		
		PathNode parent = null;
		
		public PathNode(int command, Location location, boolean hasVisited) {
			this.command = command;
			this.location = location;
			this.hasVisited = hasVisited;
		}
		
		public void addChild(int command, Location location) {
			PathNode child = new PathNode(command, location, false);
			child.parent = this;
			children.add(child);
		}
		
		public boolean hasChild(Location location) {
			return indexOfChild(location) >= 0;
		}
		public PathNode getChild(Location location) {
			int index = indexOfChild(location);
			return (index < 0) ? null : children.get(index);
		}
		
		protected int indexOfChild(Location location) {
			for (int i=0; i<children.size(); i++) {
				if (children.get(i).location.equals(location))
					return i;
			}
			return -1;
		}
		
		public void removeChild(Location location) {
			int index = indexOfChild(location);
			if (index >= 0)
				children.remove(index);
		}
		
		/**
		 * Returns TRUE if this location appears anywhere as an 
		 * ancestor in the tree.  This keeps us from crossing over our
		 * path and visiting places we've already been.
		 */
		public boolean hasAncestor(Location location) {
			PathNode node = this;
			while (node != null) {
				if (node.location.equals(location))
					return true;
				
				node = node.parent;
			}
			
			return false;
		}
		
		/**
		 * Returns the number of steps it would take take to get from the root
		 * node to this one. 
		 */
		public int getDistanceFromRoot() {
			int steps = 0;
			PathNode node = this;
			while (node.parent != null) {
				steps++;
				node = node.parent;
			}
			return steps;
		}
		
		public PathNode getNextUnvisitedChild() {
			for (PathNode child : children) {
				if (!child.hasVisited)
					return child;
			}
			return null;
		}
		
		public List<PathNode> getPathAsList() {
			List<PathNode> list = new ArrayList<>();
			PathNode node = this;
			while (node != null) {
				list.add(node);
				node = node.parent;
			}
			Collections.reverse(list);
			return list;
		}
	}
	
	/**
	 * Automated input that recursively explores the entire map.  This builds a tree
	 * of possible paths that it expands one step at a time.  If a space is empty
	 * or unknown, we will try to go there.  Each time getInput() is called, we 
	 * check to see if we actually moved or not and update our state and further
	 * possibilities accordingly.  Any path that reaches the goal has its length
	 * calculated, and we maintain the value of the shortest path.
	 */
	static class RecursiveExplorerInput implements ComputerInput {

		RepairDroid droid;
		PathNode currentPathNode;
		
		public RecursiveExplorerInput(RepairDroid droid) {
			this.droid = droid;
			this.currentPathNode = new PathNode(-1, droid.location, true);
		}
		
		int [] ALL_COMMANDS = new int [] {MOVE_WEST, MOVE_NORTH, MOVE_EAST, MOVE_SOUTH};
		
		int bestStepsToGoal = Integer.MAX_VALUE;
		PathNode bestPathToGoal = null;
		
		int longestPath = 0;
		
		/**
		 * Looks around the currentPathNode and updates its potential child steps
		 * based on what we see.  If we see a wall where we previously thought
		 * we could step, we remove it.  If we find any non-wall spaces that are
		 * not listed as children, we add them.
		 */
		protected void updateNextSteps() {
			for (int i=0; i<ALL_COMMANDS.length; i++) {
				
				int command = ALL_COMMANDS[i];
				int di = 0, dj = 0;
				
				switch(command) {
				case MOVE_NORTH: di = -1; break;
				case MOVE_SOUTH: di = +1; break;
				case MOVE_EAST:  dj = +1; break;
				case MOVE_WEST:  dj = -1; break;
				}
				
				int nextRow = droid.location.row + di;
				int nextCol = droid.location.col + dj;
				
				Location nextLoc = new Location(nextRow, nextCol);
				boolean hasChild = currentPathNode.hasChild(nextLoc);
				
				if (droid.map[nextRow][nextCol] == SPACE_WALL) {
					if (hasChild)
						currentPathNode.removeChild(nextLoc);
				} else {
					if (!hasChild && !currentPathNode.hasAncestor(nextLoc))
						currentPathNode.addChild(command, nextLoc);
				}
			}
		}
		
		public long getInput() {
			
			if (droid.inputCount % 100 == 0) {
				droid.printMap();
				System.out.println();
			}
			
			//update current path node (if we moved):
			if (!droid.location.equals(currentPathNode.location)) {
				
				if (currentPathNode.parent != null && currentPathNode.parent.location.equals(droid.location))
					currentPathNode = currentPathNode.parent;
				else {
					PathNode childNode = currentPathNode.getChild(droid.location);
					if (childNode != null)
						currentPathNode = childNode;
					else
						throw new IllegalStateException("You can't get there from here.  So how did you do it?");
				}
				
				currentPathNode.hasVisited = true;
				
				//check for WIN!
				if (droid.getMapValue(droid.location) == SPACE_GOAL) {
					int steps = currentPathNode.getDistanceFromRoot();
					if (steps < bestStepsToGoal) {
						bestStepsToGoal = steps;
						bestPathToGoal = currentPathNode;
					}
					System.out.println("Goal Found! (" + steps + " steps, best = " + bestStepsToGoal + ")");
				}
				
				this.longestPath = Math.max(this.longestPath, currentPathNode.getDistanceFromRoot());
			}
			
			//update next steps:
			updateNextSteps();
			
			//take next step (or go back)
			PathNode nextStep = currentPathNode.getNextUnvisitedChild();
			if (nextStep != null) {
				return nextStep.command;
			} else {
				
				if (currentPathNode.parent == null) {
					droid.printMap();
					System.out.println("Program over (nowhere else to go)");
					System.out.println("Shortest path to goal = " + bestStepsToGoal);
					System.out.println("Long path encountered = " + longestPath);
					
					//serialize best path:
					List<PathNode> pathList = bestPathToGoal.getPathAsList();
					StringBuilder s = new StringBuilder();
					for (int i=1; i<pathList.size(); i++)
						s.append(pathList.get(i).command);
					System.out.println("Best Path: " + s.toString());
					
					System.exit(0);
				}
				
				switch(currentPathNode.command) {
				case MOVE_NORTH: return MOVE_SOUTH;
				case MOVE_SOUTH: return MOVE_NORTH;
				case MOVE_EAST: return MOVE_WEST;
				case MOVE_WEST: return MOVE_EAST;
				default: throw new IllegalStateException("Illegal command: " + currentPathNode.command);
				}
				
			}
			
		}
		
	}
	
	
	static class PartBSolver implements ComputerInput {

		public static final int MOVE_TO_OXYGEN = 1;
		public static final int SOLVE_PART_B = 2;
		
		int mode = MOVE_TO_OXYGEN;
		
		String pathToOxygen = "111144114422224444223333332244442222224422332244223322223311331144111133113311111133113311441111332233331111114444224411442244441133114444114422441144113311331144444444442222331133222222223322444444223333332233224422223333224444441144224422332244223333331133222222442222441111114444223322442233";
		int nextIndex = 0;
		
		RecursiveExplorerInput explorer;
		RepairDroid droid;
		
		public PartBSolver(RepairDroid droid) {
			this.droid = droid;
		}
		
		public long getInput() {
			
			if (mode == MOVE_TO_OXYGEN) {
				if (nextIndex < pathToOxygen.length()) {
					int command = Integer.parseInt(pathToOxygen.substring(nextIndex, nextIndex+1));
					this.nextIndex++;
					return command;
				} else {
					mode = SOLVE_PART_B;
					this.explorer = new RecursiveExplorerInput(droid);
				}
			}
			
			if (mode == SOLVE_PART_B) {
				return explorer.getInput();
			}
			
			throw new IllegalStateException("Unexpected mode: " + mode);
		}
		
	}
	
	
}
