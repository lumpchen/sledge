package com.docscience.pathfinder.font.driver.afp;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author wxin
 *
 */
public class SFFontNameMap { 
		
	public static final int MAX_FONT_NAME_SIZE = 32759;
	
	public static final int REPEATING_GROUP_LENGTH = 0x0C;
	
	public static final int IBM_FORMAT_EBCDIC = 0x02;
	
	public static final int ADOBE_ASCII = 0x03;
	public static final int ADOBE_CMAP  = 0x05;

	private int  nameCount;
	private long dataCount;
	private int  repeatingGroupLength = REPEATING_GROUP_LENGTH;

	private int ibmFormat = IBM_FORMAT_EBCDIC;
	private int techSpecFormat;
	
	private SortedMap<String, String> nameMap = new TreeMap<String, String>();
		
	public int getNameCount() {
		return nameCount;
	}
	
	public void setNameCount(int nameCount) {
		this.nameCount = nameCount;
	}
	
	public long getDataCount() {
		return dataCount;
	}
	
	public void setDataCount(long dataCount) {
		this.dataCount = dataCount;
	}
	
	public int getRepeatingGroupLength() {
		return repeatingGroupLength;
	}

	public void setRepeatingGroupLength(int repeatingGroupLength) {
		if (repeatingGroupLength != REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid repeating group length for font name map");
		}
		this.repeatingGroupLength = repeatingGroupLength;
	}

	public Map<String, String> getNameMap() {
		return nameMap;
	}
	
	public void putNameMap(String ibmName, String techSpecName) {
		nameMap.put(ibmName, techSpecName);
	}
	
	public String getTechSpecName(String ibmName) {
		return nameMap.get(ibmName);
	}
	
	public int getIBMFormat() {
		return ibmFormat;
	}

	public void setIBMFormat(int ibmFormat) {
		this.ibmFormat = ibmFormat;
	}

	public int getTechSpecFormat() {
		return techSpecFormat;
	}

	public void setTechSpecFormat(int techSpecFormat) {
		this.techSpecFormat = techSpecFormat;
	}

	/**
	 * automatically calculate nameCount and dataCount.
	 */
	public void pack() {
		nameCount = nameMap.size();
		dataCount = 2 + nameCount * repeatingGroupLength;
		Iterator<Entry<String, String>> itr = nameMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			String techSpecName = entry.getValue();
			dataCount += techSpecName.length() + 1;
		}
	}
	
	/**
	 * Load SFFontNameMap from a serials of structure fields. An FontNameMap can
	 * be separated to many physical structure field.
	 *
	 * Before the invoking, nameCount, dataCount, and repeatingGroupLength must be set.
	 * 
	 * @param fields
	 */
	public void setStructureFields(MODCAStructureField[] fields) {
		MODCAByteBuffer parameters = new MODCAByteBuffer();
		for (int i=0; i<fields.length; ++i) {
			MODCAStructureField field = fields[i];
			if (field.getSFID() != MODCAStructureField.ID_FNN) {
				throw new IllegalArgumentException("invalid structure field id for font name map");
			}
			parameters.putBytes(field.getSFParameterData().getBytes());			
		}
		if (parameters.size() != dataCount) {
			throw new IllegalArgumentException("invalid structure field parameters");
		}
		
		ibmFormat = parameters.getUBIN1(0);
		if (ibmFormat != IBM_FORMAT_EBCDIC) {
			throw new IllegalArgumentException("bad ibm format:" + ibmFormat);
		}
		techSpecFormat = parameters.getUBIN1(1);
		if (techSpecFormat != ADOBE_ASCII
				&& techSpecFormat != ADOBE_CMAP) {
			throw new IllegalArgumentException("bad technology specific format:" + techSpecFormat);
		}
		if (techSpecFormat != ADOBE_ASCII) {
			throw new IllegalArgumentException("we don't support adobe cmap in afp font now");
		}
		
		int offset = 2;

		// section 2
		for (int i=0; i<nameCount; ++i) {
			String ibmName = parameters.getString(offset, 8).trim();
			int tsOffset   = (int) (parameters.getUBIN4(offset + 8));
			int tsLength   = parameters.getUBIN1(tsOffset);
			String tsName  = parameters.getASCIIString(tsOffset + 1, tsLength - 1);
			nameMap.put(ibmName, tsName);
			offset += repeatingGroupLength;
		}

	}

	public MODCAStructureField[] getStructureFields() {
		pack();
		final int section1Length = 2;
		final int section2Length = repeatingGroupLength * nameCount;
		final int section3Length = (int) (dataCount - section1Length - section2Length);

		if (section1Length + section2Length <= MAX_FONT_NAME_SIZE
				&& section3Length <= MAX_FONT_NAME_SIZE) {
			MODCAStructureField field1 = new MODCAStructureField();
			field1.setSFID(MODCAStructureField.ID_FNN);
			saveSection1(field1.getSFParameterData());
			saveSection2(field1.getSFParameterData());
			
			MODCAStructureField field2 = new MODCAStructureField();
			field2.setSFID(MODCAStructureField.ID_FNN);
			saveSection3(field2.getSFParameterData());

			return new MODCAStructureField[] {field1, field2};
		}
		else {
			throw new IllegalArgumentException("name map is too long");
		}
	}

	private void saveSection1(MODCAByteBuffer buffer) {
		buffer.putUBIN1(ibmFormat);
		buffer.putUBIN1(techSpecFormat);		
	}
	
	private void saveSection2(MODCAByteBuffer buffer) {
		int techSpecOffset = 2 + repeatingGroupLength * nameCount;
		Iterator<Entry<String, String>> itr = nameMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			String ibmName = (String) entry.getKey();
			String techSpecName = (String) entry.getValue();
			buffer.putString(ibmName, 8);
			buffer.putUBIN4(techSpecOffset);
			techSpecOffset += techSpecName.length() + 1;
		}
		assert(techSpecOffset == dataCount);		
	}
	
	private void saveSection3(MODCAByteBuffer buffer) {
		Iterator<Entry<String, String>> itr = nameMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			String techSpecName = (String) entry.getValue();
			buffer.putUBIN1(techSpecName.length() + 1);
			buffer.putASCIIString(techSpecName);
		}
	}
	
}
