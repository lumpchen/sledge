package com.docscience.pathfinder.font.driver.afp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxin
 *
 */
public class SFCodePageIndex {

	public static final int REPEATING_GROUP_SINGLE_BYTE = 0x0A;
	public static final int REPEATING_GROUP_DOUBLE_BYTE = 0x0B;
	public static final int REPEATING_GROUP_SINGLE_BYTE_UNICODE_SCALAR = 0xFE;
	public static final int REPEATING_GROUP_DOUBLE_BYTE_UNICODE_SCALAR = 0xFF;
	
	public static class Entry {
		
		private String graphicCharacterGID;
		private int useFlags;
		private int codepoint;
		
		public String getGraphicCharacterGID() {
			return graphicCharacterGID;
		}

		public void setGraphicCharacterGID(String graphicCharacterGID) {
			this.graphicCharacterGID = graphicCharacterGID;
		}

		public int getUseFlags() {
			return useFlags;
		}

		public void setUseFlags(int useFlags) {
			this.useFlags = useFlags;
		}

		public int getCodepoint() {
			return codepoint;
		}

		public void setCodepoint(int codepoint) {
			this.codepoint = codepoint;
		}

		@Override
		public String toString() {
			return "array[" + codepoint + "] = \"" + graphicCharacterGID + "\"";
		}
	}
	
	private int repeatingGroupLength;
	private List<Entry> entries = new ArrayList<Entry>(256);
	
	public int getRepeatingGroupLength() {
		return repeatingGroupLength;
	}
	
	public int getNumEntries() {
		return entries.size();
	}
	
	public Entry getEntry(int index) {
		return entries.get(index);
	}
	
	public void addEntry(Entry entry) {
		entries.add(entry);
	}
	
	public int findCodePoint(String graphicCharacterGID) {
		for (int i=0; i<entries.size(); ++i) {
			Entry entry = entries.get(i);
			if (entry.getGraphicCharacterGID().equals(graphicCharacterGID)) {
				return entry.getCodepoint();
			}
		}
		return -1;
	}
	
	public String findGraphicCharacterGID(int codePoint) {
		for (int i=0; i<entries.size(); ++i) {
			Entry entry = entries.get(i);
			if (entry.getCodepoint() == codePoint) {
				return entry.getGraphicCharacterGID();
			}
		}
		return null;
	}
	
	public SFCodePageIndex(int repeatingGroupLength) {
		if (repeatingGroupLength != REPEATING_GROUP_SINGLE_BYTE
				&& repeatingGroupLength != REPEATING_GROUP_DOUBLE_BYTE
				&& repeatingGroupLength != REPEATING_GROUP_SINGLE_BYTE_UNICODE_SCALAR
				&& repeatingGroupLength != REPEATING_GROUP_DOUBLE_BYTE_UNICODE_SCALAR) {
			throw new IllegalArgumentException("invalid repeating group length for code page index");
		}		
		this.repeatingGroupLength = repeatingGroupLength;
	}
		
	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_CPI) {
			throw new IllegalArgumentException("invalid structure field id for code page descriptor");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		if (parameters.size() % repeatingGroupLength != 0) {
			throw new IllegalArgumentException("invalid structure field parameters");
		}
		int offset = 0;
		while (offset < parameters.size()) {
			Entry entry = new Entry();
			entry.graphicCharacterGID = parameters.getString(offset, 8).trim();
			entry.useFlags = parameters.getUBIN1(offset + 8);
			if (repeatingGroupLength == REPEATING_GROUP_SINGLE_BYTE
					|| repeatingGroupLength == REPEATING_GROUP_SINGLE_BYTE_UNICODE_SCALAR) {
				entry.codepoint = parameters.getUBIN1(offset + 9);
				offset += 10;
			}
			else {
				entry.codepoint = parameters.getUBIN2(offset + 9);
				offset += 11;
			}
			if (repeatingGroupLength == REPEATING_GROUP_SINGLE_BYTE_UNICODE_SCALAR
					|| repeatingGroupLength == REPEATING_GROUP_DOUBLE_BYTE_UNICODE_SCALAR) {
				throw new IllegalArgumentException("we don't support unicode scalar right now");
			}
			entries.add(entry);
		}
	}
	
	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_CPI);
//		MODCAByteBuffer parameters = field.getSFParameterData();
		// TODO: ADD CODE HERE
		return field;
	}

}
