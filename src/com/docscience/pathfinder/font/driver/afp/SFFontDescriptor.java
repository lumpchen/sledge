package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class SFFontDescriptor {
	
	public static final int WEIGHT_CLASS_ULTRALIGHT = 0x01;
	public static final int WEIGHT_CLASS_EXTRALIGHT = 0x02;
	public static final int WEIGHT_CLASS_LIGHT = 0x03;
	public static final int WEIGHT_CLASS_SEMILIGHT = 0x04;
	public static final int WEIGHT_CLASS_MEDIUM = 0x05;
	public static final int WEIGHT_CLASS_SEMIBOLD = 0x06;
	public static final int WEIGHT_CLASS_BOLD = 0x07;
	public static final int WEIGHT_CLASS_EXTRABOLD = 0x08;
	public static final int WEIGHT_CLASS_ULTRABOLD = 0x09;

	public static final int WIDTH_CLASS_ULTRACONDENSED = 0x01;
	public static final int WIDTH_CLASS_EXTRACONDENSED = 0x02;
	public static final int WIDTH_CLASS_CONDENSED = 0x03;
	public static final int WIDTH_CLASS_SEMICONDENSED = 0x04;
	public static final int WIDTH_CLASS_MEDIUM = 0x05; 
	public static final int WIDTH_CLASS_SEMIEXPANDED = 0x06;
	public static final int WIDTH_CLASS_EXPANDED = 0x07;
	public static final int WIDTH_CLASS_EXTRAEXPANDED = 0x08;
	public static final int WIDTH_CLASS_ULTRAEXPANDED = 0x09;
	
	public static final int FONT_TYPEFACE_GID_RESERVED = 65279;
	public static final int FONT_TYPEFACE_GID_MIN = 0x0000;
	public static final int FONT_TYPEFACE_GID_MAX = 0xffff;
	
	private static final int TYPEFACE_DESCRIPTION_LENGTH = 32;

	private String typefaceDescription;

	private int weightClass;
	private int widthClass;

	private int maxVerticalSize;
	private int minVerticalSize;
	private int nomVerticalSize;
	
	private int maxHorizontalSize;
	private int minHorizontalSize;
	private int nomHorizontalSize;

	private int designGeneralClass;
	private int designSubClass;
	private int designSpecificGroup;
	private int designFlags;
	private int graphicCharacterSetGID;
	private int fontTypefaceGID;

	private String familyName;
	private String typefaceName;
	
	public String getTypefaceDescription() {
		return typefaceDescription;
	}

	public void setTypefaceDescription(String typefaceDescription) {
		this.typefaceDescription = typefaceDescription;
	}

	public int getWeightClass() {
		return weightClass;
	}

	public void setWeightClass(int weightClass) {
		this.weightClass = weightClass;
	}

	public int getWidthClass() {
		return widthClass;
	}

	public void setWidthClass(int widthClass) {
		this.widthClass = widthClass;
	}

	public int getMaxVerticalSize() {
		return maxVerticalSize;
	}

	public void setMaxVerticalSize(int maxVerticalSize) {
		this.maxVerticalSize = maxVerticalSize;
	}

	public int getMinVerticalSize() {
		return minVerticalSize;
	}

	public void setMinVerticalSize(int minVerticalSize) {
		this.minVerticalSize = minVerticalSize;
	}

	public int getNomVerticalSize() {
		return nomVerticalSize;
	}

	public void setNomVerticalSize(int nomVerticalSize) {
		this.nomVerticalSize = nomVerticalSize;
	}

	public int getMaxHorizontalSize() {
		return maxHorizontalSize;
	}

	public void setMaxHorizontalSize(int maxHorizontalSize) {
		this.maxHorizontalSize = maxHorizontalSize;
	}

	public int getMinHorizontalSize() {
		return minHorizontalSize;
	}

	public void setMinHorizontalSize(int minHorizontalSize) {
		this.minHorizontalSize = minHorizontalSize;
	}

	public int getNomHorizontalSize() {
		return nomHorizontalSize;
	}

	public void setNomHorizontalSize(int nomHorizontalSize) {
		this.nomHorizontalSize = nomHorizontalSize;
	}

	public int getDesignGeneralClass() {
		return designGeneralClass;
	}

	public void setDesignGeneralClass(int designGeneralClass) {
		this.designGeneralClass = designGeneralClass;
	}

	public int getDesignSubClass() {
		return designSubClass;
	}

	public void setDesignSubClass(int designSubClass) {
		this.designSubClass = designSubClass;
	}

	public int getDesignSpecificGroup() {
		return designSpecificGroup;
	}

	public void setDesignSpecificGroup(int designSpecificGroup) {
		this.designSpecificGroup = designSpecificGroup;
	}

	public int getDesignFlags() {
		return designFlags;
	}

	public void setDesignFlags(int designFlags) {
		this.designFlags = designFlags;
	}

	public int getGraphicCharacterSetGID() {
		return graphicCharacterSetGID;
	}

	public void setGraphicCharacterSetGID(int graphicCharacterSetGID) {
		this.graphicCharacterSetGID = graphicCharacterSetGID;
	}

	public int getFontTypefaceGID() {
		return fontTypefaceGID;
	}

	public void setFontTypefaceGID(int fontTypefaceGID) {
		this.fontTypefaceGID = fontTypefaceGID;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getTypefaceName() {
		return typefaceName;
	}

	public void setTypefaceName(String typefaceName) {
		this.typefaceName = typefaceName;
	}

	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_FND) {
			throw new IllegalArgumentException("invalid structure field id for font descriptor");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		typefaceDescription = parameters.getString(0, TYPEFACE_DESCRIPTION_LENGTH).trim();
		weightClass = parameters.getUBIN1(32);
		widthClass = parameters.getUBIN1(33);
		maxVerticalSize = parameters.getUBIN2(34);
		nomVerticalSize = parameters.getUBIN2(36);
		minVerticalSize = parameters.getUBIN2(38);
		maxHorizontalSize = parameters.getUBIN2(40);
		nomHorizontalSize = parameters.getUBIN2(42);
		minHorizontalSize = parameters.getUBIN2(44);
		designGeneralClass = parameters.getUBIN1(46);
		designSubClass = parameters.getUBIN1(47);
		designSpecificGroup = parameters.getUBIN1(48);
		designFlags = parameters.getUBIN2(64);
		graphicCharacterSetGID = parameters.getUBIN2(76);
		fontTypefaceGID = parameters.getUBIN2(78);
		if (parameters.size() <= 80) {
			return;
		}
		MODCATriplet[] triplets = MODCATriplet.parseTriplets(parameters, 80, parameters.size() - 80);
		for (int i=0; i<triplets.length; ++i) {
			MODCATriplet triplet = triplets[i];
			if (triplet.getTID() == MODCATriplet.ID_FULLY_QUALIFIED_NAME) {
				TFullyQualifiedName fqn = new TFullyQualifiedName();
				fqn.setTriplet(triplet);
				if (fqn.getFQNType() == TFullyQualifiedName.TYPE_FAMILY_NAME) {
					familyName = fqn.getFQNName();
				}
				else {
					assert(fqn.getFQNType() == TFullyQualifiedName.TYPE_TYPEFACE_NAME);
					typefaceName = fqn.getFQNName();
				}
			}
		}
	}
	
	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_FND);
		MODCAByteBuffer parameters = field.getSFParameterData();
		parameters.putString(typefaceDescription, TYPEFACE_DESCRIPTION_LENGTH);
		parameters.putUBIN1(weightClass);
		parameters.putUBIN1(widthClass);
		parameters.putUBIN2(maxVerticalSize);
		parameters.putUBIN2(nomVerticalSize);
		parameters.putUBIN2(minVerticalSize);
		parameters.putUBIN2(maxHorizontalSize);
		parameters.putUBIN2(nomHorizontalSize);
		parameters.putUBIN2(minHorizontalSize);
		parameters.putUBIN1(designGeneralClass);
		parameters.putUBIN1(designSubClass);
		parameters.putUBIN1(designSpecificGroup);
		assert(parameters.size() == 49);
		for (int i=49; i<=63; ++i) {
			parameters.putUBIN1(0);
		}
		assert(parameters.size() == 64);
		parameters.putUBIN2(designFlags);
		for (int i=66; i<=75; ++i) {
			parameters.putUBIN1(0);
		}
		assert(parameters.size() == 76);
		parameters.putUBIN2(graphicCharacterSetGID);
		parameters.putUBIN2(fontTypefaceGID);
		assert(parameters.size() == 80);
		if (familyName != null) {
			TFullyQualifiedName fqn = new TFullyQualifiedName();
			fqn.setFQNType(TFullyQualifiedName.TYPE_FAMILY_NAME);
			fqn.setFQNName(familyName);
			fqn.getTriplet().save(parameters);
		}
		if (typefaceName != null) {
			TFullyQualifiedName fqn = new TFullyQualifiedName();
			fqn.setFQNType(TFullyQualifiedName.TYPE_TYPEFACE_NAME);
			fqn.setFQNName(typefaceName);
			fqn.getTriplet().save(parameters);
		}
		return field;
	}
	
}
