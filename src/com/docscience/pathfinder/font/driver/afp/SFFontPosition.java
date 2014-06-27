package com.docscience.pathfinder.font.driver.afp;

/**
 * @author wxin
 *
 */
public class SFFontPosition {

	public static final int REPEATING_GROUP_LENGTH = 0x16; // 22
	
	public static class Position {
		
		int lowerCaseHeight;
		int capMHeight;
		int maxAscenderHeight;
		int maxDescenderHeight;
		int underscoreWidthUnit;
		int underscoreWidthFraction;
		int underscorePosition;
		
		public int getLowerCaseHeight() {
			return lowerCaseHeight;
		}

		public void setLowerCaseHeight(int lowerCaseHeight) {
			this.lowerCaseHeight = lowerCaseHeight;
		}

		public int getCapMHeight() {
			return capMHeight;
		}

		public void setCapMHeight(int capMHeight) {
			this.capMHeight = capMHeight;
		}

		public int getMaxAscenderHeight() {
			return maxAscenderHeight;
		}

		public void setMaxAscenderHeight(int maxAscenderHeight) {
			this.maxAscenderHeight = maxAscenderHeight;
		}

		public int getMaxDescenderHeight() {
			return maxDescenderHeight;
		}

		public void setMaxDescenderHeight(int maxDescenderHeight) {
			this.maxDescenderHeight = maxDescenderHeight;
		}

		public int getUnderscoreWidthUnit() {
			return underscoreWidthUnit;
		}

		public void setUnderscoreWidthUnit(int underscoreWidthUnit) {
			this.underscoreWidthUnit = underscoreWidthUnit;
		}

		public int getUnderscoreWidthFraction() {
			return underscoreWidthFraction;
		}

		public void setUnderscoreWidthFraction(int underscoreWidthFraction) {
			this.underscoreWidthFraction = underscoreWidthFraction;
		}

		public int getUnderscorePosition() {
			return underscorePosition;
		}

		public void setUnderscorePosition(int underscorePosition) {
			this.underscorePosition = underscorePosition;
		}

		private void load(MODCAByteBuffer buffer, int offset) {
			lowerCaseHeight = buffer.getSBIN2(offset + 2);
			capMHeight = buffer.getSBIN2(offset + 4);
			maxAscenderHeight = buffer.getSBIN2(offset + 6);
			maxDescenderHeight = buffer.getSBIN2(offset + 8);
			underscoreWidthUnit = buffer.getUBIN2(offset + 17);
			underscoreWidthFraction = buffer.getUBIN1(offset + 19);
			underscorePosition = buffer.getSBIN2(offset + 20);
		}
		
		private void save(MODCAByteBuffer buffer) {
			int size = buffer.size();
			buffer.putUBIN2(0);
			buffer.putSBIN2(lowerCaseHeight);
			buffer.putSBIN2(capMHeight);
			buffer.putSBIN2(maxAscenderHeight);
			buffer.putSBIN2(maxDescenderHeight);
			buffer.putUBIN4(0);
			buffer.putUBIN1(0);
			buffer.putUBIN1(1);
			buffer.putUBIN1(0);
			buffer.putUBIN2(underscoreWidthUnit);
			buffer.putUBIN1(underscoreWidthFraction);
			buffer.putSBIN2(underscorePosition);
			assert(buffer.size() - size == REPEATING_GROUP_LENGTH);
		}
		
	}

	private Position[] positions;
	
	public int getNumPositions() {
		return positions.length;
	}
	
	public Position getPosition(int i) {
		return positions[i];
	}
	
	public void setPosition(int i, Position pos) {
		positions[i] = pos;
	}
	
	public void addPosition(Position pos) {
		int index = 0;
		if (positions == null) {
			positions = new Position[1];
		}
		else {
			index = positions.length;
			Position[] temp = new Position[positions.length + 1];
			for (int i=0; i<positions.length; ++i) {
				temp[i] = positions[i];
			}
			positions = temp;
		}
		positions[index] = pos;
	}
	
	public void setStructureField(MODCAStructureField field) {
		if (field.getSFID() != MODCAStructureField.ID_FNP) {
			throw new IllegalArgumentException("invalid structure field id for font position");
		}
		MODCAByteBuffer parameters = field.getSFParameterData();
		if (parameters.size() % REPEATING_GROUP_LENGTH != 0) {
			throw new IllegalArgumentException("bad structure field parameters");
		}
		int numRepeatingGroups = parameters.size() / REPEATING_GROUP_LENGTH;
		positions = new Position[numRepeatingGroups];
		for (int i=0; i<numRepeatingGroups; ++i) {
			int offset = i * REPEATING_GROUP_LENGTH;
			Position pos = new Position();
			pos.load(parameters, offset);
			positions[i] = pos;
		}
	}

	public MODCAStructureField getStructureField() {
		MODCAStructureField field = new MODCAStructureField();
		field.setSFID(MODCAStructureField.ID_FNP);
		for (int i=0; i<positions.length; ++i) {
			positions[i].save(field.getSFParameterData());
		}
		return field;
	}
	
}
