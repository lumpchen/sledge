package me.lumpchen.sledge.pdf.text.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public class PDFFont {

	public static Map<String, Font> JDK_FONT_CACHE = new HashMap<String, Font>();
	static {
		Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		for (Font f : fonts) {
			String psName = f.getPSName();
			if (psName != null) {
				JDK_FONT_CACHE.put(psName, f);
			}
		}
	}

	public static final String Type_0 = "Type0";
	public static final String Type_1 = "Type1";
	public static final String MMType1 = "MMType1";
	public static final String Type3 = "Type3";
	public static final String TrueType = "TrueType";
	public static final String CIDFont = "CIDFont";
	public static final String CIDFontType0 = "CIDFontType0";
	public static final String CIDFontType2 = "CIDFontType2";

	private String baseFont;
	private String subType;
	private String postscriptName;

	public PDFFont() {
	}

	public static PDFFont create(FontObject fontObj) {
		String subType = fontObj.getSubType().getName();
		if (TrueType.equalsIgnoreCase(subType)) {
			TrueTypeFont font = new TrueTypeFont();
			font.setSubType(subType);
			font.setBaseFont(fontObj.getBaseFont().getName());
			return font;
		}

		return null;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getBaseFont() {
		return baseFont;
	}

	public String getPSName() {
		return this.postscriptName;
	}

	public void setBaseFont(String baseFont) {
		if (baseFont.indexOf("+") > 0) {
			this.postscriptName = baseFont.substring(baseFont.indexOf("+") + 1);
		} else {
			this.postscriptName = baseFont;
		}
		this.baseFont = baseFont;
	}

	public Font peerAWTFont() {
		if (JDK_FONT_CACHE.containsKey(this.postscriptName)) {
			return JDK_FONT_CACHE.get(this.postscriptName);
		}
		return null;
	}

}
