package day18;

import common.Location;

public class World {

	Maze maze;
	Location playerLocation;
	
	public World(Maze maze, Location playerLocation) {
		this.maze = maze;
		this.playerLocation = playerLocation;
	}
	public World(Maze maze) {
		this(maze, maze.find('@'));
	}
	
	public World copy() {
		return new World(maze.copy(), playerLocation);
	}
	
}
