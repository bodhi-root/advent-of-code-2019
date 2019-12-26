package day14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	static class RecipeElement {
		
		String symbol;
		int quantity;
		
		public RecipeElement(String symbol, int quantity) {
			this.symbol = symbol;
			this.quantity = quantity;
		}
		
		public String toString() {
			return String.valueOf(quantity) + " " + symbol;
		}
		public static RecipeElement parse(String text) {
			int index = text.indexOf(' ');
			int quantity = Integer.parseInt(text.substring(0, index));
			String symbol = text.substring(index+1);
			return new RecipeElement(symbol, quantity);
		}
		
	}
	
	static class Recipe {
		
		RecipeElement output;
		List<RecipeElement> inputs = new ArrayList<>();
		
		public Recipe(RecipeElement output) {
			this.output = output;
		}
		
		public void addInput(RecipeElement input) {
			inputs.add(input);
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append(inputs.get(0).toString());
			for (int i=1; i<inputs.size(); i++)
				s.append(" + ").append(inputs.get(i).toString());
				
			s.append(" ==> ").append(output.toString());
			
			return s.toString();
		}
		
	}
	
	static class Recipes {
		
		Map<String, Recipe> recipes = new HashMap<>();
		
		public void add(Recipe recipe) {
			recipes.put(recipe.output.symbol, recipe);
		}
		
		public Recipe getRecipeFor(String outputSymbol) {
			return recipes.get(outputSymbol);
		}
		
		public void loadFromFile(File file) throws IOException {
			BufferedReader in = new BufferedReader(new FileReader(file));
			try {
				String line;
				while ((line = in.readLine()) != null) {
					line = line.trim();
					if (line.isEmpty())
						continue;
					
					parseRecipeLine(line);
				}
			}
			finally {
				in.close();
			}
		}
		protected void parseRecipeLine(String line) {
			int index = line.indexOf("=>");
			String inputText = line.substring(0, index);
			String outputText = line.substring(index + 2);
			
			RecipeElement output = RecipeElement.parse(outputText.trim());
			Recipe recipe = new Recipe(output);
			
			String [] parts = inputText.split(",");
			for (String part : parts) {
				RecipeElement input = RecipeElement.parse(part.trim());
				recipe.addInput(input);
			}
			
			add(recipe);
		}
		
		public void print() {
			for (Recipe recipe : recipes.values())
				System.out.println(recipe.toString());
		}
		
	}
	
	/**
	 * Keeps track of how much we need.  A positive quantity is a need.
	 * A negative is a surplus.
	 */
	static class Counter {
		
		long quantity = 0;
		
		public void add(long value) {
			this.quantity += value;
		}
		public void subtract(long value) {
			this.quantity -= value;
		}
		
	}
	
	static class InputAnalyzer {
		
		Recipes recipes;
		Map<String, Counter> elements = new HashMap<>();
		
		public InputAnalyzer(Recipes recipes) {
			this.recipes = recipes;
		}
		
		public void clear() {
			elements.clear();
		}
		
		public long get(String symbol) {
			Counter counter = elements.get(symbol);
			return (counter == null) ? 0 : counter.quantity;
		}
		
		public long add(String symbol, long qty) {
			Counter counter = elements.get(symbol);
			if (counter == null) {
				counter = new Counter();
				elements.put(symbol, counter);
			}
			counter.add(qty);
			return counter.quantity;
		}
		
		public long subtract(String symbol, long qty) {
			return add(symbol, -qty);
		}
		
		public void breakDownToOre() {
			boolean keepGoing = true;
			while (keepGoing) {
				boolean breakDownSomething = false;
				for (Map.Entry<String, Counter> entry : elements.entrySet()) {
					
					String symbol = entry.getKey();
					long qty = entry.getValue().quantity;
					
					if (!symbol.equals("ORE") && qty > 0) {
						Recipe recipe = recipes.getRecipeFor(symbol);
						long multiple = (long)Math.ceil(((double)qty) / recipe.output.quantity);
						
						for (RecipeElement input : recipe.inputs) {
							add(input.symbol, input.quantity * multiple);
						}
						
						subtract(symbol, recipe.output.quantity * multiple);
						
						breakDownSomething = true;
						break;
					}
				}
				
				keepGoing = breakDownSomething;
			}
		}
		
	}
	
	public static void main(String [] args) {
		solvePartA();
		//solvePartB();
		//test();
	}
	
	public static void solvePartA() {
		Recipes recipes = new Recipes();
		try {
			recipes.loadFromFile(new File("files/day14/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		recipes.print();
		System.out.println();
		
		InputAnalyzer analyzer = new InputAnalyzer(recipes);
		analyzer.add("FUEL", 1);
		analyzer.breakDownToOre();
		long ore = getRequiredOreForFuel(recipes, 1);
		System.out.println(ore);
		
		//part b:
		//this is pretty brute force.  we could do a binary search that would be much faster
		long maxOre = 1000L * 1000L * 1000L * 1000L;	//1 trillion
		for (long fuel=2; fuel<=maxOre; fuel++) {
			if (getRequiredOreForFuel(recipes, fuel) > maxOre) {
				System.out.println("Max Fuel: " + (fuel-1));
				break;
			}
		}
	}
	
	public static void solvePartB() {
		long ore = 1000L * 1000L * 1000L * 1000L;
		System.out.println(ore);
		System.out.println(ore / 13312L);
		System.out.println(ore / 180697L);
		System.out.println(ore / 2210736L);
		
		//The 13312 ORE-per-FUEL example could produce 82892753 FUEL.
		//The 180697 ORE-per-FUEL example could produce 5586022 FUEL.
		//The 2210736 ORE-per-FUEL example could produce 460664 FUEL.
		
		//it couldn't be this easy, could it?
		//had to use solution at end of solvePartA instead.
	}
	
	public static long getRequiredOreForFuel(Recipes recipes, long fuelQty) {
		InputAnalyzer analyzer = new InputAnalyzer(recipes);
		analyzer.add("FUEL", fuelQty);
		analyzer.breakDownToOre();
		return analyzer.get("ORE");
	}
	
	public static void test() {
		//System.out.println(RecipeElement.parse("100 ORE").toString());
		
		Recipes recipes = new Recipes();
		try {
			recipes.loadFromFile(new File("files/day14/test.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		recipes.print();
		System.out.println();
		
		InputAnalyzer analyzer = new InputAnalyzer(recipes);
		analyzer.add("FUEL", 1);
		analyzer.breakDownToOre();
		System.out.println(analyzer.get("ORE"));
	}
	
	
}
