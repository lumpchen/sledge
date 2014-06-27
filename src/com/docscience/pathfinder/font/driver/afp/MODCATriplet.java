package com.docscience.pathfinder.font.driver.afp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxin
 *
 */
public class MODCATriplet {
		
	public static final int ID_FULLY_QUALIFIED_NAME = 0x02;
	public static final int ID_LOCAL_DATA_AND_TIME_STAMP = 0x62;
	public static final int ID_RESOURCE_MANAGEMENT = 0x63;
	public static final int ID_OBJECT_ORIGIN_IDENTIFIER = 0x64;
	public static final int ID_USER_COMMENT = 0x65;
	public static final int ID_EXTENSION_FONT = 0x6D;
	public static final int ID_METRIC_ADJUSTMENT = 0x79;
	
//	private static final int TRIPLET_BASE_LENGTH = 2;
	
//	private static final int TLENGTH_OFFSET = 0;
//	private static final int TID_OFFSET = 1;
//	private static final int TCONTENT_OFFSET = 2;

	
	private int tID;
	private MODCAByteBuffer contents = new MODCAByteBuffer();
		
	public int getTLength() {
		return contents.size() + 2;
	}
	
	public int getTID() {
		return tID;
	}
	
	public void setID(int tID) {
		this.tID = tID;
	}
	
	public int getContentsLength() {
		return contents.size();
	}
	
	public MODCAByteBuffer getContents() {
		return contents;
	}
	
	public void load(MODCAByteBuffer buffer, int offset) {
		int length = buffer.getUBIN1(offset);
		tID = buffer.getUBIN1(offset + 1);
		contents.setBytes(buffer.getBytes(offset + 2, length - 2));
	}
	
	public void save(MODCAByteBuffer buffer) {
		buffer.putUBIN1(getTLength());
		buffer.putUBIN1(getTID());
		buffer.putBytes(contents.getBytes());
	}
			
	public static MODCATriplet[] parseTriplets(MODCAByteBuffer buffer, int offset, int length) {
		List<MODCATriplet> tripletList = new ArrayList<MODCATriplet>(); 
		
		int limit = offset + length;
		while (offset < limit) {
			MODCATriplet triplet = new MODCATriplet();
			triplet.load(buffer, offset);
			offset += triplet.getTLength();
			tripletList.add(triplet);
		}
		
		return tripletList.toArray(new MODCATriplet[tripletList.size()]);
	}
	
}
