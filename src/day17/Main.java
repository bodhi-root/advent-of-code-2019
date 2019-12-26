package day17;

import java.io.File;
import java.util.List;

import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;

/**
 * OK, so I solved part B by hand instead of writing a program.  Part A printed out
 * this:
 * 
..................#######................................
..................#.....#................................
..................#.....#................................
..................#.....#................................
..................#.....#................................
..................#.....#................................
..................#.....#................................
..................#.....#................................
..................##########^...................#########
........................#.......................#.......#
........................#############...........#.......#
....................................#...........#.......#
....................................#...........#.......#
....................................#...........#.......#
....................................#.........###########
....................................#.........#.#........
....................................#.........#.#........
....................................#.........#.#........
....................................#############........
..............................................#..........
..............................................#..........
..............................................#..........
..............................................#..........
..............................................#..........
..............................................#..........
..............................................#..........
......................................#########..........
......................................#..................
......................................#..................
......................................#..................
......................................#..................
......................................#..................
......................................#..................
......................................#..................
..........#########...................#..................
..........#.......#...................#..................
..........#.......#...................#..................
..........#.......#...................#..................
..........#.......#...................###########........
..........#.......#.............................#........
........#############.#.........................#........
........#.#.......#.#.#.........................#........
........#.#.......###########...................#........
........#.#.........#.#.....#...................#........
###########.........#.#.....#...................#........
#.......#...........#.#.....#...................#........
#.......#...........#.#.....#...........#########........
#.......#...........#.#.....#...........#................
#.......#...........#############.......#................
#.......#.............#.....#...#.......#................
#########.............#######...#.......#................
................................#.......#................
................................#.......#................
................................#.......#................
................................#.......#................
................................#.......#................
................................#########................
 * 
 * The path we had to take was:
 * 
 *  L,10,R,8,R,6,R,10,L,12,R,8,L,12,L,10,R,8,R,6,R,10,L,12,R,8,L,12,L,10,R,8,R,8,
 *  L,10,R,8,R,8,L,12,R,8,L,12,L,10,R,8,R,6,R,10,L,10,R,8,R,8,L,10,R,8,R,6,R,10
 * 
 * Finding the repeating parts, lets us break this up as:
 * 
 * L,10,R,8,R,6,R,10,  (A)
 * L,12,R,8,L,12,      (B)
 * L,10,R,8,R,6,R,10,  (A)
 * L,12,R,8,L,12,      (B)
 * L,10,R,8,R,8,       (C)
 * L,10,R,8,R,8,       (C)
 * L,12,R,8,L,12,      (B)
 * L,10,R,8,R,6,R,10,  (A)
 * L,10,R,8,R,8,       (C)
 * L,10,R,8,R,6,R,10   (A)
 * 
 * This gives us the input for the program in part B.
 *
 */
public class Main {

	public static void main(String [] args) {
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartB() {
		Computer computer = new Computer(1024 * 1024);
		try {
			computer.loadProgramFromFile(new File("files/day17/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		ComputerOutput.PrintAndSave output = new ComputerOutput.PrintAndSave();
		computer.setOutput(output);
		computer.setInput(new AsciiInput(
				"A,B,A,B,C,C,B,A,C,A\n" +  //main input
				"L,10,R,8,R,6,R,10\n" + 	//routine A
				"L,12,R,8,L,12\n" + 		//routine B
				"L,10,R,8,R,8\n" + 			//routine C
				"n\n" 					    // 'n' for continuous video
				));
		
		computer.set(0, 2);
		computer.runProgram();
	}
	
	public static class AsciiInput implements ComputerInput {

		char [] chars;
		int nextIndex = 0;
				
		public AsciiInput(String text) {
			this.chars = text.toCharArray();
		}
		
		@Override
		public long getInput() {
			return (long)this.chars[this.nextIndex++];
		}
		
	}
	
	public static void solvePartA() {
		Computer computer = new Computer(1024 * 1024);
		try {
			computer.loadProgramFromFile(new File("files/day17/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		ComputerOutput.PrintAndSave output = new ComputerOutput.PrintAndSave();
		computer.setOutput(output);
		
		computer.runProgram();
		
		List<Long> values = output.getValues();
		int width = 0;
		int maxWidth = 0;
		int height = 0;
		for (long value : values) {
			if (value == 35) {
				System.out.print('#');
				width++;
			}
			else if (value == 46) {
				System.out.print('.');
				width++;
			}
			else if (value == 10) {
				System.out.println();
				maxWidth = Math.max(maxWidth, width);
				height++;
				width = 0;
			}
			else
				System.out.print('?');
		}
		
		System.out.println();
		
		Image image = Image.parseImage(values, height, maxWidth);
		image.print();
		
		System.out.println(image.getSumOfAlignmentParams());
		
	}
	
	static class Image {
		
		char [][] image;
		int height;
		int width;
		
		public Image(char [][] image) {
			this.image = image;
			this.height = image.length;
			this.width = image[0].length;
		}
		
		public void print() {
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					System.out.print(image[i][j]);
				}
				System.out.println();
			}
		}
		
		public static Image parseImage(List<Long> values, int height, int width) {
			
			char [][] imageData = new char[height][width];
			int i=0, j=0;
			for (long value : values) {
				if (value == 35) {
					imageData[i][j] = '#';
					j++;
				}
				else if (value == 46) {
					imageData[i][j] = '.';
					j++;
				}
				else if (value == 10) {
					i++;
					j=0;
				} else {
					//imageData[i][j] = '?';
					imageData[i][j] = (char)value;
					j++;
				}
			}
			
			return new Image(imageData);
		}
		
		public int getSumOfAlignmentParams() {
			
			int sum = 0;
			for (int i=1; i<height-1; i++) {
				for (int j=1; j<width-1; j++) {
					if (image[i][j] == '#' &&
					    image[i-1][j] == '#' &&
					    image[i+1][j] == '#' &&
					    image[i][j+1] == '#' &&
					    image[i][j-1] == '#') {
						sum += (i * j);
					}
				}
			}
			
			return sum;
		}
		
	}
	
}
