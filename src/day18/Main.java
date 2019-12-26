package day18;

import java.io.File;
import java.util.List;

import common.Location;

public class Main {

	public static void main(String [] args) {
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartA() {
		try {
			//Maze maze = Maze.loadFromFile(new File("files/day18/input.txt"));
			Maze maze = Maze.loadFromFile(new File("files/day18/test2.txt"));
			maze.print();
			
			//System.out.println(maze.getAllKeyAndDoorSymbolsAsString());
			
			World world = new World(maze);
			MazeSearcher searcher = new MazeSearcher(world);
			List<PathNode> path = searcher.search();
			//path.get(path.size()-1).printPathToNode();
			
			System.out.println("Program Complete!");
			if (path == null)
				System.out.println("No solution found!");
			else 
				System.out.println(path.size()-1);
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static void solvePartB() {
		try {
			Maze maze = Maze.loadFromFile(new File("files/day18/input.txt"));
			//Maze maze = Maze.loadFromFile(new File("files/day18/test3.txt"));	//answer = 72
			
			//modify maze:
			Location loc = maze.find('@');
			maze.set(loc, '#');
			
			maze.set(loc.row-1, loc.col,   '#');
			maze.set(loc.row+1, loc.col,   '#');
			maze.set(loc.row,   loc.col-1, '#');
			maze.set(loc.row,   loc.col+1, '#');
			
			maze.set(loc.row-1, loc.col-1, '@');
			maze.set(loc.row-1, loc.col+1, '@');
			maze.set(loc.row+1, loc.col+1, '@');
			maze.set(loc.row+1, loc.col-1, '@');
			
			maze.print();
			
			WorldPartB world = new WorldPartB(maze);
			MazeSearcherPartB5 searcher = new MazeSearcherPartB5(world);
			int bestDistance = searcher.search();
			System.out.println(bestDistance);
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}


}
