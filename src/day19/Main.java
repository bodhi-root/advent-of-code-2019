package day19;

import common.Location;

public class Main {

	public static void main(String [] args) {
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartA() {
		SpaceExplorer explorer = new SpaceExplorer(50, 50);
		explorer.testAll();
		explorer.printSpace();
		System.out.println(explorer.getSpaceCount(Drone.PULL));
	}
	
	public static void solvePartB() {
		BlockFinder finder = new BlockFinder();
		Location loc = finder.findBlock(900, 0);
		System.out.println(loc);
		System.out.println("Answer = " + (loc.col * 10000 + loc.row));
		
		/*
		Line(row=900, startCol=502, endCol=628, width=127)
        Line(row=1000, startCol=558, endCol=698, width=141)
        Line(row=1100, startCol=614, endCol=768, width=155)
        Location(1097,667)
        Answer = 6671097
		*/
	}
	
}
