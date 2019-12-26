package day19;

import java.util.ArrayList;
import java.util.List;

import common.Location;

public class BlockFinder {

	LineList lines = new LineList();
	Drone drone = new Drone();
	
	static class LineList {
	
		List<Line> lines = new ArrayList<>();
		
		public void add(Line line) {
			//if this line isn't even 100, there's no way to make a block that includes it
			if (line.getWidth() < 100)
				lines.clear();
			else
				lines.add(line);
			//if (lines.size() > 100)
			//	lines.remove(0);		//consider: linked list
		}
		
		public Location getBlockLocation() {
			//do we have a block?
			int lineCount = lines.size();
			if (lineCount >= 100) {
				Line line = lines.get(lineCount-100);
				if (testBlock(line.endCol - 99)) {
					Location solution = new Location(line.row, line.endCol - 99);
					while (testBlock(solution.col-1))
						solution = new Location(solution.row, solution.col-1);
					return solution;
				}
			}
			return null;
		}
		
		/**
		 * Checks for a 100x100 block in the current list of lines beginning at the
		 * specified 'startCol'.  We keep exactly 100 lines of history so this just 
		 * scans down the list to make sure each line spans from at least 'startCol'
		 * to 'startCol+99'.
		 */
		protected boolean testBlock(int startCol) {
			
			int endLine = lines.size() - 1;
			int startLine = lines.size() - 100;
			
			Line line;
			for (int i=endLine; i>=startLine; i--) {
				line = lines.get(i);
				if (line.startCol > startCol || line.endCol < (startCol + 99))
					return false;
			}
			
			return true;
		}
		
	}
	
	/**
	 * Represents a horizontal line of the tractor beam.  We keep the row,
	 * startCol, and endCol (inclusive) to specify this line.
	 */
	static class Line {
		
		int row;
		int startCol;
		int endCol;
		
		public int getWidth() {
			return endCol - startCol + 1;
		}
		
		public String toString() {
			return "Line(row=" + row + ", startCol=" + startCol + ", endCol=" + endCol + ", width=" + getWidth() + ")";
		}
		
	}
	
	/**
	 * Find a 100x100 block, beginning the search at startRow
	 */
	public Location findBlock(int startRow, int startCol) {
			
		Location solution = null;
		Line line;
		while(solution == null) {
			
			line = findLine(startRow, startCol);
			
			if (startRow % 100 == 0)
				System.out.println(line.toString());
			
			//advance to next line (and take advantage of fact that startCol for next line is >= startCol for this one)
			startRow++;
			startCol = line.startCol;
			
			lines.add(line);
			
			solution = lines.getBlockLocation();
		}
		
		return solution;
	}
	
	/**
	 * Finds a tractor beam Line in the given row, beginning the search at startCol.
	 * This will advance to the right until we find a PULL space and then continue
	 * until the PULL spaces stop.
	 */
	protected Line findLine(int row, int startCol) {
		Line line = new Line();
		line.row = row;
		
		//find start:
		while (drone.test(new Location(row, startCol)) == Drone.NO_PULL)
			startCol++;
		
		line.startCol = startCol;
		
		//find end:
		int endCol = startCol;
		while (drone.test(new Location(row, endCol)) == Drone.PULL)
			endCol++;
		
		line.endCol = endCol-1;
		
		return line;
	}
	
}
