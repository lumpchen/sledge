package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class SFFontControl {
	
	public static final int PATTERN_TECH_LASER_MATRIX = 0x05;
	public static final int PATTERN_TECH_CID_KEYED    = 0x1E;
	public static final int PATTERN_TECH_TYPE1_PFB    = 0x1F;
	
	public static final int PATTERN_DATA_ALIGN_1_BYTE = 0x00;
	public static final int PATTERN_DATA_ALIGN_4_BYTE = 0x02;
	public static final int PATTERN_DATA_ALIGN_8_BYTE = 0x03;
	
	public static final int UNIT_BASE_FIXED = 0x00;
	public static final int UNIT_BASE_RELATIVE = 0x02;
	
	public static final int UNIT_240_PELS_PER_INCH  = 0x0960;
	public static final int UNIT_300_PELS_PER_INCH  = 0x0BB8;
	public static final int UNIT_1000_PELS_PER_INCH = 0x03E8;
	
	private static final int RETAILED_PARAMETER = 0x01;
	
	private int patternTech;
	private int useFlags;
	private int xUnitBase;
	private int yUnitBase;
	private int xUnitsPerUnitBase;
	private int yUnitsPerUnitBase;
	private int maxCharacterBoxWidth;
	private int maxCharacterBoxHeight;
	private int fnoRepeatingGroupLength = SFFontOrientation.REPEATING_GROUP_LENGTH;
	private int fniRepeatingGroupLength;
	private int patternDataAlignment;
	private int rasterPatternDataCount;
	private int fnpRepeatingGroupLength = SFFontPosition.REPEATING_GROUP_LENGTH;
	private int fnmRepeatingGroupLength;
	private int shapeResolutionXUnitBase;
	private int shapeResolutionYUnitBase;
	private int shapeResolutionXUnitsPerUnitBase;
	private int shapeResolutionYUnitsPerUnitBase;
	private long outlinePatternDataCount;
	private int  fnnRepeatingGroupLength = SFFontNameMap.REPEATING_GROUP_LENGTH;
	private long fnnDataCount;
	private int  fnnNameCount;
	
	public int getPatternTech() {
		return patternTech;
	}

	public void setPatternTech(int patternTech) {
		if (patternTech != PATTERN_TECH_LASER_MATRIX 
				&& patternTech != PATTERN_TECH_CID_KEYED 
				&& patternTech != PATTERN_TECH_TYPE1_PFB) {
			throw new IllegalArgumentException("invalid pattern tech :" + patternTech);
		}
		this.patternTech = patternTech;
	}

	public int getUseFlags() {
		return useFlags;
	}

	public void setUseFlags(int useFlags) {
		this.useFlags = useFlags;
	}

	public int getXUnitBase() {
		return xUnitBase;
	}

	public void setXUnitBase(int unitBase) {
		xUnitBase = unitBase;
	}

	public int getYUnitBase() {
		return yUnitBase;
	}

	public void setYUnitBase(int unitBase) {
		yUnitBase = unitBase;
	}

	public int getXUnitsPerUnitBase() {
		return xUnitsPerUnitBase;
	}

	public void setXUnitsPerUnitBase(int unitsPerUnitBase) {
		xUnitsPerUnitBase = unitsPerUnitBase;
	}

	public int getYUnitsPerUnitBase() {
		return yUnitsPerUnitBase;
	}

	public void setYUnitsPerUnitBase(int unitsPerUnitBase) {
		yUnitsPerUnitBase = unitsPerUnitBase;
	}

	public int getMaxCharacterBoxWidth() {
		return maxCharacterBoxWidth;
	}

	public void setMaxCharacterBoxWidth(int maxCharacterBoxWidth) {
		this.maxCharacterBoxWidth = maxCharacterBoxWidth;
	}

	public int getMaxCharacterBoxHeight() {
		return maxCharacterBoxHeight;
	}

	public void setMaxCharacterBoxHeight(int maxCharacterBoxHeight) {
		this.maxCharacterBoxHeight = maxCharacterBoxHeight;
	}

	public int getFNORepeatingGroupLength() {
		return fnoRepeatingGroupLength;
	}

	public void setFNORepeatingGroupLength(int fnoRepeatingGroupLength) {
		if (fnoRepeatingGroupLength != SFFontOrientation.REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fno repeating group length");
		}
		this.fnoRepeatingGroupLength = fnoRepeatingGroupLength;
	}

	public int getFNIRepeatingGroupLength() {
		return fniRepeatingGroupLength;
	}

	public void setFNIRepeatingGroupLength(int fniRepeatingGroupLength) {
		if (fniRepeatingGroupLength != SFFontIndex.RASTER_REPEATING_GROUP_LENGTH
				&& fniRepeatingGroupLength != SFFontIndex.OUTLINE_REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fni repeating group length");
		}
		this.fniRepeatingGroupLength = fniRepeatingGroupLength;
	}

	public int getPatternDataAlignment() {
		return patternDataAlignment;
	}

	public void setPatternDataAlignment(int patternDataAlignment) {
		if (patternDataAlignment != PATTERN_DATA_ALIGN_1_BYTE
				&& patternDataAlignment != PATTERN_DATA_ALIGN_4_BYTE
				&& patternDataAlignment != PATTERN_DATA_ALIGN_8_BYTE) {
			throw new IllegalArgumentException("invalid pattern data alignment");
		}
		this.patternDataAlignment = patternDataAlignment;
	}

	public int getRasterPatternDataCount() {
		return rasterPatternDataCount;
	}

	public void setRasterPatternDataCount(int rasterPatternDataCount) {
		this.rasterPatternDataCount = rasterPatternDataCount;
	}

	public int getFNPRepeatingGroupLength() {
		return fnpRepeatingGroupLength;
	}

	public void setFNPRepeatingGroupLength(int fnpRepeatingGroupLength) {
		if (fnpRepeatingGroupLength != SFFontPosition.REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fnp repeating group length");
		}
		this.fnpRepeatingGroupLength = fnpRepeatingGroupLength;
	}

	public int getFNMRepeatingGroupLength() {
		return fnmRepeatingGroupLength;
	}

	public void setFNMRepeatingGroupLength(int fnmRepeatingGroupLength) {
		if (fnmRepeatingGroupLength != SFFontPatternsMap.NO_RASTER_REPEATING_GROUP_LENGTH
				&& fnmRepeatingGroupLength != SFFontPatternsMap.RASTER_REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fnm repeating group length");
		}
		this.fnmRepeatingGroupLength = fnmRepeatingGroupLength;
	}

	public int getShapeResolutionXUnitBase() {
		return shapeResolutionXUnitBase;
	}

	public void setShapeResolutionXUnitBase(int shapeResolutionXUnitBase) {
		this.shapeResolutionXUnitBase = shapeResolutionXUnitBase;
	}

	public int getShapeResolutionYUnitBase() {
		return shapeResolutionYUnitBase;
	}

	public void setShapeResolutionYUnitBase(int shapeResolutionYUnitBase) {
		this.shapeResolutionYUnitBase = shapeResolutionYUnitBase;
	}

	public int getShapeResolutionXUnitsPerUnitBase() {
		return shapeResolutionXUnitsPerUnitBase;
	}

	public void setShapeResolutionXUnitsPerUnitBase(
			int shapeResolutionXUnitsPerUnitBase) {
		this.shapeResolutionXUnitsPerUnitBase = shapeResolutionXUnitsPerUnitBase;
	}

	public int getShapeResolutionYUnitsPerUnitBase() {
		return shapeResolutionYUnitsPerUnitBase;
	}

	public void setShapeResolutionYUnitsPerUnitBase(
			int shapeResolutionYUnitsPerUnitBase) {
		this.shapeResolutionYUnitsPerUnitBase = shapeResolutionYUnitsPerUnitBase;
	}

	public long getOutlinePatternDataCount() {
		return outlinePatternDataCount;
	}

	public void setOutlinePatternDataCount(long outlinePatternDataCount) {
		this.outlinePatternDataCount = outlinePatternDataCount;
	}

	public int getFNNRepeatingGroupLength() {
		return fnnRepeatingGroupLength;
	}

	public void setFNNRepeatingGroupLength(int fnnRepeatingGroupLength) {
		if (fnnRepeatingGroupLength != SFFontNameMap.REPEATING_GROUP_LENGTH &&
		    fnnRepeatingGroupLength != 0) {
			throw new IllegalArgumentException("invalid fnn repeating group length");
		}
		this.fnnRepeatingGroupLength = fnnRepeatingGroupLength;
	}

	public long getFNNDataCount() {
		return fnnDataCount;
	}

	public void setFNNDataCount(long fnnDataCount) {
		this.fnnDataCount = fnnDataCount;
	}

	public int getFNNNameCount() {
		return fnnNameCount;
	}

	public void setFNNNameCount(int fnnNameCount) {
		this.fnnNameCount = fnnNameCount;
	}

	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_FNC) {
			throw new IllegalArgumentException("invalid structure field id for font control");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		patternTech = parameters.getUBIN1(1);
		if (patternTech != PATTERN_TECH_LASER_MATRIX 
				&& patternTech != PATTERN_TECH_CID_KEYED 
				&& patternTech != PATTERN_TECH_TYPE1_PFB) {
			throw new IllegalArgumentException("invalid pattern tech :" + patternTech);
		}
		useFlags = parameters.getUBIN1(3);
		xUnitBase = parameters.getUBIN1(4);
		yUnitBase = parameters.getUBIN1(5);
		xUnitsPerUnitBase = parameters.getUBIN2(6);
		yUnitsPerUnitBase = parameters.getUBIN2(8);
		maxCharacterBoxWidth = parameters.getUBIN2(10);
		maxCharacterBoxHeight = parameters.getUBIN2(12);
		fnoRepeatingGroupLength = parameters.getUBIN1(14);
		if (fnoRepeatingGroupLength != SFFontOrientation.REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fno repeating group length");
		}
		fniRepeatingGroupLength = parameters.getUBIN1(15);
		if (fniRepeatingGroupLength != SFFontIndex.RASTER_REPEATING_GROUP_LENGTH
				&& fniRepeatingGroupLength != SFFontIndex.OUTLINE_REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fni repeating group length");
		}
		patternDataAlignment = parameters.getUBIN1(16);
		if (patternDataAlignment != PATTERN_DATA_ALIGN_1_BYTE
				&& patternDataAlignment != PATTERN_DATA_ALIGN_4_BYTE
				&& patternDataAlignment != PATTERN_DATA_ALIGN_8_BYTE) {
			throw new IllegalArgumentException("invalid pattern data alignment");
		}
		rasterPatternDataCount = parameters.getUBIN3(17);
		fnpRepeatingGroupLength = parameters.getUBIN1(20);
		if (fnpRepeatingGroupLength != SFFontPosition.REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fnp repeating group length");
		}
		fnmRepeatingGroupLength = parameters.getUBIN1(21);
		if (fnmRepeatingGroupLength != SFFontPatternsMap.NO_RASTER_REPEATING_GROUP_LENGTH
				&& fnmRepeatingGroupLength != SFFontPatternsMap.RASTER_REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fnm repeating group length");
		}
		
		if (parameters.size() <= 22) {
			return;
		}
		shapeResolutionXUnitBase = parameters.getUBIN1(22);
		shapeResolutionYUnitBase = parameters.getUBIN1(23);
		shapeResolutionXUnitsPerUnitBase = parameters.getUBIN2(24);
		shapeResolutionYUnitsPerUnitBase = parameters.getUBIN2(26);
		
		if (parameters.size() <= 28) {
			return;
		}
		outlinePatternDataCount = parameters.getUBIN4(28);
		if (parameters.size() <= 32) {
			return;
		}
		// 3 reserved bytes [32, 34]
		if (parameters.size() <= 35) {
			return;
		}
		fnnRepeatingGroupLength = parameters.getUBIN1(35);
		if (fnnRepeatingGroupLength != SFFontNameMap.REPEATING_GROUP_LENGTH) {
			throw new IllegalArgumentException("invalid fnn repeating group length");
		}
		fnnDataCount = parameters.getUBIN4(36);
		fnnNameCount = parameters.getUBIN2(40);
		if (parameters.size() <= 42) {
			return;
		}
		// TODO: READ ONE EXTENSION TRIPLET HERE
	}
	
	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_FNC);
		MODCAByteBuffer parameters = field.getSFParameterData();
		parameters.putUBIN1(RETAILED_PARAMETER);
		parameters.putUBIN1(patternTech);
		parameters.putUBIN1(0);
		parameters.putUBIN1(useFlags);
		parameters.putUBIN1(xUnitBase);
		parameters.putUBIN1(yUnitBase);
		parameters.putUBIN2(xUnitsPerUnitBase);
		parameters.putUBIN2(yUnitsPerUnitBase);
		parameters.putUBIN2(maxCharacterBoxWidth);
		parameters.putUBIN2(maxCharacterBoxHeight);
		parameters.putUBIN1(fnoRepeatingGroupLength);
		parameters.putUBIN1(fniRepeatingGroupLength);
		parameters.putUBIN1(patternDataAlignment);
		parameters.putUBIN3(rasterPatternDataCount);
		parameters.putUBIN1(fnpRepeatingGroupLength);
		parameters.putUBIN1(fnmRepeatingGroupLength);
		assert(parameters.size() == 22);
		parameters.putUBIN1(shapeResolutionXUnitBase);
		parameters.putUBIN1(shapeResolutionYUnitBase);
		parameters.putUBIN2(shapeResolutionXUnitsPerUnitBase);
		parameters.putUBIN2(shapeResolutionYUnitsPerUnitBase);
		assert(parameters.size() == 28);
		if (fnnDataCount > 0) {
			parameters.putUBIN4(outlinePatternDataCount);
			assert(parameters.size() == 32);
			parameters.putUBIN3(0);
			assert(parameters.size() == 35);
			parameters.putUBIN1(fnnRepeatingGroupLength);
			parameters.putUBIN4(fnnDataCount);
			parameters.putUBIN2(fnnNameCount);
			assert(parameters.size() == 42);
		}
		return field;
	}
}
