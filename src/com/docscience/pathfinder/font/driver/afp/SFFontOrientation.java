package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class SFFontOrientation {
	
	public static final int REPEATING_GROUP_LENGTH = 0x1A;
	
	public static final int ROTATE_0_DEGREES   = 0x0000;
	public static final int ROTATE_90_DEGREES  = 0x2D00;
	public static final int ROTATE_180_DEGREES = 0x5A00;
	public static final int ROTATE_270_DEGREES = 0x8700;
	
	public static class Orientation {
		
		private int characterRotation;
		private int baselineOffset;
		private int characterIncrement;
		private int spaceCharacterIncrement;
		private int maxBaselineExtent;
		private int orientationControlFlags;
		private int emSpaceIncrement;
		private int figureSpaceIncrement;
		private int nominalCharacterIncrement;
		private int defaultBaselineIncrement;
		private int aSpace;
		
		public int getIndexNumber() {
			 return (orientationControlFlags >> 5) & 7;
		}
		
		public void setIndexNumber(int index) {
			orientationControlFlags &= 0x1f;
			orientationControlFlags |= ((index << 5) & 7);
		}
		
		public boolean hasKerning() {
			return (orientationControlFlags & 8) != 0;
		}
		
		public void setKerning(boolean b) {
			if (b) {
				orientationControlFlags |= 8;
			}
			else {
				orientationControlFlags &= ~8;
			}
		}
		
		public boolean isUniformASpace() {
			return ((orientationControlFlags & 4) != 0);
		}
		
		public void setUniformASpace(boolean b) {
			if (b) {
				orientationControlFlags |= 4;
			}
			else {
				orientationControlFlags &= ~4;
			}
		}
		
		public boolean isUniformBaselineOffset() {
			return ((orientationControlFlags & 2) != 0);
		}
		
		public void setUniformBaselineOffset(boolean b) {
			if (b) {
				orientationControlFlags |= 2;
			}
			else {
				orientationControlFlags &= ~2;
			}			
		}
		
		public boolean isUniformCharacterIncrement() {
			return ((orientationControlFlags & 1) != 0);
		}
		
		public void setUniformCharacterIncrement(boolean b) {
			if (b) {
				orientationControlFlags |= 1;
			}
			else {
				orientationControlFlags &= ~1;
			}
		}
		
		public int getCharacterRotation() {
			return characterRotation;
		}

		public void setCharacterRotation(int characterRotation) {
			this.characterRotation = characterRotation;
		}

		public int getBaselineOffset() {
			return baselineOffset;
		}

		public void setBaselineOffset(int baselineOffset) {
			this.baselineOffset = baselineOffset;
		}

		public int getCharacterIncrement() {
			return characterIncrement;
		}

		public void setCharacterIncrement(int characterIncrement) {
			this.characterIncrement = characterIncrement;
		}

		public int getSpaceCharacterIncrement() {
			return spaceCharacterIncrement;
		}

		public void setSpaceCharacterIncrement(int spaceCharacterIncrement) {
			this.spaceCharacterIncrement = spaceCharacterIncrement;
		}

		public int getMaxBaselineExtent() {
			return maxBaselineExtent;
		}

		public void setMaxBaselineExtent(int maxBaselineExtent) {
			this.maxBaselineExtent = maxBaselineExtent;
		}

		public int getOrientationControlFlags() {
			return orientationControlFlags;
		}

		public void setOrientationControlFlags(int orientationControlFlags) {
			this.orientationControlFlags = orientationControlFlags;
		}

		public int getEmSpaceIncrement() {
			return emSpaceIncrement;
		}

		public void setEmSpaceIncrement(int emSpaceIncrement) {
			this.emSpaceIncrement = emSpaceIncrement;
		}

		public int getFigureSpaceIncrement() {
			return figureSpaceIncrement;
		}

		public void setFigureSpaceIncrement(int figureSpaceIncrement) {
			this.figureSpaceIncrement = figureSpaceIncrement;
		}

		public int getNominalCharacterIncrement() {
			return nominalCharacterIncrement;
		}

		public void setNominalCharacterIncrement(int nominalCharacterIncrement) {
			this.nominalCharacterIncrement = nominalCharacterIncrement;
		}

		public int getDefaultBaselineIncrement() {
			return defaultBaselineIncrement;
		}

		public void setDefaultBaselineIncrement(int defaultBaselineIncrement) {
			this.defaultBaselineIncrement = defaultBaselineIncrement;
		}

		public int getASpace() {
			return aSpace;
		}

		public void setASpace(int space) {
			aSpace = space;
		}

		private void load(MODCAByteBuffer buffer, int offset) {
			characterRotation = buffer.getUBIN2(offset + 2);
			baselineOffset = buffer.getSBIN2(offset + 4);
			characterIncrement = buffer.getUBIN2(offset + 6);
			spaceCharacterIncrement = buffer.getUBIN2(offset + 8);
			maxBaselineExtent = buffer.getUBIN2(offset + 10);
			orientationControlFlags = buffer.getUBIN1(offset + 12);
			emSpaceIncrement = buffer.getUBIN2(offset + 14);
			figureSpaceIncrement = buffer.getUBIN2(offset + 18);
			nominalCharacterIncrement = buffer.getUBIN2(offset + 20);
			defaultBaselineIncrement = buffer.getUBIN2(offset + 22);
			aSpace = buffer.getSBIN2(offset + 24);
		}
		
		private void save(MODCAByteBuffer buffer) {
			int size = buffer.size();
			buffer.putUBIN2(0);
			buffer.putUBIN2(characterRotation);
			buffer.putSBIN2(baselineOffset);
			buffer.putUBIN2(characterIncrement);
			buffer.putUBIN2(spaceCharacterIncrement);
			buffer.putUBIN2(maxBaselineExtent);
			buffer.putUBIN1(orientationControlFlags);
			buffer.putUBIN1(0);
			buffer.putUBIN2(emSpaceIncrement);
			buffer.putUBIN2(0);
			buffer.putUBIN2(figureSpaceIncrement);
			buffer.putUBIN2(nominalCharacterIncrement);
			buffer.putUBIN2(defaultBaselineIncrement);
			buffer.putSBIN2(aSpace);
			assert(buffer.size() - size == REPEATING_GROUP_LENGTH);
		}
	}

	private Orientation[] orientations;
	
	public int getNumOrientations() {
		return orientations.length;
	}
	
	public Orientation getOrientation(int i) {
		return orientations[i];
	}
	
	public void setOrientation(int i, Orientation orn) {
		orientations[i] = orn;
	}
	
	public void addOrientation(Orientation orn) {
		int index = 0;
		if (orientations == null) {
			orientations = new Orientation[1];
		}
		else {
			index = orientations.length;
			Orientation[] temp = new Orientation[orientations.length + 1];
			for (int i=0; i<orientations.length; ++i) {
				temp[i] = orientations[i];
			}
			orientations = temp;
		}
		orientations[index] = orn;
	}

	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_FNO) {
			throw new IllegalArgumentException("invalid structure field id for font orientation");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		if (parameters.size() % REPEATING_GROUP_LENGTH != 0) {
			throw new IllegalArgumentException("bad structure field parameters");
		}
		int numRepeatingGroups = parameters.size() / REPEATING_GROUP_LENGTH;
		orientations = new Orientation[numRepeatingGroups];
		for (int i=0; i<numRepeatingGroups; ++i) {
			int offset = i * REPEATING_GROUP_LENGTH;
			Orientation orn = new Orientation();
			orn.load(parameters, offset);
			orientations[i] = orn;
		}
	}

	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_FNO);
		for (int i=0; i<orientations.length; ++i) {
			orientations[i].save(field.getSFParameterData());
		}
		return field;
	}
	
}
