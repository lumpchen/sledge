package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
import me.lumpchen.sledge.pdf.syntax.lang.PStream;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

public class FontDescriptorObj extends DocObject {

	private PString fontName;
	private PString fontFamily;
	private PString fontStrech;
	private PNumber fontWeight;
	private PNumber flags;
	private PArray fontBBox;
	private PNumber italicAngle;
	private PNumber ascent;
	private PNumber descent;
	private PNumber leading;
	private PNumber capHeight;
	private PNumber xHeight;
	private PNumber stemV;
	private PNumber stemH;
	private PNumber avgWidth;
	private PNumber maxWidth;
	private PNumber missingWidth;
	
	private PStream fontFile;
	private PStream fontFile2;
	private PStream fontFile3;
	
	private PString charSet;
	
	public FontDescriptorObj(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
		
		this.fontName = this.getValueAsString(PName.instance("FontName"));
		this.fontFamily = this.getValueAsString(PName.instance("FontFamily"));
		this.fontStrech = this.getValueAsString(PName.instance("FontStretch"));
		this.fontWeight = this.getValueAsNumber(PName.instance("FontWeight"));
		this.flags = this.getValueAsNumber(PName.instance("Flags"));
		this.fontBBox = this.getValueAsArray(PName.instance("FontBBox"));
		this.italicAngle = this.getValueAsNumber(PName.instance("ItalicAngle"));
		this.ascent = this.getValueAsNumber(PName.instance("Ascent"));
		this.descent = this.getValueAsNumber(PName.instance("Descent"));
		this.leading = this.getValueAsNumber(PName.instance("Leading"));
		this.capHeight = this.getValueAsNumber(PName.instance("CapHeight"));
		this.xHeight = this.getValueAsNumber(PName.instance("XHeight"));
		this.stemV = this.getValueAsNumber(PName.instance("StemV"));
		this.stemH = this.getValueAsNumber(PName.instance("StemH"));
		this.avgWidth = this.getValueAsNumber(PName.instance("AvgWidth"));
		this.maxWidth = this.getValueAsNumber(PName.instance("MaxWidth"));

		IndirectRef ref = this.getValueAsRef(PName.instance("FontFile"));
		if (ref != null) {
			this.fontFile = this.owner.getObject(ref).getStream();
		}
		
		ref = this.getValueAsRef(PName.instance("FontFile2"));
		if (ref != null) {
			this.fontFile2 = this.owner.getObject(ref).getStream();
		}
		
		ref = this.getValueAsRef(PName.instance("FontFile3"));
		if (ref != null) {
			this.fontFile3 = this.owner.getObject(ref).getStream();
		}
		
		this.charSet = this.getValueAsString(PName.instance("CharSet"));
	}

	@Override
	public PName getType() {
		return PName.FontDescriptor;
	}

	public PString getFontName() {
		return this.fontName;
	}

	public PString getFontFamily() {
		return this.fontFamily;
	}

	public PString getFontStrech() {
		return fontStrech;
	}

	public PNumber getFontWeight() {
		return fontWeight;
	}

	public PNumber getFlags() {
		return flags;
	}

	public PArray getFontBBox() {
		return fontBBox;
	}

	public PNumber getItalicAngle() {
		return italicAngle;
	}

	public PNumber getAscent() {
		return ascent;
	}

	public PNumber getDescent() {
		return descent;
	}

	public PNumber getLeading() {
		return leading;
	}

	public PNumber getCapHeight() {
		return capHeight;
	}

	public PNumber getxHeight() {
		return xHeight;
	}

	public PNumber getStemV() {
		return stemV;
	}

	public PNumber getStemH() {
		return stemH;
	}

	public PNumber getAvgWidth() {
		return avgWidth;
	}

	public PNumber getMaxWidth() {
		return maxWidth;
	}

	public PNumber getMissingWidth() {
		return missingWidth;
	}

	public PStream getFontFile() {
		return fontFile;
	}

	public PStream getFontFile2() {
		return fontFile2;
	}

	public PStream getFontFile3() {
		return fontFile3;
	}

	public PString getCharSet() {
		return charSet;
	}

}
