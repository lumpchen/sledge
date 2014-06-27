package com.docscience.pathfinder.font.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FontMetrics implements Serializable {

	private int unitsPerEm;
	private int ascent;
	private int descent;
	private int lineGap;
	private transient short[] advancedWidths;
	private short[] advancedWidthsCompressed;
	private transient short[] leftSideBearings;
	private short[] leftSideBearingsCompressed;
	private int xMin;
	private int yMin;
	private int xMax;
	private int yMax;

	public int getUnitsPerEm() {
		return unitsPerEm;
	}

	public void setUnitsPerEm(int unitsPerEm) {
		this.unitsPerEm = unitsPerEm;
	}

	public int getAscent() {
		return ascent;
	}

	public void setAscent(int ascent) {
		this.ascent = ascent;
	}

	public int getDescent() {
		return descent;
	}

	public void setDescent(int descent) {
		this.descent = descent;
	}

	public int getLineGap() {
		return lineGap;
	}

	public void setLineGap(int lineGap) {
		this.lineGap = lineGap;
	}

	public int getXMin() {
		return xMin;
	}

	public void setXMin(int xMin) {
		this.xMin = xMin;
	}

	public int getYMin() {
		return yMin;
	}

	public void setYMin(int yMin) {
		this.yMin = yMin;
	}

	public int getXMax() {
		return xMax;
	}

	public void setXMax(int xMax) {
		this.xMax = xMax;
	}

	public int getYMax() {
		return yMax;
	}

	public void setYMax(int yMax) {
		this.yMax = yMax;
	}

	public short[] getAdvancedWidths() {
		if (advancedWidths == null && advancedWidthsCompressed != null) {
			advancedWidths = RunLengthEncoding.decode(advancedWidthsCompressed);
		}
		return advancedWidths;
	}

	public void setAdvancedWidths(short[] advancedWidths) {
		this.advancedWidths = advancedWidths;
		this.advancedWidthsCompressed = RunLengthEncoding
				.encode(advancedWidths);
	}

	public short[] getLeftSideBearings() {
		if (leftSideBearings == null && leftSideBearingsCompressed != null) {
			leftSideBearings = RunLengthEncoding
					.decode(leftSideBearingsCompressed);
		}
		return leftSideBearings;
	}

	public void setLeftSideBearings(short[] leftSideBearings) {
		this.leftSideBearings = leftSideBearings;
		this.leftSideBearingsCompressed = RunLengthEncoding
				.encode(leftSideBearings);
	}

	public int getAdvancedWidth(int glyphId) {
		short[] advancedWidths = getAdvancedWidths();
		if (glyphId >= advancedWidths.length) {
			return advancedWidths[advancedWidths.length - 1] & 0xffff;
		} else {
			return advancedWidths[glyphId] & 0xffff;
		}
	}

	public int getLeftSideBearing(int glyphId) {
		short[] leftSideBearings = getLeftSideBearings();
		if (glyphId >= leftSideBearings.length) {
			return leftSideBearings[leftSideBearings.length - 1];
		} else {
			return leftSideBearings[glyphId];
		}
	}
	
}
