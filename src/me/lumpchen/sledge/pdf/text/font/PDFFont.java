package me.lumpchen.sledge.pdf.text.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.document.FontDescriptorObj;
import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public abstract class PDFFont {

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

	protected String baseFont;
	protected String subType;
	protected String name;

	protected int firstChar;
	protected int lastChar;
	protected int[] widths;
	protected FontDescriptor fontDescriptor;
	
	protected PName predefinedEncoding;
	protected PDFFontEncoding encoding;
	protected PDFCMap toUnicodeMap;
	
	protected FontObject descendantFontObj;

	protected String postscriptName;

	PDFFont(FontObject fontObj) {
		this.read(fontObj);
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
		
		if (fontObj.getBaseFont() != null) {
			this.baseFont = fontObj.getBaseFont().getName();			
		}
		
		if (fontObj.getFontDescriptor() != null) {
			FontDescriptorObj obj = fontObj.getFontDescriptor();
			this.fontDescriptor = new FontDescriptor(obj);
		}

		if (fontObj.getPredefinedEncoding() != null) {
			this.predefinedEncoding = fontObj.getPredefinedEncoding();
			this.encoding = new PDFFontEncoding(this.subType, this.predefinedEncoding);
		} else if (fontObj.getEncoding() != null) {
			this.encoding = new PDFFontEncoding(this.subType, fontObj.getEncoding());
		}
		
		if (fontObj.getDescendantFonts() != null) {
			this.descendantFontObj = fontObj.getDescendantFonts();
		}
	}
	
	public static PDFFont create(FontObject fontObj) {
		String subType = fontObj.getSubType().getName();
		if (TrueType.equalsIgnoreCase(subType)) {
//			TTFFont ttf = new TTFFont(fontObj);
			JTrueTypeFont ttf = new JTrueTypeFont(fontObj);
//			try {
//				TrueTypeFont ttf = new TrueTypeFont(fontObj);
				return ttf;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		} else if (Type_0.equalsIgnoreCase(subType)) {
			Type0Font type0 = new Type0Font(fontObj);
			return type0;
		} else if (Type_1.equalsIgnoreCase(subType)) {
			Type1Font type1 = new Type1Font(fontObj);
			return type1;
		} else if (CIDFontType2.equalsIgnoreCase(subType)) {
			JTrueTypeFont ttf = new JTrueTypeFont(fontObj);
			return ttf;
//			try {
//				TrueTypeFont ttf = new TrueTypeFont(fontObj);
//				return ttf;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}

		return null;
	}

	protected void setEncoding(PDFFontEncoding encoding) {
		this.encoding = encoding;
	}
	
	public boolean notEmbed() {
		if (this.fontDescriptor == null) {
			return false;
		}
		return this.fontDescriptor.notEmbed();
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

	abstract public void renderText(String s, VirtualGraphics gd) throws IOException;
	
	abstract public void close() throws IOException;
}
