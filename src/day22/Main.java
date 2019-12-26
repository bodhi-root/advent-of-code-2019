package day22;

import java.io.File;
import java.util.List;

import common.Util;

public class Main {

	public static void main(String [] args) {
		runTests();
		solvePartA();
		//solvePartB();
	}
	
	public static void solvePartA() {
		try {
			Deck deck = new Deck(10007);
			
			List<String> lines = Util.readLinesFromFile(new File("files/day22/input.txt"));
			System.out.println(lines.size() + " lines read from file");
			
			Action [] actions = new Action[lines.size()];
			for (int i=0; i<lines.size(); i++) {
				String line = lines.get(i);
				actions[i] = Action.parse(line);
			}
			
			for (Action action : actions)
				action.apply(deck);
			
			//for (int i=0; i<100; i++)
			//	System.out.println(deck.get(i));
			
			System.out.println(deck.indexOf(2019));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void solvePartB() {
		try {
			long deckLength = 119315717514047L;
			
			List<String> lines = Util.readLinesFromFile(new File("files/day22/input.txt"));
			System.out.println(lines.size() + " lines read from file");
			
			Action [] actions = new Action[lines.size()];
			for (int i=0; i<lines.size(); i++) {
				String line = lines.get(i);
				actions[i] = Action.parse(line);
			}
			
			long index = 2020;
			
			//ran all the way to 142,500,000.
			// no cycle detected...
			for (long cycle=0; cycle<101741582076661L; cycle++) {
				for (int i=actions.length-1; i>=0; i--)
					index = actions[i].getSourceIndexFor(index, deckLength);
				
				//System.out.println(index);
				
				if (cycle % 100000 == 0)
					System.out.println(cycle);
				
				if (index == 2020) {
					System.out.println("Cycle detected: " + (cycle+1));
					System.exit(0);
				}
				
			}
			
			System.out.println(index);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void runTests() {
		
		//my tests
		
		test(new String [] {
				"deal into new stack"
		}, "9 8 7 6 5 4 3 2 1 0", 10);
		
		test(new String [] {
				"cut 6"
		}, "6 7 8 9 0 1 2 3 4 5", 10);
		
		test(new String [] {
				"cut -6"
		}, "4 5 6 7 8 9 0 1 2 3", 10);
		
		test(new String [] {
				"deal with increment 3"
		}, "0 7 4 1 8 5 2 9 6 3", 10);
		
		//their tests
		
		test(new String [] {
				"deal with increment 7",
				"deal into new stack",
				"deal into new stack"
		     }, 
			"0 3 6 9 2 5 8 1 4 7",
			10);
		
		test(new String [] {
		"cut 6",
		"deal with increment 7",
		"deal into new stack"
		}, "3 0 7 4 1 8 5 2 9 6", 10);
		
		test(new String [] {
		"deal with increment 7",
		"deal with increment 9",
		"cut -2"
		}, "6 3 0 7 4 1 8 5 2 9", 10);
		
		test(new String [] {
		"deal into new stack",
		"cut -2",
		"deal with increment 7",
		"cut 8",
		"cut -4",
		"deal with increment 7",
		"cut 3",
		"deal with increment 9",
		"deal with increment 3",
		"cut -1"
		}, "9 2 5 8 1 4 7 0 3 6", 10);
	}
	
	public static void test(String [] lines, String result, int deckSize) {
		Action [] actions = new Action[lines.length];
		for (int i=0; i<actions.length; i++)
			actions[i] = Action.parse(lines[i]);
		
		Deck deck = new Deck(deckSize);
		for (int i=0; i<actions.length; i++)
			actions[i].apply(deck);
		
		System.out.println(deck.toString());
		if (!deck.toString().equals(result)) {
			System.err.println("ERROR! Expected: " + result);
			System.exit(100);
		}
		
		//test part B:
		for (int i=0; i<deckSize; i++) {
			long index = i;
			for (int actionIndex=actions.length-1; actionIndex>=0; actionIndex--)
				index = actions[actionIndex].getSourceIndexFor(index, deckSize);
			if (index != deck.get(i)) {
				System.err.println("Backward logic didn't work for index " + i);
				System.err.println("Expected " + deck.get(i) + " but got " + index);
				System.exit(100);
			}
		}
	}
	
	
	
}
