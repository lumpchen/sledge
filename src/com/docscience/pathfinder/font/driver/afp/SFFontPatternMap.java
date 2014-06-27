package com.docscience.pathfinder.font.driver.afp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wxin
 *
 */
public class SFFontPatternMap {
	
	public static final int REPEATING_GROUP_LENGTH = 0x08;
	
	public static class Index {
		
		private String graphicCharacterGID;
		private int characterBoxWidth;
		private int characterBoxHeight;
		private int patternDataOffset;
		
		public Index(String graphicCharacterGID) {
			this.graphicCharacterGID = graphicCharacterGID;
		}
		
		public String getGraphicCharacterGID() {
			return graphicCharacterGID;
		}

		public void setGraphicCharacterGID(String gcgid) {
			this.graphicCharacterGID = gcgid;
		}

		public int getcharacterBoxWidth() {
			return characterBoxWidth;
		}

		public void setcharacterBoxWidth(int characterBoxWidth) {
			this.characterBoxWidth = characterBoxWidth;
		}

		public int getcharacterBoxHeight() {
			return characterBoxHeight;
		}

		public void setcharacterBoxHeight(int characterBoxHeight) {
			this.characterBoxHeight = characterBoxHeight;
		}

		public int getpatternDataOffset() {
			return patternDataOffset;
		}

		public void setpatternDataOffset(int patternDataOffset) {
			this.patternDataOffset = patternDataOffset;
		}

		public void load(MODCAByteBuffer buffer, int offset) {
			characterBoxWidth = buffer.getUBIN2(offset);
			characterBoxHeight = buffer.getUBIN2(offset + 2);
			patternDataOffset = (int)buffer.getUBIN4(offset + 4);
		}
		
		private void save(MODCAByteBuffer buffer) {
			int size = buffer.size();
			buffer.putUBIN2(characterBoxWidth);
			buffer.putUBIN2(characterBoxHeight);
			buffer.putUBIN4(patternDataOffset);
			assert(buffer.size() - size == REPEATING_GROUP_LENGTH);
		}
	}
	
	private final int repeatingGroupLength = REPEATING_GROUP_LENGTH;
	private List<Index> patternList = new ArrayList<Index>();
	
	public SFFontPatternMap() {
	}
	
	public int getRepeatingGroupLength() {
		return this.repeatingGroupLength;
	}
	
	public int getNumIndexs() {
		return patternList.size();
	}
	
	public Index[] getIndexs() {
		return patternList.toArray(new Index[this.patternList.size()]);
	}
	
	public void addIndex(Index index) {
		patternList.add(index);
	}
	
	public void setStructureField(MODCAStructureField field, String graphicCharacterGID) {
		if (field.getSFID() != MODCAStructureField.ID_FNM) {
			throw new IllegalArgumentException("invalid structure field id for font pattern map");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		if (parameters.size() % repeatingGroupLength != 0) {
			throw new IllegalArgumentException("bad structure field parameters");
		}
		int numRepeatingGroups = parameters.size() / repeatingGroupLength;
		for (int i=0; i<numRepeatingGroups; ++i) {
			int offset = i * repeatingGroupLength;
			Index index = new Index(graphicCharacterGID);
			index.load(parameters, offset);
			patternList.add(index);
		}
	}

	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_FNM);
		Iterator<Index> itr = patternList.listIterator();
		while (itr.hasNext()) {
			Index index = (Index)itr.next();
			index.save(field.getSFParameterData());
		}
		return field;
	}
	
}
