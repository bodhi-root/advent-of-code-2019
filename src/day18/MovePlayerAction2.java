package day18;

import common.Location;

/**
 * A slight modification for when we cheat and move multiple steps with
 * one action. We add the 'steps' field to record how far we actually
 * traveled.
 */
public class MovePlayerAction2 {
	
	Location moveFrom;
	Location moveTo;
	int steps;
	
	char pickupKey;
	char openDoor;
	Location openDoorLoc;
	
	public MovePlayerAction2(Location moveFrom, Location moveTo, int steps) {
		this.moveFrom = moveFrom;
		this.moveTo = moveTo;
		this.steps = steps;
	}
	
	public void apply(World world) {
		//move to new location:
		world.playerLocation = moveTo;
		
		//check for key:
		char value = world.maze.get(moveTo);
		if (world.maze.isKey(value)) {
			//pickup key
			this.pickupKey = value;
			world.maze.set(moveTo, Maze.FLOOR);
			
			//unlock door
			this.openDoor = Character.toUpperCase(pickupKey);
			this.openDoorLoc = world.maze.find(openDoor);
			//if (this.openDoorLoc == null)
			//	throw new IllegalStateException("Could not find door '" + openDoor + "' for key '" + pickupKey + "'");
			
			//some keys don't have doors
			if (this.openDoorLoc != null)
				world.maze.set(openDoorLoc, Maze.FLOOR);
		}
	}
	
	public void undo(World world) {
		//move to old location:
		world.playerLocation = moveFrom;
		
		//if key was used, put it back and close door
		if (pickupKey != 0) {
			world.maze.set(moveTo, pickupKey);
			if (openDoorLoc != null)
				world.maze.set(openDoorLoc, openDoor);
		}
	}
	
	/**
	 * Same logic as apply(World), we just do a quick check to see which
	 * player is being moved.
	 */
	public void apply(WorldPartB world) {
		//move to new location:
		int playerIndex = -1;
		for (int i=0; i<world.playerLocations.length; i++) {
			if (world.playerLocations[i].equals(moveFrom))
				playerIndex = i;
		}
		
		if (playerIndex < 0)
			throw new IllegalStateException("No player found at " + moveFrom);
		
		world.playerLocations[playerIndex] = moveTo;
		
		//check for key:
		char value = world.maze.get(moveTo);
		if (world.maze.isKey(value)) {
			//pickup key
			this.pickupKey = value;
			world.maze.set(moveTo, Maze.FLOOR);
			
			//unlock door
			this.openDoor = Character.toUpperCase(pickupKey);
			this.openDoorLoc = world.maze.find(openDoor);
			//if (this.openDoorLoc == null)
			//	throw new IllegalStateException("Could not find door '" + openDoor + "' for key '" + pickupKey + "'");
			
			//some keys don't have doors
			if (this.openDoorLoc != null)
				world.maze.set(openDoorLoc, Maze.FLOOR);
		}
	}
	
	public void undo(WorldPartB world) {
		//move to old location:
		int playerIndex = -1;
		for (int i=0; i<world.playerLocations.length; i++) {
			if (world.playerLocations[i].equals(moveTo))
				playerIndex = i;
		}

		if (playerIndex < 0)
			throw new IllegalStateException("No player found at " + moveFrom);

		world.playerLocations[playerIndex] = moveFrom;
		
		//if key was used, put it back and close door
		if (pickupKey != 0) {
			world.maze.set(moveTo, pickupKey);
			if (openDoorLoc != null)
				world.maze.set(openDoorLoc, openDoor);
		}
	}
	
	
}
