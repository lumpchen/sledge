package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFLookupTable {

	/**
	 * This bit relates only to the correct processing of the cursive attachment
	 * lookup type (GPOS lookup type 3). When this bit is set, the last glyph in
	 * a given sequence to which the cursive attachment lookup is applied, will
	 * be positioned on the baseline. Note: Setting of this bit is not intended
	 * to be used by operating systems or applications to determine text
	 * direction.
	 */
	public static final int RIGHT_TO_LEFT = 0x0001;

	/** If set, skips over base glyphs */
	public static final int IGNORE_BASE_GLYPHS = 0x0002;

	/** If set, skips over ligatures */
	public static final int IGNORE_LIGATURES = 0x0004;

	/** If set, skips over combining marks */
	public static final int IGNORE_MARKS = 0x0008;

	/**
	 * If set, indicates that the lookup table structure is followed by a
	 * MarkFilteringSet field. The layout engine skips over all mark glyphs not
	 * in the mark filtering set indicated.
	 */
	public static final int USE_MARK_FILTERING_SET = 0x0010;

	/**
	 * If not zero, skips over all marks of attachment type different from
	 * specified.
	 */
	public static final int MARK_ATTACHMENT_TYPE = 0xFF00;

	private int lookupType;

	private int lookupFlag;

	private OTFLookupSubTable[] lookupSubTables;

	private final OTFLookupSubTableReader lookupSubTableReader;

	public OTFLookupTable(OTFLookupSubTableReader subTableReader) {
		this.lookupSubTableReader = subTableReader;
	}

	public int getLookupType() {
		return this.lookupType;
	}

	public int getLookupFlag() {
		return this.lookupFlag;
	}

	public boolean rightToLeft() {
		return (this.lookupFlag & RIGHT_TO_LEFT) != 0;
	}

	public boolean ignoreBaseGlyphs() {
		return (this.lookupFlag & IGNORE_BASE_GLYPHS) != 0;
	}

	public boolean ignoreLigatures() {
		return (this.lookupFlag & IGNORE_LIGATURES) != 0;
	}

	public boolean ignoreMarks() {
		return (this.lookupFlag & IGNORE_MARKS) != 0;
	}

	public boolean useMarkFilteringSet() {
		return (this.lookupFlag & USE_MARK_FILTERING_SET) != 0;
	}

	public int getMarkAttachmentType() {
		return (this.lookupFlag & MARK_ATTACHMENT_TYPE) >> 8;
	}

	public int getLookupSubTableCount() {
		return this.lookupSubTables.length;
	}

	public OTFLookupSubTable getLookupSubTable(int index) {
		return this.lookupSubTables[index];
	}

	public void read(TTFRandomReader rd) throws IOException, TTFFormatException {
		long offset = rd.getPosition();
		this.lookupType = rd.readTTFUShort();
		this.lookupFlag = rd.readTTFUShort();
		int subTableCount = rd.readTTFUShort();
		int[] subTableOffsets = new int[subTableCount];
		for (int i = 0; i < subTableCount; i++) {
			subTableOffsets[i] = rd.readTTFOffset();
		}

		this.lookupSubTables = new OTFLookupSubTable[subTableCount];
		for (int i = 0; i < subTableCount; i++) {
			rd.setPosition(offset + subTableOffsets[i]);
			lookupSubTables[i] = lookupSubTableReader.read(lookupType, rd);
		}
	}
}