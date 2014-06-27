package com.docscience.pathfinder.font.driver.ps;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author wxin
 *
 */
public class PSDictionary extends PSCompositeObject {

	private static class KeyComparator implements Comparator<PSObject> {
		@Override
		public int compare(PSObject o1, PSObject o2) {
			if (o1 instanceof PSName) {
				if (o2 instanceof PSName) {
					PSName name0 = (PSName) o1;
					PSName name1 = (PSName) o2;
					return name0.getName().compareTo(name1.getName());
				}
				else {
					return 1; // name always big than integer
				}
			}
			if (o1 instanceof PSInteger) {
				if (o2 instanceof PSInteger) {
					int i0 = ((PSInteger) o1).intValue();
					int i1 = ((PSInteger) o2).intValue();
					if (i0 < i1) {
						return -1;
					}
					else if (i0 > i1) {
						return 1;
					}
					else {
						return 0;
					}
				}
				else {
					return -1;
				}
			}
			
			return -1;
		}
	}
	
	private static final KeyComparator comparator = new KeyComparator();
	
	private SortedMap<PSObject, PSObject> map = new TreeMap<PSObject, PSObject>(comparator);
	private int capacity = 0;
	
	public int size() {
		return map.size();
	}
	
	public int getCapacity() {
		if (capacity < map.size()) {
			capacity = map.size();
		}
		return capacity;
	}
	
	public void setCapacity(int c) {
		if (c < map.size()) {
			c = map.size();
		}
		capacity = c;
	}
	
	public PSObject get(PSObject key) {
		if (!key.isName() && !key.isInteger()) {
			throw new IllegalArgumentException("key is not name or integer");
		}
		return map.get(key);
	}
	
	public void put(PSObject key, PSObject value) {
		if (!key.isName() && !key.isInteger()) {
			throw new IllegalArgumentException("key is not name or integer");
		}
		map.put(key, value);
		if (capacity < map.size()) {
			capacity = map.size();
		}
	}
	
	public void clear() {
		map.clear();
	}
	
	public boolean contains(PSObject key) {
		return map.containsKey(key);
	}
	
	public PSObject remove(PSObject key) {
		if (!key.isName() && !key.isInteger()) {
			throw new IllegalArgumentException("key is not name or integer");
		}
		return map.remove(key);
	}
	
	public Set<Entry<PSObject, PSObject>> entrySet() {
		return map.entrySet();
	}
	
	@Override
	public int getType() {
		return TYPE_DICTIONARY;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<<");
		Iterator<Entry<PSObject, PSObject>> itr = map.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<PSObject, PSObject> entry = itr.next();
			sb.append(entry.getKey().toString());
			sb.append(" ");
			sb.append(entry.getValue().toString());
			if (itr.hasNext()) {
				sb.append(" ");
			}
		}
		sb.append(">>");
		return sb.toString();
	}

	@Override
	public boolean isExecutable() {
		return false;
	}

	@Override
	public void setExecutable(boolean value) {
		// do nothing here
	}
}
