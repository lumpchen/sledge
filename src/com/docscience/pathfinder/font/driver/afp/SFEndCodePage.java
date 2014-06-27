package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class SFEndCodePage {
	
	private static final int NAME_LENGTH = 8;
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_ECP) {
			throw new IllegalArgumentException("invalid structure field id for end font");
		}
		if (field.getSFParameterLength() == 0) {
			// EFN can contain nothing.
			return;
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		name = parameters.getString(0, NAME_LENGTH).trim();
	}
	
	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_ECP);
		if (name != null) {
			field.getSFParameterData().putString(name, NAME_LENGTH);
		}
		return field;
	}
	
}
