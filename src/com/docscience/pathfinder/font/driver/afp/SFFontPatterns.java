package com.docscience.pathfinder.font.driver.afp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxin
 *
 */
public class SFFontPatterns {

	public static final int PATTERN_TECH_LASER_MATRIX = 0x05;
	public static final int PATTERN_TECH_CID_KEYED    = 0x1E;
	public static final int PATTERN_TECH_TYPE1_PFB    = 0x1F;

	// In specification this value is 32759, but we found it
	// doesn't work in our printer. It seems 11992 is a normal value.
	private static final int MAX_PATTERN_DATA_SIZE = 11992;
	
	private int patternTech;
	private int patternDataAlignment;
	private String techSpecObjectIdentifier;
	private String techSpecObjectDescriptor;
	private MODCAByteBuffer techSpecObjectData;

	public SFFontPatterns(int patternTech) {
		if (patternTech != PATTERN_TECH_LASER_MATRIX
				&& patternTech != PATTERN_TECH_TYPE1_PFB
				&& patternTech != PATTERN_TECH_CID_KEYED) {
			throw new IllegalArgumentException("invalid pattern tech");
		}
		this.patternTech = patternTech;
	}

	public boolean isRasterPattern() {
		return patternTech == PATTERN_TECH_LASER_MATRIX;
	}

	public int getPatternTech() {
		return patternTech;
	}

	public void setPatternTech(int patternTech) {
		this.patternTech = patternTech;
	}

	public int getPatternDataAlignment() {
		return patternDataAlignment;
	}

	public void setPatternDataAlignment(int i) {
		this.patternDataAlignment = i;
	}

	public String getTechSpecObjectIdentifier() {
		return techSpecObjectIdentifier;
	}

	public void setTechSpecObjectIdentifier(String tid) {
		this.techSpecObjectIdentifier = tid;
	}

	public String getTechSpecObjectDescriptor() {
		return techSpecObjectDescriptor;
	}

	public void setTechSpecObjectDescriptor(String tds) {
		this.techSpecObjectDescriptor = tds;
	}

	public MODCAByteBuffer getTechSpecObjectData() {
		if (techSpecObjectData == null) {
			techSpecObjectData = new MODCAByteBuffer();
		}
		return techSpecObjectData;
	}

	public void setStructureFields(MODCAStructureField[] fields) {
		if (isRasterPattern()) {
			throw new IllegalArgumentException("we don't support raster afp font now");
		}

		MODCAByteBuffer parameters = new MODCAByteBuffer();
		for (int i=0; i<fields.length; ++i) {
			MODCAStructureField field = fields[i];
			if (field.getSFID() != MODCAStructureField.ID_FNG) {
				throw new IllegalArgumentException("invalid structure field for font patterns");
			}
			parameters.putBytes(field.getSFParameterData().getBytes());
		}

//		long length   = parameters.getUBIN4(0);
		long checkSum = parameters.getUBIN4(4);
		int tidLength = parameters.getUBIN2(8);
		techSpecObjectIdentifier = parameters.getASCIIString(10, tidLength - 2);
		int tdsLength = 0;
		if (patternTech != PATTERN_TECH_TYPE1_PFB) {
			tdsLength = parameters.getUBIN2(8 + tidLength);
			techSpecObjectDescriptor = parameters.getASCIIString(10 + tidLength, tdsLength - 2);
		}
		techSpecObjectData = new MODCAByteBuffer(parameters.getBytes(8 + tidLength + tdsLength));
		if (checkSum != calculateChecksum(techSpecObjectData)) {
			throw new IllegalArgumentException("invalid checksum");
		}
	}

	public MODCAStructureField[] getStructureFields() {

		List<MODCAStructureField> fields = new ArrayList<MODCAStructureField>();
		int offset = 0;
		long length = 0;

		if (isRasterPattern()) {
			length = techSpecObjectData.size();

			while(offset < length) {
				MODCAStructureField field = new MODCAStructureField();
				field.setSFID(MODCAStructureField.ID_FNG);
				MODCAByteBuffer parameters = field.getSFParameterData();
				int sectionLength = (int) Math.min(length - offset, MAX_PATTERN_DATA_SIZE);
				parameters.putBytes(techSpecObjectData.getBytes(offset, sectionLength));
				offset += parameters.size();
				fields.add(field);
			}

		} else {
			final long checksum = calculateChecksum(techSpecObjectData);
			final int  tidLength = techSpecObjectIdentifier.length() + 2;
			final int  tdsLength = patternTech == PATTERN_TECH_TYPE1_PFB ? 0 : techSpecObjectDescriptor.length() + 2;
			final int  tddLength = techSpecObjectData.size();

			final int  headLength = 8 + tidLength + tdsLength;
			length = headLength + tddLength;

			while(offset < length) {
				MODCAStructureField field = new MODCAStructureField();
				field.setSFID(MODCAStructureField.ID_FNG);
				MODCAByteBuffer parameters = field.getSFParameterData();
				if (offset == 0) {
					parameters.putUBIN4(length);
					parameters.putUBIN4(checksum);
					parameters.putUBIN2(tidLength);
					parameters.putASCIIString(techSpecObjectIdentifier);
					if (patternTech != PATTERN_TECH_TYPE1_PFB) {
						parameters.putUBIN2(tdsLength);
						parameters.putASCIIString(techSpecObjectDescriptor);
					}
					int remainSpace = MAX_PATTERN_DATA_SIZE - parameters.size();
					if (remainSpace >= tddLength) {
						parameters.putBytes(techSpecObjectData.getBytes());
					}
					else {
						parameters.putBytes(techSpecObjectData.getBytes(0, remainSpace));
					}
					assert(parameters.size() <= MAX_PATTERN_DATA_SIZE);
				}
				else {
					int sectionLength = (int) Math.min(length - offset, MAX_PATTERN_DATA_SIZE);
					parameters.putBytes(techSpecObjectData.getBytes(offset - headLength, sectionLength));
				}
				offset += parameters.size();
				fields.add(field);
			}
		}
		assert(offset == length);
		return (MODCAStructureField[]) fields.toArray(new MODCAStructureField[fields.size()]);
	}

	public static long calculateChecksum(MODCAByteBuffer data) {
		byte[] checksumPartial = {0,0,0,0};
		int offset = 0;
		int i = 0;
		int b;
		while (offset < data.size()) {
			b = data.getByte(offset++) & 0xff;
			checksumPartial[i] = (byte) ((checksumPartial[i] & 0xff) + b);
			i = i + 1;
			if (i == 4) {
				i = 0;
			}
		}
		long checksum = 0;
		for (int j=0; j<4; j++) {
			checksum = (checksum << 8) | (checksumPartial[j] & 0xff);
		}
		return checksum;
	}

}
