package day04;

public class Main {

	public static boolean isValid(int value) {
		
		String txt = String.valueOf(value);
		int [] digits = new int[txt.length()];
		for (int i=0; i<digits.length; i++)
			digits[i] = Integer.parseInt("" + txt.charAt(i));
		
		//enforce ascending order:
		for (int i=1; i<digits.length; i++) {
			if (digits[i] < digits[i-1])
				return false;
		}
		
		/*
		//enforce two next to each other
		boolean hasDuplicate = false;
		for (int i=1; i<digits.length; i++) {
			if (digits[i] == digits[i-1]) {
				hasDuplicate = true;
				break;
			}
		}
		
		return hasDuplicate;
		*/
		
		//enforce exactly two next to each other
		int [] counts = new int[10];
		for (int i=0; i<digits.length; i++) {
			counts[digits[i]]++;
		}
		
		for (int i=0; i<counts.length; i++) {
			if (counts[i] == 2)
				return true;
		}
		
		return false;
	}
	
	public static void main(String [] args) {
		int start = 183564;
		int end   = 657474;
		
		int count = 0;
		
		for (int i=start; i<=end; i++) {
			if (isValid(i))
				count++;
		}
		
		System.out.println(count);
	}
	
}
