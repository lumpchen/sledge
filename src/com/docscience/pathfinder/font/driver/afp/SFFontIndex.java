package com.docscience.pathfinder.font.driver.afp;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author wxin
 *
 */
public class SFFontIndex {
	
	public static final int RASTER_REPEATING_GROUP_LENGTH = 0x0A;
	public static final int OUTLINE_REPEATING_GROUP_LENGTH = 0x1C;
	
	public static class Index {
		
		private String graphicCharacterGID;
		private int characterIncrement;
		private int ascenderHeight;
		private int descenderDepth;
		private int fnmIndex;
		private int aSpace;
		private int bSpace;
		private int cSpace;
		private int baselineOffset;
		private int repeatingGroupLength;
		
		public Index(int repeatingGroupLength) {
			this.repeatingGroupLength = repeatingGroupLength;
		}
		
		public String getGraphicCharacterGID() {
			return graphicCharacterGID;
		}

		public void setGraphicCharacterGID(String gcgid) {
			this.graphicCharacterGID = gcgid;
		}

		public int getCharacterIncrement() {
			return characterIncrement;
		}

		public void setCharacterIncrement(int characterIncrement) {
			this.characterIncrement = characterIncrement;
		}

		public int getAscenderHeight() {
			return ascenderHeight;
		}

		public void setAscenderHeight(int ascenderHeight) {
			this.ascenderHeight = ascenderHeight;
		}

		public int getDescenderDepth() {
			return descenderDepth;
		}

		public void setDescenderDepth(int descenderDepth) {
			this.descenderDepth = descenderDepth;
		}

		public int getFNMIndex() {
			return fnmIndex;
		}

		public void setFNMIndex(int fnmIndex) {
			this.fnmIndex = fnmIndex;
		}

		public int getASpace() {
			return aSpace;
		}

		public void setASpace(int space) {
			aSpace = space;
		}

		public int getBSpace() {
			return bSpace;
		}

		public void setBSpace(int space) {
			bSpace = space;
		}

		public int getCSpace() {
			return cSpace;
		}

		public void setCSpace(int space) {
			cSpace = space;
		}

		public int getBaselineOffset() {
			return baselineOffset;
		}

		public void setBaselineOffset(int baselineOffset) {
			this.baselineOffset = baselineOffset;
		}

		public void load(MODCAByteBuffer buffer, int offset) {
			graphicCharacterGID = buffer.getString(offset + 0, 8).trim();
			characterIncrement = buffer.getUBIN2(offset + 8);
			if (repeatingGroupLength <= 10) {
				return;
			}
			ascenderHeight = buffer.getSBIN2(offset + 10);
			descenderDepth = buffer.getSBIN2(offset + 12);
			fnmIndex = buffer.getUBIN2(offset + 16);
			aSpace = buffer.getSBIN2(offset + 18);
			bSpace = buffer.getUBIN2(offset + 20);
			cSpace = buffer.getSBIN2(offset + 22);
			baselineOffset = buffer.getSBIN2(offset + 26);
		}
		
		private void save(MODCAByteBuffer buffer) {
			int size = buffer.size();
			buffer.putString(graphicCharacterGID, 8);
			buffer.putUBIN2(characterIncrement);
			if (repeatingGroupLength <= 10) {
				return;
			}
			buffer.putSBIN2(ascenderHeight);
			buffer.putSBIN2(descenderDepth);
			buffer.putUBIN2(0);
			buffer.putUBIN2(fnmIndex);
			buffer.putSBIN2(aSpace);
			buffer.putUBIN2(bSpace);
			buffer.putSBIN2(cSpace);
			buffer.putUBIN2(0);
			buffer.putSBIN2(baselineOffset);
			assert(buffer.size() - size == OUTLINE_REPEATING_GROUP_LENGTH);
		}
	}
	
	private final int repeatingGroupLength;
	private SortedMap<String, Index> indexMap = new TreeMap<String, Index>();
	
	public SFFontIndex(int repeatingGroupLength) {
		this.repeatingGroupLength = repeatingGroupLength;
	}
	
	public int getRepeatingGroupLength() {
		return this.repeatingGroupLength;
	}
	
	public int getNumIndexs() {
		return indexMap.size();
	}
	
	public Index[] getIndexs() {
		return indexMap.values().toArray(new Index[indexMap.size()]);
	}
	
	public void addIndex(Index index) {
		assert(index.repeatingGroupLength == this.repeatingGroupLength);
		indexMap.put(index.getGraphicCharacterGID(), index);
	}
	
	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_FNI) {
			throw new IllegalArgumentException("invalid structure field id for font index");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		if (parameters.size() % repeatingGroupLength != 0) {
			throw new IllegalArgumentException("bad structure field parameters");
		}
		int numRepeatingGroups = parameters.size() / repeatingGroupLength;
		for (int i=0; i<numRepeatingGroups; ++i) {
			int offset = i * repeatingGroupLength;
			Index index = new Index(repeatingGroupLength);
			index.load(parameters, offset);
			indexMap.put(index.graphicCharacterGID, index);
		}
	}

	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_FNI);
		Iterator<Entry<String, Index>> itr = indexMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Index> entry = itr.next();
			Index index = (Index) entry.getValue();
			index.save(field.getSFParameterData());
		}
		return field;
	}
	
}
