package day16;

/**
 * Memory optimized version of the FFT
 */
public class FFT2 {

	byte [] state;
	byte [] cache;
	
	public FFT2(String text) {
		this.state = new byte[text.length()];
		for (int i=0; i<state.length; i++)
			state[i] = Byte.parseByte(text.substring(i,i+1));
		
		this.cache = new byte[state.length];
	}
	
	public String getText() {
		StringBuilder s = new StringBuilder(state.length);
		for (int i=0; i<state.length; i++)
			s.append(String.valueOf(state[i]));
		
		return s.toString();
	}
	
	public void apply() {
		for (int i=0; i<state.length; i++) 
			cache[i] = calculateOutputFor(i);
		
		//swap cache with state
		byte [] tmp = this.state;
		this.state = this.cache;
		this.cache = tmp;
	}
	
	public static final byte [] BASE_PATTERN = new byte [] {0, 1, 0, -1};
	
	protected byte calculateOutputFor(int index) {
		if (index > 0 && index % 100000 == 0)
			System.out.println("Index: " + index);
		
		int reps = index + 1;
		
		long sum = 0;
		
		//start with second value (second repetition of first value)
		int baseIndex = 0;
		int repIndex = 1;
		
		//unless this is index 0 where rep=1.  Then just proceed to second value
		if (index == 0) {
			baseIndex = 1;
			repIndex = 0;
		}
		
		int value, stopIndex;
		
		int j = 0;
		while (j < state.length) {
			
			value = BASE_PATTERN[baseIndex % BASE_PATTERN.length];
			
			stopIndex = Math.min(j + (reps - repIndex), state.length);
			
			if (value == 0) {
				
				j = stopIndex;
				
			} else if (value == 1) {
			
				while (j < stopIndex)
					sum += state[j++];
				
			} else if (value == -1) {
				
				while (j < stopIndex)
					sum -= state[j++];
				
			}
			
			baseIndex++;
			repIndex = 0;
		}
		
		return (byte)(Math.abs(sum) % 10);
	}

	/**
	 * Used to solve part B.  This takes advantage of the fact that we are 
	 * multiply our state vector by a matrix that looks like:
	 * 
	 *  1, 0,-1, 0, 1, 0,-1, 0
	 *  0, 1, 1, 0, 0,-1,-1, 0
	 *  0, 0, 1, 1, 1, 0, 0, 0
	 *  0, 0, 0, 1, 1, 1, 1, 0
	 *  0, 0, 0, 0, 1, 1, 1, 1
	 *  0, 0, 0, 0, 0, 1, 1, 1
	 *  0, 0, 0, 0, 0, 0, 1, 1
	 *  0, 0, 0, 0, 0, 0, 0, 1
	 *  
	 *  Notice that this is an upper triangular matrix.  The bottom left
	 *  portion is all zeroes.  Also, the bottom half of the matrix 
	 *  (beginning at row N/2+1)  has 1's in the non-zero section.  This
	 *  makes it easy to solve for the bottom part of the state vector 
	 *  that is multiplied by this part of the matrix.  You can solve it
	 *  very quickly by noting that:
	 *  
	 *    output[N] = state[N] * 1
	 *    output[N-1] = state[N] + state[N-1]
	 *  
	 *  or, recursively for i < N: 
	 *  
	 *    output[i] = output[i+1] + state[i]
	 * 
	 *  This is implemented here.  We will only update the state vector
	 *  beginning at the specified offset.
	 */
	public void applyLower(int offset) {
		if (offset < state.length / 2)
			throw new IllegalArgumentException("Offset must be larger than " + (state.length / 2) + " for calculation to be valid");
		
		//set last element:
		int i = state.length - 1;
		this.cache[i] = this.state[i];
		
		while (i > offset) {
			i--;
			this.cache[i] = (byte)((this.cache[i+1] + this.state[i]) % 10);
		}
		
		for (int j=offset; j<cache.length; j++)
			this.cache[j] = (byte)Math.abs(this.cache[j]);
		
		//swap cache with state
		byte [] tmp = this.state;
		this.state = this.cache;
		this.cache = tmp;
	}
	
	
}
