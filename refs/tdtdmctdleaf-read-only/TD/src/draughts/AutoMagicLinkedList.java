package draughts;

import java.util.LinkedHashMap;
import java.util.Map;

public class AutoMagicLinkedList extends LinkedHashMap<Key, Data> {
	private static final int MAX_ENTRIES = 1000;

	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > MAX_ENTRIES;
	}

	public AutoMagicLinkedList(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

}
