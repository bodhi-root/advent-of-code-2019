package day12;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String [] args) {
		//test();
		//solvePartA();
		solvePartB();
	}

	public static Simulation createInputSimulation() {
		Simulation sim = new Simulation();
		sim.add(new Planet(  1,   4,   4));
		sim.add(new Planet( -4,  -1,  19));
		sim.add(new Planet(-15, -14,  12));
		sim.add(new Planet(-17,   1,  10));
		return sim;
	}

	public static void solvePartA() {
		Simulation sim = createInputSimulation();
		for (int t=0; t<1000; t++)
			sim.step();
		sim.print();
		System.out.println("Energy = " + sim.getTotalEnergy());
	}
	
	/**
	 * X repeat detected: t=186028 matches t=0
     * Y repeat detected: t=231614 matches t=0
     * Z repeat detected: t=108344 matches t=0
	 */
	public static void solvePartB() {
		//solve for X:
		Simulation sim = createInputSimulation();
		int t = 0;
		
		Map<String, Integer> states = new HashMap<>(100000);
		states.put(getXStateRep(sim), t);
		
		Integer prev = null;
		while (prev == null) {
			sim.step();
			t++;
			
			prev = states.put(getXStateRep(sim), t);
		}
		
		System.out.println("X repeat detected: t=" + t + " matches t=" + prev.intValue());
	
		//solve for Y:
		sim = createInputSimulation();
		t = 0;
		
		states.clear();
		states.put(getYStateRep(sim), t);
		
		prev = null;
		while (prev == null) {
			sim.step();
			t++;
			
			prev = states.put(getYStateRep(sim), t);
		}
		
		System.out.println("Y repeat detected: t=" + t + " matches t=" + prev.intValue());
	
		//solve for Z:
		sim = createInputSimulation();
		t = 0;
		
		states.clear();
		states.put(getZStateRep(sim), t);
		
		prev = null;
		while (prev == null) {
			sim.step();
			t++;
			
			prev = states.put(getZStateRep(sim), t);
		}
		
		System.out.println("Z repeat detected: t=" + t + " matches t=" + prev.intValue());
	
		//find LCM:
		long [] cycles = new long [] {
				186028,
				231614,
				108344
		};
		long [] values = Arrays.copyOf(cycles, cycles.length);
		System.out.println(Arrays.toString(values));
		
		boolean keepGoing = true;
		while (keepGoing) {
			
			//find minimum value and increase it:
			int minIndex = 0;
			for (int i=1; i<values.length; i++) {
				if (values[i] < values[minIndex])
					minIndex = i;
			}
			values[minIndex] += cycles[minIndex];
			//System.out.println(Arrays.toString(values));
			
			//stop when they match:
			if (values[0] == values[1] &&
				values[1] == values[2])
				keepGoing = false;
		}
		
		System.out.println(values[0]);
	}
	
	/**
	 * Returns a representation of the planets' x and vx values
	 * that will help us test for uniqueness.
	 */
	protected static String getXStateRep(Simulation sim) {
		StringBuilder s = new StringBuilder();
		for (Planet planet : sim.planets)
			s.append(planet.x).append(',').append(planet.vx).append(':');
		return s.toString();
	}
	protected static String getYStateRep(Simulation sim) {
		StringBuilder s = new StringBuilder();
		for (Planet planet : sim.planets)
			s.append(planet.y).append(',').append(planet.vy).append(':');
		return s.toString();
	}
	protected static String getZStateRep(Simulation sim) {
		StringBuilder s = new StringBuilder();
		for (Planet planet : sim.planets)
			s.append(planet.z).append(',').append(planet.vz).append(':');
		return s.toString();
	}

	
	public static void test() {
		Simulation sim = new Simulation();
		sim.add(new Planet(-1, 0, 2));
		sim.add(new Planet(2, -10, -7));
		sim.add(new Planet(4, -8, 8));
		sim.add(new Planet(3, 5, -1));
		
		for (int t=0; t<10; t++)
			sim.step();
		sim.print();
		System.out.println("Energy = " + sim.getTotalEnergy());
		
		/*
		//after 1 step:
		pos=<x= 2, y=-1, z= 1>, vel=<x= 3, y=-1, z=-1>
		pos=<x= 3, y=-7, z=-4>, vel=<x= 1, y= 3, z= 3>
		pos=<x= 1, y=-7, z= 5>, vel=<x=-3, y= 1, z=-3>
		pos=<x= 2, y= 2, z= 0>, vel=<x=-1, y=-3, z= 1>
		
		//after 10 steps:
		pos=<x= 2, y= 1, z=-3>, vel=<x=-3, y=-2, z= 1>
        pos=<x= 1, y=-8, z= 0>, vel=<x=-1, y= 1, z= 3>
        pos=<x= 3, y=-6, z= 1>, vel=<x= 3, y= 2, z=-3>
        pos=<x= 2, y= 0, z= 4>, vel=<x= 1, y=-1, z=-1>
		*/
	}
	
	
}
