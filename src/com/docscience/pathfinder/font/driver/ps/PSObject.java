package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public abstract class PSObject {

	// --- simple types ---
	public static final int TYPE_NULL = 0;
	public static final int TYPE_BOOLEAN = 1;
	public static final int TYPE_INTEGER = 2;
	public static final int TYPE_REAL = 3;
	public static final int TYPE_NAME = 4;
	public static final int TYPE_OPERATOR = 5;
	public static final int TYPE_MARK = 6;
	public static final int TYPE_FONTID = 7;
	
	// --- composite types --- 
	public static final int TYPE_ARRAY = 8;
	public static final int TYPE_DICTIONARY = 9;
	public static final int TYPE_FILE = 10;
	public static final int TYPE_GSTATE = 11;
	public static final int TYPE_PACKEDARRAY = 12;
	public static final int TYPE_SAVE = 13;
	public static final int TYPE_STRING = 14;
	
	public abstract int getType();

	public boolean isComposite() {
		return false;
	}
	
	public abstract boolean isExecutable();
	
	public abstract void setExecutable(boolean value);
	
	public final boolean isNull() {
		return getType() == TYPE_NULL;
	}
	
	public final boolean isBoolean() {
		return getType() == TYPE_BOOLEAN;
	}
	
	public final boolean isNumber() {
		return isInteger() || isReal();
	}
	
	public final boolean isInteger() {
		return getType() == TYPE_INTEGER;
	}
	
	public final boolean isReal() {
		return getType() == TYPE_REAL;
	}
	
	public final boolean isName() {
		return getType() == TYPE_NAME;
	}
	
	public final boolean isOperator() {
		return getType() == TYPE_OPERATOR;
	}
	
	public final boolean isMark() {
		return getType() == TYPE_MARK;
	}
	
	public final boolean isFontID() {
		return getType() == TYPE_FONTID;
	}
	
	public final boolean isArray() {
		return getType() == TYPE_ARRAY;
	}
	
	public final boolean isDictionary() {
		return getType() == TYPE_DICTIONARY;
	}
	
	public final boolean isFile() {
		return getType() == TYPE_FILE;
	}
	
	public final boolean isGState() {
		return getType() == TYPE_GSTATE;
	}
	
	public final boolean isPackedArray() {
		return getType() == TYPE_PACKEDARRAY;
	}
	
	public final boolean isSave() {
		return getType() == TYPE_SAVE;
	}
	
	public final boolean isString() {
		return getType() == TYPE_STRING;
	}
	
}