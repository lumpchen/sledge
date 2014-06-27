package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class SFCodePageDescriptor {

	public static final int ENCODING_SCHEME_NO_SPECIFIED = 0x0000;
	public static final int ENCODING_SCHEME_SINGLE_BYTE_NO_SPECIFIED = 0x0100;
	public static final int ENCODING_SCHEME_DOUBLE_BYTE_NO_SPECIFIED = 0x0200;
	public static final int ENCODING_SCHEME_SINGLE_BYTE_IBM_PC = 0x2100;
	public static final int ENCODING_SCHEME_SINGLE_BYTE_EBCDIC = 0x6100;
	public static final int ENCODING_SCHEME_DOUBLE_BYTE_EBCDIC = 0x6200;
	public static final int ENCODING_SCHEME_DOUBLE_BYTE_UCS = 0x8200;
	
	private static final int CODE_PAGE_DESCRIPTION_LENGTH = 32;
	private static final int GRAPHIC_CHARACTER_GID_LENGTH = 8;
	
	private String codePageDescription;
	private int graphicCharacterGIDLength = GRAPHIC_CHARACTER_GID_LENGTH;
	private int numCodedGraphicCharacters;
	private int graphicCharacterSetGID;
	private int codePageGID;
	private int encodingScheme;
	
	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_CPD) {
			throw new IllegalArgumentException("invalid structure field id for code page descriptor");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		codePageDescription = parameters.getString(0, CODE_PAGE_DESCRIPTION_LENGTH).trim();
		graphicCharacterGIDLength = parameters.getUBIN2(32);
		if (graphicCharacterGIDLength != GRAPHIC_CHARACTER_GID_LENGTH) {
			throw new IllegalArgumentException("invalid graphic character global id length");
		}
		numCodedGraphicCharacters = (int) parameters.getUBIN4(34);
		graphicCharacterSetGID = parameters.getUBIN2(38);
		codePageGID = parameters.getUBIN2(40);
		if (parameters.size() <= 42) {
			return;
		}
		encodingScheme = parameters.getUBIN2(42);
	}
	
	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_CPD);
		MODCAByteBuffer parameters = field.getSFParameterData();
		parameters.putString(codePageDescription, CODE_PAGE_DESCRIPTION_LENGTH);
		parameters.putUBIN2(graphicCharacterGIDLength);
		parameters.putUBIN4(numCodedGraphicCharacters);
		parameters.putUBIN2(graphicCharacterSetGID);
		parameters.putUBIN2(codePageGID);
		parameters.putUBIN2(encodingScheme);
		assert(parameters.size() == 44);
		return field;
	}
	
}
