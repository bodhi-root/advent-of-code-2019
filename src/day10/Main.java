package day10;

import java.io.File;
import java.util.List;

public class Main {

	public static void main(String [] args) {
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartA() {
		try {
			SpaceMap map = SpaceMap.loadFromFile(new File("files/day10/input.txt"));
			//SpaceMap map = SpaceMap.loadFromFile(new File("files/day10/test1.txt"));
			map.print();
			System.out.println(map.height + " x " + map.width);
			System.out.println("Asteroids = " + map.getAsteroidCount());
			map.findLocationWithMostVisibleAsteroids();
			// Best Location: (28, 29)
			// Count = 256
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testPartB() {
		try {
			SpaceMap map = SpaceMap.loadFromFile(new File("files/day10/test1b.txt"));
			map.print();
			List<Point> points = map.fireTheLaser(3, 8);
			for (Point point : points)
				System.out.println(point);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void solvePartB() {
		try {
			SpaceMap map = SpaceMap.loadFromFile(new File("files/day10/input.txt"));
			map.print();
			List<Point> points = map.fireTheLaser(28, 29);
			System.out.println(points.get(199));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
