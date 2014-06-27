package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class TResourceManagement {
	
	public static final int TYPE_CRC_RESOURCE_MANAGEMENT = 1;
	public static final int TYPE_FONT_RESOURCE_MANAGEMENT = 2;
	
	public static final int RESOUCE_CLASS_FLAG_PUBLIC_OR_PRIVATE = 0x80;
	
	private int type;
	private int retiredResouceManagementValue;
	private int resourceClassFlag;

	public void setTriplet(MODCATriplet triplet) {
		if (triplet.getTID() != MODCATriplet.ID_RESOURCE_MANAGEMENT) {
			throw new IllegalArgumentException("invalid triplet id for resource management");
		}
		MODCAByteBuffer contents = triplet.getContents();
		type = contents.getUBIN1(0);
		if (type == TYPE_CRC_RESOURCE_MANAGEMENT) {
			retiredResouceManagementValue = contents.getUBIN2(1);
			resourceClassFlag = contents.getUBIN1(3);
		}
		else {
			// DO NOTHING HERE, WE DON'T SUPPORT READ OTHER TYPES FOR NOW
		}
	}
	
	public MODCATriplet getTriplet() {
		MODCATriplet triplet = new MODCATriplet();
		triplet.setID(MODCATriplet.ID_RESOURCE_MANAGEMENT);
		MODCAByteBuffer contents = triplet.getContents();
		contents.putUBIN1(type);
		if (type == TYPE_CRC_RESOURCE_MANAGEMENT) {
			contents.putUBIN2(retiredResouceManagementValue);
			contents.putUBIN1(resourceClassFlag);
		}
		else {
			assert(false) : "not supported";
		}
		return triplet;
	}
	
}
