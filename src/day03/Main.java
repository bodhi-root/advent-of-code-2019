package day03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import day03.WireTracker.Point;

public class Main {

	public static void main(String [] args) {
		String path1Text, path2Text;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("files/day03/input.txt"));
			try {
				path1Text = in.readLine();
				path2Text = in.readLine();
			}
			finally {
				in.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		List<Point> path1 = WireTracker.expandPath(path1Text);
		List<Point> path2 = WireTracker.expandPath(path2Text);
		
		//Point answer = WireTracker.findClosestOverlap(path1, path2);
		Point answer = WireTracker.findOverlapWithLowestCombinedSteps(path1, path2);
		System.out.println(answer);
		
		//weird error:
		//That's not the right answer; your answer is too low. Curiously, it's the right 
		//answer for someone else; you might be logged in to the wrong account or just 
		//unlucky. In any case, you need to be using your puzzle input. If you're stuck, 
		//make sure you're using the full input data; there are also some general tips on 
		//the about page, or you can ask for hints on the subreddit. Please wait one minute 
		//before trying again. (You guessed 9238.)
		//
		//I forgot to add 2...
	}
	
}
