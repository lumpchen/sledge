package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class TFullyQualifiedName {
	
	public static final int TYPE_FAMILY_NAME = 0x07;
	public static final int TYPE_TYPEFACE_NAME = 0x08;
	
	private int fqnType;
	private String fqnName;
	
	public int getFQNType() {
		return fqnType;
	}
	
	public String getFQNName() {
		return fqnName;
	}
	
	public void setFQNType(int type) {
		if (type != TYPE_FAMILY_NAME	&& type != TYPE_TYPEFACE_NAME) {
			throw new IllegalArgumentException("invalid FQN type: " + type);
		}
		fqnType = type;
	}
	
	public void setFQNName(String name) {
		fqnName = name;
	}
	
	public void setTriplet(MODCATriplet triplet) {
		if (triplet.getTID() != MODCATriplet.ID_FULLY_QUALIFIED_NAME) {
			throw new IllegalArgumentException("invalid triplet id for fully qualified name");
		}
		fqnType = triplet.getContents().getUBIN1(0);
		if (fqnType != TYPE_FAMILY_NAME	&& fqnType != TYPE_TYPEFACE_NAME) {
			throw new IllegalArgumentException("invalid fully qualified name type: " + fqnType);
		}
		fqnName = triplet.getContents().getString(2, triplet.getContentsLength() - 2).trim();
	}

	public MODCATriplet getTriplet() {
		assert(fqnType == TYPE_FAMILY_NAME || fqnType == TYPE_TYPEFACE_NAME);
		MODCATriplet triplet = new MODCATriplet();
		triplet.setID(MODCATriplet.ID_FULLY_QUALIFIED_NAME);
		triplet.getContents().putUBIN1(fqnType);
		triplet.getContents().putUBIN1(0); // reserved byte
		triplet.getContents().putString(fqnName);
		return triplet;
	}

}
