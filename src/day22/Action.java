package day22;

public interface Action {

	/**
	 * Applies the action to the deck
	 */
	void apply(Deck deck);
	
	/**
	 * Returns the index of the card that will end up in the given index
	 * after this action is applied.  This is used for working backward
	 * from a final index without actually doing the shuffle.
	 */
	long getSourceIndexFor(long index, long deckLength);

	// --- Static Methods & Classes -------------------------------------------
	
	public static Action parse(String line) {
		if (line.equals("deal into new stack"))
			return REVERSE;
		else if (line.startsWith("deal with increment ")) {
			int n = Integer.parseInt(line.substring("deal with increment ".length()));
			return new DealIncrement(n);
		}
		else if (line.startsWith("cut ")) {
			int n = Integer.parseInt(line.substring("cut ".length()));
			return new DealCut(n);
		} else {
			throw new IllegalArgumentException("Unrecognized command: " + line);
		}
	}
	
	public static Action REVERSE = new Action() {
		public void apply(Deck deck) {
			deck.reverse();
		}
		public long getSourceIndexFor(long index, long deckLength) {
			return deckLength - index - 1;
		}
	};
	
	public static class DealCut implements Action {

		int n;
		
		public DealCut(int n) {
			this.n = n;
		}
		
		public void apply(Deck deck) {
			deck.dealCut(n);
		}
		public long getSourceIndexFor(long index, long deckLength) {
			long nLong = (long)n;
			if (nLong < 0)
				nLong = deckLength + nLong;
			
			if (index < deckLength - nLong)
				return index + nLong;
			else
				return index + nLong - deckLength;
		}
		
	}
	
	public static class DealIncrement implements Action {

		int n;
		
		public DealIncrement(int n) {
			this.n = n;
		}
		
		public void apply(Deck deck) {
			deck.dealIncrement(n);
		}
		
		public long getSourceIndexFor(long index, long deckLength) {
			
			while (index % n > 0)
				index += deckLength;

			return index / n;
		}
		
	}
	
}
