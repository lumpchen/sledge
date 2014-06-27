package com.docscience.pathfinder.font.driver.ttf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.docscience.pathfinder.font.driver.AbstractFontReader;
import com.docscience.pathfinder.font.driver.ttf.TTFWrapper.Name;
import com.docscience.pathfinder.font.shared.FontDictionary;
import com.docscience.pathfinder.font.shared.FontEncoding;
import com.docscience.pathfinder.font.shared.FontMetrics;
import com.docscience.pathfinder.font.shared.FontTechnology;
import com.docscience.pathfinder.font.shared.GlyphDescription;
import com.docscience.pathfinder.font.shared.GlyphPath;

public class TTFReader extends AbstractFontReader {

	private TTFRandomReader ttfRandom;
	private TTFWrapper ttfWrapper;

	public TTFReader(File file) throws IOException {
		try {
			this.ttfRandom = new TTFRandomFileReader(file);
			this.ttfWrapper = new TTFWrapper(this.ttfRandom);
		} catch (Throwable e) {
			if (ttfRandom != null) {
				ttfRandom.close();
			}
			throw new IOException(e);
		}
	}

	public TTFReader(byte[] data) throws IOException {
		try {
			this.ttfRandom = new TTFRandomArrayReader(data);
			this.ttfWrapper = new TTFWrapper(this.ttfRandom);
		} catch (Throwable e) {
			if (ttfRandom != null) {
				ttfRandom.close();
			}
			throw new IOException(e);
		}
	}

	@Override
	public void close() throws IOException {
		this.ttfRandom.close();
		this.ttfWrapper = null;
		this.ttfRandom = null;
	}

	@Override
	public int getNumFonts() throws IOException {
		return this.ttfWrapper.getNumFonts();
	}

	@Override
	public void selectFont(int index) throws IOException {
		this.ttfWrapper.setCurrentFont(index);
	}

	@Override
	public String getFontFamily() throws IOException {
		Name fontFamily = this.ttfWrapper
				.getDefaultName(TTFEncoding.NAME_PREFERRED_FAMILY);
		if (fontFamily == null) {
			fontFamily = this.ttfWrapper
					.getDefaultName(TTFEncoding.NAME_FONT_FAMILY);
		}
		return fontFamily.getUnicode();
	}

	@Override
	public String getFontStyle() throws IOException {
		Name fontStyle = this.ttfWrapper
				.getDefaultName(TTFEncoding.NAME_PREFERRED_SUBFAMILY);
		if (fontStyle == null) {
			fontStyle = this.ttfWrapper
					.getDefaultName(TTFEncoding.NAME_FONT_SUBFAMILY);
		}
		return fontStyle.getUnicode();
	}

	@Override
	public FontTechnology getTechnology() throws IOException {
		if (this.ttfWrapper.isOpenType()) {
			if (this.ttfWrapper.isPostScriptOutline()) {
				return FontTechnology.OPEN_TYPE_PS;
			} else {
				return FontTechnology.OPEN_TYPE_TT;
			}
		} else {
			return FontTechnology.TRUE_TYPE;
		}
	}

	@Override
	public FontEncoding getEncoding() throws IOException {
		FontEncoding encoding = new FontEncoding();
		if (ttfWrapper.setCurrentEncodingToMSUnicodeFull()) {
			encoding.setName("Unicode Full");
		} else if (ttfWrapper.setCurrentEncodingToMSUnicodeBMP()) {
			encoding.setName("Unicode BMP");
		} else if (ttfWrapper.setCurrentEncodingToMSSymbol()) {
			encoding.setName("Symbol");
		} else {
			throw new TTFException("cannot find font encoding");
		}

		ArrayList<FontEncoding.Range> list = getRangesFromCMap();
		encoding.setRanges(list);

		return encoding;
	}

	private ArrayList<FontEncoding.Range> getRangesFromCMap()
			throws TTFException {
		ArrayList<FontEncoding.Range> list = new ArrayList<FontEncoding.Range>();
		TTFCMapEncoding cmap = ttfWrapper.getCurrentEncoding();
		if (cmap.getFormat() == TTFCMapEncoding.CMAP_FORMAT_0
				|| cmap.getFormat() == TTFCMapEncoding.CMAP_FORMAT_6) {
			int start = cmap.getFirstChar();
			short[] glyphIds = new short[cmap.getLastChar()
					- cmap.getFirstChar() + 1];
			for (int i = 0; i < glyphIds.length; i++) {
				glyphIds[i] = (short) cmap.getGlyphID(i + start);
			}
			FontEncoding.Range range = new FontEncoding.Range(start, glyphIds);
			list.add(range);

		} else if (cmap.getFormat() == TTFCMapEncoding.CMAP_FORMAT_4) {
			TTFCMapEncodingFormat4 f4 = (TTFCMapEncodingFormat4) cmap;
			for (int i = 0; i < f4.getEndCount().length; i++) {
				int start = f4.getStartCount()[i];
				int end = f4.getEndCount()[i];
				int range = f4.getIdRangeOffset()[i];
				if (range != 0) {
					short[] glyphIds = new short[end - start + 1];
					int index = (range / 2) - f4.getSegCountX2() / 2 + i;
					for (int j = 0; j < glyphIds.length; j++) {
						glyphIds[j] = (short) f4.getGlyphIdArray()[index + j];
					}
					FontEncoding.Range r = new FontEncoding.Range(start,
							glyphIds);
					list.add(r);
				} else {
					int glyphId = f4.getIdDelta()[i];
					FontEncoding.Range r = new FontEncoding.Range(start, end,
							glyphId);
					list.add(r);
				}
			}
		} else {
			throw new TTFException("unsupported cmap encoding format "
					+ cmap.getFormat());
		}
		return list;
	}

	@Override
	public FontDictionary[] getDictionaries() throws IOException {
		Map<Locale, FontDictionary> namesMap = new HashMap<Locale, FontDictionary>();
		for (Name name : ttfWrapper.getNames()) {
			FontDictionary names = namesMap.get(name.getLocale());
			if (names == null) {
				names = new FontDictionary(name.getLocale().toString());
				namesMap.put(name.getLocale(), names);
			}
			addName(names, name);
		}
		return namesMap.values().toArray(new FontDictionary[namesMap.size()]);
	}

	private void addName(FontDictionary names, Name name) {
		switch (name.getNameID()) {
		case TTFEncoding.NAME_COPYRIGHT_NOTICE:
			if (names.getCopyrightNotice() == null) {
				names.setCopyrightNotice(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_FONT_FAMILY:
			if (names.getFamilyName() == null) {
				names.setFamilyName(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_FONT_SUBFAMILY:
			if (names.getSubfamilyName() == null) {
				names.setSubfamilyName(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_FONT_IDENTIFIER:
			if (names.getUniqueIdentifier() == null) {
				names.setUniqueIdentifier(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_FONT_FULL_NAME:
			if (names.getFullName() == null) {
				names.setFullName(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_VERSION_STRING:
			if (names.getVersion() == null) {
				names.setVersion(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_POSTSCRIPT_FONT_NAME:
			if (names.getPostScriptFontName() == null) {
				names.setPostScriptFontName(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_TRADEMARK:
			if (names.getTrademark() == null) {
				names.setTrademark(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_MANUFACTURER:
			if (names.getManufacturer() == null) {
				names.setManufacturer(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_DESIGNER:
			if (names.getDesigner() == null) {
				names.setDesigner(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_DESCRIPTION:
			if (names.getDescription() == null) {
				names.setDescription(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_URL_VENDOR:
			if (names.getVendorUrl() == null) {
				names.setVendorUrl(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_URL_DESIGNER:
			if (names.getDesigner() == null) {
				names.setDesigner(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_LICENSE_DESCRIPTION:
			if (names.getLicenseDescription() == null) {
				names.setLicenseDescription(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_LICENSE_INFO_URL:
			if (names.getLicenseUrl() == null) {
				names.setLicenseUrl(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_PREFERRED_FAMILY:
			if (names.getPreferredFamilyName() == null) {
				names.setPreferredFamilyName(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_PREFERRED_SUBFAMILY:
			if (names.getPreferredSubfamilyName() == null) {
				names.setPreferredSubfamilyName(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_COMPATIBLE_FULL:
			if (names.getCompatibleFullName() == null) {
				names.setCompatibleFullName(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_SAMPLE_TEXT:
			if (names.getSampleText() == null) {
				names.setSampleText(name.getUnicode());
			}
			break;
		case TTFEncoding.NAME_POSTSCRIPT_CID_FINDFONT_NAME:
			if (names.getPostScriptCidFindFontName() == null) {
				names.setPostScriptCidFindFontName(name.getUnicode());
			}
			break;
		default:
			// ignore
		}
	}

	@Override
	public FontMetrics getMetrics() throws IOException {
		FontMetrics metrics = new FontMetrics();
		metrics.setUnitsPerEm(ttfWrapper.getHeadTable().getUnitsPerEm());
		if (ttfWrapper.getOS_2Table() != null) {
			metrics.setAscent(ttfWrapper.getOS_2Table().getSTypoAscender());
			metrics.setDescent(ttfWrapper.getOS_2Table().getSTypoDescender());
			metrics.setLineGap(ttfWrapper.getOS_2Table().getSTypoLineGap());
		} else {
			metrics.setAscent(ttfWrapper.getHheaTable().getAscender());
			metrics.setDescent(ttfWrapper.getHheaTable().getDescender());
			metrics.setLineGap(ttfWrapper.getHheaTable().getLineGap());
		}
		metrics.setXMax(ttfWrapper.getHeadTable().getXMax());
		metrics.setXMin(ttfWrapper.getHeadTable().getXMin());
		metrics.setYMax(ttfWrapper.getHeadTable().getYMax());
		metrics.setYMin(ttfWrapper.getHeadTable().getYMin());
		metrics.setAdvancedWidths(ttfWrapper.getHmtxTable().getAdvanceWidths());
		metrics.setLeftSideBearings(ttfWrapper.getHmtxTable().getLeftSideBearings());
		return metrics;
	}

	@Override
	public int getGlyphCount() throws IOException {
		if (ttfWrapper.isPostScriptOutline()) {
			return ttfWrapper.getCFFWrapper().getCIDCount();
		} else {
			return ttfWrapper.getGlyphCount();
		}
	}

	@Override
	public GlyphDescription getGlyph(int glyphId) throws IOException {
		GlyphPath glyphPath;
		if (ttfWrapper.isPostScriptOutline()) {
			glyphPath = ttfWrapper.getCFFWrapper().getType2CharString(glyphId).getPath();
		} else {
			glyphPath = ttfWrapper.getGlyphPath(glyphId, true);
		}
		return new GlyphDescription(glyphId, glyphPath);
	}

}
