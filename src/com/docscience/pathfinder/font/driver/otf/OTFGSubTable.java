package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFGSubTable extends OTFGCommonTable {

	public static final int LOOKUP_TYPE_SINGLE = 1; // Replace one glyph with one glyph
	public static final int LOOKUP_TYPE_MULTIPLE = 2; // Replace one glyph with more than one glyph 
	public static final int LOOKUP_TYPE_ALTERNATE = 3; // Replace one glyph with one of many glyphs 
	public static final int LOOKUP_TYPE_LIGATURE = 4; // Replace multiple glyphs with one glyph 
	public static final int LOOKUP_TYPE_CONTEXT = 5; //  Replace one or more glyphs in context 
	public static final int LOOKUP_TYPE_CHAINING_CONTEXT = 6; //  Replace one or more glyphs in chained context 
	public static final int LOOKUP_TYPE_EXTENSION_SUBSTITUTION = 7; // Extension mechanism for other substitutions (i.e. this excludes the Extension type substitution itself) 
	public static final int LOOKUP_TYPE_REVERSE_CHAINING_CONTEXT = 8; // Applied in reverse order, replace single glyph in chaining context
	
	public static class SingleSubstFormat1 extends SingleSubst {
		OTFCoverageTable coverageTable;
		int deltaGlyphID;

		@Override
		public void read(TTFRandomReader rd) throws IOException, TTFFormatException {
			long offset = rd.getPosition() - 2;
			long coverageOffset = rd.readTTFOffset();
			this.deltaGlyphID = rd.readTTFShort();

			rd.setPosition(offset + coverageOffset);
			this.coverageTable = OTFCoverageTable.readCoverageTable(rd);
		}

		@Override
		public int getSubstitute(int glyphID) {
			if (coverageTable.isInCoverage(glyphID)) {
				return glyphID + deltaGlyphID;
			} else {
				return glyphID;
			}
		}
	}
	
	public static class SingleSubstFormat2 extends SingleSubst {
		private OTFCoverageTable coverageTable;
		private int[] substitute;

		@Override
		public void read(TTFRandomReader rd) throws IOException, TTFFormatException {
			long offset = rd.getPosition() - 2;
			long coverageOffset = rd.readTTFOffset();
			int glyphCount = rd.readTTFUShort();
			this.substitute = new int[glyphCount];
			for (int i=0; i<glyphCount; ++i) {
				this.substitute[i] = rd.readTTFGlyphID();
			}
			rd.setPosition(offset + coverageOffset);
			this.coverageTable = OTFCoverageTable.readCoverageTable(rd);
		}

		@Override
		public int getSubstitute(int glyphID) {
			int index = this.coverageTable.getCoverageIndex(glyphID);
			if (index >= 0) {
				return this.substitute[index];
			} else {
				return glyphID;
			}
		}
	}
	
	public static abstract class SingleSubst extends OTFLookupSubTable {
		
		public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;
		
		public abstract int getSubstitute(int glyphID);
		
		public static SingleSubst readSingleSubstTable(TTFRandomReader rd) throws IOException, TTFFormatException {
			int format = rd.readTTFUShort();
			if (format == 1) {
				SingleSubstFormat1 st = new SingleSubstFormat1();
				st.read(rd);
				return st;
			} else if (format == 2) {
				SingleSubstFormat2 st = new SingleSubstFormat2();
				st.read(rd);
				return st;
			} else {
				throw new TTFFormatException("unsupported single substitute table format [" + format + "]");
			}
		}

	}
	
	public static class MultipleSubstFormat1 extends MultipleSubst {

		private int[][] sequence;
		private OTFCoverageTable coverageTable;

		@Override
		public void read(TTFRandomReader rd) throws IOException,
				TTFFormatException {
			long offset = rd.getPosition() - 2;
			long coverageOffset = rd.readTTFOffset();
			int sequenceCount = rd.readTTFUShort();
			this.sequence = new int[sequenceCount][];
			for (int i=0; i<sequenceCount; ++i) {
				int glyphCount = rd.readTTFUShort();
				int[] glyphs = new int[glyphCount];
				for (int j=0; j<glyphCount; ++j) {
					glyphs[j] = rd.readTTFGlyphID();
				}
				this.sequence[i] = glyphs;
			}
			
			rd.setPosition(offset + coverageOffset);
			this.coverageTable = OTFCoverageTable.readCoverageTable(rd);
		}

		@Override
		public int[] getSubstitute(int glyphID) {
			int index = coverageTable.getCoverageIndex(glyphID);
			if (index >= 0) {
				return this.sequence[index].clone();
			} else {
				return new int[] {glyphID};
			}
		}

	}
	
	public static abstract class MultipleSubst extends OTFLookupSubTable {
		
		public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;
		
		public abstract int[] getSubstitute(int glyphID);
		
		public static MultipleSubst readMultipleSubst(TTFRandomReader rd) throws IOException, TTFFormatException {
			int format = rd.readTTFUShort();
			if (format == 1) {
				MultipleSubstFormat1 st = new MultipleSubstFormat1();
				st.read(rd);
				return st;
			} else {
				throw new TTFFormatException("unsupported multiple substitute table format [" + format + "]");
			}
		}
	}

	public static class AlternateSubstFormat1 extends AlternateSubst {

		private OTFCoverageTable coverageTable;
		private int[][] alternateSet;

		@Override
		public int[] getAlternate(int glyphID) {
			int index = this.coverageTable.getCoverageIndex(glyphID);
			if (index >= 0) {
				return alternateSet[index].clone();
			} else {
				return new int[] {glyphID};
			}
		}

		@Override
		public void read(TTFRandomReader rd) throws IOException,
				TTFFormatException {
			long offset = rd.getPosition() - 2;
			long coverageOffset = rd.readTTFOffset();
			
			int alternateCount = rd.readTTFUShort();
			
			int[] alternateSetOffsets = new int[alternateCount];
			for (int i=0; i<alternateCount; ++i) {
				alternateSetOffsets[i] = rd.readTTFOffset();
			}
			
			this.alternateSet = new int[alternateCount][];
			for (int i=0; i<alternateCount; ++i) {
				rd.setPosition(offset + alternateSetOffsets[i]);
				int glyphCount = rd.readTTFUShort();
				int[] glyphs = new int[glyphCount];
				for (int j=0; j<glyphs.length; ++j) {
					glyphs[j] = rd.readTTFGlyphID();
				}
				this.alternateSet[i] = glyphs;
			}
			
			rd.setPosition(offset + coverageOffset);
			this.coverageTable = OTFCoverageTable.readCoverageTable(rd);
		}
		
	}
	
	public static abstract class AlternateSubst extends OTFLookupSubTable {
		
		public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;
		
		public abstract int[] getAlternate(int glyphID);
			
		public static AlternateSubst readAlternateSubst(TTFRandomReader rd) throws IOException, TTFFormatException {
			int format = rd.readTTFUShort();
			if (format == 1) {
				AlternateSubstFormat1 st = new AlternateSubstFormat1();
				st.read(rd);
				return st;
			} else {
				throw new TTFFormatException("unsupported alternate substitute table format [" + format + "]");
			}
		}
	}

	public static class LigatureSet {
	
		private Ligature[] ligatures;
		
		public void read(TTFRandomReader rd) throws IOException, TTFFormatException {
			long offset = rd.getPosition();
			int ligatureCount = rd.readTTFUShort();
			
			int[] ligatureOffsets = new int[ligatureCount];
			for (int i=0; i<ligatureCount; ++i) {
				ligatureOffsets[i] = rd.readTTFOffset();
			}
			
			this.ligatures = new Ligature[ligatureCount];
			for (int i=0; i<ligatureCount; ++i) {
				rd.setPosition(offset + ligatureOffsets[i]);
				this.ligatures[i] = new Ligature(); 
				this.ligatures[i].read(rd);
			}
		}
		
	}
	
	public static class Ligature {

		private int ligatureGlyph;
		private int[] components;

		public void read(TTFRandomReader rd) throws IOException, TTFFormatException {
			this.ligatureGlyph = rd.readTTFGlyphID();
			int componentCount = rd.readTTFUShort() - 1;
			this.components = new int[componentCount];
			for (int i=0; i<componentCount; ++i) {
				this.components[i] = rd.readTTFGlyphID();
			}
		}
		
	}
	
	public static class LigatureSubstFormat1 extends LigatureSubst {

		private LigatureSet[] ligatureSets;
		private OTFCoverageTable coverageTable;
		
		@Override
		public void read(TTFRandomReader rd) throws IOException,
				TTFFormatException {
			long offset = rd.getPosition() - 2;
			long coverageOffset = rd.readTTFOffset();
		
			int ligatureSetCount = rd.readTTFUShort();
			int[] ligatureSetOffsets = new int[ligatureSetCount];
			for (int i=0; i<ligatureSetCount; ++i) {
				ligatureSetOffsets[i] = rd.readTTFOffset();
			}
			
			this.ligatureSets = new LigatureSet[ligatureSetCount];
			for (int i=0; i<ligatureSetCount; ++i) {
				rd.setPosition(offset + ligatureSetOffsets[i]);
				this.ligatureSets[i] = new LigatureSet();
				this.ligatureSets[i].read(rd);
			}
			
			rd.setPosition(offset + coverageOffset);
			this.coverageTable = OTFCoverageTable.readCoverageTable(rd);
		}

		private boolean match(Ligature ligature, int[] glyphs, int index) {
			if (index + ligature.components.length > glyphs.length) {
				return false;
			}
			for (int i=0; i<ligature.components.length; ++i) {
				if (ligature.components[i] != glyphs[index + i]) {
					return false;
				}
			}
			return true;
		}
		
		@Override
		public int[] substitute(int[] glyphs) {
			int[] sub = new int[glyphs.length];
			int n = 0;
			for (int i=0; i<glyphs.length; i++, n++) {
				int gid = glyphs[i];
				sub[n] = gid;

				int coverageIndex = this.coverageTable.getCoverageIndex(gid);
				if (coverageIndex >= 0) {
					Ligature[] ligatures = this.ligatureSets[coverageIndex].ligatures;
					for (int j=0; j<ligatures.length; ++j) {
						if (match(ligatures[j], glyphs, i + 1)) {
							sub[n] = ligatures[j].ligatureGlyph;
							i += ligatures[j].components.length;
							break;
						}
					}
				}
			}
			
			int[] result = new int[n];
			
			System.arraycopy(sub, 0, result, 0, n);
			
			return result;
		}
		
	}
	
	public static abstract class LigatureSubst extends OTFLookupSubTable {
		
		public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;
		
		public abstract int[] substitute(int[] glyphs);
		
		public static LigatureSubst readLigatureSubst(TTFRandomReader rd) throws IOException, TTFFormatException {
			int format = rd.readTTFUShort();
			if (format == 1) {
				LigatureSubstFormat1 st = new LigatureSubstFormat1();
				st.read(rd);
				return st;
			} else {
				throw new TTFFormatException("unsupported ligature substitute table format [" + format + "]");
			}
		}
	}
		
	public static class ContextSubstFormat1 extends ContextSubst {

		@Override
		public void read(TTFRandomReader rd) throws IOException,
				TTFFormatException {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static class ContextSubstFormat2 extends ContextSubst {

		@Override
		public void read(TTFRandomReader rd) throws IOException,
				TTFFormatException {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static class ContextSubstFormat3 extends ContextSubst {

		@Override
		public void read(TTFRandomReader rd) throws IOException,
				TTFFormatException {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static abstract class ContextSubst extends OTFLookupSubTable {
		
		public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;
		
		public static ContextSubst readContextSubst(TTFRandomReader rd) throws IOException, TTFFormatException {
			int format = rd.readTTFUShort();
			if (format == 1) {
				ContextSubstFormat1 st = new ContextSubstFormat1();
				st.read(rd);
				return st;
			} else if (format == 2) {
				ContextSubstFormat2 st = new ContextSubstFormat2();
				st.read(rd);
				return st;
			} else if (format == 3) {
				ContextSubstFormat3 st = new ContextSubstFormat3();
				st.read(rd);
				return st;
			} else {
				throw new TTFFormatException("unsupported context substitute table format [" + format + "]");	
			}

		}
	}
	
	private static class LookupSubTableReader implements OTFLookupSubTableReader {

		public static final LookupSubTableReader instance = new LookupSubTableReader();

		@Override
		public OTFLookupSubTable read(int lookupType, TTFRandomReader rd)
				throws IOException, TTFFormatException {
			switch (lookupType) {
			case LOOKUP_TYPE_SINGLE:
				return SingleSubst.readSingleSubstTable(rd);
			case LOOKUP_TYPE_MULTIPLE:
				return MultipleSubst.readMultipleSubst(rd);
			case LOOKUP_TYPE_ALTERNATE:
				return AlternateSubst.readAlternateSubst(rd);
			case LOOKUP_TYPE_LIGATURE:
				return LigatureSubst.readLigatureSubst(rd);
			case LOOKUP_TYPE_CONTEXT:
				return ContextSubst.readContextSubst(rd);
			case LOOKUP_TYPE_CHAINING_CONTEXT:
			case LOOKUP_TYPE_EXTENSION_SUBSTITUTION:
			case LOOKUP_TYPE_REVERSE_CHAINING_CONTEXT:
				// TODO ADD CODE HERE
				return null;
			default:
				throw new TTFFormatException("unsupported lookup type [" + lookupType + "]");
			}
		}		
		
	}
	
	@Override
	public int getTag() {
		return TAG_GSUB_TABLE;
	}

	@Override
	protected OTFLookupSubTableReader getLookupSubTableReader() {
		return LookupSubTableReader.instance;
	}
		
}