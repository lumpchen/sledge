package com.docscience.pathfinder.font.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("serial")
public final class Font implements Serializable {

	public static final String EN = "en";
	public static final String EN_US = "en_US";

	private Long id;

	private FontTechnology technology;
	
	private HashSet<String> locales = new HashSet<String>();

	private transient String defaultLocale;
	
	private HashMap<String, FontDictionary> dictionaries = new HashMap<String, FontDictionary>();

	private transient FontDictionary defaultDictionary;

	private FontEncoding encoding;

	private FontMetrics metrics;

	private TreeSet<Integer> unmappedGlyphIds = new TreeSet<Integer>();
	
	private HashMap<Integer, GlyphDescription> glyphs = new HashMap<Integer, GlyphDescription>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FontTechnology getTechnology() {
		return technology;
	}

	public void setTechnology(FontTechnology technology) {
		this.technology = technology;
	}
	
	public String getDefaultLocale() {
		if (defaultLocale == null) {
			if (this.locales.contains(EN_US)) {
				defaultLocale = EN_US;
			} else if (this.locales.contains(EN)) {
				defaultLocale = EN;
			} else {
				defaultLocale = locales.iterator().next();
			}
		}
		return defaultLocale;
	}

	public Set<String> getLocales() {
		return locales;
	}

	public void setLocales(Collection<String> locales) {
		this.locales.clear();
		this.locales.addAll(locales);
	}

	public void addLocale(String locale) {
		this.locales.add(locale);
		this.defaultLocale = null;
	}

	public FontDictionary getDictionary() {
		if (defaultDictionary == null) {
			defaultDictionary = getDictionary(getDefaultLocale());
		}
		return defaultDictionary;
	}

	public FontDictionary getDictionary(String locale) {
		return getDictionary(locale, false);
	}
	
	public FontDictionary getDictionary(String locale, boolean exactlyMatch) {
		FontDictionary dict = this.dictionaries.get(locale);
		if (dict == null && !exactlyMatch) {
			dict = getDictionary();
		}
		return dict;
	}

	public List<FontDictionary> getDictionaries() {
		return new ArrayList<FontDictionary>(dictionaries.values());
	}

	public void addDictionary(FontDictionary dict) {
		this.dictionaries.put(dict.getLocale(), dict);
		this.addLocale(dict.getLocale());
	}

	public FontEncoding getEncoding() {
		return encoding;
	}

	public void setEncoding(FontEncoding encoding) {
		this.encoding = encoding;
	}

	public FontMetrics getMetrics() {
		return metrics;
	}

	public void setMetrics(FontMetrics metrics) {
		this.metrics = metrics;
	}
	
	public Set<Integer> getUnmappedGlyphIds() {
		return unmappedGlyphIds;
	}
	
	public void setUnmappedGlyphIds(Collection<Integer> ids) {
		this.unmappedGlyphIds.clear();
		this.unmappedGlyphIds.addAll(ids);
	}

	public Map<Integer, GlyphDescription> getGlyphs() {
		return Collections.unmodifiableMap(glyphs);
	}

	public void addGlyph(GlyphDescription glyph) {
		glyphs.put(glyph.getId(), glyph);
	}

	public int getGlyphId(int codePoint) {
		return encoding.getGlyphId(codePoint);
	}

	public String getFontFamily() {
		return getFontFamily(getDefaultLocale());
	}
	
	public String getFontFamily(String locale) {
		FontDictionary names = getDictionary(locale);
		String fontFamily = names.getPreferredFamilyName();
		if (fontFamily == null) {
			fontFamily = names.getFamilyName();
		}
		return fontFamily;
	}
	
	public String getFontStyle() {
		return getFontStyle(getDefaultLocale());
	}
	
	public String getFontStyle(String locale) {
		FontDictionary names = getDictionary(locale);
		String fontStyle = names.getPreferredSubfamilyName();
		if (fontStyle == null) {
			fontStyle = names.getSubfamilyName();
		}
		return fontStyle;
	}
	
	public String getPostScriptFontName() {
		return getDictionary().getPostScriptFontName();
	}
		
	public double getAscent(double fontSize) {
		return metrics.getAscent() * fontSize / metrics.getUnitsPerEm();
	}

	public double getDescent(double fontSize) {
		return metrics.getDescent() * fontSize / metrics.getUnitsPerEm();
	}

	public double getLineGap(double fontSize) {
		return metrics.getLineGap() * fontSize / metrics.getUnitsPerEm();
	}

	public double getAdvancedWidth(int glyphId, double fontSize) {
		return metrics.getAdvancedWidth(glyphId) * fontSize
				/ metrics.getUnitsPerEm();
	}

	public double getLeftSideBearing(int glyphId, double fontSize) {
		return metrics.getLeftSideBearing(glyphId) * fontSize
				/ metrics.getUnitsPerEm();
	}
}
