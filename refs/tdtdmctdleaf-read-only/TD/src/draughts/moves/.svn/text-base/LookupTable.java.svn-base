package draughts.moves;

import java.util.LinkedHashMap;
import java.util.Map;

import draughts.AutoMagicLinkedList;
import draughts.Data;
import draughts.Key;

public class LookupTable {

	private Map<Key, Data> lookupTable = new AutoMagicLinkedList(100,0.75f,true);
	private int cacheHit = 0, cacheMiss = 0;

	public Data lookup(Node node, int depth) {
		Data d = lookupTable.get(new Key(node, depth));
		if (d != null) {
			cacheHit++;
		} else {
			cacheMiss++;
		}
		return d;
	}

	public void storeUpper(Node node, int depth, double upper) {
		Key key = new Key(node, depth);
		Data data = lookupTable.get(key);
		if (data == null) {
			data = new Data();
			lookupTable.put(key, data);
		}
		data.setUpper(upper);
	}

	public void storeLower(Node node, int depth, double lower) {
		Key key = new Key(node, depth);
		Data data = lookupTable.get(key);
		if (data == null) {
			data = new Data();
			lookupTable.put(key, data);
		}
		data.setLower(lower);
	}
}


