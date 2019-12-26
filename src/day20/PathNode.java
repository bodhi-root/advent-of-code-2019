package day20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.Location;

/**
 * Largely copied from day15.RepairDroid.PathNode
 */
public class PathNode {

	Action action;	//command that brought us here (-1 for root node)
	Location location;
	int level = 0;		//for part B (otherwise, keep at 0)
	
	List<PathNode> children = new ArrayList<>();
	
	PathNode parent = null;
	
	public PathNode(Action action, Location location, int level) {
		this.action = action;
		this.location = location;
		this.level = level;
	}
	
	public PathNode addChild(Action action, Location location, int level) {
		PathNode child = new PathNode(action, location, level);
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
	public boolean hasAncestor(Location location, int level) {
		PathNode node = this;
		while (node.parent != null) {
			if (node.parent.location.equals(location) && node.parent.level == level)
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
		System.out.println(path.get(0).location);
		for (int i=1; i<path.size(); i++) {
			PathNode node = path.get(i);
			
			String actionText = "?";
			switch(node.action) {
			case MOVE_UP:    actionText = "U"; break;
			case MOVE_DOWN:  actionText = "D"; break;
			case MOVE_LEFT:  actionText = "L"; break;
			case MOVE_RIGHT: actionText = "R"; break;
			}
			
			System.out.println(actionText + " to " + node.location);
		}
	}
	
}
