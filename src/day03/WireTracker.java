package day03;

import java.util.ArrayList;
import java.util.List;

public class WireTracker {

	static class Point {
		
		int x;
		int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int hashCode() {
			return x * 41 + y;
		}
		public boolean equals(Object o) {
			Point that = (Point)o;
			return this.x == that.x &&
				   this.y == that.y;
		}
		
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
	
	public static List<Point> expandPath(String pathText) {
		List<Point> path = new ArrayList<>();
		
		int x = 0;
		int y = 0;
		
		String [] parts = pathText.split(",");
		for (String part : parts) {
		    char dir = part.charAt(0);
		    int len = Integer.parseInt(part.substring(1));
		    
		    int dx = 0;
		    int dy = 0;
		    
		    switch(dir) {
		    case 'U':
		    	dy = -1;
		    	break;
		    case 'D':
		    	dy = +1;
		    	break;
		    case 'R':
		    	dx = +1;
		    	break;
		    case 'L':
		    	dx = -1;
		    	break;
		    }
		    
		    for (int i=0; i<len; i++) {
		    	x += dx;
		    	y += dy;
		    	
		    	path.add(new Point(x, y));
		    }
		}
		
		return path;
	}
	
	public static Point findClosestOverlap(List<Point> path1, List<Point> path2) {
		Point bestPoint = null;
		int bestDistance = Integer.MAX_VALUE;
		
		for (Point point : path1) {
			int index = path2.indexOf(point);
			if (index >= 0) {
				Point overlap = path2.get(index);
				int distance = Math.abs(overlap.x) + Math.abs(overlap.y);
				if (distance < bestDistance) {
					bestDistance = distance;
					bestPoint = overlap;
				}
			}
		}
		
		System.out.println(bestPoint);
		System.out.println(Math.abs(bestPoint.x) + Math.abs(bestPoint.y));
		
		return bestPoint;
	}
	
	public static Point findOverlapWithLowestCombinedSteps(List<Point> path1, List<Point> path2) {
		Point bestPoint = null;
		int bestDistance = Integer.MAX_VALUE;
		
		for (int i=0; i<path1.size(); i++) {
			Point point = path1.get(i);
			
			int index = path2.indexOf(point);
			if (index >= 0) {
				Point overlap = path2.get(index);
				int distance = i + index + 2;	//don't forget +2 (since index 0 is 1 step)
				if (distance < bestDistance) {
					bestDistance = distance;
					bestPoint = overlap;
				}
			}
		}
		
		System.out.println(bestPoint);
		System.out.println(bestDistance);
		
		return bestPoint;
	}
	
}
