package day16;

public class FFT {

	int [] state;
	
	public FFT(String text) {
		this.state = new int[text.length()];
		for (int i=0; i<state.length; i++)
			state[i] = Integer.parseInt(text.substring(i,i+1));
	}
	
	public String getText() {
		StringBuilder s = new StringBuilder(state.length);
		for (int i=0; i<state.length; i++)
			s.append(String.valueOf(state[i]));
		
		return s.toString();
	}
	
	public void apply() {
		int [] output = new int[state.length];
		for (int i=0; i<state.length; i++) {
			int [] pattern = createPattern(i);
			long sum = 0;
			for (int j=0; j<state.length; j++) {
				sum += state[j] * pattern[j];
			}
			output[i] = (int)(Math.abs(sum) % 10);
		}
		this.state = output;
	}
	
	/**
	 * Creates the pattern for the give index.  This is horribly inefficient, but
	 * it works.
	 */
	protected int [] createPattern(int index) {
		
		int [] basePattern = new int [] {0, 1, 0, -1};
		int reps = index + 1;
		
		//replicate values in basePattern 'reps' times
		int [] repPattern = new int [basePattern.length * reps];
		for (int i=0; i<basePattern.length; i++) {
			for (int j=0; j<reps; j++) {
				repPattern[i*reps+j] = basePattern[i];
			}
		}
		
		//repeat pattern to be of length state.length+1
		int [] expandedPattern = new int[state.length+1];
		for (int i=0; i<expandedPattern.length; i++)
			expandedPattern[i] = repPattern[i % repPattern.length];
		
		//drop first value
		int [] finalPattern = new int[state.length];
		System.arraycopy(expandedPattern, 1, finalPattern, 0, finalPattern.length);
		
		return finalPattern;
	}
	
}
