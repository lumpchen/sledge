package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.syntax.document.FontDescriptorObj;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;

public class FontDescriptor {

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

	public FontDescriptor(FontDescriptorObj obj) {
		if (obj.getFontName() != null) {
			this.fontName = obj.getFontName().toJavaString();
		}
		
		if (obj.getFontFamily() != null) {
			this.fontFamily = obj.getFontFamily().toJavaString();
		}
		
		if (obj.getFontStrech() != null) {
			this.fontStrech = obj.getFontStrech().toJavaString();
		}
		
		if (obj.getFontWeight() != null) {
			this.fontWeight = obj.getFontWeight().doubleValue();
		}

		if (obj.getFlags() != null) {
			this.flags = obj.getFlags().intValue();
		}

		if (obj.getFontBBox() != null) {
			PArray arr = obj.getFontBBox();
			this.fontBBox = new int[arr.size()];
			
			for (int i = 0; i < arr.size(); i++) {
				this.fontBBox[i] = ((PNumber) arr.get(i)).intValue();
			}
		}

		if (obj.getItalicAngle() != null) {
			this.italicAngle = obj.getItalicAngle().doubleValue();
		}

		if (obj.getAscent() != null) {
			this.ascent = obj.getAscent().doubleValue();
		}

		if (obj.getDescent() != null) {
			this.descent = obj.getDescent().doubleValue();
		}
		
		if (obj.getLeading() != null) {
			this.leading = obj.getLeading().doubleValue();
		}

		if (obj.getCapHeight() != null) {
			this.capHeight = obj.getCapHeight().doubleValue();
		}

		if (obj.getxHeight() != null) {
			this.xHeight = obj.getxHeight().doubleValue();
		}
		
		if (obj.getStemH() != null) {
			this.stemH = obj.getStemH().doubleValue();
		}
		
		if (obj.getStemV() != null) {
			this.stemV = obj.getStemV().doubleValue();
		}
		
		if (obj.getAvgWidth() != null) {
			this.avgWidth = obj.getAvgWidth().doubleValue();
		}
		
		if (obj.getMaxWidth() != null) {
			this.maxWidth = obj.getMaxWidth().doubleValue();
		}

		if (obj.getMissingWidth() != null) {
			this.missingWidth = obj.getMissingWidth().doubleValue();
		}

		if (obj.getFontFile() != null) {
			this.fontFile = new FontFile(obj.getFontFile());
		}
		
		if (obj.getFontFile2() != null) {
			this.fontFile2 = new FontFile(obj.getFontFile2());
		}
		
		if (obj.getFontFile3() != null) {
			this.fontFile3 = new FontFile(obj.getFontFile3());
		}
		
		if (obj.getCharSet() != null) {
			this.charset = obj.getCharSet().toJavaString();
		}
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
	
	public boolean notEmbed() {
		return this.fontFile == null && this.fontFile2 == null && this.fontFile3 == null;
	}
}
