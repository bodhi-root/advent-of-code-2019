package day13;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import day09.ComputerInput;
import day09.ComputerOutput;

public class Game {

	public static final int EMPTY = 0;
	public static final int WALL = 1;
	public static final int BLOCK = 2;
	public static final int PADDLE = 3;
	public static final int BALL = 4;
	
	int [][] screen;
	int height;
	int width;
	
	long score = 0;
	
	public Game(int height, int width) {
		this.screen = new int[height][width];
		this.height = height;
		this.width = width;
	}
	
	public void print() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (screen[i][j] == 0)
					System.out.print(' ');
				else
					System.out.print(screen[i][j]);
			}
			System.out.println();
		}
		System.out.println("SCORE: " + score);
	}
	
	public int getTileCount(int tileId) {
		int count = 0;
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (screen[i][j] == tileId)
					count++;
			}
		}
		
		return count;
	}
	
	public int getColumnFor(int tileId) {
		for (int j=0; j<width; j++) {
			for (int i=0; i<height; i++) {
				if (screen[i][j] == tileId)
					return j;
			}
		}
		
		return -1;
	}
	
	
	public GameIO getIO() {
		return new GameIO(this);
	}
	
	static class GameIO implements ComputerOutput, ComputerInput {
		
		Game game;
		boolean autoPlayer = true;
		
		long [] cache = new long[3];
		int nextIndex = 0;
		
		public GameIO(Game game) {
			this.game = game;
		}
		
		public void write(long value) {
			cache[nextIndex++] = value;
			if (nextIndex >= cache.length) {
				if (cache[0] == -1 && cache[1] == 0)
					game.score = cache[2];
				else
					game.screen[(int)cache[1]][(int)cache[0]] = (int)cache[2];
				nextIndex = 0;
			}
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		public long getInput() {
			if (autoPlayer) {
				
				if (game.getTileCount(Game.BLOCK) == 0) {
					game.print();
					System.out.println("GAME OVER! YOU WIN!");
					System.out.print("Press <ENTER> to continue");
					try {
						in.readLine();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				int ballColumn = game.getColumnFor(Game.BALL);
				int paddleColumn = game.getColumnFor(Game.PADDLE);
			    return (int)Math.signum(ballColumn - paddleColumn);
			}
			else {
				//manual player (1=left, 2=stay, 3=right):
				while(true) {
					game.print();
					System.out.print("INPUT (1,2,3): ");
					try {
						int answer = Integer.parseInt(in.readLine());
						return answer - 2;
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}	
			}
		}
		
	}
	
}
