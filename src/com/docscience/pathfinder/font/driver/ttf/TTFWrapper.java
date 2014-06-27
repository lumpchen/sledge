/**
 * 
 */
package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import com.docscience.pathfinder.font.driver.cff.CFFFormatException;
import com.docscience.pathfinder.font.driver.cff.CFFRandomReader;
import com.docscience.pathfinder.font.driver.cff.CFFRandomReaderWrapper;
import com.docscience.pathfinder.font.driver.cff.CFFWrapper;
import com.docscience.pathfinder.font.driver.otf.OTFGDefTable;
import com.docscience.pathfinder.font.driver.otf.OTFGPosTable;
import com.docscience.pathfinder.font.driver.otf.OTFGSubTable;
import com.docscience.pathfinder.font.driver.otf.OTFTable;
import com.docscience.pathfinder.font.shared.GlyphPath;

/**
 * @author wxin
 * 
 */
public class TTFWrapper {

	private TTFRandomReader ttfRandomReader;
	private TTFFile ttfFontFile;
	private TTFFontDirectory ttfFontDirectory;

	// --- cached table section ---
	private TTFCMapTable ttfCMapTable;
	private TTFHeadTable ttfHeadTable;
	private TTFHheaTable ttfHheaTable;
	private TTFHmtxTable ttfHmtxTable;
	private TTFLocaTable ttfLocaTable;
	private TTFMaxpTable ttfMaxpTable;
	private TTFNameTable ttfNameTable;
	private TTFOS_2Table ttfOS_2Table;
	private TTFPCLTTable ttfPCLTTable;
	private TTFPostTable ttfPostTable;
	private TTFKernTable ttfKernTable;

	private OTFGSubTable otfGSubTable;
	private OTFGPosTable otfGPosTable;
	private OTFGDefTable otfGDefTable;

	// --- cached TTF name ---
	private Name[] ttfNames;

	// --- cached TTF encoding ---
	private TTFCMapEncoding ttfCMapEncoding;

	// --- cached CFF wrapper ---
	private CFFWrapper cffWrapper;

	public class Name {

		private int nameID;
		private int platformID;
		private int encodingID;
		private int languageID;
		private String unicode;
		private Locale locale;

		public int getNameID() {
			return nameID;
		}

		public int getPlatformID() {
			return platformID;
		}

		public int getEncodingID() {
			return encodingID;
		}

		public int getLanguageID() {
			return languageID;
		}

		public String getUnicode() {
			return unicode;
		}

		public Locale getLocale() {
			return locale;
		}

		@Override
		public String toString() {
			return "Name [nameID=" + nameID + ", platformID=" + platformID
					+ ", encodingID=" + encodingID + ", languageID="
					+ languageID + ", name=" + TTFEncoding.getNameDesc(nameID)
					+ ", unicode=" + unicode + ", locale=" + locale + "]";
		}

	}

	public TTFWrapper(TTFRandomReader reader) throws TTFFormatException,
			IOException {
		ttfRandomReader = reader;
		init();
	}

	private void init() throws TTFFormatException, IOException {
		ttfFontFile = new TTFFile();
		ttfFontFile.read(ttfRandomReader);
	}

	private void clearCachedData() {
		ttfCMapTable = null;
		ttfHeadTable = null;
		ttfHheaTable = null;
		ttfHmtxTable = null;
		ttfLocaTable = null;
		ttfMaxpTable = null;
		ttfNameTable = null;
		ttfOS_2Table = null;
		ttfPCLTTable = null;
		ttfPostTable = null;
		ttfKernTable = null;
		ttfNames = null;
		ttfCMapEncoding = null;
	}

	public int getNumFonts() {
		return ttfFontFile.getNumFonts();
	}

	/**
	 * Select font in TTC file. For TTF Font always use id = 0.
	 * 
	 * @param id
	 */
	public void setCurrentFont(int id) {
		clearCachedData();
		ttfFontDirectory = ttfFontFile.getFontDirection(id);
	}

	public boolean isOpenType() {
		if (this.hasTable(OTFTable.TAG_DSIG_TABLE) || isPostScriptOutline()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPostScriptOutline() {
		return ttfFontDirectory.withCFFOutline();
	}

	public TTCHeader getTTCHeader() {
		return ttfFontFile.getTTCHeader();
	}

	public boolean hasTable(int ttfTableTag) {
		return ttfFontDirectory.hasTable(ttfTableTag);
	}

	public TTFTableDirectory getTableDirectory(int ttfTableTag) {
		return ttfFontDirectory.getTableDirectoryByTag(ttfTableTag);
	}

	public byte[] getTableRawData(int ttfTableTag) throws IOException {
		return getTableRawData(ttfTableTag, null, 0);
	}

	public byte[] getTableRawData(int ttfTableTag, byte[] rawData, int offset)
			throws IOException {
		TTFTableDirectory dir = getTableDirectory(ttfTableTag);
		if (dir == null) {
			return null;
		}
		if (rawData == null) {
			rawData = new byte[(int) dir.getLength()];
		}
		ttfRandomReader.setPosition(dir.getOffset());
		ttfRandomReader.readBytes(rawData, offset, (int) dir.getLength());
		return rawData;
	}

	public TTFCMapTable getCMapTable() throws TTFFormatException, IOException {
		if (ttfCMapTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_CMAP_TABLE);
			if (dir != null) {
				ttfCMapTable = new TTFCMapTable();
				ttfCMapTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfCMapTable;
	}

	public TTFHeadTable getHeadTable() throws TTFFormatException, IOException {
		if (ttfHeadTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_HEAD_TABLE);
			if (dir != null) {
				ttfHeadTable = new TTFHeadTable();
				ttfHeadTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfHeadTable;
	}

	public TTFHheaTable getHheaTable() throws TTFFormatException, IOException {
		if (ttfHheaTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_HHEA_TABLE);
			if (dir != null) {
				ttfHheaTable = new TTFHheaTable();
				ttfHheaTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfHheaTable;
	}

	public TTFHmtxTable getHmtxTable() throws TTFFormatException, IOException {
		if (ttfHmtxTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_HMTX_TABLE);
			if (dir != null) {
				if (getHheaTable() == null) {
					throw new TTFFormatException("hhea table not exist");
				}
				if (getMaxpTable() == null) {
					throw new TTFFormatException("maxp table not exist");
				}
				int numberOfHMetrics = getHheaTable().getNumberOfHMetrics();
				int numGlyphs = getMaxpTable().getNumGlyphs();
				if (numGlyphs < numberOfHMetrics) {
					throw new TTFFormatException(
							"hhea table numberOfHMetrics bigger than maxp table numGlyphs");
				}
				ttfHmtxTable = new TTFHmtxTable(numberOfHMetrics, numGlyphs);
				ttfHmtxTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfHmtxTable;
	}

	public TTFLocaTable getLocaTable() throws TTFFormatException, IOException {
		if (ttfLocaTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_LOCA_TABLE);
			if (dir != null) {
				if (getHeadTable() == null) {
					throw new TTFFormatException("head table not exist");
				}
				if (getMaxpTable() == null) {
					throw new TTFFormatException("maxp table not exist");
				}
				ttfLocaTable = new TTFLocaTable(getHeadTable()
						.getIndexToLocFormat(), getMaxpTable().getNumGlyphs());
				ttfLocaTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfLocaTable;
	}

	public TTFMaxpTable getMaxpTable() throws TTFFormatException, IOException {
		if (ttfMaxpTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_MAXP_TABLE);
			if (dir != null) {
				ttfMaxpTable = new TTFMaxpTable();
				ttfMaxpTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfMaxpTable;
	}

	public TTFNameTable getNameTable() throws TTFFormatException, IOException {
		if (ttfNameTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_NAME_TABLE);
			if (dir != null) {
				ttfNameTable = new TTFNameTable();
				ttfNameTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfNameTable;
	}

	public TTFOS_2Table getOS_2Table() throws TTFFormatException, IOException {
		if (ttfOS_2Table == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_OS_2_TABLE);
			if (dir != null) {
				ttfOS_2Table = new TTFOS_2Table();
				ttfOS_2Table.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfOS_2Table;
	}

	public TTFPCLTTable getPCLTTable() throws TTFFormatException, IOException {
		if (ttfPCLTTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_PCLT_TABLE);
			if (dir != null) {
				ttfPCLTTable = new TTFPCLTTable();
				ttfPCLTTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfPCLTTable;
	}

	public TTFPostTable getPostTable() throws TTFFormatException, IOException {
		if (ttfPostTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_POST_TABLE);
			if (dir != null) {
				ttfPostTable = new TTFPostTable();
				ttfPostTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfPostTable;
	}

	public TTFKernTable getKernTable() throws TTFFormatException, IOException {
		if (ttfKernTable == null) {
			TTFTableDirectory dir = getTableDirectory(TTFTable.TAG_KERN_TABLE);
			if (dir != null) {
				ttfKernTable = new TTFKernTable();
				ttfKernTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return ttfKernTable;
	}

	public OTFGSubTable getGSubTable() throws TTFFormatException, IOException {
		if (otfGSubTable == null) {
			TTFTableDirectory dir = getTableDirectory(OTFTable.TAG_GSUB_TABLE);
			if (dir != null) {
				otfGSubTable = new OTFGSubTable();
				otfGSubTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return otfGSubTable;
	}

	public OTFGPosTable getGPosTable() throws TTFFormatException, IOException {
		if (otfGPosTable == null) {
			TTFTableDirectory dir = getTableDirectory(OTFTable.TAG_GPOS_TABLE);
			if (dir != null) {
				otfGPosTable = new OTFGPosTable();
				otfGPosTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return otfGPosTable;
	}

	public OTFGDefTable getGDefTable() throws TTFFormatException, IOException {
		if (otfGDefTable == null) {
			TTFTableDirectory dir = getTableDirectory(OTFTable.TAG_GDEF_TABLE);
			if (dir != null) {
				otfGDefTable = new OTFGDefTable();
				otfGDefTable.read(dir.getOffset(), dir.getLength(),
						ttfRandomReader);
			}
		}
		return otfGDefTable;
	}

	private void loadNames() throws IOException, TTFFormatException {
		TTFTableDirectory nameDir = getTableDirectory(TTFTable.TAG_NAME_TABLE);
		TTFNameTable nameTable = getNameTable();
		ttfNames = new Name[nameTable.getNumNameRecords()];
		for (int i = 0; i < nameTable.getNumNameRecords(); ++i) {
			TTFNameRecord nameRec = nameTable.getNameRecord(i);

			Name name = new Name();
			name.nameID = nameRec.getNameID();
			name.platformID = nameRec.getPlatformID();
			name.encodingID = nameRec.getEncodingID();
			name.languageID = nameRec.getLanguageID();

			byte[] tmp = new byte[nameRec.getLength()];
			ttfRandomReader.setPosition(nameDir.getOffset()
					+ nameTable.getStringOffset() + nameRec.getOffset());
			ttfRandomReader.readBytes(tmp);
			name.unicode = new String(tmp, TTFEncoding.getCharset(
					nameRec.getPlatformID(), nameRec.getEncodingID()));

			name.locale = TTFEncoding.getLocale(nameRec.getPlatformID(),
					nameRec.getLanguageID());

			ttfNames[i] = name;
		}
	}

	public Name[] getNames() throws TTFFormatException, IOException {
		if (ttfNames == null) {
			loadNames();
		}
		return ttfNames;
	}

	public Name[] getNames(int nameID) throws TTFFormatException, IOException {
		if (ttfNames == null) {
			loadNames();
		}
		LinkedList<Name> list = new LinkedList<Name>();
		for (int i = 0; i < ttfNames.length; ++i) {
			if (ttfNames[i].nameID == nameID) {
				list.add(ttfNames[i]);
			}
		}
		return list.toArray(new Name[list.size()]);
	}

	public Name[] getNames(int nameID, int platformID, int encodingID)
			throws TTFFormatException, IOException {
		if (ttfNames == null) {
			loadNames();
		}
		LinkedList<Name> list = new LinkedList<Name>();
		for (int i = 0; i < ttfNames.length; ++i) {
			if (ttfNames[i].nameID == nameID
					&& ttfNames[i].platformID == platformID
					&& ttfNames[i].encodingID == encodingID) {
				list.add(ttfNames[i]);
			}
		}
		return list.toArray(new Name[list.size()]);
	}

	public Name[] getNames(int nameID, Locale locale)
			throws TTFFormatException, IOException {
		if (ttfNames == null) {
			loadNames();
		}
		LinkedList<Name> list = new LinkedList<Name>();
		for (int i = 0; i < ttfNames.length; ++i) {
			if (ttfNames[i].nameID == nameID
					&& ttfNames[i].locale.equals(locale)) {
				list.add(ttfNames[i]);
			}
		}
		return list.toArray(new Name[list.size()]);
	}

	public Name getDefaultName(int nameID) throws TTFFormatException,
			IOException {
		if (ttfNames == null) {
			loadNames();
		}
		for (int i = 0; i < ttfNames.length; ++i) {
			if (ttfNames[i].nameID == nameID
					&& ttfNames[i].platformID == TTFEncoding.PLATFORM_UNICODE) {
				return ttfNames[i];
			}
		}
		for (int i = 0; i < ttfNames.length; ++i) {
			if (ttfNames[i].nameID == nameID
					&& ttfNames[i].platformID == TTFEncoding.PLATFORM_MICROSOFT
					&& ttfNames[i].locale.equals(Locale.US)) {
				return ttfNames[i];
			}
		}
		Name[] names = getNames(nameID);
		if (names.length == 0) {
			return null;
		} else {
			return names[0];
		}
	}

	public int getNumEncodings() throws TTFFormatException, IOException {
		return getCMapTable().getNumEncodings();
	}

	public TTFCMapEncodingRecord getEncodingRecord(int i)
			throws TTFFormatException, IOException {
		return getCMapTable().getEncoding(i);
	}

	public TTFCMapEncodingRecord getEncodingRecord(int platformID,
			int encodingID) throws TTFFormatException, IOException {
		return getCMapTable().getEncoding(platformID, encodingID);
	}

	public TTFCMapEncoding getCurrentEncoding() {
		return this.ttfCMapEncoding;
	}

	public boolean setCurrentEncoding(int i) throws TTFFormatException,
			IOException {
		TTFTableDirectory cmapDir = getTableDirectory(TTFTable.TAG_CMAP_TABLE);
		TTFCMapEncodingRecord cmapRec = getEncodingRecord(i);
		if (cmapRec == null) {
			return false;
		}
		ttfCMapEncoding = TTFCMapEncoding.readCMapEncoding(cmapDir.getOffset()
				+ cmapRec.getOffset(), ttfRandomReader);
		return true;
	}

	public boolean setCurrentEncoding(int platformID, int encodingID)
			throws TTFFormatException, IOException {
		TTFTableDirectory cmapDir = getTableDirectory(TTFTable.TAG_CMAP_TABLE);
		TTFCMapEncodingRecord cmapRec = getEncodingRecord(platformID,
				encodingID);
		if (cmapRec == null) {
			return false;
		}
		ttfCMapEncoding = TTFCMapEncoding.readCMapEncoding(cmapDir.getOffset()
				+ cmapRec.getOffset(), ttfRandomReader);
		return true;
	}

	public boolean setCurrentEncodingToMSUnicode() throws TTFFormatException,
			IOException {
		return setCurrentEncodingToMSUnicodeFull()
				|| setCurrentEncodingToMSUnicodeBMP();
	}

	public boolean setCurrentEncodingToMSUnicodeBMP()
			throws TTFFormatException, IOException {
		return setCurrentEncoding(TTFEncoding.PLATFORM_MICROSOFT,
				TTFEncoding.MICROSOFT_ENCODING_UNICODE_BMP);
	}

	public boolean setCurrentEncodingToMSUnicodeFull()
			throws TTFFormatException, IOException {
		return setCurrentEncoding(TTFEncoding.PLATFORM_MICROSOFT,
				TTFEncoding.MICROSOFT_ENCODING_UNICODE_FULL);
	}

	public boolean setCurrentEncodingToMSSymbol() throws TTFFormatException,
			IOException {
		return setCurrentEncoding(TTFEncoding.PLATFORM_MICROSOFT,
				TTFEncoding.MICROSOFT_ENCODING_SYMBOL);
	}

	public boolean isValidGlyphID(int glyphID) throws TTFFormatException,
			IOException {
		if (glyphID < 0 || glyphID >= getLocaTable().getNumGlyphs()) {
			return false;
		} else {
			return true;
		}
	}

	public int getGlyphID(int codepoint) {
		return ttfCMapEncoding.getGlyphID(codepoint);
	}

	public int getGlyphCount() throws TTFFormatException, IOException {
		return getLocaTable().getNumGlyphs();
	}

	public byte[] getGlyphBytes(int glyphID) throws TTFFormatException,
			IOException {
		TTFLocaTable locaTable = getLocaTable();
		assert (glyphID >= 0 && glyphID < locaTable.getNumGlyphs());
		long goff = locaTable.getGlyphOffset(glyphID);
		long glen = locaTable.getGlyphOffset(glyphID + 1) - goff;
		byte[] tmp = new byte[(int) glen];
		ttfRandomReader.setPosition(getTableDirectory(TTFTable.TAG_GLYF_TABLE)
				.getOffset() + goff);
		ttfRandomReader.readBytes(tmp);
		return tmp;
	}

	public TTFGlyph getGlyph(int glyphID) throws TTFGlyphException,
			TTFFormatException, IOException {
		return new TTFGlyph(getGlyphBytes(glyphID));
	}

	public void getDependentGlyphs(TTFGlyph glyph,
			HashMap<Integer, TTFGlyph> dependents) throws TTFGlyphException,
			TTFFormatException, IOException {
		if (glyph.isComposited()) {
			for (int i = 0; i < glyph.getNumSubGlyphs(); i++) {
				int subGlyphID = glyph.getSubGlyphID(i);
				if (!dependents.containsKey(subGlyphID)) {
					TTFGlyph subGlyph = this.getGlyph(subGlyphID);
					dependents.put(subGlyphID, subGlyph);
					if (subGlyph.isComposited()) {
						getDependentGlyphs(subGlyph, dependents);
					}
				}
			}
		}
	}

	public GlyphPath getGlyphPath(int glyphID, boolean convertQuad2Cubic)
			throws TTFGlyphException, TTFFormatException, IOException {
		TTFGlyph glyph = this.getGlyph(glyphID);
		if (glyph.isComposited()) {
			HashMap<Integer, TTFGlyph> dependents = new HashMap<Integer, TTFGlyph>();
			getDependentGlyphs(glyph, dependents);
			return glyph.getGlyphPath(dependents, convertQuad2Cubic);
		} else {
			return glyph.getGlyphPath(convertQuad2Cubic);
		}
	}

	public CFFWrapper getCFFWrapper() throws IOException, CFFFormatException {
		if (cffWrapper == null) {
			TTFTableDirectory cff_Dir = getTableDirectory(TTFTable.TAG_CFF__TABLE);
			CFFRandomReader cffRead = new CFFRandomReaderWrapper(
					ttfRandomReader, cff_Dir.getOffset(), cff_Dir.getLength());
			cffWrapper = new CFFWrapper(cffRead);
		}
		return cffWrapper;
	}
}
