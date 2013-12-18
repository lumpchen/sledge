package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public class PDFFont {
	
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

	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}
	
	
	
}
