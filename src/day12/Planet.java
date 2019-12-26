package day12;

public class Planet {

	public int x;
	public int y;
	public int z;
	
	public int vx;
	public int vy;
	public int vz;
	
	public Planet(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.vx = 0;
		this.vy = 0;
		this.vz = 0;
	}
	
	public int getTotalEnergy() {
		int pe = Math.abs(x) + Math.abs(y) + Math.abs(z);
		int ke = Math.abs(vx) + Math.abs(vy) + Math.abs(vz);
		return pe * ke;
	}
	
}
