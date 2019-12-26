package day18;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.Location;

public class PathNode {

	MovePlayerAction action;
	Location location;
	String gameState;
	
	List<PathNode> children = new ArrayList<>();
	
	PathNode parent = null;
	
	public PathNode(MovePlayerAction action, Location location, String gameState) {
		this.action = action;
		this.location = location;
		this.gameState = gameState;
	}
	
	public PathNode addChild(MovePlayerAction action, Location location, String gameState) {
		PathNode child = new PathNode(action, location, gameState);
		child.parent = this;
		children.add(child);
		return child;
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
	public boolean hasAncestor(Location location, String gameState) {
		PathNode node = this;
		while (node.parent != null) {
			if (node.parent.location.equals(location) && node.parent.gameState.equals(gameState))
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
	
	public void printPathToNode() {
		List<PathNode> path = getPathAsList();
		for (PathNode node : path)
			System.out.println(node.location + " (state=" + node.gameState + ")");
		
	}
	
}
