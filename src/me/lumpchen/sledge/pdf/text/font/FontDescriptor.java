package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class FontDescriptor {

	private String type;
	private String fontName;
	private String fontFamily;
	private String fontStrech;
	private double fontWeight;
	private int flags;
	private int[] fontBBox;
	private double italicAngle;
	private double ascent;
	private double descent;
	private double leading;
	private double capHeight;
	private double xHeight;
	private double stemV;
	private double stemH;
	private double avgWidth;
	private double maxWidth;
	private double missingWidth;
	
	private FontFile fontFile;
	private FontFile fontFile2;
	private FontFile fontFile3;
	
	private String charset;
	
	public FontDescriptor(PDictionary dict) {
		this.read(dict);
	}
	
	private void read(PDictionary dict) {
		PString s = dict.getValueAsString(PName.instance("FontFamily"));
		if (s != null) {
			this.fontFamily = s.toJavaString();
		}
		
		s = dict.getValueAsString(PName.instance("FontName"));
		if (s != null) {
			this.fontName = s.toJavaString();
		}
		
		s = dict.getValueAsString(PName.instance("FontStretch"));
		if (s != null) {
			this.fontStrech = s.toJavaString();
		}
		
		PNumber n = dict.getValueAsNumber(PName.instance("FontWeight"));
		if (n != null) {
			this.fontWeight = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("Flags"));
		if (n != null) {
			this.flags = n.intValue();
		}
		
		PArray arr = dict.getValueAsArray(PName.instance("FontBBox"));
		if (arr != null) {
			this.fontBBox = new int[arr.size()];
			for (int i = 0; i < arr.size(); i++) {
				this.fontBBox[i] = ((PNumber) arr.get(i)).intValue();
			}
		}
		
		n = dict.getValueAsNumber(PName.instance("ItalicAngle"));
		if (n != null) {
			this.italicAngle = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("Ascent"));
		if (n != null) {
			this.ascent = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("Descent"));
		if (n != null) {
			this.descent = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("Leading"));
		if (n != null) {
			this.leading = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("CapHeight"));
		if (n != null) {
			this.capHeight = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("XHeight"));
		if (n != null) {
			this.xHeight = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("StemV"));
		if (n != null) {
			this.stemV = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("StemH"));
		if (n != null) {
			this.stemH = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("AvgWidth"));
		if (n != null) {
			this.avgWidth = n.doubleValue();
		}
		
		n = dict.getValueAsNumber(PName.instance("MaxWidth"));
		if (n != null) {
			this.maxWidth = n.doubleValue();
		}
		
		IndirectRef ref = dict.getValueAsRef(PName.instance("FontFile"));
		if (ref != null) {
			
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public String getFontStrech() {
		return fontStrech;
	}

	public void setFontStrech(String fontStrech) {
		this.fontStrech = fontStrech;
	}

	public double getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(double fontWeight) {
		this.fontWeight = fontWeight;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int[] getFontBBox() {
		return fontBBox;
	}

	public void setFontBBox(int[] fontBBox) {
		this.fontBBox = fontBBox;
	}

	public double getItalicAngle() {
		return italicAngle;
	}

	public void setItalicAngle(double italicAngle) {
		this.italicAngle = italicAngle;
	}

	public double getAscent() {
		return ascent;
	}

	public void setAscent(double ascent) {
		this.ascent = ascent;
	}

	public double getDescent() {
		return descent;
	}

	public void setDescent(double descent) {
		this.descent = descent;
	}

	public double getLeading() {
		return leading;
	}

	public void setLeading(double leading) {
		this.leading = leading;
	}

	public double getCapHeight() {
		return capHeight;
	}

	public void setCapHeight(double capHeight) {
		this.capHeight = capHeight;
	}

	public double getxHeight() {
		return xHeight;
	}

	public void setxHeight(double xHeight) {
		this.xHeight = xHeight;
	}

	public double getStemV() {
		return stemV;
	}

	public void setStemV(double stemV) {
		this.stemV = stemV;
	}

	public double getStemH() {
		return stemH;
	}

	public void setStemH(double stemH) {
		this.stemH = stemH;
	}

	public double getAvgWidth() {
		return avgWidth;
	}

	public void setAvgWidth(double avgWidth) {
		this.avgWidth = avgWidth;
	}

	public double getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}

	public double getMissingWidth() {
		return missingWidth;
	}

	public void setMissingWidth(double missingWidth) {
		this.missingWidth = missingWidth;
	}

	public FontFile getFontFile() {
		return fontFile;
	}

	public void setFontFile(FontFile fontFile) {
		this.fontFile = fontFile;
	}

	public FontFile getFontFile2() {
		return fontFile2;
	}

	public void setFontFile2(FontFile fontFile2) {
		this.fontFile2 = fontFile2;
	}

	public FontFile getFontFile3() {
		return fontFile3;
	}

	public void setFontFile3(FontFile fontFile3) {
		this.fontFile3 = fontFile3;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
}
