package me.lumpchen.sledge.pdf.text.font;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public abstract class PDFFont {

	public static Map<String, Font> JDK_FONT_CACHE = new HashMap<String, Font>();
	static {
//		Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
//		for (Font f : fonts) {
//			String psName = f.getPSName();
//			if (psName != null) {
//				JDK_FONT_CACHE.put(psName, f);
//			}
//		}
	}

	public static final String Type_0 = "Type0";
	public static final String Type_1 = "Type1";
	public static final String MMType1 = "MMType1";
	public static final String Type3 = "Type3";
	public static final String TrueType = "TrueType";
	public static final String CIDFont = "CIDFont";
	public static final String CIDFontType0 = "CIDFontType0";
	public static final String CIDFontType2 = "CIDFontType2";

	protected String baseFont;
	protected String subType;
	protected String name;

	protected int firstChar;
	protected int lastChar;
	protected int[] widths;
	protected FontDescriptor fontDescriptor;
	protected PDFEncoding encoding;
	protected PDFCMap toUnicodeMap;

	protected String postscriptName;

	PDFFont() {
	};

	protected void read(FontObject fontObj) {
		if (fontObj.getSubType() != null) {
			this.subType = fontObj.getSubType().getName();			
		}

		if (fontObj.getName() != null) {
			this.name = fontObj.getName().getName();
		}
		
		if (fontObj.getFirstChar() != null) {
			this.firstChar = fontObj.getFirstChar().intValue(); 
		}
		
		if (fontObj.getLastChar() != null) {
			this.lastChar = fontObj.getLastChar().intValue();
		}
		
		if (fontObj.getWidths() != null) {
			PArray arr = fontObj.getWidths();
			this.widths = new int[arr.size()];
			
			for (int i = 0; i < arr.size(); i++) {
				PNumber num = (PNumber) arr.get(i);
				this.widths[i] = num.intValue();
			}
		}
		
		this.baseFont = fontObj.getBaseFont();
		
		if (fontObj.getFontDescriptor() != null) {
			PDictionary dict = fontObj.getFontDescriptor();
			this.fontDescriptor = new FontDescriptor(dict);
		}

		if (fontObj.getEncoding() != null) {
			this.encoding = new PDFEncoding(fontObj.getEncoding());
		}
	}
	
	public static PDFFont create(FontObject fontObj) {
		String subType = fontObj.getSubType().getName();
		if (TrueType.equalsIgnoreCase(subType)) {
			TrueTypeFont ttf = new TrueTypeFont();
			ttf.read(fontObj);
			return ttf;
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
