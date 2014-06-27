package com.docscience.pathfinder.font.driver.ps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wxin
 *
 */
public class PSArray extends PSCompositeObject {

	private final List<PSObject> objects;
	private boolean executable;
	private boolean indexFormat;
	
	public PSArray() {
		objects = new ArrayList<PSObject>();
	}
	
	public PSArray(int capacity) {
		objects = new ArrayList<PSObject>(capacity);
	}
	
	public void setIndexFormat(boolean b) {
		indexFormat = b;
	}
	
	public boolean isIndexFormat() {
		return indexFormat;
	}
	
	public static final PSArray convert(int[] values) {
		PSArray array = new PSArray(values.length);
		for (int i=0; i<values.length; ++i) {
			array.add(PSInteger.valueOf(values[i]));
		}
		return array;
	}
	
	public static final PSArray convert(double[] values) {
		PSArray array = new PSArray(values.length);
		for (int i=0; i<values.length; ++i) {
			array.add(new PSReal(values[i]));
		}
		return array;
	}

	public static final PSArray convert(PSObject[] objects) {
		PSArray array = new PSArray(objects.length);
		for (int i=0; i<objects.length; ++i) {
			array.add(objects[i]);
		}
		return array;
	}
	
	public int size() {
		return objects.size();
	}
	
	public PSObject get(int index) {
		return objects.get(index);
	}
	
	public void add(PSObject obj) {
		objects.add(obj);
	}

	public void add(int index, PSObject object) {
		objects.add(index, object);
	}

	public void remove(int idx) {
		objects.remove(idx);
	}
	
	public int indexOf(PSObject obj) {
		return objects.indexOf(obj);
	}
	
	@Override
	public int getType() {
		return TYPE_ARRAY;
	}

	@Override
	public boolean isExecutable() {
		return executable;
	}
	
	@Override
	public void setExecutable(boolean executable) {
		this.executable = executable;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (executable) {
			sb.append("{");
		}
		else {
			sb.append("[");
		}
		for (int i=0; i<objects.size(); ++i) {
			PSObject obj = objects.get(i);
			sb.append(obj.toString());
			if (i != objects.size() - 1) {
				sb.append(" ");
			}
		}
		if (executable) {
			sb.append("}");
		}
		else {
			sb.append("]");
		}
		return sb.toString();
	}

	public void set(int index, PSObject object) {
		objects.set(index, object);
	}

	public PSObject[] toArray() {
		return objects.toArray(new PSObject[objects.size()]);
	}
	
	public double[] toDoubleArray() {
		double[] doubleArray = new double[objects.size()];
		int i = 0;
		Iterator<PSObject> itr = objects.iterator();
		while (itr.hasNext()) {
			PSObject psObject = itr.next();
			if (!psObject.isNumber()) {
				return null;
			}
			else {
				PSNumber number = (PSNumber) psObject;
				doubleArray[i++] = number.doubleValue();
			}
		}
		return doubleArray;
	}
	
	public int[] toIntegerArray() {
		int[] intArray = new int[objects.size()];
		int i = 0;
		Iterator<PSObject> itr = objects.iterator();
		while (itr.hasNext()) {
			PSObject psObject = itr.next();
			if (!psObject.isInteger()) {
				return null;
			}
			else {
				PSInteger number = (PSInteger) psObject;
				intArray[i++] = number.intValue();
			}
		}
		return intArray;		
	}
}
