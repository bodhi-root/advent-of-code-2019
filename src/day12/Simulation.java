package day12;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

	List<Planet> planets = new ArrayList<>();
	
	public void add(Planet planet) {
		planets.add(planet);
	}
	
	public void print() {
		for (Planet planet : planets) {
			System.out.println("pos=<x=" + planet.x + ", y=" + planet.y + ", z=" + planet.z + ">, " +
					           "vel=<x=" + planet.vx + ", y=" + planet.vy + ", z=" + planet.vz + ">");
		}
	}
	
	public void step() {
		
		//update velocities based on interactions:
		for (int i=0; i<planets.size(); i++) {
			for (int j=(i+1); j<planets.size(); j++) {
				
				Planet p1 = planets.get(i);
				Planet p2 = planets.get(j);
				
				if (p1.x > p2.x) {
					p1.vx--;
					p2.vx++;
				} else if (p1.x < p2.x) {
					p1.vx++;
					p2.vx--;
				}
				
				if (p1.y > p2.y) {
					p1.vy--;
					p2.vy++;
				} else if (p1.y < p2.y) {
					p1.vy++;
					p2.vy--;
				}
				
				if (p1.z > p2.z) {
					p1.vz--;
					p2.vz++;
				} else if (p1.z < p2.z) {
					p1.vz++;
					p2.vz--;
				}
			}
		}
		
		//update positions:
		for (Planet planet : planets) {
			planet.x += planet.vx;
			planet.y += planet.vy;
			planet.z += planet.vz;
		}
	}
	
	public int getTotalEnergy() {
		int sum = 0;
		for (Planet planet : planets)
			sum += planet.getTotalEnergy();
		
		return sum;
	}
	
}
