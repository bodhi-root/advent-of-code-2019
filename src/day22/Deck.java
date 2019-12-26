package day22;

public class Deck {

	int [] cards;
	
	int [] cache;
	
	public Deck(int size) {
		this.cards = new int[size];
		for (int i=0; i<size; i++)
			cards[i] = i;
		
		this.cache = new int[size];
	}
	
	public int get(int index) {
		return cards[index];
	}
	public int indexOf(int value) {
		for (int i=0; i<cards.length; i++) {
			if (cards[i] == value)
				return i;
		}
		return -1;
	}
	protected void swapCache() {
		int [] tmp = this.cards;
		this.cards = this.cache;
		this.cache = tmp;
	}
	
	/**
	 * Reverse the array
	 */
	public void reverse() {
		for (int i=0; i<cards.length; i++)
			cache[i] = cards[cards.length-i-1];
		swapCache();
	}
	
	public void dealCut(int n) {
		if (n < 0)
			n = cards.length + n;
		
		if (n > 0) {
			for (int i=0; i<n; i++)
				cache[cache.length-n+i] = cards[i];
			for (int i=n; i<cards.length; i++)
				cache[i-n] = cards[i];
		}
		else if (n < 0) {
			throw new IllegalStateException("Should not happen");
		}
		else {
			throw new IllegalArgumentException("n cannot be zero");
		}
		swapCache();
	}
	
	public void dealIncrement(int n) {
		int nextIndex = 0;
		for (int i=0; i<cards.length; i++) {
			cache[nextIndex] = cards[i];
			nextIndex += n;
			if (nextIndex >= cards.length)
				nextIndex -= cards.length;
		}
		swapCache();
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(cards[0]);
		for (int i=1; i<cards.length; i++)
			s.append(' ').append(cards[i]);
		return s.toString();
	}
	
}
