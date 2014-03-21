package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.syntax.document.FontObject;
import me.lumpchen.sledge.pdf.text.font.ttf.HeadTable;
import me.lumpchen.sledge.pdf.text.font.ttf.TrueTypeFont;

public class TTFFont extends PDFFont {

	private TrueTypeFont ttf;
	private double unitsPerEm;
	
	public TTFFont(FontObject fontObj) {
		super(fontObj);
		
		FontFile fontFile = this.fontDescriptor.getFontFile2();
		if (fontFile != null) {
			this.parse(fontFile.getBytes());
		}
	}

	private void parse(byte[] fontBytes) {
		this.ttf = TrueTypeFont.parseFont(fontBytes);
		
        // read the units per em from the head table
        HeadTable head = (HeadTable) ttf.getTable ("head");
        this.unitsPerEm = head.getUnitsPerEm ();
	}
	
	@Override
	public PDFGlyph getGlyph(char c) {
		// TODO Auto-generated method stub
		return null;
	}

}
