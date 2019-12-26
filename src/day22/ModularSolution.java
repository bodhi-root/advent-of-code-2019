package day22;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import common.Util;

/**
 * I never would have figured this out myself.  I had to grab some code from the reddit 
 * help board.
 * 
 * Based on the python solution at:
 * https://topaz.github.io/paste/#XQAAAQAgBQAAAAAAAAAzHIoib6pENkSmUIKIED8dy140D1lKWSMhNhZz+hjKgIgfJKPuwdqIBP14lxcYH/qI+6TyUGZUnsGhS4MQYaEtf9B1X3qIIO2JSejFjoJr8N1aCyeeRSnm53tWsBtER8F61O2YFrnp7zwG7y303D8WR4V0eGFqtDhF/vcF1cQdZLdxi/WhfyXZuWC+hs8WQCBmEtuId6/G0PeMA1Fr78xXt96Um/CIiLCievFE2XuRMAcBDB5We73jvDO95Cjg0CF2xgF4yt3v4RB9hmxa+gmt6t7wRI4vUIGoD8kX2k65BtmhZ7zSZk1Hh5p1obGZ6nuuFIHS7FpuSuv1faQW/FuXlcVmhJipxi37mvPNnroYrDM3PFeMw/2THdpUwlNQj0EDsslC7eSncZQPVBhPAHfYojh/LlqSf4DrfsM926hSS9Fdjarb9xBYjByQpAxLDcmDCMRFH5hkmLYTYDVguXbOCHcY+TFbl+G/37emZRFh/d+SkeGqbFSf64HJToM2I7N2zMrWP7NDDY5FWehD5gzKsJpEg34+sG7x2O82wO39qBlYHcYg1Gz4cLBrH1K1P+KWvEdcdj/NBtrl6yftMlCu6pH4WTGUe9oidaiRuQZOGtw71QsTQUuhpdoWO4mEH0U9+CiPZCZLaQolFDSky1J9nDhZZHy3+ETcUeDOfSu+HI3WuKC0AtIRPdG8B9GhtxZQKAx+5kyi/ek7A2JAY9SjrTuvRADxx5AikbHWXIsegZQkupAc2msammSkwY8dRMk0ilf5vh6kR0jHNbSi0g0KJLCJfqggeX24fKk5Mdh8ULZXnMfMZOmwEGfegByYbu91faLijfW4hoXCB1nlsWTPZEw2PCZqqhl9oc1q25H2YkkvKLxEZWl6a9eFuRzxhB840I1zdBjUVgfKd9/V4VdodzU2Z2e+VEh7RbJjQNFC/rG8dg==
 * (code included below).
 * 
 * I tried using the C++ version at: https://github.com/vilya/AdventOfCode-2019/blob/master/day22/day22a.cpp
 * This just did not give me the right answer (even with several changes/fixes I made).
 * I think Java's long is just not big enough for these calculations.  The C code is using
 * an int128 which is twice as big as the 64-bit long in Java.  I had to re-write the code
 * to use BigInteger instead.
 */
public class ModularSolution {

	public static final BigInteger ZERO = BigInteger.ZERO;
	public static final BigInteger ONE = BigInteger.ONE;
	public static final BigInteger TWO = BigInteger.valueOf(2);
	
	BigInteger numCards;
	BigInteger A;
	BigInteger B;
	
	public ModularSolution(BigInteger numCards) {
		this.numCards = numCards;
	}
	
	public static void main(String [] args) {
		solvePartA();
		solvePartB();
	}
	
	public static void solvePartA() {
		
		BigInteger numCards = new BigInteger("10007");
		ModularSolution app = new ModularSolution(numCards);
		app.loadShuffleCoefficients(new File("files/day22/input.txt"));
		
		BigInteger startPos = new BigInteger("2019");
		BigInteger nextPos = app.getFinalPositionOfCard(startPos);
		
		System.out.println("Card " + startPos + " is at index " + nextPos + " after shuffle");
	}
	
	public static void solvePartB() {
		
		BigInteger numCards    = new BigInteger("119315717514047");
		BigInteger numShuffles = new BigInteger("101741582076661");
		BigInteger finalPos    = new BigInteger("2020");
		
		ModularSolution app = new ModularSolution(numCards);
		app.loadShuffleCoefficients(new File("files/day22/input.txt"));
		BigInteger startPos = app.getStartingPositionOfCard(finalPos, numShuffles);
		System.out.println("The card ending at position + " + finalPos + " started at " + startPos);
	}
	
	/**
	 * Loads the coefficients A and B.  The position of card 'c' after one
	 * shuffle can be calculated with: (A * c + B) % numCards
	 */
	public void loadShuffleCoefficients(File file) {
		
		//read in file:
		List<String> lines;
		try {
			lines = Util.readLinesFromFile(file);
		}
		catch(IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(lines.size() + " lines read from file");
		
		//convert to actions:
		Action [] actions = new Action[lines.size()];
		for (int i=0; i<lines.size(); i++) {
			String line = lines.get(i);
			actions[i] = Action.parse(line);
		}
		
		//convert to coefficients:
		BigInteger A = ONE;
		BigInteger B = ZERO;
		
		for (Action action : actions) {
			BigInteger a, b;
			if (action instanceof Action.DealCut) {
				BigInteger n = BigInteger.valueOf(((Action.DealCut)action).n);
				a = ONE;
				b = numCards.subtract(n); 
			}
			else if (action instanceof Action.DealIncrement) {
				BigInteger n = BigInteger.valueOf(((Action.DealIncrement)action).n);
				a = n;
				b = ZERO;
			}
			else {
				a = ONE.negate();
				b = numCards.subtract(ONE);
			}
			
			A = a.multiply(A).mod(numCards);
			B = a.multiply(B).add(b).mod(numCards);
		}
		
		//System.out.println("A = " + A);
		//System.out.println("B = " + B);
		
		this.A = A;
		this.B = B;
	}
	
	/**
	 * Returns the position of the given card after 1 shuffle
	 */
	public BigInteger getFinalPositionOfCard(BigInteger card) {
		return A.multiply(card).add(B).mod(numCards);
	}
	
	public BigInteger getStartingPositionOfCard(BigInteger finalPos, BigInteger numShuffles) {
			
		//get coefficients that represent 'numShuffles' shuffles
		
		//Ma = pow(a, M, n)
		//Mb = (b * (Ma - 1) * inv(a-1, n)) % n
		
		BigInteger Ma = A.modPow(numShuffles,  numCards);
		BigInteger Mb = B.multiply((Ma.subtract(ONE)).multiply(inv(A.subtract(ONE), numCards))).mod(numCards);

		//We could solve "where does 2020 end up" with:
		//  finalPos = (Ma * startPos + Mb) % n
		//but I want "what is at 2020".  So we need to invert this and get:
		//  startPos = ((finalPos - Mb) * inv(Ma, n)) % n
		BigInteger answer = finalPos.subtract(Mb).multiply(inv(Ma, numCards)).mod(numCards);
		if (answer.signum() < 0)
			answer = answer.add(numCards);
		
		return answer;
		
		//System.out.println("Actual answer is 4893716342290");           
	}

	static BigInteger inv(BigInteger a, BigInteger n) {
		return a.modPow(n.subtract(TWO), n);
	}
	
	// The python code below was very helpful:
	/*
	from __future__ import division, print_function
	import fileinput

	n = 119315717514047
	c = 2020

	a, b = 1, 0
	for l in fileinput.input():
	    if l == 'deal into new stack\n':
	        la, lb = -1, -1
	    elif l.startswith('deal with increment '):
	        la, lb = int(l[len('deal with increment '):]), 0
	    elif l.startswith('cut '):
	        la, lb = 1, -int(l[len('cut '):])
	    # la * (a * x + b) + lb == la * a * x + la*b + lb
	    # The `% n` doesn't change the result, but keeps the numbers small.
	    a = (la * a) % n
	    b = (la * b + lb) % n

	M = 101741582076661
	# Now want to morally run:
	# la, lb = a, b
	# a = 1, b = 0
	# for i in range(M):
	#     a, b = (a * la) % n, (la * b + lb) % n

	# For a, this is same as computing (a ** M) % n, which is in the computable
	# realm with fast exponentiation.
	# For b, this is same as computing ... + a**2 * b + a*b + b
	# == b * (a**(M-1) + a**(M) + ... + a + 1) == b * (a**M - 1)/(a-1)
	# That's again computable, but we need the inverse of a-1 mod n.

	# Fermat's little theorem gives a simple inv:
	def inv(a, n): return pow(a, n-2, n)

	Ma = pow(a, M, n)
	Mb = (b * (Ma - 1) * inv(a-1, n)) % n

	# This computes "where does 2020 end up", but I want "what is at 2020".
	#print((Ma * c + Mb) % n)

	# So need to invert (2020 - MB) * inv(Ma)
	print(((c - Mb) * inv(Ma, n)) % n)
	*/

}
