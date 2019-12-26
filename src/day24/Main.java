package day24;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String [] args) {
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartB() {
		StateB state = new StateB(5, 5);
		try {
			state.loadFromFile(new File("files/day24/input.txt"));
			//state.loadFromFile(new File("files/day24/test.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		//state.print();
		System.out.println("=================================================");
		
		for (int i=0; i<200; i++) {
			state.step();
		}
		//state.printAllCounts();
		//state.printAll();
		System.out.println("Total Bugs: " + state.getTotalBugCount());
	}
	
	
	public static void solvePartA() {
		State state = new State(5, 5);
		try {
			state.loadFromFile(new File("files/day24/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		Set<String> pastStates = new HashSet<>(100000);
		
		state.print();
		System.out.println();
		//for (int i=1; i<5; i++) {
		while(true) {
			state.step();
			//state.print();
			//System.out.println();
			
			if (!pastStates.add(state.toString())) {
				state.print();
				System.out.println(state.getBiodiversityRating());
				break;
			}
		}
	}
	
}
