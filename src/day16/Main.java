package day16;

import java.io.File;

public class Main {

	public static void main(String [] args) {
		
		tests();
		//solvePartA();
		solvePartB();
		
	}
	
	public static void tests() {
		FFT2 fft = new FFT2("12345678");
		assertEquals("12345678", fft.getText());
		fft.apply();
		assertEquals("48226158", fft.getText());
		fft.apply();
		assertEquals("34040438", fft.getText());
		fft.apply();
		assertEquals("03415518", fft.getText());
		fft.apply();
		assertEquals("01029498", fft.getText());
		
		test("80871224585914546619083218645595", "24176176");
		test("19617804207202209144916044189917", "73745418");
		test("69317163492948606335995924319873", "52432133");
		
		testFull("80871224585914546619083218645595", "24176176480919046114038763195595");
		testFull("19617804207202209144916044189917", "73745418557257259149466599639917");
		testFull("69317163492948606335995924319873", "52432133292998606880495974869873");
		
		testLower("80871224585914546619083218645595", "6114038763195595");
		testLower("19617804207202209144916044189917", "9149466599639917");
		testLower("69317163492948606335995924319873", "6880495974869873");
	}
	
	public static void test(String input, String output8) {
		FFT2 fft = new FFT2(input);
		for (int i=0; i<100; i++)
			fft.apply();
		
		assertEquals(fft.getText().substring(0, 8), output8);
	}

	public static void testFull(String input, String output) {
		FFT2 fft = new FFT2(input);
		for (int i=0; i<100; i++)
			fft.apply();
		
		assertEquals(fft.getText(), output);
	}
	
	public static void testLower(String input, String output) {
		FFT2 fft = new FFT2(input);
		for (int i=0; i<100; i++)
			fft.applyLower(input.length() / 2);
		
		assertEquals(fft.getText().substring(input.length() / 2), output);
	}
	
	public static void assertEquals(String text1, String text2) {
		if (!text1.equals(text2))
			throw new RuntimeException("Assertion failed: " + text1 + " != " + text2);
	}
	
	public static void solvePartA() {
		try {
			String input = common.Util.readLineFromFile(new File("files/day16/input.txt"));
			FFT fft = new FFT(input);
			for (int i=0; i<100; i++)
				fft.apply();
			
			System.out.println(fft.getText().substring(0, 8));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * File has 650 characters
	 * x 10,000 = 6.5 million characters (6.5 MB)
	 * 
	 */
	public static void solvePartB() {
		try {
			//repeat file text 10,000 times
			String fileText = common.Util.readLineFromFile(new File("files/day16/input.txt"));
			int offset = Integer.parseInt(fileText.substring(0,7));
			
			StringBuilder input = new StringBuilder();
			for (int i=0; i<10000; i++)
				input.append(fileText);
			
			//run FFT 100 times:
			System.out.println(java.time.LocalDateTime.now());
			
			FFT2 fft = new FFT2(input.toString());
			for (int i=0; i<100; i++) {
				System.out.println(i);
				fft.applyLower(offset);
			}
			
			System.out.println("SOLUTION: " + fft.getText().substring(offset, offset+8));
			System.out.println(java.time.LocalDateTime.now());
			
			//started at: 2019-12-17T19:11:41.928
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
