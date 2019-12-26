package day18;

import java.util.List;

import common.Location;

public class WorldPartB {

	Maze maze;
	Location [] playerLocations;
	
	public WorldPartB(Maze maze, Location [] playerLocations) {
		this.maze = maze;
		this.playerLocations = playerLocations;
	}
	public WorldPartB(Maze maze) {
		this.maze = maze;
		this.playerLocations = new Location[4];
	
		//NOTE: this might not be the best way to do it.  It would be nice
		// to guarantee that the robot in the upper-left quadrant always 
		// ended up there.  However, since I only expect to call this with
		// a fresh Maze it will always initialize in the same way.
		List<Location> locs = maze.findAll('@');
		for (int i=0; i<4; i++) {
			playerLocations[i] = locs.get(i);
		}
	}
	
	public WorldPartB copy() {
		Location [] newLocations = new Location[4];
		for (int i=0; i<4; i++)
			newLocations[i] = playerLocations[i];
		
		return new WorldPartB(maze.copy(), newLocations);
	}
	
	public String hashText() {
		StringBuilder s = new StringBuilder();
		s.append(maze.getAllKeyAndDoorSymbolsAsString());
		for (int i=0; i<playerLocations.length; i++) {
			s.append(':').append(playerLocations[i].row).append(':').append(playerLocations[i].col);
		}
		
		return s.toString();
	}
	
}
