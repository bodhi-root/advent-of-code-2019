package day20;

import java.io.File;
import java.util.List;

/**
 * Next test: we only need to connect labeled spaces.  Example: portal BC to portal DE.
 * We don't need to go step by step on each level if we know how to get from one portal
 * to another.  Instead:
 *   - calculate all connections
 *   - then build a search tree using them
 *   - need to save number of steps in each connection
 *   - can re-use same PathNode, but when we add children: add step count
 */
public class Main {

	public static void main(String [] args) {
		try {
			Maze maze = Maze.loadFromFile(new File("files/day20/input.txt"));
			//Maze maze = Maze.loadFromFile(new File("files/day20/test3.txt"));
			
			maze.set(maze.findLocationWithLabel("AA"), Maze.WALL);
			maze.set(maze.findLocationWithLabel("ZZ"), Maze.WALL);
			
			//maze.set(maze.findLocationWithLabel("START"), '*');
			//maze.set(maze.findLocationWithLabel("END"), '*');
			maze.print();
			
			MazeSearcher3 searcher = new MazeSearcher3(
					maze,
					Mode.PART_B,
					maze.findLocationWithLabel("START"),
					maze.findLocationWithLabel("END"));
			
			List<PathNode> path = searcher.search();
			if (path == null)
				System.out.println("No solution found.");
			else {
				//path.get(path.size()-1).printPathToNode();
				System.out.println("Best solution had " + (path.size() - 1) + " steps.");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
