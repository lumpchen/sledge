package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class SFCodePageControl {

	private static final int GRAPHIC_CHARACTER_GID_LENGTH = 8;
	
	private String defaultGraphicCharacterGID;
	private int defaultCharacterUseFlags;
	private int cpiRepeatingGroupLength;
	private int spaceCharacterSectionNumber;
	private int spaceCharacterCodePoint;
	private int useFlags;
	private long unicodeScalar;
	
	public String getDefaultGraphicCharacterGID() {
		return defaultGraphicCharacterGID;
	}

	public void setDefaultGraphicCharacterGID(String defaultGraphicCharacterGID) {
		this.defaultGraphicCharacterGID = defaultGraphicCharacterGID;
	}

	public int getDefaultCharacterUseFlags() {
		return defaultCharacterUseFlags;
	}

	public void setDefaultCharacterUseFlags(int defaultCharacterUseFlags) {
		this.defaultCharacterUseFlags = defaultCharacterUseFlags;
	}

	public int getCPIRepeatingGroupLength() {
		return cpiRepeatingGroupLength;
	}

	public void setCPIRepeatingGroupLength(int cpiRepeatingGroupLength) {
		this.cpiRepeatingGroupLength = cpiRepeatingGroupLength;
	}

	public int getSpaceCharacterSectionNumber() {
		return spaceCharacterSectionNumber;
	}

	public void setSpaceCharacterSectionNumber(int spaceCharacterSectionNumber) {
		this.spaceCharacterSectionNumber = spaceCharacterSectionNumber;
	}

	public int getSpaceCharacterCodePoint() {
		return spaceCharacterCodePoint;
	}

	public void setSpaceCharacterCodePoint(int spaceCharacterCodePoint) {
		this.spaceCharacterCodePoint = spaceCharacterCodePoint;
	}

	public int getUseFlags() {
		return useFlags;
	}

	public void setUseFlags(int useFlags) {
		this.useFlags = useFlags;
	}

	public long getUnicodeScalar() {
		return unicodeScalar;
	}

	public void setUnicodeScalar(long unicodeScalar) {
		this.unicodeScalar = unicodeScalar;
	}

	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_CPC) {
			throw new IllegalArgumentException("invalid structure field id for code page descriptor");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		defaultGraphicCharacterGID = parameters.getString(0, GRAPHIC_CHARACTER_GID_LENGTH).trim();
		defaultCharacterUseFlags = parameters.getUBIN1(8);
		cpiRepeatingGroupLength = parameters.getUBIN1(9);
		spaceCharacterSectionNumber = parameters.getUBIN1(10);
		spaceCharacterCodePoint = parameters.getUBIN1(11);
		useFlags = parameters.getUBIN1(12);
		if (cpiRepeatingGroupLength == SFCodePageIndex.REPEATING_GROUP_SINGLE_BYTE_UNICODE_SCALAR
				|| cpiRepeatingGroupLength == SFCodePageIndex.REPEATING_GROUP_DOUBLE_BYTE_UNICODE_SCALAR) {
			unicodeScalar = parameters.getUBIN4(13);
		}
	}
	
	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_CPC);
		MODCAByteBuffer parameters = field.getSFParameterData();
		parameters.putString(defaultGraphicCharacterGID, GRAPHIC_CHARACTER_GID_LENGTH);
		parameters.putUBIN1(defaultCharacterUseFlags);
		parameters.putUBIN1(cpiRepeatingGroupLength);
		parameters.putUBIN1(spaceCharacterSectionNumber);
		parameters.putUBIN1(spaceCharacterCodePoint);
		parameters.putUBIN1(useFlags);
		if (cpiRepeatingGroupLength == SFCodePageIndex.REPEATING_GROUP_SINGLE_BYTE_UNICODE_SCALAR
				|| cpiRepeatingGroupLength == SFCodePageIndex.REPEATING_GROUP_DOUBLE_BYTE_UNICODE_SCALAR) {
			parameters.putUBIN4(unicodeScalar);
		}		
		return field;
	}

}
